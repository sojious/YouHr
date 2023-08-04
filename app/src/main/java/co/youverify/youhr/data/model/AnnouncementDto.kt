package co.youverify.youhr.data.model



data class AnnouncementListResponse(
    val success: Boolean,
    val statusCode: Int,
    val message: String,
    val data: AnnouncementListData,
    val links: List<Any>
)

data class AnnouncementListData(
    val docs: List<AnnouncementDto>,
    val pagination: Pagination
)

data class AnnouncementDto(
    val viewBy: List<String>,
    val announcement: String,
    val subject: String,
    val postedBy: String,
    val userId: UserId,
    val createdAt: String,
    val lastModifiedAt: String,
    val _createdAt: String,
    val _lastModifiedAt: String,
    val id: String
)
data class UserId(
    val _id: String,
    val _createdAt: String,
    val _lastModifiedAt: String,
    val id: String,
    val displayPicture:String
)


