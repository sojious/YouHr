package co.youverify.youhr.domain.model

data class LeaveSummary(
    val annualLeaveTaken: Int,
    val casualLeaveTaken: Int,
    val sickLeaveTaken: Int,
    val studyLeaveTaken: Int,
    val parentalLeaveTaken: Int,
    val bereavementLeaveTaken: Int,
    val compassionateLeaveTaken: Int,
    val id: Int
)