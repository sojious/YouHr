package co.youverify.youhr.domain.use_case

import co.youverify.youhr.core.util.NetworkResult
import co.youverify.youhr.data.model.AuthResponse
import co.youverify.youhr.data.model.LoginWithCodeRequest
import co.youverify.youhr.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class LoginWithCodeUseCase(private val authRepository: AuthRepository) {

     suspend operator fun invoke(loginWithCodeRequest: LoginWithCodeRequest): Flow<NetworkResult<AuthResponse>> {
        return authRepository.loginWithCode(loginWithCodeRequest)
    }
}