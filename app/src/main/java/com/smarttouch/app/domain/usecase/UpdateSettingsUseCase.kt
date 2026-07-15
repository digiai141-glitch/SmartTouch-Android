package com.smarttouch.app.domain.usecase

import com.smarttouch.app.data.repository.SettingsRepository
import javax.inject.Inject

class UpdateSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository,
) {
    suspend fun setServiceEnabled(enabled: Boolean) = repository.updateServiceEnabled(enabled)
    suspend fun setHapticFeedback(enabled: Boolean) = repository.updateHapticFeedback(enabled)
    suspend fun setStartOnBoot(enabled: Boolean) = repository.updateStartOnBoot(enabled)
    suspend fun setSensitivity(value: Int) = repository.updateSensitivity(value)
    suspend fun setGestureZoneSize(dp: Int) = repository.updateGestureZoneSize(dp)
    suspend fun reset() = repository.reset()
}
