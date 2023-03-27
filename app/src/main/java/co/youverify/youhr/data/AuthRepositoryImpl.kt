package co.youverify.youhr.data

import co.youverify.youhr.data.model.LoginWithCodeRequest
import co.youverify.youhr.data.model.LoginWithPassWordRequest
import co.youverify.youhr.data.model.ResetPasswordRequest
import co.youverify.youhr.data.repository.auth.AuthRemoteDataSource
import co.youverify.youhr.domain.repository.AuthRepository

class AuthRepositoryImpl(authRemoteDataSource: AuthRemoteDataSource):AuthRepository {

    override fun loginWithPassword(loginWithPassWordRequest: LoginWithPassWordRequest) {
        TODO("Not yet implemented")
    }

    override fun loginWithCode(loginWithCodeRequest: LoginWithCodeRequest) {
        TODO("Not yet implemented")
    }

    override fun loginWithCode(resetPasswordRequest: ResetPasswordRequest) {
        TODO("Not yet implemented")
    }
}