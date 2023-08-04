package co.youverify.youhr.domain.model

data class EmployeeOnLeave(
    val name:String,
    val jobRole:String,
    val relieverName:String,
    val startDateString:String,
    val endDateString:String,
    val displayPicture:String
)
