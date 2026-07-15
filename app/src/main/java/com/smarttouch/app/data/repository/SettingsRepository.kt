package com.smarttouch.app.data.repository

import com.smarttouch.app.data.model.AppSettings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    val settingsFlow: Flow<AppSettings>
    suspend fun updateServiceEnabled(enabled: Boolean)
    suspend fun updateHapticFeedback(enabled: Boolean)
    suspend fun updateStartOnBoot(enabled: Boolean)
    suspend fun updateSensitivity(value: Int)
    suspend fun updateGestureZoneSize(dp: Int)
    suspend fun reset()
}
