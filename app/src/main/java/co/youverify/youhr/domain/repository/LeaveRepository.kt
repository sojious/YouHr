package co.youverify.youhr.domain.repository

import co.youverify.youhr.core.util.Result
import co.youverify.youhr.data.model.LeaveApplicationRequest
import co.youverify.youhr.data.model.LeaveApplicationResponse
import co.youverify.youhr.domain.model.LeaveRequest
import co.youverify.youhr.domain.model.LeaveSummary
import kotlinx.coroutines.flow.Flow

interface LeaveRepository {
    suspend fun getLeaveRequests(isFirstLoad:Boolean): Flow<Result<List<LeaveRequest>>>
    suspend fun getLeaveSummary(isFirstLoad:Boolean): Flow<Result<LeaveSummary>>
    suspend fun createLeaveRequest(request: LeaveApplicationRequest):Flow<Result<LeaveApplicationResponse>>
}