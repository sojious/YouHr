package co.youverify.youhr.domain.use_case

import co.youverify.youhr.core.util.NetworkResult
import co.youverify.youhr.data.model.AuthResponse
import co.youverify.youhr.data.model.LoginWithPassWordRequest
import co.youverify.youhr.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LoginWithPasswordUseCase(private val authRepository: AuthRepository) {

     suspend operator fun invoke(loginWithPassWordRequest: LoginWithPassWordRequest): Flow<NetworkResult<AuthResponse>> {

         if (loginWithPassWordRequest.password.isEmpty()||loginWithPassWordRequest.password.length<6)
             return flow { emit(NetworkResult.Error(code = 200, message = "password cannot be empty")) }

        return authRepository.loginWithPassword(loginWithPassWordRequest)
    }
}