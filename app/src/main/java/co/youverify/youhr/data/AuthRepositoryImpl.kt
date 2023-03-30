package co.youverify.youhr.data

import co.youverify.youhr.core.util.NetworkResult
import co.youverify.youhr.data.model.*
import co.youverify.youhr.data.repository.auth.AuthRemoteDataSource
import co.youverify.youhr.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl constructor( @Inject private val authRemoteDataSource: AuthRemoteDataSource):AuthRepository {

    override suspend fun loginWithPassword(loginWithPassWordRequest: LoginWithPassWordRequest): Flow<NetworkResult<AuthResponse>> =
         flow {
             emit(NetworkResult.Loading())

             val networkResult= authRemoteDataSource.loginWithPassword(loginWithPassWordRequest)
             emit(networkResult)
         }

    override suspend fun loginWithCode(loginWithCodeRequest: LoginWithCodeRequest): Flow<NetworkResult<AuthResponse>> =
        flow {
        emit(NetworkResult.Loading())

        val networkResult= authRemoteDataSource.loginWithCode(loginWithCodeRequest)
        emit(networkResult)
    }

    override suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Flow<NetworkResult<GenericResponse>> =
        flow {
        emit(NetworkResult.Loading())

        val networkResult= authRemoteDataSource.resetPassword(resetPasswordRequest)
        emit(networkResult)
    }

    override suspend fun createCode(createCodeRequest: CreateCodeRequest): Flow<NetworkResult<GenericResponse>> =
        flow {
        emit(NetworkResult.Loading())

        val networkResult= authRemoteDataSource.createCode(createCodeRequest)
        emit(networkResult)
    }
}