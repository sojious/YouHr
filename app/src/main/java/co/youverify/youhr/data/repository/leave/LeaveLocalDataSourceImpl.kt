package co.youverify.youhr.data.repository.leave

import co.youverify.youhr.data.local.DbLeaveRequest
import co.youverify.youhr.data.local.DbLeaveSummary
import co.youverify.youhr.data.local.YouHrDatabase
import javax.inject.Inject

class LeaveLocalDataSourceImpl @Inject constructor(private val youHrDatabase: YouHrDatabase):LeaveLocalDataSource {
    override suspend fun getLeaveRequests(): List<DbLeaveRequest> {
        return youHrDatabase.leaveDao().getLeaveRequests()
    }

    override suspend fun saveLeaveRequests(requests:List<DbLeaveRequest>){
        return youHrDatabase.leaveDao().saveLeaveRequests(requests)
    }

    override suspend fun getLeaveSummary(): DbLeaveSummary? {
        return youHrDatabase.leaveDao().getLeaveSummary()
    }

    override suspend fun saveLeaveSummary(summary: DbLeaveSummary) {
        return youHrDatabase.leaveDao().saveLeaveSummary(summary)
    }

    override suspend fun clearAllLeaveRequest() {
        return youHrDatabase.leaveDao().clearAllLeaveRequests()
    }
}