package com.smarttouch.app.data.model

/**
 * Global app preferences stored via DataStore.
 *
 * @param isServiceEnabled   Whether the overlay/gesture service should run.
 * @param hapticFeedback     Vibrate when a gesture fires.
 * @param startOnBoot        Re-enable service after device reboot.
 * @param sensitivity        Gesture detection sensitivity 1–10 (default 5).
 * @param gestureZoneSize    Thickness of edge zones in dp (default 24).
 */
data class AppSettings(
    val isServiceEnabled: Boolean = false,
    val hapticFeedback: Boolean = true,
    val startOnBoot: Boolean = true,
    val sensitivity: Int = 5,
    val gestureZoneSize: Int = 24,
) {
    companion object {
        val Default = AppSettings()
    }
}
