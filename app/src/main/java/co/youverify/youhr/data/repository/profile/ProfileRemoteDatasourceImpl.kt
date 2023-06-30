package co.youverify.youhr.data.repository.profile

import co.youverify.youhr.core.util.Result
import co.youverify.youhr.core.util.handleApi
import co.youverify.youhr.data.model.FilterUserResponse
import co.youverify.youhr.data.model.UpdateUserProfileRequest
import co.youverify.youhr.data.model.UserProfileResponse
import co.youverify.youhr.data.remote.YouHrService
import okhttp3.MultipartBody
import retrofit2.Response
import javax.inject.Inject

class ProfileRemoteDatasourceImpl @Inject constructor(private val youHrService: YouHrService):ProfileRemoteDataSource {
    override suspend fun getUserProfile(): Result<UserProfileResponse> {
       return handleApi { youHrService.getUserProfile() }
    }

    override suspend fun filterAllUser(): Result<FilterUserResponse> {
        return handleApi { youHrService.filterAllUser() }
    }

    override suspend fun updateUserProfile(request: UpdateUserProfileRequest): Result<UserProfileResponse> {
        return handleApi { youHrService.updateProfile(request) }
    }

    override suspend fun updateUserProfilePic(imageFile: MultipartBody.Part): Result<UserProfileResponse> {
        return handleApi { youHrService.updateDisplayPic(imageFile) }

    }

    override suspend fun filterAllLineManager(): Result<FilterUserResponse> {
        return handleApi { youHrService.filterAllLineManager() }
    }


}