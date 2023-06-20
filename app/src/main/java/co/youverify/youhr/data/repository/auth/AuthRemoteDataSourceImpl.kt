package co.youverify.youhr.data.repository.auth

import co.youverify.youhr.core.util.Result
import co.youverify.youhr.core.util.handleApi
import co.youverify.youhr.data.model.*
import co.youverify.youhr.data.remote.YouHrService
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(private val youHrService: YouHrService): AuthRemoteDataSource {


    override suspend fun loginWithPassword(loginWithPassWordRequest: LoginWithPassWordRequest): Result<AuthResponse> =
        handleApi { youHrService.loginWithPassword(loginWithPassWordRequest) }

    override suspend fun loginWithCode(loginWithCodeRequest: LoginWithCodeRequest): Result<AuthResponse> =
        handleApi { youHrService.loginWithCode(loginWithCodeRequest) }

    override suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Result<GenericResponse> =
        handleApi { youHrService.resetPassword(resetPasswordRequest) }

    override suspend fun createCode(createCodeRequest: CreateCodeRequest): Result<GenericResponse> =
        handleApi { youHrService.createCode(createCodeRequest)}
}