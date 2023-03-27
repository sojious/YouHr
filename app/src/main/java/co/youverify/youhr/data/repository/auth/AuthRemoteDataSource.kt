package co.youverify.youhr.data.repository.auth

import co.youverify.youhr.data.model.*

interface AuthRemoteDataSource {

    suspend fun loginWithPassword(loginWithPassWordRequest: LoginWithPassWordRequest):AuthResponse?

    suspend fun loginWithCode(loginWithCodeRequest: LoginWithCodeRequest):AuthResponse?

    suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest):AuthResponse?

    suspend fun createCode(createCodeRequest: CreateCodeRequest):AuthResponse?
}