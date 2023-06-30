package co.youverify.youhr.domain.use_case

import co.youverify.youhr.core.util.INPUT_ERROR_CODE
import co.youverify.youhr.core.util.Result
import co.youverify.youhr.data.model.ChangePasswordRequest
import co.youverify.youhr.data.model.GenericResponse
import co.youverify.youhr.data.model.ResetPasswordRequest
import co.youverify.youhr.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ChangePasswordUseCase @Inject constructor(private val authRepository: AuthRepository) {

     suspend operator fun invoke(changePasswordRequest: ChangePasswordRequest): Flow<Result<GenericResponse>> {
        return authRepository.changePassword(changePasswordRequest)
    }
}