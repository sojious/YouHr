package co.youverify.youhr.domain.repository

import co.youverify.youhr.core.util.NetworkResult
import co.youverify.youhr.data.model.*
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun loginWithPassword(loginWithPassWordRequest: LoginWithPassWordRequest): Flow<NetworkResult<AuthResponse>>

    suspend fun loginWithCode(loginWithCodeRequest: LoginWithCodeRequest): Flow<NetworkResult<AuthResponse>>

   suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Flow<NetworkResult<GenericResponse>>

    suspend fun createCode(createCodeRequest: CreateCodeRequest): Flow<NetworkResult<GenericResponse>>
}