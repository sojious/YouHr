package co.youverify.youhr.domain.repository

import co.youverify.youhr.core.util.Result
import co.youverify.youhr.domain.model.User
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    suspend fun getUserProfile(isFirstLogin:Boolean): Flow<Result<User>>
    //suspend fun setUserProfile(): Flow<Result<User>>
}