package com.smarttouch.app.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PixelFormat
import android.os.IBinder
import android.view.Gravity
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.app.NotificationCompat
import com.smarttouch.app.R
import com.smarttouch.app.SmartTouchApp
import com.smarttouch.app.data.model.AppSettings
import com.smarttouch.app.data.model.GestureMapping
import com.smarttouch.app.data.model.GestureType
import com.smarttouch.app.data.model.GestureZone
import com.smarttouch.app.data.repository.GestureRepository
import com.smarttouch.app.data.repository.SettingsRepository
import com.smarttouch.app.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class OverlayService : Service() {

    @Inject lateinit var gestureRepository: GestureRepository
    @Inject lateinit var settingsRepository: SettingsRepository

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
    private lateinit var windowManager: WindowManager
    private val overlayViews = mutableListOf<FrameLayout>()

    /** Always-current local copy of settings, updated whenever DataStore emits. */
    private var currentSettings = AppSettings.Default

    /** Always-current local copy of mappings, updated whenever DataStore emits. */
    private var currentMappings = emptyList<GestureMapping>()

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        startForeground(
            SmartTouchApp.NOTIFICATION_ID_SERVICE,
            buildNotification(),
        )
        collectSettings()
        collectMappings()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP) {
            stopSelf()
            return START_NOT_STICKY
        }
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        removeAllOverlays()
        scope.cancel()
    }

    // ── Data collection ──────────────────────────────────────────────────────

    private fun collectSettings() {
        scope.launch {
            settingsRepository.settingsFlow.collect { settings ->
                val zoneChanged = settings.gestureZoneSize != currentSettings.gestureZoneSize
                currentSettings = settings
                if (zoneChanged) rebuildOverlays()
            }
        }
    }

    private fun collectMappings() {
        scope.launch {
            gestureRepository.mappingsFlow.collect { mappings ->
                currentMappings = mappings
            }
        }
        // Build initial overlays once settings are first emitted.
        scope.launch {
            settingsRepository.settingsFlow.collect {
                rebuildOverlays()
                return@collect  // only rebuild once; subsequent rebuilds happen in collectSettings
            }
        }
    }

    // ── Overlay management ────────────────────────────────────────────────────

    private fun rebuildOverlays() {
        removeAllOverlays()
        val zoneSizePx = dpToPx(currentSettings.gestureZoneSize)
        val displayMetrics = resources.displayMetrics
        val screenW = displayMetrics.widthPixels
        val screenH = displayMetrics.heightPixels

        addZoneOverlay(GestureZone.TOP_EDGE,    screenW,    zoneSizePx, Gravity.TOP)
        addZoneOverlay(GestureZone.BOTTOM_EDGE, screenW,    zoneSizePx, Gravity.BOTTOM)
        addZoneOverlay(GestureZone.LEFT_EDGE,   zoneSizePx, screenH,    Gravity.START or Gravity.TOP)
        addZoneOverlay(GestureZone.RIGHT_EDGE,  zoneSizePx, screenH,    Gravity.END or Gravity.TOP)
    }

    private fun addZoneOverlay(zone: GestureZone, w: Int, h: Int, gravity: Int) {
        val detector = GestureDetector(this, currentSettings.sensitivity) { gestureType ->
            handleGesture(zone, gestureType)
        }

        val view = object : FrameLayout(this) {
            override fun onTouchEvent(event: MotionEvent): Boolean =
                detector.onTouchEvent(event)
        }
        view.setBackgroundColor(Color.TRANSPARENT)

        val params = WindowManager.LayoutParams(
            w, h,
            WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    or WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH,
            PixelFormat.TRANSLUCENT,
        ).apply {
            this.gravity = gravity
        }

        windowManager.addView(view, params)
        overlayViews.add(view)
    }

    private fun removeAllOverlays() {
        overlayViews.forEach { view ->
            runCatching { windowManager.removeView(view) }
        }
        overlayViews.clear()
    }

    /**
     * Looks up the matching mapping from the in-memory cache and executes the action.
     * No new coroutine or Flow collection is started — this is safe to call on every gesture.
     */
    private fun handleGesture(zone: GestureZone, gestureType: GestureType) {
        val mapping = currentMappings.firstOrNull {
            it.zone == zone && it.gestureType == gestureType && it.isEnabled
        } ?: return

        val accessibility = SmartTouchAccessibilityService.instance ?: return
        val executor = ActionExecutor(this, accessibility)
        executor.execute(mapping, currentSettings.hapticFeedback)
    }

    // ── Notification ──────────────────────────────────────────────────────────

    private fun buildNotification(): Notification {
        val contentIntent = PendingIntent.getActivity(
            this, 0,
            Intent(this, MainActivity::class.java),
            PendingIntent.FLAG_IMMUTABLE,
        )
        val stopIntent = PendingIntent.getService(
            this, 1,
            Intent(this, OverlayService::class.java).apply { action = ACTION_STOP },
            PendingIntent.FLAG_IMMUTABLE,
        )
        return NotificationCompat.Builder(this, SmartTouchApp.CHANNEL_ID_SERVICE)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_text))
            .setSmallIcon(R.drawable.ic_notification)
            .setContentIntent(contentIntent)
            .addAction(0, "Stop", stopIntent)
            .setOngoing(true)
            .setSilent(true)
            .build()
    }

    private fun dpToPx(dp: Int): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }

    companion object {
        const val ACTION_STOP = "com.smarttouch.app.ACTION_STOP_OVERLAY"

        fun start(context: Context) {
            context.startForegroundService(Intent(context, OverlayService::class.java))
        }

        fun stop(context: Context) {
            context.stopService(Intent(context, OverlayService::class.java))
        }
    }
}
