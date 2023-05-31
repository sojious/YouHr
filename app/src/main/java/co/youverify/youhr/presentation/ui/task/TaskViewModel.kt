package co.youverify.youhr.presentation.ui.task



import DateRange
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.youverify.youhr.core.util.Result
import co.youverify.youhr.domain.model.Task
import co.youverify.youhr.domain.use_case.GetTasksUseCase
import co.youverify.youhr.presentation.TaskDetail
import co.youverify.youhr.presentation.ui.Navigator
import co.youverify.youhr.presentation.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val navigator: Navigator,
    private val getTasksUseCase: GetTasksUseCase
    ) : ViewModel(){


    var allTasks: List<Task>?=null
    private set
    var internetUnavailable by mutableStateOf(false)
    private set
    var isFetchingMore by mutableStateOf(false)
    private set
    var currentEditableDateInputField by  mutableStateOf(0)
    private set
    //var  currentTaskList by mutableStateOf(pendingTasks)
    //private set
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

    private var _uiEventFlow= MutableSharedFlow<UiEvent>()
    val uiEventFlow=_uiEventFlow.asSharedFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean>
        get() = _isRefreshing.asStateFlow()

    init {
        getTaskFirstLoad()
    }

    private fun getTaskFirstLoad() {
        viewModelScope.launch {
            getTasksUseCase.invoke(firstLoad = true)
                .collect{
                    when(it){
                        is Result.Success->{
                            _uiStateFlow.value = _uiStateFlow.value.copy(loading = false,tasks = it.data)
                            allTasks= _uiStateFlow.value.tasks
                        }
                        is Result.Error->{
                            _uiStateFlow.value = _uiStateFlow.value.copy(loading = false,tasks =null)
                            _uiEventFlow.emit(UiEvent.ShowToast("An unexpected error occured while fetchind tasks!!"))
                        }
                        is Result.Exception->{
                            _uiStateFlow.value = _uiStateFlow.value.copy(loading = false,tasks = null, connectionError = true)
                            //internetUnavailable=true
                        }
                    }
                }
        }
    }

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
        //currentTaskList= pendingTasks
        categoryDropDownExpanded=false
        categorySpinnerText="Pending"
    }

 private   fun onCompletedClicked() {
        //currentTaskList= completedTasks
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

    fun fetchMore() {


            isFetchingMore=true
            viewModelScope.launch {
                getTasksUseCase.invoke(firstLoad = false, page = uiStateFlow.value.tasks?.lastIndex!!+1)
                    .collect{
                        when(it){
                            is Result.Success->{
                                _uiStateFlow.value = _uiStateFlow.value.copy(loading = false,tasks = _uiStateFlow.value.tasks?.plus(it.data))
                                allTasks=_uiStateFlow.value.tasks
                                isFetchingMore=false

                            }
                            is Result.Error->{
                                _uiStateFlow.value = _uiStateFlow.value.copy(loading = false,tasks =null)
                                isFetchingMore=false
                                _uiEventFlow.emit(UiEvent.ShowToast("An unexpected error occured while fetchind tasks!!"))

                            }
                            is Result.Exception->{
                                _uiStateFlow.value = _uiStateFlow.value.copy(loading = false,tasks = null)
                                isFetchingMore=false
                                internetUnavailable=true
                            }
                        }
                    }
            }

    }

    fun refresh(){

        viewModelScope.launch {
            _uiStateFlow.value = _uiStateFlow.value.copy(loading = true)
            getTasksUseCase.invoke(firstLoad = true)
                .collect{
                    when(it){
                        is Result.Success->{
                            _uiStateFlow.value = _uiStateFlow.value.copy(loading = false,tasks = it.data)
                            allTasks= _uiStateFlow.value.tasks
                            _isRefreshing.emit(false)

                        }
                        is Result.Error->{
                            _uiStateFlow.value = _uiStateFlow.value.copy(loading = false,tasks =null)
                            _uiEventFlow.emit(UiEvent.ShowToast("An unexpected error occured while fetchind tasks!!"))
                            _isRefreshing.emit(false)
                        }
                        is Result.Exception->{
                            _uiStateFlow.value = _uiStateFlow.value.copy(loading = false,tasks = null, connectionError = true)
                            _isRefreshing.emit(false)
                            //internetUnavailable=true
                        }
                    }
                }
        }
    }

    data class UiState(val loading:Boolean=true, val tasks:List<Task>?=null,val connectionError:Boolean=false )
}
