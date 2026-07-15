package com.smarttouch.app.service

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.ViewConfiguration
import com.smarttouch.app.data.model.GestureType
import kotlin.math.abs

/**
 * Stateful gesture recogniser that converts raw [MotionEvent] streams into
 * high-level [GestureType] callbacks.
 *
 * All callbacks arrive on the thread that calls [onTouchEvent].
 */
class GestureDetector(
    context: Context,
    private val sensitivity: Int = 5,
    private val onGestureDetected: (GestureType) -> Unit,
) {

    private val viewConfig = ViewConfiguration.get(context)
    private val handler = Handler(Looper.getMainLooper())

    /** Minimum swipe distance in pixels, scaled by inverse sensitivity. */
    private val swipeMinDistance: Int
        get() = (100 - sensitivity * 8).coerceAtLeast(20)

    /** Maximum time (ms) allowed for a swipe gesture. */
    private val swipeMaxDuration: Long = 500L

    /** Long press threshold. */
    private val longPressTimeout: Long =
        ViewConfiguration.getLongPressTimeout().toLong()

    /** Double-tap threshold. */
    private val doubleTapTimeout: Long =
        ViewConfiguration.getDoubleTapTimeout().toLong()

    private var downX = 0f
    private var downY = 0f
    private var downTime = 0L
    private var lastTapTime = 0L
    private var waitingForDoubleTap = false
    private var longPressPosted = false

    private val longPressRunnable = Runnable {
        longPressPosted = false
        onGestureDetected(GestureType.LONG_PRESS)
    }

    fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                downX = event.rawX
                downY = event.rawY
                downTime = System.currentTimeMillis()
                postLongPress()
            }

            MotionEvent.ACTION_MOVE -> {
                val dx = abs(event.rawX - downX)
                val dy = abs(event.rawY - downY)
                if (dx > viewConfig.scaledTouchSlop || dy > viewConfig.scaledTouchSlop) {
                    cancelLongPress()
                }
            }

            MotionEvent.ACTION_UP -> {
                cancelLongPress()
                val dx = event.rawX - downX
                val dy = event.rawY - downY
                val duration = System.currentTimeMillis() - downTime
                val absDx = abs(dx)
                val absDy = abs(dy)

                when {
                    duration < swipeMaxDuration && absDx > swipeMinDistance && absDx > absDy -> {
                        // Horizontal swipe
                        if (dx > 0) onGestureDetected(GestureType.SWIPE_RIGHT)
                        else onGestureDetected(GestureType.SWIPE_LEFT)
                    }

                    duration < swipeMaxDuration && absDy > swipeMinDistance && absDy > absDx -> {
                        // Vertical swipe
                        if (dy > 0) onGestureDetected(GestureType.SWIPE_DOWN)
                        else onGestureDetected(GestureType.SWIPE_UP)
                    }

                    else -> {
                        // Tap family
                        val now = System.currentTimeMillis()
                        if (waitingForDoubleTap && now - lastTapTime < doubleTapTimeout) {
                            waitingForDoubleTap = false
                            handler.removeCallbacksAndMessages(SINGLE_TAP_TOKEN)
                            onGestureDetected(GestureType.DOUBLE_TAP)
                        } else {
                            waitingForDoubleTap = true
                            lastTapTime = now
                            handler.postAtTime(
                                {
                                    waitingForDoubleTap = false
                                    onGestureDetected(GestureType.SINGLE_TAP)
                                },
                                SINGLE_TAP_TOKEN,
                                android.os.SystemClock.uptimeMillis() + doubleTapTimeout,
                            )
                        }
                    }
                }
            }

            MotionEvent.ACTION_CANCEL -> cancelLongPress()
        }
        return true
    }

    private fun postLongPress() {
        longPressPosted = true
        handler.postDelayed(longPressRunnable, longPressTimeout)
    }

    private fun cancelLongPress() {
        if (longPressPosted) {
            handler.removeCallbacks(longPressRunnable)
            longPressPosted = false
        }
    }

    fun destroy() {
        handler.removeCallbacksAndMessages(null)
    }

    companion object {
        private val SINGLE_TAP_TOKEN = Any()
    }
}
