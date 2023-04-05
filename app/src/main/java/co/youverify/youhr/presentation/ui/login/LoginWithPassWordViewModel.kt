package co.youverify.youhr.presentation.ui.login


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.youverify.youhr.core.util.BAD_REQUEST_ERROR_CODE
import co.youverify.youhr.core.util.INPUT_ERROR_CODE
import co.youverify.youhr.core.util.NetworkResult
import co.youverify.youhr.core.util.RESOURCE_NOT_FOUND_ERROR_CODE
import co.youverify.youhr.data.model.LoginWithPassWordRequest
import co.youverify.youhr.data.remote.TokenInterceptor
import co.youverify.youhr.domain.repository.PreferencesRepository
import co.youverify.youhr.domain.use_case.LoginWithPasswordUseCase
import co.youverify.youhr.presentation.*
import co.youverify.youhr.presentation.ui.Navigator
import co.youverify.youhr.presentation.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginWithPassWordViewModel @Inject constructor(
    private val navigator: Navigator,
    private val stateHandle: SavedStateHandle,
    val loginWithPasswordUseCase: LoginWithPasswordUseCase,
    private val preferencesRepository: PreferencesRepository,
    private val tokenInterceptor: TokenInterceptor
    ) : ViewModel(){


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

    fun logUserIn() {


        viewModelScope.launch {

            _uIStateFlow.value=_uIStateFlow.value.copy(loading = true)

            val loginRequest=LoginWithPassWordRequest(email = userEmail, password = userPassword )

            loginWithPasswordUseCase(loginRequest).collect{networkResult->
                when(networkResult){

                    is NetworkResult.Success->{

                        val savedEmail=preferencesRepository.getUserEmail().first()
                        if (savedEmail.isEmpty()) preferencesRepository.saveUserEmail(loginRequest.email)

                        val savedToken=preferencesRepository.getUserToken().first()
                        if (savedToken.isEmpty()) preferencesRepository.saveUserToken(networkResult.data.data.token)


                        if (tokenInterceptor.getToken().isEmpty())
                            tokenInterceptor.setToken(networkResult.data.data.token)


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
                    is NetworkResult.Error->{

                        val authError:String = when(networkResult.code){
                            INPUT_ERROR_CODE->networkResult.message.toString()
                            RESOURCE_NOT_FOUND_ERROR_CODE -> "Invalid Email!"
                            BAD_REQUEST_ERROR_CODE ->"Wrong Password!"
                            else-> "oop!,Something went wrong, try again"
                        }

                        isErrorPassword=true
                        _uIStateFlow.value=_uIStateFlow.value.copy(loading = false, authenticationError =authError )
                    }
                    is NetworkResult.Exception->{
                        _uIStateFlow.value=_uIStateFlow.value.copy(loading = false)
                        isErrorPassword=false
                        _uIEventFlow.send(UiEvent.ShowSnackBar(message = networkResult.genericMessage))
                    }
                }
            }
        }

    }

    fun onForgetPasswordClicked() {

        navigator.navigate(
            //toRoute = "${CreatePassword.route}?createPassword=true",

            toRoute = ResetPassword.route,
        )
    }

    fun navigateBack() =navigator.navigateBack()

}


