package co.youverify.youhr.data.repository.leave

import co.youverify.youhr.data.model.LeaveRequestsResponse
import co.youverify.youhr.core.util.Result
import co.youverify.youhr.core.util.handleApi
import co.youverify.youhr.data.model.LeaveApplicationRequest
import co.youverify.youhr.data.model.LeaveApplicationResponse
import co.youverify.youhr.data.model.LeaveSummaryResponse
import co.youverify.youhr.data.remote.YouHrService
import javax.inject.Inject

class LeaveRemoteDatasourceImpl @Inject constructor(private val youHrService: YouHrService):LeaveRemoteDataSource {
    override suspend fun getLeaveRequests(): Result<LeaveRequestsResponse> {
        return handleApi { youHrService.getLeaveRequests() }
    }

    override suspend fun getLeaveSummary(): Result<LeaveSummaryResponse> {
        return handleApi { youHrService.getLeaveSummary() }
    }

    override suspend fun createLeaveRequest(request:LeaveApplicationRequest): Result<LeaveApplicationResponse> {
        return handleApi { youHrService.createLeaveRequest(request) }
    }

}