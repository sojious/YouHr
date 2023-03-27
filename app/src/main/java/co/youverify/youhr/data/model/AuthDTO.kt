package co.youverify.youhr.data.model



data class AuthResponse(
    val `data`: Data,
    val links: List<String>,
    val message: String,
    val statusCode: Int,
    val success: Boolean
)


data class LoginWithPassWordRequest(
    val email: String,
    val password: String
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

data class Data(
    val response: String
)


