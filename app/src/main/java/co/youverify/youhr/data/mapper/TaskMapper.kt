package co.youverify.youhr.data.mapper

import co.youverify.youhr.data.local.DBDoc
import co.youverify.youhr.data.local.DBTask
import co.youverify.youhr.data.local.DbDocList
import co.youverify.youhr.data.model.ApiTask
import co.youverify.youhr.domain.model.AttachedDoc
import co.youverify.youhr.domain.model.Task

class DbToDomainTaskMapper:Mapper<DBTask, Task>{
    override fun map(input: DBTask): Task {
        return Task(
            id=input.id,
            title = input.title,
            description = input.description,
            dueDate = input.dueDate,
            status = input.status,
            assignedBy = input.assignedBy,
            type = input.type,
            attachedDocs = List(input.attachedDocs.docs.size){
                AttachedDoc(input.attachedDocs.docs[it].name,input.attachedDocs.docs[it].url)
            },
            hasNextPage = input.hasNextPage,
            page = input.page,
            timeStampCreated = input.createdAtTimeStamp
        )
    }

}


class DtoToDbTaskMapper:Mapper<ApiTask, DBTask>{

    override fun map(input: ApiTask): DBTask {
        return DBTask(
            id=input.id,
            title = input.title,
            description = input.taskDescription,
            dueDate = input.deadLine,
            status = input.status,
            assignedBy = input.assignedByName,
            type = input.taskType,
            attachedDocs = DbDocList(
                docs = List(input.attachment.size){
                    DBDoc(input.attachment[it].fileName,input.attachment[it].fileUrl)
                }
            ),
            hasNextPage = false,
            page = 1,
            createdAtTimeStamp = input.createdAt
        )
    }

}

class DbToDomainTaskListMapper(private val mapper: DbToDomainTaskMapper):ListMapper<DBTask,Task>{
    override fun map(input: List<DBTask>): List<Task> {
      return  input.map {
            mapper.map(it)
        }
    }

}


class DtoToDbTaskListMapper(private val mapper: DtoToDbTaskMapper):ListMapper<ApiTask,DBTask>{
    override fun map(input: List<ApiTask>): List<DBTask> {
        return  input.map {
            mapper.map(it)
        }
    }

}