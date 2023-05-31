package co.youverify.youhr.domain.repository

import co.youverify.youhr.core.util.Result
import co.youverify.youhr.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun getAssignedTaskFirstLoad(): Flow<Result<List<Task>>>
    suspend fun getAssignedTasksPaginated(page:Int): Flow<Result<List<Task>>>
    suspend fun refreshTasks(): Flow<Result<List<Task>>>
}