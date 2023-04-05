package co.youverify.youhr.domain.use_case

import co.youverify.youhr.core.util.INPUT_ERROR_CODE
import co.youverify.youhr.core.util.NetworkResult
import co.youverify.youhr.data.model.GenericResponse
import co.youverify.youhr.data.model.ResetPasswordRequest
import co.youverify.youhr.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(private val authRepository: AuthRepository) {

     suspend operator fun invoke(resetPasswordRequest: ResetPasswordRequest): Flow<NetworkResult<GenericResponse>> {

         if (resetPasswordRequest.email.isEmpty())
             return flow {
                emit(NetworkResult.Error(code = INPUT_ERROR_CODE, message = "Email cannot be empty!"))
             }

        return authRepository.resetPassword(resetPasswordRequest)
    }
}