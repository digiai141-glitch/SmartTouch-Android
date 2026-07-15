package com.smarttouch.app.data.model

import com.smarttouch.app.R

/**
 * Represents a distinct screen region that SmartTouch monitors for gestures.
 */
enum class GestureZone(val key: String, val displayNameRes: Int) {
    TOP_NOTCH("top_notch", R.string.zone_top_notch),
    TOP_EDGE("top_edge", R.string.zone_top_edge),
    LEFT_EDGE("left_edge", R.string.zone_left_edge),
    RIGHT_EDGE("right_edge", R.string.zone_right_edge),
    BOTTOM_EDGE("bottom_edge", R.string.zone_bottom_edge),
    FLOATING("floating", R.string.zone_floating);

    companion object {
        fun fromKey(key: String): GestureZone =
            entries.firstOrNull { it.key == key } ?: TOP_EDGE
    }
}
