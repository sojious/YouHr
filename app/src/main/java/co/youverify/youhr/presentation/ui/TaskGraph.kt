package co.youverify.youhr.presentation.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import co.youverify.youhr.presentation.TaskDetail
import co.youverify.youhr.presentation.TaskGraph
import co.youverify.youhr.presentation.TaskList
import co.youverify.youhr.presentation.ui.task.*
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.TaskGraph(taskViewModel: TaskViewModel,taskDetailViewModel: TaskDetailViewModel){

    navigation(startDestination =TaskList.route , route =TaskGraph.route ){

        composable(route= TaskList.route){

           TaskListScreen(
               tasks = taskViewModel.currentTaskList,
               listState = rememberLazyListState(),
               categoryDropDownExpanded = taskViewModel.categoryDropDownExpanded,
               showDatePicker = taskViewModel.showDatePicker,
               onCategorySpinnerClicked = { taskViewModel.updateCategoryDropDownState() },
               onDateSpinnerClicked = { taskViewModel.updateDatePickerExpandedState() },
               categoryDropDownOnDismissCallBack = {taskViewModel.categoryDropDownOnDismissCallBack()},
               onTaskItemClicked = {
                   taskViewModel.showTaskDetail(it)
               },
               onPendingClicked = {taskViewModel.onPendingClicked()},
               onCompletedClicked = {taskViewModel.onCompletedClicked()},
               categorySpinnerText = taskViewModel.categorySpinnerText,
           )
        }

        composable(
            route= TaskDetail.routWithArgs,
            arguments = TaskDetail.args
        ){navBackStackEntry->

            TaskDetailScreen(
                taskMessage =taskDetailViewModel.taskMessage,
                onTaskMessageChanged = {taskDetailViewModel.updateTaskMessage(it)},
                taskId = navBackStackEntry.arguments?.getInt(TaskDetail.taskIdArg)!!,
                onSendMessageButtonClicked = {},
                currentTaskList = taskViewModel.currentTaskList,
                onBackArrowClicked = {taskViewModel.navigateBack()}
            )

        }

    }
}