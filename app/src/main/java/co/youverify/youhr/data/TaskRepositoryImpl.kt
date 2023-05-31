package co.youverify.youhr.data

import co.youverify.youhr.core.util.Result
import co.youverify.youhr.data.mapper.DbToDomainTaskListMapper
import co.youverify.youhr.data.mapper.DtoToDbTaskListMapper
import co.youverify.youhr.data.repository.task.TaskLocalDataSource
import co.youverify.youhr.data.repository.task.TaskRemoteDataSource
import co.youverify.youhr.domain.model.Task
import co.youverify.youhr.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val taskRemoteDataSource: TaskRemoteDataSource,
    private val taskLocalDataSource: TaskLocalDataSource,
    private val dbToDomainTaskListMapper: DbToDomainTaskListMapper,
    private val dtoToDbTaskListMapper: DtoToDbTaskListMapper
) :TaskRepository{

    override suspend fun getAssignedTaskFirstLoad():Flow<Result<List<Task>>> {
        var tasks:List<Task> = emptyList()
        val result:Result<List<Task>>

        taskLocalDataSource.getTasks().collect{dataBaseTasks->
                tasks=dbToDomainTaskListMapper.map(dataBaseTasks)
        }

        result = if(tasks.isEmpty()){
            getPaginatedTask(page = 1)
        } else{
            Result.Success(data = tasks)
        }

            return flow { emit(result) }
    }



    override suspend fun getAssignedTasksPaginated(page: Int): Flow<Result<List<Task>>> {
        val result=getPaginatedTask(page=page)
        return flow { emit(result) }
    }


    override suspend fun refreshTasks(): Flow<Result<List<Task>>> {
        taskLocalDataSource.clearTasks()
        val result=getPaginatedTask(page=1)
        return flow { emit(result) }
    }

    private suspend fun getPaginatedTask(page: Int): Result<List<Task>> {
        val result:Result<List<Task>>
        val getRemoteTaskResult=taskRemoteDataSource.getTasks(page = page)
        result = when(getRemoteTaskResult){
            is Result.Success->{
                var dbTasks=dtoToDbTaskListMapper.map(getRemoteTaskResult.data.data.apiTasks)

                //Add pagination information to each task
                dbTasks=dbTasks.map {
                    it.copy(
                        hasNextPage = getRemoteTaskResult.data.data.pagination.hasNextPage,
                        page = getRemoteTaskResult.data.data.pagination.currentPage,
                    )
                }

                taskLocalDataSource.insertTasks(dbTasks)
                Result.Success(data=dbToDomainTaskListMapper.map(dbTasks))
            }

            is Result.Error->{
                Result.Error(code=getRemoteTaskResult.code,message = getRemoteTaskResult.message)
            }

            is Result.Exception->{
                Result.Exception(e=getRemoteTaskResult.e, genericMessage = getRemoteTaskResult.genericMessage)
            }
        }

        return result
    }
}