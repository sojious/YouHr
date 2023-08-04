package co.youverify.youhr.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingSourceFactory
import co.youverify.youhr.core.util.Result
import co.youverify.youhr.data.mapper.DtoToDomainAnnouncementListMapper
import co.youverify.youhr.data.repository.announcement.AnnouncementPagingSource
import co.youverify.youhr.data.repository.announcement.AnnouncementRemoteDataSource
import co.youverify.youhr.domain.model.Announcement
import co.youverify.youhr.domain.repository.AnnouncementRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AnnouncementRepositoryImpl @Inject constructor(
    private val announcementRemoteDataSource: AnnouncementRemoteDataSource,
    private val announcementListMapper: DtoToDomainAnnouncementListMapper
    ):AnnouncementRepository {
    override suspend fun getAllAnnouncement(): Flow<PagingData<Announcement>> {

        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = {
                AnnouncementPagingSource(
                    announcementRemoteDataSource, announcementListMapper
                )
            }
        ).flow

    }
}