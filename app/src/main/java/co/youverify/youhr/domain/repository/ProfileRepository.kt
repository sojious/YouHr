package co.youverify.youhr.domain.repository

import co.youverify.youhr.core.util.Result
import co.youverify.youhr.data.model.FilterUserResponse
import co.youverify.youhr.data.model.GenericResponse
import co.youverify.youhr.data.model.UpdateUserProfileRequest
import co.youverify.youhr.data.model.UserProfileResponse
import co.youverify.youhr.domain.model.FilteredUser
import co.youverify.youhr.domain.model.User
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface ProfileRepository {
    suspend fun getUserProfile(): Flow<Result<User>>
    //suspend fun setUserProfile(): Flow<Result<User>>
    suspend fun filterAllUser(): Flow<Result<List<FilteredUser>>>

    suspend fun updateUserProfile(request: UpdateUserProfileRequest): Result<UserProfileResponse>
    suspend fun updateUserProfilePic(imageFile: MultipartBody.Part):Result<UserProfileResponse>
    suspend fun filterAllLineManager(): Flow<Result<List<FilteredUser>>>
}