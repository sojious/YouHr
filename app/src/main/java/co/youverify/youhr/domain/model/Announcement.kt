package co.youverify.youhr.domain.model

data class Announcement(
    val postedBy:String,
    val message:String,
    val createdAt:String,
    val title:String,
    val postedByDisplayPicUrl:String
    )
