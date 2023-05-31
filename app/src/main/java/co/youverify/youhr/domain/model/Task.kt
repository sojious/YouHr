package co.youverify.youhr.domain.model


data class Task(
     val id:String,
     val title:String,
     val description:String,
     val dueDate:String,
     val status:String,
     val assignedBy:String,
     val type:String,
     val attachedDocs:List<AttachedDoc>,
     val hasNextPage:Boolean,
     val page:Int,
     val timeStampCreated:String
 )

data class AttachedDoc(val name: String,val url:String)
