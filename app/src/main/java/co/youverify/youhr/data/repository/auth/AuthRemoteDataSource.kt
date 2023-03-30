package co.youverify.youhr.data.repository.auth

import co.youverify.youhr.core.util.NetworkResult
import co.youverify.youhr.data.model.*

interface AuthRemoteDataSource {

    suspend fun loginWithPassword(loginWithPassWordRequest: LoginWithPassWordRequest): NetworkResult<AuthResponse>

    suspend fun loginWithCode(loginWithCodeRequest: LoginWithCodeRequest): NetworkResult<AuthResponse>

    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): NetworkResult<GenericResponse>

    suspend fun createCode(createCodeRequest: CreateCodeRequest): NetworkResult<GenericResponse>
}