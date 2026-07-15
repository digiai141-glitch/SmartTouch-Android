package com.smarttouch.app.domain.usecase

import com.smarttouch.app.data.model.GestureMapping
import com.smarttouch.app.data.repository.GestureRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetGestureMappingsUseCase @Inject constructor(
    private val repository: GestureRepository,
) {
    operator fun invoke(): Flow<List<GestureMapping>> = repository.mappingsFlow
}
