package com.smarttouch.app.service

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.util.Log

/**
 * SmartTouch Accessibility Service.
 *
 * Its primary purpose is to provide a live [AccessibilityService] handle to
 * [ActionExecutor] for global actions (lock screen, screenshot, etc.).
 *
 * It does NOT intercept key events or monitor on-screen content —
 * gesture detection happens via the [OverlayService] and [GestureDetector].
 *
 * Privacy note: canRetrieveWindowContent is false in the XML config.
 */
class SmartTouchAccessibilityService : AccessibilityService() {

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        Log.d(TAG, "Accessibility service connected")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // Not used — SmartTouch does not monitor screen content.
    }

    override fun onInterrupt() {
        Log.d(TAG, "Accessibility service interrupted")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (instance === this) instance = null
        Log.d(TAG, "Accessibility service destroyed")
    }

    companion object {
        private const val TAG = "SmartTouchA11y"

        /** Live reference set when the service connects. Null when disconnected. */
        @Volatile
        var instance: SmartTouchAccessibilityService? = null
            private set
    }
}
