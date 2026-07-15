package com.smarttouch.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smarttouch.app.data.model.GestureMapping
import com.smarttouch.app.data.model.GestureZone
import com.smarttouch.app.domain.usecase.GetGestureMappingsUseCase
import com.smarttouch.app.domain.usecase.SaveGestureMappingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GestureConfigViewModel @Inject constructor(
    getMappings: GetGestureMappingsUseCase,
    private val saveMapping: SaveGestureMappingUseCase,
) : ViewModel() {

    /** All mappings grouped by [GestureZone]. */
    val mappingsByZone: StateFlow<Map<GestureZone, List<GestureMapping>>> =
        getMappings()
            .map { list -> list.groupBy { it.zone } }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyMap(),
            )

    fun updateMapping(mapping: GestureMapping) {
        viewModelScope.launch { saveMapping(mapping) }
    }
}
