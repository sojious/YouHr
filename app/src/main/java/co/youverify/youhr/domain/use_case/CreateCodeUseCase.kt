package co.youverify.youhr.domain.use_case

import co.youverify.youhr.core.util.EMPTY_PASSCODE_VALUE
import co.youverify.youhr.core.util.INPUT_ERROR_CODE
import co.youverify.youhr.core.util.NetworkResult
import co.youverify.youhr.data.model.CreateCodeRequest
import co.youverify.youhr.data.model.GenericResponse
import co.youverify.youhr.domain.repository.AuthRepository
import co.youverify.youhr.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateCodeUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferencesRepository: PreferencesRepository
    ) {

     suspend operator fun invoke(createCodeRequest: CreateCodeRequest): Flow<NetworkResult<GenericResponse>> {

         if (createCodeRequest.passcode== EMPTY_PASSCODE_VALUE)
             return flow {
                 emit(NetworkResult.Error(code = INPUT_ERROR_CODE, message = "Passcode  cannot be empty!") )
             }

         if (createCodeRequest.passcode.toString().length!=6)
             return flow {
                 emit(NetworkResult.Error(code = INPUT_ERROR_CODE, message = "Passcode  cannot be less than 6 digits!") )
             }




        return authRepository.createCode(createCodeRequest)
    }
}