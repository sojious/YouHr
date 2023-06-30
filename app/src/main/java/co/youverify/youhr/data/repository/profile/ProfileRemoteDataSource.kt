package co.youverify.youhr.data.repository.profile

import co.youverify.youhr.core.util.Result
import co.youverify.youhr.data.model.FilterUserResponse
import co.youverify.youhr.data.model.UpdateUserProfileRequest
import co.youverify.youhr.data.model.UserProfileResponse
import okhttp3.MultipartBody

interface ProfileRemoteDataSource {

    suspend fun getUserProfile():Result<UserProfileResponse>
    suspend fun filterAllUser():Result<FilterUserResponse>
    suspend fun updateUserProfile(request: UpdateUserProfileRequest):Result<UserProfileResponse>
    suspend fun updateUserProfilePic(imageFile: MultipartBody.Part):Result<UserProfileResponse>
    suspend fun filterAllLineManager(): Result<FilterUserResponse>
}