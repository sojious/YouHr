package co.youverify.youhr.data.repository.task

import co.youverify.youhr.data.local.DBTask
import kotlinx.coroutines.flow.Flow


interface TaskLocalDataSource {
    fun getTasks(): Flow<List<DBTask>>
    fun getTasksPaginated(pageNumber: Int): Flow<List<DBTask>>
    suspend fun clearTasks()
    suspend fun insertTasks(tasks: List<DBTask>)
}