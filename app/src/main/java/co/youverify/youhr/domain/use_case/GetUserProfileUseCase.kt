package co.youverify.youhr.domain.use_case

import co.youverify.youhr.core.util.Result
import co.youverify.youhr.domain.model.User
import co.youverify.youhr.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserProfileUseCase @Inject constructor(private val profileRepository: ProfileRepository) {
    suspend fun invoke(): Flow<Result<User>> {
        return profileRepository.getUserProfile()
    }
}