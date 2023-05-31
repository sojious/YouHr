package co.youverify.youhr.domain.use_case

import co.youverify.youhr.core.util.EMPTY_PASSCODE_VALUE
import co.youverify.youhr.core.util.INPUT_ERROR_CODE
import co.youverify.youhr.core.util.Result
import co.youverify.youhr.data.model.GenericResponse
import co.youverify.youhr.domain.repository.AuthRepository
import co.youverify.youhr.domain.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CreateCodeUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val preferencesRepository: PreferencesRepository
    ) {

     suspend operator fun invoke(
         createCodeRequest: co.youverify.youhr.data.model.CreateCodeRequest,
         passcode1: Int
     ): Flow<Result<GenericResponse>> {

         if (createCodeRequest.passcode== EMPTY_PASSCODE_VALUE)
             return flow {
                 emit(Result.Error(code = INPUT_ERROR_CODE, message = "Passcode  cannot be empty!") )
             }

         if (createCodeRequest.passcode.toString().length!=6)
             return flow {
                 emit(Result.Error(code = INPUT_ERROR_CODE, message = "Passcode  cannot be less than 6 digits!") )
             }

         if (createCodeRequest.passcode!=passcode1)
             return flow {
                 emit(Result.Error(code = INPUT_ERROR_CODE, message = "Passcodes  do not match! Try again") )
             }




        return authRepository.createCode(createCodeRequest)
    }
}