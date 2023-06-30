package co.youverify.youhr.domain.use_case

import co.youverify.youhr.core.util.Result
import co.youverify.youhr.data.model.UpdateUserProfileRequest
import co.youverify.youhr.data.model.UserProfileResponse
import co.youverify.youhr.domain.model.User
import co.youverify.youhr.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject

class UpdateUserProfileUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    suspend fun invoke(request:UpdateUserProfileRequest,imageFile:MultipartBody.Part): Flow<Result<UserProfileResponse>> {
        return flow {

            emit(profileRepository.updateUserProfilePic(imageFile))
            emit(profileRepository.updateUserProfile(request))
        }
    }
}