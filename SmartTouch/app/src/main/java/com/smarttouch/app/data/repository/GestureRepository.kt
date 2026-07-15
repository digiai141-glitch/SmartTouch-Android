package com.smarttouch.app.data.repository

import com.smarttouch.app.data.model.GestureMapping
import kotlinx.coroutines.flow.Flow

interface GestureRepository {
    val mappingsFlow: Flow<List<GestureMapping>>
    suspend fun updateMapping(mapping: GestureMapping)
    suspend fun resetAll()
}
