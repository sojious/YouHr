package co.youverify.youhr.data.model

data class FilterUserDto(
    val role: String,
    val isEmployed: Boolean,
    val isConfirmed: Boolean,
    val isPasswordSet: Boolean,
    val status:String?,
    val serviceDocument: List<Any>?,
    val email: String,
    val createdAt: String,
    val lastModifiedAt: String,
    val firstName: String?,
    val lastName: String?,
    val password: String?,
    val passcode: String?,
    val _createdAt: String,
    val dob:String?,
    val phoneNumber: String?,
    val department: String?,
    val jobRole: String?,
    val _lastModifiedAt: String,
    val nextofKin: String?,
    val nextofKinContact: String?,
    val nextofKinNumber:String?,
    val nextofKinEmail:String?,
    val address:String?,
    val middleName: String?,
    val gender:String?,
    val id: String
)

data class FilterUserResponse(
    val success: Boolean,
    val statusCode: Int,
    val message: String,
    val data: List<FilterUserDto>
)