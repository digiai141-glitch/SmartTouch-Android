package com.smarttouch.app.service

import android.accessibilityservice.AccessibilityService
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.media.AudioManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.provider.MediaStore
import android.util.Log
import android.view.KeyEvent
import com.smarttouch.app.data.model.GestureAction
import com.smarttouch.app.data.model.GestureMapping

private const val TAG = "ActionExecutor"

/**
 * Executes the system action bound to a [GestureMapping].
 *
 * Requires an [AccessibilityService] reference for global-action operations
 * (screen lock, screenshot, notification panel).
 *
 * Privacy note: no network calls are made; all operations are entirely on-device.
 */
class ActionExecutor(
    private val context: Context,
    private val accessibilityService: AccessibilityService,
) {
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    private val vibrator: Vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        (context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager)
            .defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    private val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    private val torchCameraId: String? by lazy { findTorchCamera() }

    /** Tracks flashlight state between calls. */
    private var flashlightOn = false

    // ── Dispatch ──────────────────────────────────────────────────────────────

    fun execute(mapping: GestureMapping, hapticEnabled: Boolean) {
        if (hapticEnabled) hapticTick()
        when (mapping.action) {
            GestureAction.NONE             -> Unit
            GestureAction.SCREEN_LOCK      -> lockScreen()
            GestureAction.FLASHLIGHT       -> toggleFlashlight()
            GestureAction.VOLUME_UP        -> changeVolume(AudioManager.ADJUST_RAISE)
            GestureAction.VOLUME_DOWN      -> changeVolume(AudioManager.ADJUST_LOWER)
            GestureAction.MUTE             -> toggleMute()
            GestureAction.VIBRATE          -> toggleVibrate()
            GestureAction.MEDIA_PLAY_PAUSE -> sendMediaKey(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE)
            GestureAction.MEDIA_NEXT       -> sendMediaKey(KeyEvent.KEYCODE_MEDIA_NEXT)
            GestureAction.MEDIA_PREV       -> sendMediaKey(KeyEvent.KEYCODE_MEDIA_PREVIOUS)
            GestureAction.QUICK_SETTINGS   -> expandQuickSettings()
            GestureAction.NOTIFICATION_PANEL -> expandNotificationPanel()
            GestureAction.CAMERA           -> openCamera()
            GestureAction.ASSISTANT        -> openAssistant()
            GestureAction.SCREENSHOT       -> takeScreenshot()
            GestureAction.OPEN_APP         -> openApp(mapping.appPackageName)
        }
    }

    // ── Individual actions ───────────────────────────────────────────────────

    private fun lockScreen() {
        accessibilityService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_LOCK_SCREEN)
    }

    private fun toggleFlashlight() {
        val id = torchCameraId ?: run {
            Log.w(TAG, "No torch camera found")
            return
        }
        flashlightOn = !flashlightOn
        runCatching {
            cameraManager.setTorchMode(id, flashlightOn)
        }.onFailure { e ->
            Log.e(TAG, "Torch error: ${e.message}")
            flashlightOn = !flashlightOn // revert
        }
    }

    private fun findTorchCamera(): String? = runCatching {
        cameraManager.cameraIdList.firstOrNull { id ->
            cameraManager.getCameraCharacteristics(id)
                .get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
        }
    }.getOrNull()

    private fun changeVolume(direction: Int) {
        audioManager.adjustStreamVolume(
            AudioManager.STREAM_MUSIC,
            direction,
            AudioManager.FLAG_SHOW_UI,
        )
    }

    private fun toggleMute() {
        val isMuted = audioManager.ringerMode == AudioManager.RINGER_MODE_SILENT
        audioManager.ringerMode =
            if (isMuted) AudioManager.RINGER_MODE_NORMAL else AudioManager.RINGER_MODE_SILENT
    }

    private fun toggleVibrate() {
        val isVibrate = audioManager.ringerMode == AudioManager.RINGER_MODE_VIBRATE
        audioManager.ringerMode =
            if (isVibrate) AudioManager.RINGER_MODE_NORMAL else AudioManager.RINGER_MODE_VIBRATE
    }

    private fun sendMediaKey(keyCode: Int) {
        audioManager.dispatchMediaKeyEvent(KeyEvent(KeyEvent.ACTION_DOWN, keyCode))
        audioManager.dispatchMediaKeyEvent(KeyEvent(KeyEvent.ACTION_UP, keyCode))
    }

    @Suppress("DEPRECATION")
    private fun expandQuickSettings() {
        // Primary: accessibility global action (API 31+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            accessibilityService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_QUICK_SETTINGS)
            return
        }
        // Fallback: internal status bar API
        runCatching {
            val cls = Class.forName("android.app.StatusBarManager")
            val statusBar = context.getSystemService("statusbar") ?: return
            cls.getMethod("expandSettingsPanel").invoke(statusBar)
        }.onFailure {
            accessibilityService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_QUICK_SETTINGS)
        }
    }

    @Suppress("DEPRECATION")
    private fun expandNotificationPanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            accessibilityService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_NOTIFICATIONS)
            return
        }
        runCatching {
            val cls = Class.forName("android.app.StatusBarManager")
            val statusBar = context.getSystemService("statusbar") ?: return
            cls.getMethod("expandNotificationsPanel").invoke(statusBar)
        }.onFailure {
            accessibilityService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_NOTIFICATIONS)
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startIfResolvable(intent)
    }

    private fun openAssistant() {
        // Standard voice assistant intent
        val intent = Intent(Intent.ACTION_VOICE_COMMAND).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        if (!startIfResolvable(intent)) {
            // Fallback: search intent handled by assistant
            val searchIntent = Intent(Intent.ACTION_SEARCH_LONG_PRESS).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            startIfResolvable(searchIntent)
        }
    }

    private fun takeScreenshot() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            accessibilityService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_TAKE_SCREENSHOT)
        }
        // Below API 30 screenshot via accessibility is not supported; action is silently ignored.
    }

    private fun openApp(packageName: String) {
        if (packageName.isBlank()) {
            Log.w(TAG, "openApp called with blank packageName")
            return
        }
        val intent = context.packageManager
            .getLaunchIntentForPackage(packageName)
            ?.apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK }

        if (intent != null) {
            context.startActivity(intent)
        } else {
            Log.w(TAG, "No launch intent for: $packageName")
        }
    }

    /** Returns true and starts the activity if the intent resolves to at least one activity. */
    private fun startIfResolvable(intent: Intent): Boolean {
        return if (context.packageManager
                .resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY) != null
        ) {
            context.startActivity(intent)
            true
        } else false
    }

    private fun hapticTick() = runCatching {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            vibrator.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_TICK))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(20L)
        }
    }
}
