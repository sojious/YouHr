package co.youverify.youhr.domain.use_case

import androidx.paging.PagingData
import co.youverify.youhr.core.util.Result
import co.youverify.youhr.data.model.AnnouncementListResponse
import co.youverify.youhr.domain.model.Announcement
import co.youverify.youhr.domain.repository.AnnouncementRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllAnnouncementUseCase @Inject constructor(private val announcementRepository: AnnouncementRepository) {
    suspend fun invoke(): Flow<PagingData<Announcement>> {
       return announcementRepository.getAllAnnouncement()
    }
}