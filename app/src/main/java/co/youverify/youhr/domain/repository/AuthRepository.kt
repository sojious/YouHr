package co.youverify.youhr.domain.repository

import co.youverify.youhr.data.model.LoginWithCodeRequest
import co.youverify.youhr.data.model.LoginWithPassWordRequest
import co.youverify.youhr.data.model.ResetPasswordRequest

interface AuthRepository {

    fun loginWithPassword(loginWithPassWordRequest: LoginWithPassWordRequest)

    fun loginWithCode(loginWithCodeRequest: LoginWithCodeRequest)

    fun loginWithCode(resetPasswordRequest: ResetPasswordRequest)
}