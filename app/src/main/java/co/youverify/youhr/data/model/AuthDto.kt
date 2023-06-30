package co.youverify.youhr.data.model


data class GenericResponse(
    val `data`: Data,
    val links: List<Any>,
    val message: String,
    val statusCode: Int,
    val success: Boolean
)
data class AuthResponse(
    val `data`: TokenData,
    val links: List<Any>,
    val message: String,
    val statusCode: Int,
    val success: Boolean
)


data class LoginWithPassWordRequest(
    val email: String,
    val password: String
)

data class ChangePasswordRequest(
    val password: String,
    val newPassword: String
)
data class LoginWithCodeRequest(
    val email: String,
    val passcode: Int
)

data class ResetPasswordRequest(
    val email: String
)

data class CreateCodeRequest(
    val passcode: Int
)

data class TokenData(
    val token: String
)
class Data




