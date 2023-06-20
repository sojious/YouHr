package co.youverify.youhr.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "assigned_task")
data class DBTask(

    @PrimaryKey val id:String,
    val title:String,
    val description:String,
    val dueDate:String,
    val status:String,
    val assignedBy:String,
    val type:String,
    val attachedDocs:DbDocList,
    val page:Int,
    val hasNextPage:Boolean,
    val createdAtTimeStamp:String
)

@Entity(tableName = "user")
data class DbUser(
    @PrimaryKey
    val id: String,
    val role: String,
    val jobRole:String,
    val status: String,
    val email: String,
    val firstName: String,
    val lastName: String,
    val middleName:String,
    val phoneNumber:String,
    val password: String,
    val passcode: String,
    val address: String,
    val dob: String,
    val gender: String,
    val nextOfKin: String,
    val nextOfKinContact: String,
    val displayPicture: String,
    val nextOfKinNumber: String,

)

@Entity(tableName = "leave_request")
data class DbLeaveRequest(
    @PrimaryKey val id: String,
    val linemanagerStatus: String,
    val hrStatus: String,
    val status:String,
    val linemanagerApprovalDate:String,
    val hrApprovalDate: String,
    val email: String,
    val leaveType:String,
    val startDate: String,
    val endDate: String,
    val reasonForLeave:String ,
    val contactName:String,
    val contactEmail: String,
    val contactNumber:String,
    val releiverName: String,
    val linemanagerEmail: String,
    val linemanagerName: String,
    val leaveDaysRequested:String,
    val linemanagerComment: String,
    val hrComment: String,
)

@Entity(tableName = "leave_summary")
data class DbLeaveSummary (
    @PrimaryKey
    val id: String,
    val annualLeaveTaken: Int,
    val casualLeaveTaken: Int,
    val sickLeaveTaken: Int,
    val studyLeaveTaken: Int,
    val parentalLeaveTaken: Int,
    val bereavementLeaveTaken: Int,
    val compassionateLeaveTaken: Int,
)

data class DbDocList(val docs:List<DBDoc>)
data class DBDoc(val name: String, val url:String)
