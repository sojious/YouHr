package co.youverify.youhr.data.repository.task

import co.youverify.youhr.data.local.DBTask
import kotlinx.coroutines.flow.Flow


interface TaskLocalDataSource {
    suspend fun getTasks(): List<DBTask>
    suspend fun getTasksPaginated(pageNumber: Int): List<DBTask>
    suspend fun clearTasks()
    suspend fun insertTasks(tasks: List<DBTask>)
}