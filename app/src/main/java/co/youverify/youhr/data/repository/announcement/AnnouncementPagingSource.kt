package co.youverify.youhr.data.repository.announcement

import androidx.paging.PagingSource
import androidx.paging.PagingState
import co.youverify.youhr.core.util.Result
import co.youverify.youhr.data.mapper.DtoToDomainAnnouncementListMapper
import co.youverify.youhr.domain.model.Announcement
import javax.inject.Inject

class AnnouncementPagingSource @Inject constructor(
    private val announcementRemoteDataSource: AnnouncementRemoteDataSource,
    private val dtoToDomainAnnouncementListMapper: DtoToDomainAnnouncementListMapper): PagingSource<Int, Announcement>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Announcement> {
        val apiResult=announcementRemoteDataSource.getAllAnnouncement()
    return    when(apiResult){
            is Result.Success->{
                val domainAnnouncements=dtoToDomainAnnouncementListMapper.map(apiResult.data.data.docs)
                LoadResult.Page(
                    data = domainAnnouncements,
                    prevKey = null,
                    nextKey =apiResult.data.data.pagination.nextPage
                )
            }
            is Result.Error->{ LoadResult.Error(throwable = Throwable(message = apiResult.message)) }
            is Result.Exception->{ LoadResult.Error(throwable = Throwable(message = apiResult.genericMessage)) }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Announcement>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}