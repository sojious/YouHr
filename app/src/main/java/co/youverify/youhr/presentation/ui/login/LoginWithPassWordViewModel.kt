package co.youverify.youhr.presentation.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.youverify.youhr.core.util.NetworkResult
import co.youverify.youhr.data.model.LoginWithPassWordRequest
import co.youverify.youhr.domain.use_case.LoginWithPasswordUseCase
import co.youverify.youhr.presentation.*
import co.youverify.youhr.presentation.ui.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginWithPassWordViewModel @Inject constructor( private val navigator: Navigator, val stateHandle: SavedStateHandle,val loginWithPasswordUseCase: LoginWithPasswordUseCase) : ViewModel(){


    var userPassword by  mutableStateOf("")
    private set

    var hideUserPassword by  mutableStateOf(true)
    private set

    var isErrorPassword by  mutableStateOf(false)
        private set

    var errorPasswordMessage by  mutableStateOf("")
        private set

   private val _uIStateFlow= MutableStateFlow(UiState())
    val uIStatFlow=_uIStateFlow.asStateFlow()


   private val _uIEventFlow = MutableSharedFlow<UiEvent>()
    val uiEventFlow =_uIEventFlow.asSharedFlow()

    private val userEmail:String? =stateHandle[LoginWithPassword.userEmailArg]

    fun updateUserPassword(newValue:String){
        userPassword=newValue
    }

    fun updateIsErrorPassword(){
        isErrorPassword=!isErrorPassword
    }

    fun updateErrorPasswordMessage(newValue:String){
        errorPasswordMessage=newValue
    }

    fun togglePasswordVisibility(){
        hideUserPassword=!hideUserPassword
    }

    fun logUserIn() {

        /*navigator.navigatePopToInclusive(
            //toRoute = "${CreatePassword.route}?createPassword=true",

            toRoute = CreateCode.route,
            popToRoute = LoginWithPassword.route
        )*/

        viewModelScope.launch {
            val loginRequest=LoginWithPassWordRequest(email =requireNotNull(userEmail), password = userPassword )
            loginWithPasswordUseCase(loginRequest).collect{networkResult->
                when(networkResult){
                    is NetworkResult.Loading-> _uIStateFlow.value=_uIStateFlow.value.copy(loading = true)
                    is NetworkResult.Success->{
                        _uIEventFlow.emit(UiEvent.ShowSnackBar(message = networkResult.data.message))
                        _uIStateFlow.value=_uIStateFlow.value.copy(loading = false,authenticated = true)

                    }
                    is NetworkResult.Error->{

                        val authError=if (networkResult.code==200) networkResult.message.toString() else "Unexpected error occurred"

                        _uIStateFlow.value=_uIStateFlow.value.copy(loading = false, authenticationError =authError )

                    }
                    is NetworkResult.Exception->{
                        _uIStateFlow.value=_uIStateFlow.value.copy(loading = false)
                        _uIEventFlow.emit(UiEvent.ShowSnackBar(message = networkResult.genericMessage))
                    }
                    else -> {}
                }
            }
        }

    }

    fun onForgetPasswordClicked() {

        navigator.navigatePopTo(
            //toRoute = "${CreatePassword.route}?createPassword=true",

            toRoute = ResetPassword.route,
            popToRoute = LoginWithPassword.route
        )
    }

}


sealed class UiEvent{
    data class ShowSnackBar(val message:String) : UiEvent()
}