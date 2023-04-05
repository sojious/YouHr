package co.youverify.youhr.domain.use_case

import co.youverify.youhr.core.util.EMPTY_PASSCODE_VALUE
import co.youverify.youhr.core.util.INPUT_ERROR_CODE
import co.youverify.youhr.core.util.NetworkResult
import co.youverify.youhr.data.model.AuthResponse
import co.youverify.youhr.data.model.LoginWithCodeRequest
import co.youverify.youhr.domain.repository.AuthRepository
import co.youverify.youhr.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginWithCodeUseCase @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val authRepository: AuthRepository
    ) {

     suspend operator fun invoke(loginWithCodeRequest: LoginWithCodeRequest): Flow<NetworkResult<AuthResponse>> {



         if (loginWithCodeRequest.passcode==EMPTY_PASSCODE_VALUE)
             return flow {
                 emit(NetworkResult.Error(code = INPUT_ERROR_CODE, message = "Passcode  cannot be empty!") )
             }

         if (loginWithCodeRequest.passcode.toString().length!=6)
             return flow {
                 emit(NetworkResult.Error(code = INPUT_ERROR_CODE, message = "Passcode  cannot be less than 6 digits!") )
             }


         //determine if user has a code
         val hasPasscode=preferencesRepository.getUserPasscodeCreationStatus().first()

         //get user email from app preferences
         val email=preferencesRepository.getUserEmail().first()

         //if the user haven't created a code, emit an Error
         if (!hasPasscode)
             return flow {
                 emit(NetworkResult.Error(code = INPUT_ERROR_CODE, message = "Login with your password and create a code first!") )
             }



        return authRepository.loginWithCode(loginWithCodeRequest.copy(email=email))
    }
}