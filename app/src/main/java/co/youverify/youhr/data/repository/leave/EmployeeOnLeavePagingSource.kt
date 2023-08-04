package co.youverify.youhr.data.repository.leave

import androidx.paging.PagingSource
import androidx.paging.PagingState
import co.youverify.youhr.core.util.Result
import co.youverify.youhr.data.mapper.DtoToDomainAnnouncementListMapper
import co.youverify.youhr.data.mapper.DtoToDomainEmployeeOnLeaveListMapper
import co.youverify.youhr.domain.model.Announcement
import co.youverify.youhr.domain.model.EmployeeOnLeave
import javax.inject.Inject

class EmployeeOnLeavePagingSource @Inject constructor(
    private val leaveRemoteDataSource: LeaveRemoteDataSource,
    private val dtoToDomainEmployeeOnLeaveListMapper: DtoToDomainEmployeeOnLeaveListMapper
    ): PagingSource<Int, EmployeeOnLeave>() {


    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, EmployeeOnLeave> {
        val apiResult=leaveRemoteDataSource.getEmployeesOnLeave()
    return    when(apiResult){
            is Result.Success->{
                val domainEmployees=dtoToDomainEmployeeOnLeaveListMapper.map(apiResult.data.data.employeeOnLeave)
                LoadResult.Page(
                    data = domainEmployees,
                    prevKey = null,
                    nextKey =apiResult.data.data.pagination.nextPage
                )
            }
            is Result.Error->{ LoadResult.Error(throwable = Throwable(message = apiResult.message)) }
            is Result.Exception->{ LoadResult.Error(throwable = Throwable(message = apiResult.genericMessage)) }
        }
    }

    override fun getRefreshKey(state: PagingState<Int, EmployeeOnLeave>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}