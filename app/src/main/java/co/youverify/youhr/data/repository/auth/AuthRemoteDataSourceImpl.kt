package co.youverify.youhr.data.repository.auth

import co.youverify.youhr.core.util.NetworkResult
import co.youverify.youhr.core.util.handleApi
import co.youverify.youhr.data.model.*
import co.youverify.youhr.data.remote.YouHrService
import javax.inject.Inject

class AuthRemoteDataSourceImpl @Inject constructor(  val youHrService: YouHrService):AuthRemoteDataSource {


    override suspend fun loginWithPassword(loginWithPassWordRequest: LoginWithPassWordRequest): NetworkResult<AuthResponse> =
        handleApi { youHrService.loginWithPassword(loginWithPassWordRequest) }

    override suspend fun loginWithCode(loginWithCodeRequest: LoginWithCodeRequest): NetworkResult<AuthResponse> =
        handleApi { youHrService.loginWithCode(loginWithCodeRequest) }

    override suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): NetworkResult<GenericResponse> =
        handleApi { youHrService.resetPassword(resetPasswordRequest) }

    override suspend fun createCode(createCodeRequest: CreateCodeRequest):  NetworkResult<GenericResponse> =
        handleApi { youHrService.createCode(createCodeRequest)}
}