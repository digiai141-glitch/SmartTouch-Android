package com.smarttouch.app.domain.usecase

import com.smarttouch.app.data.model.GestureMapping
import com.smarttouch.app.data.repository.GestureRepository
import javax.inject.Inject

class SaveGestureMappingUseCase @Inject constructor(
    private val repository: GestureRepository,
) {
    suspend operator fun invoke(mapping: GestureMapping) = repository.updateMapping(mapping)
}
