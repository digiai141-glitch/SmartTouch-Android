package com.smarttouch.app.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.smarttouch.app.data.model.GestureAction
import com.smarttouch.app.data.model.GestureMapping
import com.smarttouch.app.data.model.GestureType
import com.smarttouch.app.data.model.GestureZone
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.mappingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "gesture_mappings")

@Singleton
class GestureMappingDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val dataStore = context.mappingsDataStore

    /** Emits the full list of gesture mappings whenever any value changes. */
    val mappingsFlow: Flow<List<GestureMapping>> = dataStore.data.map { prefs ->
        buildDefaultMappings().map { default ->
            val actionKey = prefs[actionKey(default.id)] ?: default.action.key
            val appPkg = prefs[appPkgKey(default.id)] ?: ""
            val enabled = prefs[enabledKey(default.id)] ?: true
            default.copy(
                action = GestureAction.fromKey(actionKey),
                appPackageName = appPkg,
                isEnabled = enabled,
            )
        }
    }

    suspend fun updateMapping(mapping: GestureMapping) = dataStore.edit { prefs ->
        prefs[actionKey(mapping.id)] = mapping.action.key
        prefs[appPkgKey(mapping.id)] = mapping.appPackageName
        prefs[enabledKey(mapping.id)] = mapping.isEnabled
    }

    suspend fun resetAll() = dataStore.edit { prefs -> prefs.clear() }

    // ---------- helpers ----------

    private fun actionKey(id: String) = stringPreferencesKey("${id}__action")
    private fun appPkgKey(id: String) = stringPreferencesKey("${id}__app")
    private fun enabledKey(id: String) = booleanPreferencesKey("${id}__enabled")

    /** Produces every legal zone × gesture combination as a default-action mapping. */
    private fun buildDefaultMappings(): List<GestureMapping> = buildList {
        for (zone in GestureZone.entries) {
            for (gesture in GestureType.entries) {
                add(GestureMapping(zone = zone, gestureType = gesture))
            }
        }
    }
}
