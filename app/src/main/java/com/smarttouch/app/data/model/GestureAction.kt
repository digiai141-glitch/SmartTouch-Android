package com.smarttouch.app.data.model

import com.smarttouch.app.R

/**
 * All actions that SmartTouch can execute when a gesture is recognised.
 *
 * [OPEN_APP] carries an optional [appPackageName] payload stored in [GestureMapping].
 */
enum class GestureAction(val key: String, val displayNameRes: Int) {
    NONE("none", R.string.action_none),
    SCREEN_LOCK("screen_lock", R.string.action_screen_lock),
    FLASHLIGHT("flashlight", R.string.action_flashlight),
    VOLUME_UP("volume_up", R.string.action_volume_up),
    VOLUME_DOWN("volume_down", R.string.action_volume_down),
    MUTE("mute", R.string.action_mute),
    VIBRATE("vibrate", R.string.action_vibrate),
    MEDIA_PLAY_PAUSE("media_play_pause", R.string.action_media_play_pause),
    MEDIA_NEXT("media_next", R.string.action_media_next),
    MEDIA_PREV("media_prev", R.string.action_media_prev),
    QUICK_SETTINGS("quick_settings", R.string.action_quick_settings),
    NOTIFICATION_PANEL("notification_panel", R.string.action_notification_panel),
    CAMERA("camera", R.string.action_camera),
    ASSISTANT("assistant", R.string.action_assistant),
    SCREENSHOT("screenshot", R.string.action_screenshot),
    OPEN_APP("open_app", R.string.action_open_app);

    companion object {
        fun fromKey(key: String): GestureAction =
            entries.firstOrNull { it.key == key } ?: NONE
    }
}
