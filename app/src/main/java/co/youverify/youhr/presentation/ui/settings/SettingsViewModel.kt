package co.youverify.youhr.presentation.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.youverify.youhr.core.util.Result
import co.youverify.youhr.data.model.ChangePasswordRequest
import co.youverify.youhr.data.model.CreateCodeRequest
import co.youverify.youhr.data.model.LoginWithCodeRequest
import co.youverify.youhr.data.model.LoginWithPassWordRequest
import co.youverify.youhr.data.remote.TokenInterceptor
import co.youverify.youhr.domain.model.User
import co.youverify.youhr.domain.repository.LeaveRepository
import co.youverify.youhr.domain.repository.PreferencesRepository
import co.youverify.youhr.domain.repository.TaskRepository
import co.youverify.youhr.domain.use_case.ChangePasswordUseCase
import co.youverify.youhr.domain.use_case.CreateCodeUseCase
import co.youverify.youhr.domain.use_case.GetLeaveRequestsUseCase
import co.youverify.youhr.domain.use_case.GetTasksUseCase
import co.youverify.youhr.domain.use_case.LoginWithCodeUseCase
import co.youverify.youhr.domain.use_case.LoginWithPasswordUseCase
import co.youverify.youhr.presentation.ChangePasscode
import co.youverify.youhr.presentation.ChangePassword
import co.youverify.youhr.presentation.Home
import co.youverify.youhr.presentation.InputEmail
import co.youverify.youhr.presentation.Profile
import co.youverify.youhr.presentation.ui.Navigator
import co.youverify.youhr.presentation.ui.UiEvent
import co.youverify.youhr.presentation.ui.home.HomeViewModel
import co.youverify.youhr.presentation.ui.login.InputEmailViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val navigator: Navigator,
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val createCodeUseCase: CreateCodeUseCase,
    private val loginWithPasswordUseCase: LoginWithPasswordUseCase,
    private val preferencesRepository: PreferencesRepository,
    private val getLeaveRequestsUseCase: GetLeaveRequestsUseCase,
    private val getTasksUseCase: GetTasksUseCase,
    val tokenInterceptor: TokenInterceptor
):ViewModel() {


    var currentUser: User? by mutableStateOf(null)
        private set

    var currentPassword = ""
        private set

    var currentPasscode = ""
        private set

    var settingsScreenLoading  by mutableStateOf(false)
        private set
    //val changePasswordScreenInputFieldsState = ChangePasswordInputFieldsState()

   // val changePasscodeScreenInputFieldsState = ChangePasscodeInputFieldsState()

    private val _changePasswordUiStateFlow = MutableStateFlow(ChangePasswordScreenUiState())
    val changePasswordUiStateFlow = _changePasswordUiStateFlow.asStateFlow()

    private val _changePasscodeUiStateFlow = MutableStateFlow(ChangePasscodeScreenUiState())
    val changePasscodeUiStateFlow = _changePasscodeUiStateFlow.asStateFlow()

    private val _uiEventFlow = MutableSharedFlow<UiEvent>()
     val uiEventFlow = _uiEventFlow.asSharedFlow()

    fun onSettingsItemClicked(index: Int,inputEmailViewModel: InputEmailViewModel) {
        if (index==1) navigator.navigate(ChangePassword.route)
        if (index==2) navigator.navigate(ChangePasscode.route)
        if (index==3) {logOut(inputEmailViewModel)}
    }

    private fun logOut(inputEmailViewModel: InputEmailViewModel) {
        viewModelScope.launch {
            settingsScreenLoading=true
            preferencesRepository.saveUserEmail("")
            preferencesRepository.saveUserToken("")
            tokenInterceptor.setToken("")
            preferencesRepository.setUserPasscodeCreationStatus(false)
            preferencesRepository.saveUserPasscode("")
            preferencesRepository.saveUserPassword("")
            preferencesRepository.setLogOutStatus(loggedOut = true)
            settingsScreenLoading=false
            getLeaveRequestsUseCase.invoke(clearLeaveData = true, isFirstLoad = false).first()
            getTasksUseCase.invoke(clearTasks = true, firstLoad = false).first()
            inputEmailViewModel.updateUserEmail("")
            //homeViewModel.resetCurrentUser()
            navigator.navigatePopToInclusive(toRoute = InputEmail.route, popToRoute = Home.route)
        }
    }

    fun onProfilePicClicked() {
        navigator.navigate(Profile.route)
    }

    fun updateCurrentUser(user: User?) {
        currentUser=user
    }

    suspend fun changePassword(changePasswordScreenInputFieldsState: ChangePasswordInputFieldsState) {
        //val regex="""^(?=.*[a-z])(?=.*[A-Z])(?=.*[\W_]).{8,}$""".toRegex()

        viewModelScope.launch {
            _changePasswordUiStateFlow.value=_changePasswordUiStateFlow.value.copy(loading = true, success = false)

            val savedPassword=preferencesRepository.getUserPassword().first()

            if (savedPassword.isEmpty()){
               // _changePasswordUiStateFlow.value=_changePasswordUiStateFlow.value.copy(loading = true, success = false)

                val userEmail= preferencesRepository.getUserEmail().first()
                loginWithPasswordUseCase.invoke(
                    loginWithPassWordRequest = LoginWithPassWordRequest(userEmail,changePasswordScreenInputFieldsState.oldPasswordValue)
                ).collect{
                   when(it){
                       is Result.Success->{
                           proceedToChangePassword(
                               changePasswordScreenInputFieldsState=changePasswordScreenInputFieldsState,
                               savedPassword = changePasswordScreenInputFieldsState.oldPasswordValue
                           )
                       }
                       is Result.Error->{
                           _changePasswordUiStateFlow.value=_changePasswordUiStateFlow.value.copy(loading = false)
                           changePasswordScreenInputFieldsState.updateIsOldPasswordError(true)
                       }
                       is Result.Exception->{
                           _changePasswordUiStateFlow.value=_changePasswordUiStateFlow.value.copy(loading = false)
                           _uiEventFlow.emit(UiEvent.ShowToast("Connection error!..check your internet connection"))
                       }
                   }
                }
            }else{
                proceedToChangePassword(
                    changePasswordScreenInputFieldsState=changePasswordScreenInputFieldsState,
                    savedPassword = savedPassword
                )
            }

        }
    }

    private suspend fun proceedToChangePassword(changePasswordScreenInputFieldsState:ChangePasswordInputFieldsState, savedPassword:String) {
        if (changePasswordScreenInputFieldsState.oldPasswordValue!=savedPassword){
            _changePasswordUiStateFlow.value=_changePasswordUiStateFlow.value.copy(loading = false)
            changePasswordScreenInputFieldsState.updateIsOldPasswordError(true)
        }
        else{

            //_changePasswordUiStateFlow.value=_changePasswordUiStateFlow.value.copy(loading = true, success = false)
            val changePasswordRequest=ChangePasswordRequest(
                password = changePasswordScreenInputFieldsState.oldPasswordValue,
                newPassword = changePasswordScreenInputFieldsState.newPasswordValue
            )
            changePasswordUseCase.invoke(changePasswordRequest).collect{changePasswordResult->
                when(changePasswordResult){
                    is Result.Success->{

                        preferencesRepository.saveUserPassword(changePasswordScreenInputFieldsState.newPasswordValue)
                        _changePasswordUiStateFlow.value=_changePasswordUiStateFlow.value.copy(loading = false,success = true)
                        delay(3000)
                        navigator.navigateBack()
                        //changePasswordScreenInputFieldsState.clearFields()
                    }
                    is Result.Error->{
                        _changePasswordUiStateFlow.value=_changePasswordUiStateFlow.value.copy(loading = false)
                        _uiEventFlow.emit(UiEvent.ShowToast(changePasswordResult.message?:"An unexpected error occurred!"))
                    }
                    is Result.Exception->{
                        _changePasswordUiStateFlow.value=_changePasswordUiStateFlow.value.copy(loading = false)
                        _uiEventFlow.emit(UiEvent.ShowToast("Connection error!..check your internet connection"))

                    }
                }
            }
        }
    }

    fun hideSuccessDialogForPasswordChange() {_changePasswordUiStateFlow.value = _changePasswordUiStateFlow.value.copy(success = false) }
    fun hideSuccessDialogForPasscodeChange() {_changePasscodeUiStateFlow.value = _changePasscodeUiStateFlow.value.copy(success = false) }
     fun changePasscode(changePasscodeScreenInputFieldsState:ChangePasscodeInputFieldsState) {

        viewModelScope.launch {
            _changePasscodeUiStateFlow.value=_changePasscodeUiStateFlow.value.copy(loading = true,success = false)
            if (!changePasscodeUiStateFlow.value.showOldPasscodeField){
                //_changePasscodeUiStateFlow.value=_changePasscodeUiStateFlow.value.copy(loading = true, success = false)

                //val userPasscode= changePasscodeScreenInputFieldsState.newPasscodeValue
                //val userEmail= preferencesRepository.getUserEmail().first()
                createCodeUseCase.invoke(
                    CreateCodeRequest(passcode = changePasscodeScreenInputFieldsState.newPasscodeValue.toInt()),
                    passcode1 = changePasscodeScreenInputFieldsState.newPasscodeValue.toInt()
                ).collect{
                    when(it){
                        is Result.Success->{
                            preferencesRepository.saveUserPasscode(changePasscodeScreenInputFieldsState.newPasscodeValue)
                            _changePasscodeUiStateFlow.value=_changePasscodeUiStateFlow.value.copy(loading = false,success = true)
                            delay(3000)
                            navigator.navigateBack()
                        }
                        is Result.Error->{
                            _changePasswordUiStateFlow.value=_changePasswordUiStateFlow.value.copy(loading = false)
                            _uiEventFlow.emit(UiEvent.ShowToast(it.message?:"An unexpectedError Occurred"))
                        }
                        is Result.Exception->{
                            _changePasswordUiStateFlow.value=_changePasswordUiStateFlow.value.copy(loading = false)
                            _uiEventFlow.emit(UiEvent.ShowToast("Connection error!..check your internet connection"))
                        }
                    }
                }
            }
            else{
                val savedPasscode=preferencesRepository.getUserPasscode().first()

                proceedToChangePasscode(
                    changePasscodeScreenInputFieldsState=changePasscodeScreenInputFieldsState,
                    savedPasscode = savedPasscode
                )
            }
        }
    }

    private suspend fun proceedToChangePasscode(changePasscodeScreenInputFieldsState: ChangePasscodeInputFieldsState, savedPasscode: String) {
       // _changePasscodeUiStateFlow.value=_changePasscodeUiStateFlow.value.copy(loading = true,success = false)
        if (changePasscodeScreenInputFieldsState.oldPasscodeValue!=savedPasscode){
            _changePasscodeUiStateFlow.value=_changePasscodeUiStateFlow.value.copy(loading = false)
            changePasscodeScreenInputFieldsState.updateIsOldPasscodeError(true)
        }else{
            val changePasscodeRequest=CreateCodeRequest(
                passcode = changePasscodeScreenInputFieldsState.newPasscodeValue.toInt(),
            )
            createCodeUseCase.invoke(changePasscodeRequest, passcode1 = changePasscodeScreenInputFieldsState.newPasscodeValue.toInt()).collect{changePasscodeResult->
                when(changePasscodeResult){
                    is Result.Success->{
                        preferencesRepository.saveUserPasscode(changePasscodeScreenInputFieldsState.newPasscodeValue)
                        _changePasscodeUiStateFlow.value=_changePasscodeUiStateFlow.value.copy(loading = false,success = true)
                        delay(3000)
                        navigator.navigateBack()
                        //changePasscodeScreenInputFieldsState.clearFields()
                    }
                    is Result.Error->{
                        _changePasscodeUiStateFlow.value=_changePasscodeUiStateFlow.value.copy(loading = false)
                        _uiEventFlow.emit(UiEvent.ShowToast(changePasscodeResult.message?:"An unexpected error occurred!"))
                    }
                    is Result.Exception->{
                        _changePasscodeUiStateFlow.value=_changePasscodeUiStateFlow.value.copy(loading = false)
                        _uiEventFlow.emit(UiEvent.ShowToast("Connection error!..check your internet connection"))

                    }
                }
            }
        }

    }

    fun resetState() {
        _changePasswordUiStateFlow.value=_changePasswordUiStateFlow.value.copy(loading = false,success = false)
        _changePasscodeUiStateFlow.value=_changePasscodeUiStateFlow.value.copy(loading = false,success = false)
    }

    fun onBackArrowClicked() {
        navigator.navigateBack()
    }

    fun showAppropriateInputFields() {
        viewModelScope.launch {
            val savedPasscode=preferencesRepository.getUserPasscode().first()
            if (savedPasscode.isEmpty()){
                _changePasscodeUiStateFlow.value=_changePasscodeUiStateFlow.value.copy(showOldPasscodeField = false)
            }else{ _changePasscodeUiStateFlow.value=_changePasscodeUiStateFlow.value.copy(showOldPasscodeField = true) }
        }
    }


}

data class ChangePasswordScreenUiState(val loading: Boolean =false, val success: Boolean =false)
data class ChangePasscodeScreenUiState(val loading: Boolean =false, val success: Boolean =false,val showOldPasscodeField:Boolean=true)
