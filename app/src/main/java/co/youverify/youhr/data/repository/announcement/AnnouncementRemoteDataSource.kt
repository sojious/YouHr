package co.youverify.youhr.data.repository.announcement

import co.youverify.youhr.core.util.Result
import co.youverify.youhr.data.model.AnnouncementListResponse
import co.youverify.youhr.domain.model.Announcement

interface AnnouncementRemoteDataSource {
    suspend fun getAllAnnouncement(): Result<AnnouncementListResponse>
}