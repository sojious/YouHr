package co.youverify.youhr.presentation.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.youverify.youhr.core.util.*
import co.youverify.youhr.data.model.LoginWithCodeRequest
import co.youverify.youhr.domain.repository.PreferencesRepository
import co.youverify.youhr.domain.use_case.LoginWithCodeUseCase
import co.youverify.youhr.presentation.*
import co.youverify.youhr.presentation.ui.Navigator
import co.youverify.youhr.presentation.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginWithCodeViewModel @Inject constructor(
    private val navigator: Navigator,
    val loginWithCodeUseCase: LoginWithCodeUseCase,
    val preferencesRepository: PreferencesRepository,
    ) : ViewModel(){




    //initialize codeinputField variables

    var activeCodeInputFieldIndex by mutableStateOf(1)
        private set
    var code1 by mutableStateOf("")
        private set
    var code2 by mutableStateOf("")
        private set
    var code3 by mutableStateOf("")
        private set
    var code4 by mutableStateOf("")
        private set
    var code5 by mutableStateOf("")
        private set
    var code6 by mutableStateOf("")
        private set
    var isErrorCode by mutableStateOf(false)
        private set


    private val _uIStateFlow= MutableStateFlow(UiState())
    val uIStatFlow=_uIStateFlow.asStateFlow()


    private val _uIEventFlow = Channel<UiEvent>()
    val uiEventFlow =_uIEventFlow.receiveAsFlow()





    fun updateCode(newValue:String, index:Int){

        if (isErrorCode) isErrorCode=false

        if(newValue.length==1 || newValue.isEmpty()){
            when(index){
                1->code1=newValue
                2->code2=newValue
                3->code3=newValue
                4->code4=newValue
                5->code5=newValue
                6->code6=newValue

            }
        }
    }


     fun onPasswordLoginOptionClicked() { navigator.navigate(toRoute = LoginWithPassword.route) }

    fun logUserIn() {

        _uIStateFlow.value=_uIStateFlow.value.copy(loading = true)

        viewModelScope.launch {
            var passcode= EMPTY_PASSCODE_VALUE

            try { passcode="$code1$code2$code3$code4$code5$code6".toInt() }
            catch (exception:NumberFormatException){
                exception.printStackTrace()
            }


            val loginRequest= LoginWithCodeRequest(passcode = passcode, email = "")

            loginWithCodeUseCase(loginRequest).collect{networkResult->
                when(networkResult){

                    is Result.Success->{
                        _uIStateFlow.value=_uIStateFlow.value.copy(loading = false,authenticated = true)
                        _uIEventFlow.send(UiEvent.ShowToast(message = networkResult.data.message))

                        //check if LoginWithEmail Screen is presently on the BackStack pop up to it
                        //if (destinationOnBackStack(LoginWithEmail.route))
                            navigator.navigatePopToInclusive(toRoute = BottomNavGraph.route, popToRoute = LoginWithCode.route)
                    }

                    is Result.Error->{
                        val authError:String = when(networkResult.code){
                            INPUT_ERROR_CODE->networkResult.message.toString()
                            RESOURCE_NOT_FOUND_ERROR_CODE-> "Invalid Email!"
                            BAD_REQUEST_ERROR_CODE->"Wrong Passcode!"
                            else-> "oop!,Something went wrong, try again"
                        }
                        _uIStateFlow.value=_uIStateFlow.value.copy(loading = false, authenticationError =authError )
                        isErrorCode=true
                    }

                    is Result.Exception->{
                        _uIStateFlow.value=_uIStateFlow.value.copy(loading = false)
                        isErrorCode=false
                        _uIEventFlow.send(UiEvent.ShowSnackBar(message = networkResult.genericMessage))
                    }
                }
            }
        }

    }
    fun updateActiveCodeInputFieldIndex(newActiveIndex: Int) {
        if (newActiveIndex in 1..6)
            activeCodeInputFieldIndex=newActiveIndex
    }

    fun onBackSpaceKeyPressed(codeInputFieldIndex: Int) {
        if(codeInputFieldIndex==2 && code2.isEmpty())
            activeCodeInputFieldIndex-=1

        if(codeInputFieldIndex==3 && code3.isEmpty())
            activeCodeInputFieldIndex-=1

        if(codeInputFieldIndex==4 && code4.isEmpty())
            activeCodeInputFieldIndex-=1

        if(codeInputFieldIndex==5 && code5.isEmpty())
            activeCodeInputFieldIndex-=1

        if(codeInputFieldIndex==6 && code6.isEmpty())
            activeCodeInputFieldIndex-=1

    }

}
