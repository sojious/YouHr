package co.youverify.youhr.data.repository.profile

import co.youverify.youhr.core.util.Result
import co.youverify.youhr.core.util.handleApi
import co.youverify.youhr.data.model.UserProfileResponse
import co.youverify.youhr.data.remote.YouHrService
import javax.inject.Inject

class ProfileRemoteDatasourceImpl @Inject constructor(private val youHrService: YouHrService):ProfileRemoteDataSource {
    override suspend fun getUserProfile(): Result<UserProfileResponse> {
       return handleApi { youHrService.getUserProfile() }
    }
}