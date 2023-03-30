package co.youverify.youhr.domain.use_case

import co.youverify.youhr.core.util.NetworkResult
import co.youverify.youhr.data.model.CreateCodeRequest
import co.youverify.youhr.data.model.GenericResponse
import co.youverify.youhr.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class CreateCodeUseCase(private val authRepository: AuthRepository) {

     suspend operator fun invoke(createCodeRequest: CreateCodeRequest): Flow<NetworkResult<GenericResponse>> {
        return authRepository.createCode(createCodeRequest)
    }
}