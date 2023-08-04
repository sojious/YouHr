package co.youverify.youhr.data.repository.announcement

import co.youverify.youhr.core.util.Result
import co.youverify.youhr.core.util.handleApi
import co.youverify.youhr.data.model.AnnouncementListResponse
import co.youverify.youhr.data.remote.YouHrService
import javax.inject.Inject

class AnnouncementRemoteDataSourceImpl @Inject constructor(private val youHrService: YouHrService):AnnouncementRemoteDataSource {
    override suspend fun getAllAnnouncement(): Result<AnnouncementListResponse> {
        return handleApi { youHrService.getAllAnnouncement() }
    }
}