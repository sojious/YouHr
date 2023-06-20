package co.youverify.youhr.data

import co.youverify.youhr.core.util.Result
import co.youverify.youhr.data.model.*
import co.youverify.youhr.data.repository.auth.AuthRemoteDataSource
import co.youverify.youhr.domain.repository.AuthRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class AuthRepositoryImpl  @Inject constructor(private val authRemoteDataSource: AuthRemoteDataSource):AuthRepository {

    override suspend fun loginWithPassword(loginWithPassWordRequest: LoginWithPassWordRequest): Flow<Result<AuthResponse>> =
         flow {
             val networkResult = authRemoteDataSource.loginWithPassword(loginWithPassWordRequest)
             emit(networkResult)
         }

    override suspend fun loginWithCode(loginWithCodeRequest: LoginWithCodeRequest): Flow<Result<AuthResponse>> =
        flow {
        val networkResult = authRemoteDataSource.loginWithCode(loginWithCodeRequest)
        emit(networkResult)
    }

    override suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Flow<Result<GenericResponse>> =
        flow {
        val networkResult = authRemoteDataSource.resetPassword(resetPasswordRequest)
        emit(networkResult)
    }

    override suspend fun createCode(createCodeRequest: CreateCodeRequest): Flow<Result<GenericResponse>> =
        flow {

        val networkResult = authRemoteDataSource.createCode(createCodeRequest)
        emit(networkResult)
    }



}