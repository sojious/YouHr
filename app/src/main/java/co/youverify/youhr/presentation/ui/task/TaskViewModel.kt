package co.youverify.youhr.presentation.ui.task



import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusState
import androidx.lifecycle.ViewModel
import co.youverify.youhr.presentation.TaskDetail
import co.youverify.youhr.presentation.ui.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor( private val navigator: Navigator) : ViewModel(){


    var currentEditableDateInputField by  mutableStateOf(0)
    private set
    var  currentTaskList by mutableStateOf(pendingTasks)
    private set
    var categoryDropDownExpanded by mutableStateOf(false)
        private set
    var showDatePicker by mutableStateOf(false)
        private set

    var categorySpinnerText by mutableStateOf("Pending")
        private set

    var isEmptyState by mutableStateOf(false)
        private set

    var dateRange by mutableStateOf(DateRange())
        private set

    private var _uiStateFlow= MutableStateFlow(UiState())
    val uiStateFlow=_uiStateFlow.asStateFlow()





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


    fun navigateBack() =navigator.navigateBack()
    fun onDateInputFieldClicked(index: Int) {

            currentEditableDateInputField=index
            showDatePicker=true
    }

    fun onTaskProgressDropDownItemClicked(itemIndex: Int) {
        when(itemIndex){
            1-> onPendingClicked()
            2-> onCompletedClicked()
        }
    }

   private fun onPendingClicked() {
        currentTaskList= pendingTasks
        categoryDropDownExpanded=false
        categorySpinnerText="Pending"
    }

 private   fun onCompletedClicked() {
        currentTaskList= completedTasks
        categoryDropDownExpanded=false
        categorySpinnerText="Completed"
    }

    fun onDatePickerCancelClicked( ) {
        showDatePicker=false
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun onDatePickerOkClicked(datePickerState: DatePickerState, dateInputFieldIndex: Int) {

        if (dateInputFieldIndex==1){
            dateRange=dateRange.copy(startDateMillis = datePickerState.selectedDateMillis)
            showDatePicker=false
        }

        if (dateInputFieldIndex==2){
            dateRange=dateRange.copy(endDateMillis = datePickerState.selectedDateMillis)
            showDatePicker=false
        }

    }

    data class UiState(val loading:Boolean=false, val tasks:List<Task> = pendingTasks)
}
