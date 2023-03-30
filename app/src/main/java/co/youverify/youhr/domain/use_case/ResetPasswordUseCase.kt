package co.youverify.youhr.domain.use_case

import co.youverify.youhr.core.util.NetworkResult
import co.youverify.youhr.data.model.GenericResponse
import co.youverify.youhr.data.model.ResetPasswordRequest
import co.youverify.youhr.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class ResetPasswordUseCase(private val authRepository: AuthRepository) {

     suspend operator fun invoke(resetPasswordRequest: ResetPasswordRequest): Flow<NetworkResult<GenericResponse>> {
        return authRepository.resetPassword(resetPasswordRequest)
    }
}