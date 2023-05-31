package co.youverify.youhr.domain.repository

import co.youverify.youhr.core.util.Result
import co.youverify.youhr.data.model.*
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun loginWithPassword(loginWithPassWordRequest: LoginWithPassWordRequest): Flow<Result<AuthResponse>>

    suspend fun loginWithCode(loginWithCodeRequest: LoginWithCodeRequest): Flow<Result<AuthResponse>>

   suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Flow<Result<GenericResponse>>

    suspend fun createCode(createCodeRequest: CreateCodeRequest): Flow<Result<GenericResponse>>


}