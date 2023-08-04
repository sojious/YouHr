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
import co.youverify.youhr.core.util.toEpochMillis
import co.youverify.youhr.domain.model.Task
import co.youverify.youhr.domain.use_case.GetTasksUseCase
import co.youverify.youhr.presentation.TaskDetail
import co.youverify.youhr.presentation.ui.Navigator
import co.youverify.youhr.presentation.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val navigator: Navigator,
    private val getTasksUseCase: GetTasksUseCase
    ) : ViewModel(){


    var allTasks: List<Task> = emptyList()
    private set


    //var filteredTasks: List<Task>? = null
        //private set
    //var internetUnavailable by mutableStateOf(false)
    //private set
    //var isFetchingMore by mutableStateOf(false)
    //private set
    //var currentEditableDateInputField by  mutableStateOf(0)
    //private set

    //var categoryDropDownExpanded by mutableStateOf(false)
        //private set
    //var showDatePicker by mutableStateOf(false)
       // private set

    //var categorySpinnerText by mutableStateOf("All")
       // private set

    //var isEmptyState by mutableStateOf(false)
       // private set

    var dateRange by mutableStateOf(DateRange())
        private set

    //private var _uiStateFlow = MutableStateFlow(UiState())
    //val uiStateFlow=_uiStateFlow.asStateFlow()

    private var _uiEventFlow = MutableSharedFlow<UiEvent>()
    val uiEventFlow = _uiEventFlow.asSharedFlow()

    private val _uiStateFlow = MutableStateFlow(TaskListUiState())
    val uIStateFlow=_uiStateFlow.asStateFlow()

    //private val _isRefreshing = MutableStateFlow(false)
    //val isRefreshing: StateFlow<Boolean>
        //get() = _isRefreshing.asStateFlow()

    init {
        //getTaskFirstLoad()
    }
    fun getTaskFirstLoad() {
        viewModelScope.launch {
            _uiStateFlow.value = _uiStateFlow.value.copy(loading = true)
            getTasksUseCase.invoke(firstLoad = true)
                .collect{
                    when(it){
                        is Result.Success->{

                            _uiStateFlow.value = _uiStateFlow.value.copy(
                                loading = false, filteredList = it.data, taskListIsEmpty = it.data.isEmpty(),
                                internetConnectionError = false, unexpectedError = false, emptyTaskContentMessage = "You haven't been assigned any tasks yet"
                            )
                            allTasks=it.data
                        }
                        is Result.Error->{
                            //_uiStateFlow.value = _uiStateFlow.value.copy(loading = false,tasks = null)
                            _uiStateFlow.value = _uiStateFlow.value.copy(loading = false, unexpectedError = true, internetConnectionError = false)
                           // _uiEventFlow.emit(UiEvent.ShowToast("An unexpected error occured while fetchind tasks!!"))
                        }
                        is Result.Exception->{
                            //_uiStateFlow.value = _uiStateFlow.value.copy(loading = false,tasks = null, connectionError = true)
                            _uiStateFlow.value = _uiStateFlow.value.copy(loading = false, internetConnectionError = true, unexpectedError = false)
                        }
                    }
                }
        }
    }

    fun updateCategoryDropDownState() {
        val updatedState = !_uiStateFlow.value.categoryDropdownExpanded
        _uiStateFlow.value = _uiStateFlow.value.copy(categoryDropdownExpanded = updatedState)
    }

    fun updateDatePickerExpandedState() {
        //showDatePicker=!showDatePicker
        val updatedState = !_uiStateFlow.value.showDatePicker
        _uiStateFlow.value = _uiStateFlow.value.copy(showDatePicker = updatedState)
    }

    fun showTaskDetail(taskId: Int) {
        navigator.navigate("${TaskDetail.route}/$taskId")
    }

    fun categoryDropDownOnDismissCallBack() {
        //categoryDropDownExpanded = false
        _uiStateFlow.value = _uiStateFlow.value.copy(categoryDropdownExpanded = false)
    }


    fun navigateBack() = navigator.navigateBack()
    fun onDateInputFieldClicked(index: Int) {

            //currentEditableDateInputField = index
            //showDatePicker = true
        _uiStateFlow.value = _uiStateFlow.value.copy(showDatePicker = true, currentEditableDateInputField = index)
    }

    fun filterTasksByStatus(status: TaskStatus) {

        val filteredTasks= if (status==TaskStatus.ALL)  allTasks else allTasks.filter { it.status == status.id }
        _uiStateFlow.value = _uiStateFlow.value.copy(filterText = status.id, filteredList =filteredTasks,
            categoryDropdownExpanded = false, emptyTaskContentMessage = "", taskListIsEmpty = filteredTasks.isEmpty())

    }



    fun onDatePickerCancelClicked( ) { _uiStateFlow.value=_uiStateFlow.value.copy(showDatePicker = false) }

    @OptIn(ExperimentalMaterial3Api::class)
    fun onDatePickerOkClicked(datePickerState: DatePickerState, dateInputFieldIndex: Int) {

        if (dateInputFieldIndex == 1){
            dateRange = dateRange.copy(startDateMillis = datePickerState.selectedDateMillis)
            _uiStateFlow.value=_uiStateFlow.value.copy(showDatePicker = false)
        }

        if (dateInputFieldIndex == 2){
            dateRange = dateRange.copy(endDateMillis = datePickerState.selectedDateMillis)
            _uiStateFlow.value=_uiStateFlow.value.copy(showDatePicker = false)
        }

    }


    fun fetchMore() {


            _uiStateFlow.value=_uiStateFlow.value.copy(isFetchingMore = true)
            viewModelScope.launch {
                getTasksUseCase.invoke(firstLoad = false, page = allTasks.last().page+1)
                    .collect{
                        when(it){
                            is Result.Success->{
                               allTasks= allTasks.plus(it.data)
                                _uiStateFlow.value = _uiStateFlow.value.copy(isFetchingMore = false, filteredList = _uiStateFlow.value.filteredList.plus(it.data))
                                //isFetchingMore = false

                            }
                            is Result.Error->{
                                _uiStateFlow.value = _uiStateFlow.value.copy(isFetchingMore = false)
                                //isFetchingMore=false
                                _uiEventFlow.emit(UiEvent.ShowToast("An unexpected error occurred while trying to fetch more tasks!!"))

                            }
                            is Result.Exception->{
                                _uiStateFlow.value = _uiStateFlow.value.copy(isFetchingMore = false)
                                _uiEventFlow.emit(UiEvent.ShowToast("Connection error!..Check your internet connection and try again"))
                                //isFetchingMore = false
                                //internetUnavailable = true
                            }
                        }
                    }
            }

    }

    fun refresh(){

        viewModelScope.launch {
            _uiStateFlow.value = _uiStateFlow.value.copy(isRefreshing = true)
            getTasksUseCase.invoke(firstLoad = true)
                .collect{
                    when(it){
                        is Result.Success->{
                            _uiStateFlow.value = _uiStateFlow.value.copy(isRefreshing = false, filteredList = it.data, taskListIsEmpty = it.data.isEmpty())
                            allTasks= it.data

                        }
                        is Result.Error->{
                            _uiStateFlow.value = _uiStateFlow.value.copy(isRefreshing = false)
                            _uiEventFlow.emit(UiEvent.ShowToast("An unexpected error occurred while fetching tasks!!"))
                        }
                        is Result.Exception->{
                            _uiStateFlow.value = _uiStateFlow.value.copy(isRefreshing = false)
                            _uiEventFlow.emit(UiEvent.ShowToast("Connection error!..Check your internet connection and try again"))
                        }
                    }
                }
        }
    }

    fun filterTasksByDate() {

        val filteredTasks = allTasks.filter {
            it.timeStampCreated.toEpochMillis() >= dateRange.startDateMillis!! &&
                    it.timeStampCreated.toEpochMillis() <= dateRange.endDateMillis!!
        }

        _uiStateFlow.value=_uiStateFlow.value.copy(
            filteredList = filteredTasks, showDatePicker = false,
            emptyTaskContentMessage = "", taskListIsEmpty = filteredTasks.isEmpty()
            )

    }

   /*data class UiState(
        val loading:Boolean = true,
        var tasks:List<Task>? = null,
        val connectionError:Boolean = false,
        val emptyTaskContentMessage:String = "You haven’t been assigned any tasks yet"
    )*/
}

data class TaskListUiState(
    val taskListIsEmpty:Boolean = false,
    val isRefreshing:Boolean = false,
    val categoryDropdownExpanded:Boolean = false,
    val showDatePicker:Boolean=false,
    val filterText:String= TaskStatus.ALL.id,
    val internetConnectionError:Boolean = false,
    val unexpectedError:Boolean = false,
    val loading:Boolean = false,
    val isFetchingMore:Boolean=false,
    val currentEditableDateInputField:Int=0,
    var filteredList: List<Task> = emptyList(),
    val emptyTaskContentMessage:String=""

)
