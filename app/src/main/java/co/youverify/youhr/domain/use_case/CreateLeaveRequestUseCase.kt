package co.youverify.youhr.domain.use_case

import co.youverify.youhr.core.util.Result
import co.youverify.youhr.data.model.LeaveApplicationRequest
import co.youverify.youhr.data.model.LeaveApplicationResponse
import co.youverify.youhr.domain.model.LeaveRequest
import co.youverify.youhr.domain.repository.LeaveRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CreateLeaveRequestUseCase @Inject constructor(private val leaveRepository: LeaveRepository) {
    suspend operator fun invoke(request:LeaveApplicationRequest): Flow<Result<LeaveApplicationResponse>> {
       return leaveRepository.createLeaveRequest(request)
    }
}