package com.smarttouch.app.data.model

/**
 * Binds a [GestureType] within a [GestureZone] to a [GestureAction].
 *
 * @param zone         The screen region monitored.
 * @param gestureType  The type of gesture performed.
 * @param action       The action to execute.
 * @param appPackageName  Optional package name when [action] == [GestureAction.OPEN_APP].
 * @param isEnabled    Whether this mapping is active.
 */
data class GestureMapping(
    val zone: GestureZone,
    val gestureType: GestureType,
    val action: GestureAction = GestureAction.NONE,
    val appPackageName: String = "",
    val isEnabled: Boolean = true,
) {
    /** Stable key used for DataStore serialisation. */
    val id: String get() = "${zone.key}__${gestureType.key}"
}
