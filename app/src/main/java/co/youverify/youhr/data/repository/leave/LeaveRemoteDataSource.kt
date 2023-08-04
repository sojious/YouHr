package co.youverify.youhr.data.repository.leave

import co.youverify.youhr.data.model.LeaveRequestsResponse
import co.youverify.youhr.core.util.Result
import co.youverify.youhr.data.model.EmployeeOnLeaveResponse
import co.youverify.youhr.data.model.LeaveApplicationRequest
import co.youverify.youhr.data.model.LeaveApplicationResponse
import co.youverify.youhr.data.model.LeaveSummaryResponse

interface LeaveRemoteDataSource {
    suspend fun getLeaveRequests(): Result<LeaveRequestsResponse>
    suspend fun getLeaveSummary(): Result<LeaveSummaryResponse>
    suspend fun createLeaveRequest(request: LeaveApplicationRequest): Result<LeaveApplicationResponse>
    suspend fun getEmployeesOnLeave():Result<EmployeeOnLeaveResponse>

}