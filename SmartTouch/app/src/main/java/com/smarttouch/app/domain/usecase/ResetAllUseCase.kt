package com.smarttouch.app.domain.usecase

import com.smarttouch.app.data.repository.GestureRepository
import com.smarttouch.app.data.repository.SettingsRepository
import javax.inject.Inject

class ResetAllUseCase @Inject constructor(
    private val gestureRepository: GestureRepository,
    private val settingsRepository: SettingsRepository,
) {
    suspend operator fun invoke() {
        gestureRepository.resetAll()
        settingsRepository.reset()
    }
}
