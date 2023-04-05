package co.youverify.youhr.presentation.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.youverify.youhr.core.util.BAD_REQUEST_ERROR_CODE
import co.youverify.youhr.core.util.INPUT_ERROR_CODE
import co.youverify.youhr.core.util.NetworkResult
import co.youverify.youhr.core.util.RESOURCE_NOT_FOUND_ERROR_CODE
import co.youverify.youhr.data.model.ResetPasswordRequest
import co.youverify.youhr.domain.use_case.ResetPasswordUseCase
import co.youverify.youhr.presentation.*
import co.youverify.youhr.presentation.ui.Navigator
import co.youverify.youhr.presentation.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPassWordViewModel @Inject constructor(
    private val navigator: Navigator,
    private val resetPasswordUseCase: ResetPasswordUseCase
    ) : ViewModel(){



    var userEmail by  mutableStateOf("")
        private set

    var isErrorValue by  mutableStateOf(false)
        private set

    private val _uIStateFlow= MutableStateFlow(UiState())
    val uIStatFlow=_uIStateFlow.asStateFlow()


    private val _uIEventFlow = Channel<UiEvent>()
    val uiEventFlow =_uIEventFlow.receiveAsFlow()



    fun updateUserEmail(newValue:String){
        if (isErrorValue) isErrorValue=false
        userEmail=newValue
    }



    fun resetPassword() {



        viewModelScope.launch {

            _uIStateFlow.value=_uIStateFlow.value.copy(loading = true)

            val resetPasswordRequest= ResetPasswordRequest(email =userEmail)

            resetPasswordUseCase(resetPasswordRequest).collect{ networkResult->
                when(networkResult){

                    is NetworkResult.Success->{

                        _uIStateFlow.value=_uIStateFlow.value.copy(loading = false,authenticated = true)
                        _uIEventFlow.send(UiEvent.ShowToast(message = networkResult.data.message))


                        navigator.routeToCheckFor=InputEmail.route
                        navigator.checkForDestinationOnBackStack()
                        delay(100)

                        if (navigator.destinationIsOnBackStack)
                            navigator.navigatePopToInclusive(toRoute = RecoveryEmailSent.route, popToRoute = InputEmail.route)
                        else
                            navigator.navigatePopToInclusive(toRoute = RecoveryEmailSent.route, popToRoute = LoginWithCode.route)

                    }
                    is NetworkResult.Error->{

                        val authError:String = when(networkResult.code){
                            INPUT_ERROR_CODE->networkResult.message.toString()
                            RESOURCE_NOT_FOUND_ERROR_CODE -> "Invalid Email!"
                            else-> "oop!,Something went wrong, try again"
                        }
                        _uIStateFlow.value=_uIStateFlow.value.copy(loading = false, authenticationError =authError )
                        isErrorValue=true

                    }
                    is NetworkResult.Exception->{
                        _uIStateFlow.value=_uIStateFlow.value.copy(loading = false)
                        _uIEventFlow.send(UiEvent.ShowSnackBar(message = networkResult.genericMessage))
                        isErrorValue=false

                    }
                }
            }
        }
    }

     fun onGotItClicked() {

         navigator.userPasswordReset=true

         navigator.navigatePopToInclusive(toRoute = InputEmail.route, popToRoute = RecoveryEmailSent.route)
    }

    fun navigateBack() =navigator.navigateBack()


}