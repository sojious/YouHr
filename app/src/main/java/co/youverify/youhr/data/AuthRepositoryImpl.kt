package co.youverify.youhr.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import co.youverify.youhr.core.util.NetworkResult
import co.youverify.youhr.core.util.PreferenceKeys
import co.youverify.youhr.data.model.*
import co.youverify.youhr.data.repository.auth.AuthRemoteDataSource
import co.youverify.youhr.domain.repository.AuthRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class AuthRepositoryImpl  @Inject constructor(val authRemoteDataSource: AuthRemoteDataSource):AuthRepository {

    override suspend fun loginWithPassword(loginWithPassWordRequest: LoginWithPassWordRequest): Flow<NetworkResult<AuthResponse>> =
         flow {

             val networkResult= authRemoteDataSource.loginWithPassword(loginWithPassWordRequest)
             emit(networkResult)
         }

    override suspend fun loginWithCode(loginWithCodeRequest: LoginWithCodeRequest): Flow<NetworkResult<AuthResponse>> =
        flow {
        val networkResult= authRemoteDataSource.loginWithCode(loginWithCodeRequest)
        emit(networkResult)
    }

    override suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Flow<NetworkResult<GenericResponse>> =
        flow {
        val networkResult= authRemoteDataSource.resetPassword(resetPasswordRequest)
        emit(networkResult)
    }

    override suspend fun createCode(createCodeRequest: CreateCodeRequest): Flow<NetworkResult<GenericResponse>> =
        flow {

        val networkResult= authRemoteDataSource.createCode(createCodeRequest)
        emit(networkResult)
    }



}