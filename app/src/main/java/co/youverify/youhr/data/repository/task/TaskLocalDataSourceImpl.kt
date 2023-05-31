package co.youverify.youhr.data.repository.task

import co.youverify.youhr.data.local.DBTask
import co.youverify.youhr.data.local.YouHrDatabase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskLocalDataSourceImpl @Inject constructor(private val youHrDatabase: YouHrDatabase):TaskLocalDataSource {
    override fun getTasks(): Flow<List<DBTask>> {
       return youHrDatabase.taskDao().getAll()
    }

    override fun getTasksPaginated(pageNumber: Int): Flow<List<DBTask>> {
        return youHrDatabase.taskDao().getPaginated(pageNumber)
    }

    override suspend fun clearTasks() {
        youHrDatabase.taskDao().clearAll()
    }

    override suspend fun insertTasks(tasks: List<DBTask>) {
        youHrDatabase.taskDao().insertTasks(tasks)
    }
}