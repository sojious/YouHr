package co.youverify.youhr.domain.use_case

import co.youverify.youhr.core.util.Result
import co.youverify.youhr.domain.model.LeaveRequest
import co.youverify.youhr.domain.repository.LeaveRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLeaveRequestsUseCase @Inject constructor(private val leaveRepository: LeaveRepository) {
    suspend operator fun invoke(isFirstLoad:Boolean): Flow<Result<List<LeaveRequest>>> {
       return leaveRepository.getLeaveRequests(isFirstLoad=isFirstLoad)
    }
}