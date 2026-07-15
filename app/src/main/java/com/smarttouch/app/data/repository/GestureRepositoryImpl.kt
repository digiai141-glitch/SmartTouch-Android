package com.smarttouch.app.data.repository

import com.smarttouch.app.data.datastore.GestureMappingDataStore
import com.smarttouch.app.data.model.GestureMapping
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GestureRepositoryImpl @Inject constructor(
    private val dataStore: GestureMappingDataStore,
) : GestureRepository {

    override val mappingsFlow: Flow<List<GestureMapping>> = dataStore.mappingsFlow

    override suspend fun updateMapping(mapping: GestureMapping) =
        dataStore.updateMapping(mapping)

    override suspend fun resetAll() = dataStore.resetAll()
}
