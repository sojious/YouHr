package co.youverify.youhr.data.model




data class UserProfileResponse(
    val success: Boolean,
    val statusCode: Int,
    val message: String,
    val data: UserData,
    val links: List<Any>
)

data class UserData(
    val role: String?,
    val jobRole:String?,
    val status: String?,
    val isEmployed: Boolean,
    val isConfirmed: Boolean,
    val isPasswordSet: Boolean,
    val email: String?,
    val createdAt: String,
    val lastModifiedAt:String,
    val firstName: String?,
    val lastName: String?,
    val password: String?,
    val middleName:String?,
    val phoneNumber:String?,
    val passcode: String?,
    val address: String?,
    val department: String?,
    val dob: String?,
    val gender: String?,
    val nextofKin: String?,
    val nextofKinContact: String?,
    val serviceDocument: List<ServiceDocument>?,
    val displayPicture: String?,
    val nextofKinNumber: String?,
    val _createdAt: String,
    val _lastModifiedAt: String,
    val id: String
)
data class ServiceDocument(
    val _id: String,
    val title: String,
    val uploadedAt: String,
    val content: String
)

data class UpdateUserProfileRequest(
    val nextofKin:String,
    val nextofKinContact:String,
    val address:String,
    val nextofKinNumber:String,
    val phoneNumber:String
)



