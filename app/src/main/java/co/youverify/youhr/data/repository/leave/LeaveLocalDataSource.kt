package co.youverify.youhr.data.repository.leave

import co.youverify.youhr.data.local.DbLeaveRequest
import co.youverify.youhr.data.local.DbLeaveSummary

interface LeaveLocalDataSource {
   suspend fun getLeaveRequests(): List<DbLeaveRequest>
   suspend fun saveLeaveRequests(requests: List<DbLeaveRequest>)
   suspend fun getLeaveSummary(): DbLeaveSummary?
   suspend fun saveLeaveSummary(summary: DbLeaveSummary)

}