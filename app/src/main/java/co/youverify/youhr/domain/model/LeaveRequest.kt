package co.youverify.youhr.domain.model

import java.sql.Timestamp

data class LeaveRequest(
    val id:String,
    val linemanagerStatus: String,
    val hrStatus: String,
    val linemanagerApprovalDate:String,
    val hrApprovalDate: String,
    val status:String,
    val email: String,
    val leaveType:String,
    val startDate: String,
    val endDate: String,
    val reasonForLeave:String,
    val contactName:String,
    val contactEmail: String,
    val contactNumber:String,
    val relieverName: String,
    val linemanagerEmail: String,
    val linemanagerName: String,
    val leaveDaysRequested:String,
    val linemanagerComment: String,
    val hrComment: String,
)
