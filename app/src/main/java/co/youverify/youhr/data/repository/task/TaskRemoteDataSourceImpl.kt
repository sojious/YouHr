package co.youverify.youhr.data.repository.task

import co.youverify.youhr.core.util.Result
import co.youverify.youhr.core.util.handleApi
import co.youverify.youhr.data.model.AssignedTasksResponse
import co.youverify.youhr.data.remote.YouHrService
import javax.inject.Inject

class TaskRemoteDataSourceImpl @Inject constructor(private val youHrService: YouHrService):TaskRemoteDataSource {
    override suspend fun getTasks(page: Int): Result<AssignedTasksResponse> {
        return handleApi { youHrService.getAssignedTask(page =page) }
    }
}