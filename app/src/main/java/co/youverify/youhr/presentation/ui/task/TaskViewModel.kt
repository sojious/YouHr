package co.youverify.youhr.presentation.ui.task



import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import co.youverify.youhr.presentation.TaskDetail
import co.youverify.youhr.presentation.ui.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor( private val navigator: Navigator) : ViewModel(){


    var  currentTaskList by mutableStateOf(pendingTasks)
    private set
    var categoryDropDownExpanded by mutableStateOf(false)
        private set
    var showDatePicker by mutableStateOf(false)
        private set

    var categorySpinnerText by mutableStateOf("Pending")
        private set




    fun updateCategoryDropDownState() {
        categoryDropDownExpanded = !categoryDropDownExpanded
        //currentTaskList = if (currentTaskList[0].isCompleted) pendingTasks else completedTasks
    }

    fun updateDatePickerExpandedState() {
        showDatePicker=!showDatePicker
    }

    fun showTaskDetail(taskId: Int) {
        navigator.navigate("${TaskDetail.route}/$taskId")
    }

    fun categoryDropDownOnDismissCallBack() {
        categoryDropDownExpanded=false
    }


    fun onPendingClicked() {
        currentTaskList= pendingTasks
        categoryDropDownExpanded=false
        categorySpinnerText="Pending"
    }

    fun onCompletedClicked() {
        currentTaskList= completedTasks
        categoryDropDownExpanded=false
        categorySpinnerText="Completed"
    }

    fun navigateBack() =navigator.navigateBack()


}
