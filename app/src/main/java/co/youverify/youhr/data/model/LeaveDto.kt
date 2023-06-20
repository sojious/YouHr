package co.youverify.youhr.data.model

data class LeaveRequestsResponse(
    val success: Boolean,
    val statusCode: Int,
    val message: String,
    val data: LeaveRequestData?,
    val pagination: Pagination
)
data class LeaveSummaryResponse(
    val success: Boolean,
    val statusCode: Int,
    val message: String,
    val data: LeaveSummaryDto,
    val links: List<Any>
)




data class LeaveRequestData(
    val docs: List<LeaveRequestDto>
)

data class LeaveRequestDto(
    val linemanagerStatus: String,
    val hrStatus: String,
    val status: String,
    val linemanagerApprovalDate:String?,
    val hrApprovalDate: String?,
    val userId: String,
    val email: String,
    val displayPicture: String?,
    val leaveType: String,
    val startDate: String,
    val endDate: String,
    val reasonForLeave: String?,
    val contactName: String,
    val contactEmail: String,
    val contactNumber: String,
    val releiverName: String,
    val linemanagerEmail: String,
    val linemanagerName: String,
    val linemanagerId: String,
    val name: String,
    val leaveDaysRequested: String,
    val availableLeaveDays: String,
    val leaveTaken: String,
    val createdAt: String,
    val lastModifiedAt: String,
    val linemanagerComment: String?,
    val hrComment: String?,
    val _createdAt: String,
    val _lastModifiedAt: String,
    val id: String
)
data class LeaveSummaryDto(
    val annaulLeaveTaken: Int,
    val casualLeaveTaken: Int,
    val sickLeaveTaken: Int,
    val studyLeaveTaken: Int,
    val parentalLeaveTaken: Int,
    val bereavementLeaveTaken: Int,
    val compassionateLeaveTaken: Int,
    val userId: String,
    val createdAt: String,
    val lastModifiedAt: String,
    val _createdAt: String,
    val _lastModifiedAt: String,
    val id: String
)
data class LeaveApplicationRequest(
    val leaveType: String,
    val startDate: String,
    val endDate: String,
    val reasonForLeave: String,
    val contactName: String,
    val contactEmail: String,
    val contactNumber: String,
    val releiverName: String,
    val linemanagerEmail: String,
    val linemanagerName: String
)

data class LeaveApplicationResponse(
    val success: Boolean,
    val statusCode: Int,
    val message: String,
    val data: LeaveRequestDto,
    val links: List<Any>
)

