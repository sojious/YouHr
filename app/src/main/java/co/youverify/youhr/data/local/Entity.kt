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

data class DbDocList(val docs:List<DBDoc>)
data class DBDoc(
    val name: String,
    val url:String
    )