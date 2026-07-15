package com.smarttouch.app.data.repository

import com.smarttouch.app.data.datastore.SettingsDataStore
import com.smarttouch.app.data.model.AppSettings
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: SettingsDataStore,
) : SettingsRepository {

    override val settingsFlow: Flow<AppSettings> = dataStore.settingsFlow

    override suspend fun updateServiceEnabled(enabled: Boolean) =
        dataStore.updateServiceEnabled(enabled)

    override suspend fun updateHapticFeedback(enabled: Boolean) =
        dataStore.updateHapticFeedback(enabled)

    override suspend fun updateStartOnBoot(enabled: Boolean) =
        dataStore.updateStartOnBoot(enabled)

    override suspend fun updateSensitivity(value: Int) =
        dataStore.updateSensitivity(value)

    override suspend fun updateGestureZoneSize(dp: Int) =
        dataStore.updateGestureZoneSize(dp)

    override suspend fun reset() = dataStore.reset()
}
