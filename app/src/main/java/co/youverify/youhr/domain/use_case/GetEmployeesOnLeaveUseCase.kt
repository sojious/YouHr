package co.youverify.youhr.domain.use_case

import androidx.paging.PagingData
import co.youverify.youhr.core.util.Result
import co.youverify.youhr.data.model.AnnouncementListResponse
import co.youverify.youhr.domain.model.Announcement
import co.youverify.youhr.domain.model.EmployeeOnLeave
import co.youverify.youhr.domain.repository.AnnouncementRepository
import co.youverify.youhr.domain.repository.LeaveRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetEmployeesOnLeaveUseCase @Inject constructor(private val leaveRepository: LeaveRepository) {
    suspend fun invoke(): Flow<PagingData<EmployeeOnLeave>> {
       return leaveRepository.getEmployeesOnLeave()
    }
}