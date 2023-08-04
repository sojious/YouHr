package co.youverify.youhr.data.model

import com.google.gson.annotations.SerializedName

data class AssignedTasksResponse(
    val `data`: TaskData,
    val links: List<Any>,
    val message: String,
    val statusCode: Int,
    val success: Boolean
)

data class TaskData(
    val docs: List<ApiTask>,
    val pagination: Pagination
)

data class ApiTask(
    val _createdAt: String,
    val _lastModifiedAt: String,
    val assignedById: String,
    val assignedByName: String,
    val assignedTo: AssignedTo,
    val createdAt: String,
    val attachment: List<Attachment>,
    val deadLine: String,
    val id: String,
    val lastModifiedAt: String,
    val status: String,
    val taskType: String,
    val taskDescription: String,
    val title: String
)

data class Pagination(
    val currentPage: Int,
    val hasNextPage: Boolean,
    val hasPrevPage: Boolean,
    val nextPage: Int?,
    val perPage: Int,
    val prevPage: Int?,
    val serialNo: Int,
    val offset: Int?,
    val totalDocs: Int,
    val totalPages: Int
)

data class AssignedTo(
    val email: String,
    val id: String,
    val name: String
)

data class Attachment(
    val _id: String,
    val fileName: String,
    @SerializedName("fileurl")
    val fileUrl: String
)



