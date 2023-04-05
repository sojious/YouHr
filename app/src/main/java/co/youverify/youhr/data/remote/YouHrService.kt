package co.youverify.youhr.data.remote

import co.youverify.youhr.data.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface YouHrService {

    @POST("user/login")
    @Headers("No-Authentication: true")
    suspend fun loginWithPassword( @Body loginWithPassWordRequest: LoginWithPassWordRequest): Response<AuthResponse>

    @POST("user/loginpasscode")
    @Headers("No-Authentication: true")
    suspend fun loginWithCode( @Body loginWithCodeRequest: LoginWithCodeRequest): Response<AuthResponse>

    @POST("user/resetpassword")
    @Headers("No-Authentication: true")
    suspend fun resetPassword( @Body resetPasswordRequest: ResetPasswordRequest): Response<GenericResponse>

    @POST("user/setpasscode")
    suspend fun createCode( @Body createCodeRequest:CreateCodeRequest): Response<GenericResponse>
}