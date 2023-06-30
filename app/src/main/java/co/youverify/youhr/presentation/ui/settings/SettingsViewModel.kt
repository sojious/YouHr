package co.youverify.youhr.presentation.ui.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.youverify.youhr.core.util.Result
import co.youverify.youhr.data.model.ChangePasswordRequest
import co.youverify.youhr.data.model.CreateCodeRequest
import co.youverify.youhr.data.remote.TokenInterceptor
import co.youverify.youhr.domain.model.User
import co.youverify.youhr.domain.repository.PreferencesRepository
import co.youverify.youhr.domain.use_case.ChangePasswordUseCase
import co.youverify.youhr.domain.use_case.CreateCodeUseCase
import co.youverify.youhr.presentation.ChangePasscode
import co.youverify.youhr.presentation.ChangePassword
import co.youverify.youhr.presentation.Home
import co.youverify.youhr.presentation.InputEmail
import co.youverify.youhr.presentation.Profile
import co.youverify.youhr.presentation.ui.Navigator
import co.youverify.youhr.presentation.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
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
    val preferencesRepository: PreferencesRepository,
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

    fun onSettingsItemClicked(index: Int) {
        if (index==1) navigator.navigate(ChangePassword.route)
        if (index==2) navigator.navigate(ChangePasscode.route)
        if (index==3) {logOut()}
    }

    private fun logOut() {
        viewModelScope.launch {
            settingsScreenLoading=true
            preferencesRepository.saveUserEmail("")
            preferencesRepository.saveUserToken("")
            tokenInterceptor.setToken("")
            preferencesRepository.setUserPasscodeCreationStatus(false)
            preferencesRepository.saveUserPasscode("")
            preferencesRepository.saveUserPassword("")
            settingsScreenLoading=false
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
        val regex="""^(?=.*[a-z])(?=.*[A-Z])(?=.*[\W_]).{8,}$""".toRegex()
        val savedPassword=preferencesRepository.getUserPassword().first()

        if (changePasswordScreenInputFieldsState.newPasswordValue != changePasswordScreenInputFieldsState.confirmPasswordValue){
            changePasswordScreenInputFieldsState.updateIsConfirmPasswordError(true)
        }else if (changePasswordScreenInputFieldsState.oldPasswordValue!=savedPassword){
            changePasswordScreenInputFieldsState.updateIsOldPasswordError(true)
        }
        else{
            viewModelScope.launch {

                if (!regex.matches(changePasswordScreenInputFieldsState.newPasswordValue)){
                    _uiEventFlow.emit(UiEvent.ShowToast("New password does not meet requirements!"))
                    return@launch
                }

                _changePasswordUiStateFlow.value=_changePasswordUiStateFlow.value.copy(loading = true, success = false)
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
    }

    fun hideSuccessDialogForPasswordChange() {_changePasswordUiStateFlow.value = _changePasswordUiStateFlow.value.copy(success = false) }
    fun hideSuccessDialogForPasscodeChange() {_changePasscodeUiStateFlow.value = _changePasscodeUiStateFlow.value.copy(success = false) }
    suspend fun changePasscode(changePasscodeScreenInputFieldsState:ChangePasscodeInputFieldsState) {

        val savedPasscode=preferencesRepository.getUserPasscode().first()
        if (changePasscodeScreenInputFieldsState.newPasscodeValue != changePasscodeScreenInputFieldsState.confirmPasscodeValue){
            changePasscodeScreenInputFieldsState.updateIsConfirmPasscodeError(true)
        } else if (changePasscodeScreenInputFieldsState.oldPasscodeValue!=savedPasscode){
            changePasscodeScreenInputFieldsState.updateIsOldPasscodeError(true)
        }
        else{
            viewModelScope.launch {

                if (
                    changePasscodeScreenInputFieldsState.newPasscodeValue.trim().length!=6
                    ||changePasscodeScreenInputFieldsState.confirmPasscodeValue.trim().length!=6
                ){
                    _uiEventFlow.emit(UiEvent.ShowToast("New passcode does not meet requirements!"))
                    return@launch
                }

                _changePasscodeUiStateFlow.value=_changePasscodeUiStateFlow.value.copy(loading = true,success = false)
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
    }

    fun resetState() {
        _changePasswordUiStateFlow.value=_changePasswordUiStateFlow.value.copy(loading = false,success = false)
        _changePasscodeUiStateFlow.value=_changePasscodeUiStateFlow.value.copy(loading = false,success = false)
    }

    fun onBackArrowClicked() {
        navigator.navigateBack()
    }


}

data class ChangePasswordScreenUiState(val loading: Boolean =false, val success: Boolean =false)
data class ChangePasscodeScreenUiState(val loading: Boolean =false, val success: Boolean =false)
