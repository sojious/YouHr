package co.youverify.youhr.data.repository.auth

import co.youverify.youhr.core.util.Result
import co.youverify.youhr.data.model.*

interface AuthRemoteDataSource {

    suspend fun loginWithPassword(loginWithPassWordRequest: LoginWithPassWordRequest): Result<AuthResponse>

    suspend fun loginWithCode(loginWithCodeRequest: LoginWithCodeRequest): Result<AuthResponse>

    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Result<GenericResponse>

    suspend fun createCode(createCodeRequest: CreateCodeRequest): Result<GenericResponse>
    suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): Result<GenericResponse>
}