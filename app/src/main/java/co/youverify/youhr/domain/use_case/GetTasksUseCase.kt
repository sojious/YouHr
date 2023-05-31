package co.youverify.youhr.domain.use_case

import co.youverify.youhr.core.util.Result
import co.youverify.youhr.domain.model.Task
import co.youverify.youhr.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTasksUseCase @Inject constructor(private val taskRepository: TaskRepository) {

    suspend fun invoke(firstLoad:Boolean, page:Int=1): Flow<Result<List<Task>>> {
      return  if(firstLoad){
          taskRepository.getAssignedTaskFirstLoad()
      }
      else if (page>1){
          taskRepository.getAssignedTasksPaginated(page=page)
      } else {
            taskRepository.refreshTasks()
        }
    }
}