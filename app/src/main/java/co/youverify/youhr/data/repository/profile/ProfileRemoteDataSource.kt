package co.youverify.youhr.data.repository.profile

import co.youverify.youhr.core.util.Result
import co.youverify.youhr.data.model.UserProfileResponse

interface ProfileRemoteDataSource {

    suspend fun getUserProfile():Result<UserProfileResponse>
}