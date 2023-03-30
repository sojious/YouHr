package co.youverify.youhr.presentation.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import co.youverify.youhr.presentation.TaskDetail
import co.youverify.youhr.presentation.TaskGraph
import co.youverify.youhr.presentation.TaskList
import co.youverify.youhr.presentation.ui.task.TaskDetailScreen
import co.youverify.youhr.presentation.ui.task.TaskListScreen
import co.youverify.youhr.presentation.ui.task.TaskViewModel
import co.youverify.youhr.presentation.ui.task.pendingTasks
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.TaskGraph(navHostController: NavHostController,taskViewModel: TaskViewModel){

    navigation(startDestination =TaskList.route , route =TaskGraph.route ){

        composable(route= TaskList.route){

           TaskListScreen(
               tasks = taskViewModel.currentTaskList,
               listState = rememberLazyListState(),
               categoryDropDownExpanded = taskViewModel.categoryDropDownExpanded,
               dateDropDownExpanded = taskViewModel.dateDropDownExpanded,
               onCategorySpinnerClicked = { taskViewModel.updateCategoryDropDownState() },
               onDateSpinnerClicked = { taskViewModel.updateDateDropDownState() },
               onTaskItemClicked = {
                   taskViewModel.showTaskDetail(it)
               },
               categoryDropDownOnDismissCallBack = {taskViewModel.categoryDropDownOnDismissCallBack()},
               dateDropDownOnDismissCallBack = {taskViewModel.dateDropDownOnDismissCallBack()},
               onPendingClicked = {taskViewModel.onPendingClicked()},
               onCompletedClicked = {taskViewModel.onCompletedClicked()}
           )
        }

        composable(
            route= TaskDetail.routWithArgs,
            arguments = TaskDetail.args
        ){navBackStackEntry->

            TaskDetailScreen(
                taskMessage = "",
                onTaskMessageChanged = {},
                taskId = navBackStackEntry.arguments?.getInt(TaskDetail.taskIdArg)!!,
                onSendMessageButtonClicked = {},
                currentTaskList = taskViewModel.currentTaskList
            )

        }

    }
}