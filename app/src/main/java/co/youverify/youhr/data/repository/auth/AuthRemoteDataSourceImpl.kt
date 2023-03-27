package co.youverify.youhr.data.repository.auth

import co.youverify.youhr.data.model.*
import co.youverify.youhr.data.remote.YouHrService

class AuthRemoteDataSourceImpl(youHrService: YouHrService):AuthRemoteDataSource {


    override suspend fun loginWithPassword(loginWithPassWordRequest: LoginWithPassWordRequest): AuthResponse? {
        return null
    }

    override suspend fun loginWithCode(loginWithCodeRequest: LoginWithCodeRequest): AuthResponse? {
        return null
    }

    override suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): AuthResponse? {
        return null
    }

    override suspend fun createCode(createCodeRequest: CreateCodeRequest): AuthResponse? {
        return null
    }
}