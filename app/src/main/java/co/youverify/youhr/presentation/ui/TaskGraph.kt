package co.youverify.youhr.presentation.ui

import TaskListScreen
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import co.youverify.youhr.presentation.TaskDetail
import co.youverify.youhr.presentation.TaskGraph
import co.youverify.youhr.presentation.TaskList
import co.youverify.youhr.presentation.ui.task.*
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
fun NavGraphBuilder.TaskGraph(taskViewModel: TaskViewModel,taskDetailViewModel: TaskDetailViewModel){

    navigation(startDestination =TaskList.route , route =TaskGraph.route ){

        composable(route= TaskList.route){

            val uiState=taskViewModel.uiStateFlow.collectAsState()
            val isRefreshing by taskViewModel.isRefreshing.collectAsState()
           TaskListScreen(
               uiState =uiState,
               listState = rememberLazyListState(),
               categoryDropDownExpanded = taskViewModel.categoryDropDownExpanded,
               showDatePicker =taskViewModel.showDatePicker,
               onDatePickerOkClicked = { datePickerState, dateInputFieldIndex->
                   taskViewModel.onDatePickerOkClicked(datePickerState,dateInputFieldIndex)
                                       },
               onCategoryFilterClicked = { taskViewModel.updateCategoryDropDownState() },
               categoryDropDownOnDismissCallBack = {taskViewModel.categoryDropDownOnDismissCallBack()},
               onTaskItemClicked = {
                   taskViewModel.showTaskDetail(it)
               },
               categorySpinnerText = taskViewModel.categorySpinnerText,
               isEmptyState = taskViewModel.isEmptyState,
               dateRange = taskViewModel.dateRange,
               onDateInputFieldClicked = { index->
                   taskViewModel.onDateInputFieldClicked(index)
               },
               onTaskProgressDropDownItemClicked = { itemIndex ->
                   taskViewModel.onTaskProgressDropDownItemClicked(itemIndex)
               },
               currentEditableDateInputField = taskViewModel.currentEditableDateInputField,
               onDatePickerCancelClicked = {taskViewModel.onDatePickerCancelClicked()},
               fetchMore = {taskViewModel.fetchMore()},
               isFetchingMore = taskViewModel.isFetchingMore,
               isRefreshing = isRefreshing
           ) { taskViewModel.refresh() }
        }

        composable(
            route= TaskDetail.routWithArgs,
            arguments = TaskDetail.args
        ){navBackStackEntry->

            val taskId=navBackStackEntry.arguments?.getInt(TaskDetail.taskIdArg)
            val task=taskViewModel.allTasks?.get(taskId!!)!!
            TaskDetailScreen(
                taskMessage =taskDetailViewModel.taskMessage,
                onTaskMessageChanged = {taskDetailViewModel.updateTaskMessage(it)},
                onSendMessageButtonClicked = {},
                currentTask =task ,
                onBackArrowClicked = {taskViewModel.navigateBack()}
            )

        }

    }
}