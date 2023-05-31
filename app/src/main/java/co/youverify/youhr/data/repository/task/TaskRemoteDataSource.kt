package co.youverify.youhr.data.repository.task

import co.youverify.youhr.core.util.Result
import co.youverify.youhr.data.model.AssignedTasksResponse

interface TaskRemoteDataSource {
    suspend fun getTasks(page:Int): Result<AssignedTasksResponse>
}