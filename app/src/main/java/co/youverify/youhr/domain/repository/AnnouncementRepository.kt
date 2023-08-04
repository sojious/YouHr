package co.youverify.youhr.domain.repository

import androidx.paging.PagingData
import co.youverify.youhr.domain.model.Announcement
import kotlinx.coroutines.flow.Flow

interface AnnouncementRepository {
    suspend fun getAllAnnouncement(): Flow<PagingData<Announcement>>
}