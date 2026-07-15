package com.smarttouch.app.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.smarttouch.app.data.model.AppSettings
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "app_settings")

@Singleton
class SettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val dataStore = context.settingsDataStore

    val settingsFlow: Flow<AppSettings> = dataStore.data.map { prefs ->
        AppSettings(
            isServiceEnabled = prefs[Keys.SERVICE_ENABLED] ?: false,
            hapticFeedback = prefs[Keys.HAPTIC_FEEDBACK] ?: true,
            startOnBoot = prefs[Keys.START_ON_BOOT] ?: true,
            sensitivity = prefs[Keys.SENSITIVITY] ?: 5,
            gestureZoneSize = prefs[Keys.ZONE_SIZE] ?: 24,
        )
    }

    suspend fun updateServiceEnabled(enabled: Boolean) = dataStore.edit { prefs ->
        prefs[Keys.SERVICE_ENABLED] = enabled
    }

    suspend fun updateHapticFeedback(enabled: Boolean) = dataStore.edit { prefs ->
        prefs[Keys.HAPTIC_FEEDBACK] = enabled
    }

    suspend fun updateStartOnBoot(enabled: Boolean) = dataStore.edit { prefs ->
        prefs[Keys.START_ON_BOOT] = enabled
    }

    suspend fun updateSensitivity(value: Int) = dataStore.edit { prefs ->
        prefs[Keys.SENSITIVITY] = value.coerceIn(1, 10)
    }

    suspend fun updateGestureZoneSize(dp: Int) = dataStore.edit { prefs ->
        prefs[Keys.ZONE_SIZE] = dp.coerceIn(8, 64)
    }

    suspend fun reset() = dataStore.edit { prefs ->
        prefs.clear()
    }

    private object Keys {
        val SERVICE_ENABLED = booleanPreferencesKey("service_enabled")
        val HAPTIC_FEEDBACK = booleanPreferencesKey("haptic_feedback")
        val START_ON_BOOT = booleanPreferencesKey("start_on_boot")
        val SENSITIVITY = intPreferencesKey("sensitivity")
        val ZONE_SIZE = intPreferencesKey("zone_size")
    }
}
