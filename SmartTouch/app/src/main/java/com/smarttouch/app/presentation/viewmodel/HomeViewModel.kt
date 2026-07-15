package com.smarttouch.app.presentation.viewmodel

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smarttouch.app.data.model.AppSettings
import com.smarttouch.app.domain.usecase.GetSettingsUseCase
import com.smarttouch.app.domain.usecase.UpdateSettingsUseCase
import com.smarttouch.app.service.OverlayService
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    getSettings: GetSettingsUseCase,
    private val updateSettings: UpdateSettingsUseCase,
) : ViewModel() {

    val settings: StateFlow<AppSettings> = getSettings()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AppSettings.Default,
        )

    fun toggleService(enabled: Boolean) {
        viewModelScope.launch {
            updateSettings.setServiceEnabled(enabled)
            if (enabled) {
                OverlayService.start(context)
            } else {
                OverlayService.stop(context)
            }
        }
    }

    fun hasOverlayPermission(): Boolean =
        Settings.canDrawOverlays(context)

    fun requestOverlayPermission(): Intent =
        Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${context.packageName}"),
        )

    fun openAccessibilitySettings(): Intent =
        Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
}
