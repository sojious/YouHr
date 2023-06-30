package co.youverify.youhr.presentation.ui.leave


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.youverify.youhr.core.util.Result
import co.youverify.youhr.core.util.toFormattedDateString
import co.youverify.youhr.data.model.FilterUserDto
import co.youverify.youhr.data.model.LeaveApplicationRequest
import co.youverify.youhr.domain.model.FilteredUser
import co.youverify.youhr.domain.model.LeaveRequest
import co.youverify.youhr.domain.model.LeaveSummary
import co.youverify.youhr.domain.model.User
import co.youverify.youhr.domain.use_case.CreateLeaveRequestUseCase
import co.youverify.youhr.domain.use_case.GetLeaveRequestsUseCase
import co.youverify.youhr.domain.use_case.GetLeaveSummaryUseCase
import co.youverify.youhr.presentation.LeaveDetail
import co.youverify.youhr.presentation.ui.Navigator
import co.youverify.youhr.presentation.ui.UiEvent
import co.youverify.youhr.presentation.ui.login.ConfirmCodeViewModel
import co.youverify.youhr.presentation.ui.login.LoginWithCodeViewModel
import co.youverify.youhr.presentation.ui.login.LoginWithPassWordViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LeaveManagementViewModel @Inject constructor(
    val navigator: Navigator,
    val getLeaveRequestsUseCase: GetLeaveRequestsUseCase,
    private val getLeaveSummaryUseCase: GetLeaveSummaryUseCase,
    private val createLeaveRequestUseCase: CreateLeaveRequestUseCase
):ViewModel() {



    var allLeaveRequests:List<LeaveRequest> by mutableStateOf(emptyList())
        private set

     var allUsers:List<FilteredUser> = emptyList()
        private set

    var userGender ="Male"
        private set

    var allLineManagers:List<FilteredUser> = emptyList()
        private set
    var creatingNewLeaveRequest by mutableStateOf(false)
    private set

    var showNewLeaveRequestSuccessDialog by mutableStateOf(false)
    private set

    private val _uIStateFlow = MutableStateFlow(LeaveManagementUiState())
    val uIStateFlow=_uIStateFlow.asStateFlow()

    private val _detailUiState = MutableStateFlow(LeaveDetailUiState())
    val detailUiState=_detailUiState.asStateFlow()

    private val _uIEventFlow:MutableSharedFlow<UiEvent> = MutableSharedFlow()
    val uIEventFlow=_uIEventFlow.asSharedFlow()




    fun createNewLeaveRequest(leaveApplicationFormState: LeaveApplicationFormState){

        creatingNewLeaveRequest=true
        viewModelScope.launch {
            //_uIStateFlow.value=_uIStateFlow.value.copy(creatingNewLeaveRequest = true)

            val newLeaveRequest=LeaveApplicationRequest(
                leaveType = "${leaveApplicationFormState.selectedLeaveType.id} Leave",
                startDate = leaveApplicationFormState.leaveStartDateMillis?.toFormattedDateString("yyyy-MM-dd")!!,
                endDate = leaveApplicationFormState.leaveEndDateMillis?.toFormattedDateString("yyyy-MM-dd")!!,
                reasonForLeave = leaveApplicationFormState.reason,
                //contactName = leaveApplicationFormState.contactName,
                //contactEmail = leaveApplicationFormState.contactEmail,
                //contactNumber = leaveApplicationFormState.phoneNumber,
                relieverName = leaveApplicationFormState.reliever,
                linemanagerEmail = leaveApplicationFormState.lineManagerEmail,
                linemanagerName = leaveApplicationFormState.lineManager,
                relieverEmail = leaveApplicationFormState.relieverEmail,
                alternativeNumber = leaveApplicationFormState.alternativePhoneNumber,
                relieverId =leaveApplicationFormState.relieverId
            )

           createLeaveRequestUseCase.invoke(request = newLeaveRequest).collect{newLeaveRequestResult->
               when(newLeaveRequestResult){
                   is Result.Success->{
                       creatingNewLeaveRequest = false
                       showNewLeaveRequestSuccessDialog = true
                   }
                   is Result.Error->{
                       creatingNewLeaveRequest = false
                       _uIEventFlow.emit(UiEvent.ShowToast(newLeaveRequestResult.message?:"An unexpected error occurred!!..Pls try again"))
                   }
                   is Result.Exception->{
                      // _uIStateFlow.value=_uIStateFlow.value.copy(creatingNewLeaveRequest = false)
                       creatingNewLeaveRequest = false
                       _uIEventFlow.emit(UiEvent.ShowToast("Error connecting to the network!!..Check your internet connection and try again"))
                   }
               }
           }
        }
    }
    fun getLeaveRequestsAndSummaryOnFirstLoad() {
        viewModelScope.launch {
            _uIStateFlow.value = _uIStateFlow.value.copy(loading = true)

            getLeaveRequestsUseCase(isFirstLoad = true).collect{leaveRequestsResult->
                when(leaveRequestsResult){
                    is Result.Success->{

                        getLeaveSummaryUseCase.invoke(isFirstLoad = true).collect{leaveSummaryResult->
                            when(leaveSummaryResult){
                                is Result.Success->{
                                    _uIStateFlow.value = _uIStateFlow.value.copy(
                                        loading = false, filteredList = leaveRequestsResult.data,leaveSummary=leaveSummaryResult.data,
                                        historyIsEmpty = leaveRequestsResult.data.isEmpty(), internetConnectionError = false, unexpectedError = false
                                    )
                                    allLeaveRequests = leaveRequestsResult.data
                                }
                                is Result.Error->{_uIStateFlow.value = _uIStateFlow.value.copy(loading = false, unexpectedError = true, internetConnectionError = false)}
                                is Result.Exception->{_uIStateFlow.value = _uIStateFlow.value.copy(loading = false, internetConnectionError = true, unexpectedError = false)}
                            }
                        }

                    }
                    is Result.Error->{ _uIStateFlow.value = _uIStateFlow.value.copy(loading = false, unexpectedError = true, internetConnectionError = false) }
                    is Result.Exception->{ _uIStateFlow.value = _uIStateFlow.value.copy(loading = false, internetConnectionError = true, unexpectedError = false) }
                }
            }


        }

    }

    fun onRefresh(){
        _uIStateFlow.value=_uIStateFlow.value.copy(isRefreshing = true)
        viewModelScope.launch {

            getLeaveRequestsUseCase(isFirstLoad = false).collect{leaveRequestsResult->
                when(leaveRequestsResult){
                    is Result.Success->{

                    getLeaveSummaryUseCase.invoke(isFirstLoad = false).collect{leaveSummaryResult->
                        when(leaveSummaryResult){
                            is Result.Success->{
                                _uIStateFlow.value = _uIStateFlow.value.copy(isRefreshing = false, filteredList = leaveRequestsResult.data,leaveSummary=leaveSummaryResult.data,
                                    historyIsEmpty = leaveRequestsResult.data.isEmpty(), unexpectedError = false, internetConnectionError = false)
                                allLeaveRequests = leaveRequestsResult.data
                            }
                            is Result.Error->{
                                _uIStateFlow.value = _uIStateFlow.value.copy(isRefreshing = false)
                                _uIEventFlow.emit(UiEvent.ShowSnackBar("An unexpected error occurred!!..try again"))
                            }
                            is Result.Exception->{
                                _uIStateFlow.value = _uIStateFlow.value.copy(isRefreshing = false)
                                _uIEventFlow.emit(UiEvent.ShowSnackBar("Network error...Check your internet connection and retry!!"))
                            }
                        }
                    }

                    }


                    is Result.Error->{
                        _uIStateFlow.value = _uIStateFlow.value.copy(isRefreshing = false)
                        _uIEventFlow.emit(UiEvent.ShowSnackBar("An unexpected error occurred!!..try again"))
                    }
                    is Result.Exception->{
                        _uIStateFlow.value = _uIStateFlow.value.copy(isRefreshing = false)
                        _uIEventFlow.emit(UiEvent.ShowSnackBar("Network error...Check your internet connection and retry!!"))
                    }
                }
            }
        }
    }

    fun onDropDownItemClicked(index:Int){
        val updatedFilterText=when(index){
            1->{
                _uIStateFlow.value.filteredList = allLeaveRequests
                LeaveStatus.ALL.id
            }
            2->{
                _uIStateFlow.value.filteredList = allLeaveRequests.filter {
                    it.status == LeaveStatus.PENDING.id
                }
                LeaveStatus.PENDING.id
            }
            3->{
                _uIStateFlow.value.filteredList = allLeaveRequests.filter {
                    it.status==LeaveStatus.APPROVED.id
                }
                LeaveStatus.APPROVED.id
            }
            else->{
                _uIStateFlow.value.filteredList = allLeaveRequests.filter {
                    it.status == LeaveStatus.REJECTED.id
                }
                LeaveStatus.REJECTED.id
            }
        }

        _uIStateFlow.value = _uIStateFlow.value.copy(dropDownExpanded = false, filterText = updatedFilterText)
    }

    fun onDropDownODismissRequested(){
        _uIStateFlow.value = _uIStateFlow.value.copy(dropDownExpanded = false)
    }

    fun updateDropDownExpandedStatus(){
        _uIStateFlow.value = _uIStateFlow.value.copy(dropDownExpanded = true)
    }
    fun onCreateLeaveRequestClicked(){
        navigator.navigate(co.youverify.youhr.presentation.LeaveRequest.route)
    }

    fun displayLeaveDetail(leaveId: Int) {
        navigator.navigate(toRoute = "${LeaveDetail.route}/$leaveId")
    }

    fun updateLeaveDetailExpanded() {
        val expanded = !_detailUiState.value.leaveDetailExpanded
        _detailUiState.value = _detailUiState.value.copy(leaveDetailExpanded = expanded )
    }

    fun updateContactInfoExpanded() {
        val expanded = !_detailUiState.value.contactInfoExpanded
        _detailUiState.value = _detailUiState.value.copy(contactInfoExpanded = expanded )
    }

    fun onDialogCloseClicked() {
        showNewLeaveRequestSuccessDialog = false
        navigator.navigateBack()
    }

    fun initializeUsers(
        loginWithCodeViewModel: LoginWithCodeViewModel,
        loginWithPassWordViewModel: LoginWithPassWordViewModel
    ){
        allUsers = if (loginWithCodeViewModel.allUsers.isNotEmpty()) loginWithCodeViewModel.allUsers
        else loginWithPassWordViewModel.allUsers

        allLineManagers=if (loginWithCodeViewModel.allLineManagers.isNotEmpty()) loginWithCodeViewModel.allLineManagers
        else loginWithPassWordViewModel.allLineManagers
    }

    fun updateUserGender(gender: String) { userGender=gender}
    fun onBackArrowClicked() {
        navigator.navigateBack()
    }


}

data class LeaveManagementUiState(
    val historyIsEmpty:Boolean = false,
    val isRefreshing:Boolean = false,
    val dropDownExpanded:Boolean = false,
    val filterText:String=LeaveStatus.ALL.id,
    val internetConnectionError:Boolean = false,
    val unexpectedError:Boolean = false,
    val loading:Boolean = true,
    var filteredList: List<LeaveRequest> = emptyList(),
    val leaveSummary: LeaveSummary = LeaveSummary(
        0,0,0, 0,
        0,0,0,""
    )
)

data class LeaveDetailUiState(
    val contactInfoExpanded:Boolean = false,
    val leaveDetailExpanded:Boolean = false
)