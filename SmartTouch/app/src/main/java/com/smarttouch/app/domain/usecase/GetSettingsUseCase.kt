package com.smarttouch.app.domain.usecase

import com.smarttouch.app.data.model.AppSettings
import com.smarttouch.app.data.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository,
) {
    operator fun invoke(): Flow<AppSettings> = repository.settingsFlow
}
