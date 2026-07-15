package com.smarttouch.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smarttouch.app.data.model.AppSettings
import com.smarttouch.app.domain.usecase.GetSettingsUseCase
import com.smarttouch.app.domain.usecase.ResetAllUseCase
import com.smarttouch.app.domain.usecase.UpdateSettingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    getSettings: GetSettingsUseCase,
    private val updateSettings: UpdateSettingsUseCase,
    private val resetAll: ResetAllUseCase,
) : ViewModel() {

    val settings: StateFlow<AppSettings> = getSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AppSettings.Default,
        )

    fun setSensitivity(value: Int) {
        viewModelScope.launch { updateSettings.setSensitivity(value) }
    }

    fun setZoneSize(dp: Int) {
        viewModelScope.launch { updateSettings.setGestureZoneSize(dp) }
    }

    fun setHapticFeedback(enabled: Boolean) {
        viewModelScope.launch { updateSettings.setHapticFeedback(enabled) }
    }

    fun setStartOnBoot(enabled: Boolean) {
        viewModelScope.launch { updateSettings.setStartOnBoot(enabled) }
    }

    fun resetAllSettings() {
        viewModelScope.launch { resetAll() }
    }
}
