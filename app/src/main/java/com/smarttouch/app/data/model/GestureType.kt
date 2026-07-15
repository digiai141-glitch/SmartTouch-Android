package com.smarttouch.app.data.model

import com.smarttouch.app.R

/**
 * The type of physical gesture the user performs within a [GestureZone].
 */
enum class GestureType(val key: String, val displayNameRes: Int) {
    SINGLE_TAP("single_tap", R.string.gesture_single_tap),
    DOUBLE_TAP("double_tap", R.string.gesture_double_tap),
    LONG_PRESS("long_press", R.string.gesture_long_press),
    SWIPE_LEFT("swipe_left", R.string.gesture_swipe_left),
    SWIPE_RIGHT("swipe_right", R.string.gesture_swipe_right),
    SWIPE_UP("swipe_up", R.string.gesture_swipe_up),
    SWIPE_DOWN("swipe_down", R.string.gesture_swipe_down);

    companion object {
        fun fromKey(key: String): GestureType =
            entries.firstOrNull { it.key == key } ?: SINGLE_TAP
    }
}
