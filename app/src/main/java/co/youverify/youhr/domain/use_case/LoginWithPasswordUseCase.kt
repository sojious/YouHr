package co.youverify.youhr.domain.use_case

import co.youverify.youhr.core.util.INPUT_ERROR_CODE
import co.youverify.youhr.core.util.NetworkResult
import co.youverify.youhr.data.model.AuthResponse
import co.youverify.youhr.data.model.LoginWithPassWordRequest
import co.youverify.youhr.domain.repository.AuthRepository
import co.youverify.youhr.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginWithPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferencesRepository: PreferencesRepository
) {

     suspend operator fun invoke(loginWithPassWordRequest: LoginWithPassWordRequest): Flow<NetworkResult<AuthResponse>> {

         var newLoginRequest=loginWithPassWordRequest
         val emailFromPreferences=preferencesRepository.getUserEmail().first()

         if (emailFromPreferences.isNotEmpty())
             newLoginRequest=loginWithPassWordRequest.copy(email = emailFromPreferences)


         if (newLoginRequest.email.isEmpty())
         //emit an successful network response with an error body
             return flow { emit(NetworkResult.Error(code = INPUT_ERROR_CODE, message = "No email entered!! Go back and enter your emai")) }


         if (newLoginRequest.password.isEmpty())
             //emit an successful network response with an error body
             return flow { emit(NetworkResult.Error(code = INPUT_ERROR_CODE, message = "password cannot be empty")) }

        return authRepository.loginWithPassword(newLoginRequest)
    }
}