package co.youverify.youhr.presentation.ui.login


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.youverify.youhr.core.util.BAD_REQUEST_ERROR_CODE
import co.youverify.youhr.core.util.INPUT_ERROR_CODE
import co.youverify.youhr.core.util.Result
import co.youverify.youhr.core.util.RESOURCE_NOT_FOUND_ERROR_CODE
import co.youverify.youhr.data.model.FilterUserDto
import co.youverify.youhr.data.model.LoginWithPassWordRequest
import co.youverify.youhr.data.remote.TokenInterceptor
import co.youverify.youhr.domain.model.FilteredUser
import co.youverify.youhr.domain.repository.PreferencesRepository
import co.youverify.youhr.domain.use_case.FilterAllLineManagerUseCase
import co.youverify.youhr.domain.use_case.FilterAllUserUseCase
import co.youverify.youhr.domain.use_case.LoginWithPasswordUseCase
import co.youverify.youhr.presentation.*
import co.youverify.youhr.presentation.ui.Navigator
import co.youverify.youhr.presentation.ui.UiEvent
import co.youverify.youhr.presentation.ui.settings.SettingsViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginWithPassWordViewModel @Inject constructor(
    private val navigator: Navigator,
    val loginWithPasswordUseCase: LoginWithPasswordUseCase,
    private val preferencesRepository: PreferencesRepository,
    private val tokenInterceptor: TokenInterceptor,
    private val filterAllUserUseCase: FilterAllUserUseCase,
    val filterAllLineManagerUseCase: FilterAllLineManagerUseCase
    ) : ViewModel(){


    var allUsers: List<FilteredUser> = emptyList()
    private set

    var allLineManagers: List<FilteredUser> = emptyList()
        private set
    var userPassword by  mutableStateOf("")
    private set

    var hideUserPassword by  mutableStateOf(true)
    private set

    var isErrorPassword by  mutableStateOf(false)
        private set

   private val _uIStateFlow= MutableStateFlow(UiState())
    val uIStatFlow=_uIStateFlow.asStateFlow()

   private val _uIEventFlow = Channel<UiEvent>()
    val uiEventFlow =_uIEventFlow.receiveAsFlow()

     var userEmail:String =""



    fun updateUserPassword(newValue:String){
        if (isErrorPassword) isErrorPassword=false
        userPassword=newValue
    }
    fun togglePasswordVisibility(){
        hideUserPassword=!hideUserPassword
    }
    fun logUserIn(settingsVm:SettingsViewModel) {

        viewModelScope.launch {

            _uIStateFlow.value=_uIStateFlow.value.copy(loading = true)
            val loginRequest=LoginWithPassWordRequest(email = userEmail, password = userPassword )

            loginWithPasswordUseCase(loginRequest).collect{networkResult->
                when(networkResult){

                    is Result.Success->{

                        //settingsVm.setCurrentPassword(userPassword)

                        val savedPassword=preferencesRepository.getUserPassword().first()
                        if (savedPassword.isEmpty()) preferencesRepository.saveUserPassword(userPassword)

                        val savedEmail=preferencesRepository.getUserEmail().first()
                        if (savedEmail.isEmpty()) preferencesRepository.saveUserEmail(loginRequest.email)

                        val savedToken=preferencesRepository.getUserToken().first()
                        if (savedToken.isEmpty()) preferencesRepository.saveUserToken(networkResult.data.data.token)


                        if (tokenInterceptor.getToken().isEmpty())
                            tokenInterceptor.setToken(networkResult.data.data.token)


                        getAllUser()
                        getAllLineManager()

                        val savedCodeCreationStatus=preferencesRepository.getUserPasscodeCreationStatus().first()
                        _uIStateFlow.value=_uIStateFlow.value.copy(loading = false,authenticated = true)
                        _uIEventFlow.send(UiEvent.ShowToast(message = networkResult.data.message))

                        //if user alrealdy created a code, go to home screen else go to createcode screen
                        if (savedCodeCreationStatus&& navigator.userPasswordReset)
                            navigator.navigatePopToInclusive(toRoute = BottomNavGraph.route, popToRoute = InputEmail.route)

                        if (savedCodeCreationStatus && !navigator.userPasswordReset)
                            navigator.navigatePopToInclusive(toRoute = BottomNavGraph.route, popToRoute = LoginWithCode.route)

                        if (!savedCodeCreationStatus)
                            navigator.navigatePopToInclusive(toRoute = CreateCode.route, popToRoute = InputEmail.route)
                    }

                    is Result.Error->{
                        val authError:String = when(networkResult.code){
                            INPUT_ERROR_CODE->networkResult.message.toString()
                            RESOURCE_NOT_FOUND_ERROR_CODE -> "Invalid Email!"
                            BAD_REQUEST_ERROR_CODE ->"Wrong Password!"
                            else-> "oop!,Something went wrong, try again"
                        }

                        isErrorPassword=true
                        _uIStateFlow.value=_uIStateFlow.value.copy(loading = false, authenticationError =authError )
                    }

                    is Result.Exception->{
                        _uIStateFlow.value=_uIStateFlow.value.copy(loading = false)
                        isErrorPassword=false
                        _uIEventFlow.send(UiEvent.ShowSnackBar(message = networkResult.genericMessage))
                    }
                }
            }
        }

    }

    fun onForgetPasswordClicked() { navigator.navigate(toRoute = ResetPassword.route) }
    fun navigateBack() =navigator.navigateBack()

    private suspend fun getAllUser() {
        filterAllUserUseCase.invoke().collect{result->
            when(result){

                is Result.Success->{
                    allUsers = result.data
                }
                is Result.Error->{

                    isErrorPassword = false
                    _uIStateFlow.value =_uIStateFlow.value.copy(loading = false)
                    _uIEventFlow.send(UiEvent.ShowToast(message = "Unexpected error occurred,try again!"))

                }
                is Result.Exception->{
                    _uIStateFlow.value = _uIStateFlow.value.copy(loading = false)
                    isErrorPassword = false
                    _uIEventFlow.send(UiEvent.ShowToast(message = "No internet connection"))
                }
            }
        }
    }

    private suspend fun getAllLineManager() {
        filterAllLineManagerUseCase.invoke().collect{result->
            when(result){

                is Result.Success->{
                    allLineManagers = result.data

                }
                is Result.Error->{

                    isErrorPassword = false
                    _uIStateFlow.value =_uIStateFlow.value.copy(loading = false)
                    _uIEventFlow.send(UiEvent.ShowToast(message = "Unexpected error occurred,try again!"))

                }
                is Result.Exception->{
                    _uIStateFlow.value = _uIStateFlow.value.copy(loading = false)
                    isErrorPassword = false
                    _uIEventFlow.send(UiEvent.ShowToast(message = "No internet connection"))
                }
            }
        }
    }

}


