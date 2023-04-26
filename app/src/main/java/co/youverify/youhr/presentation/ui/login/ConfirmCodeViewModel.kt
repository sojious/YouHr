package co.youverify.youhr.presentation.ui.login


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.youverify.youhr.core.util.EMPTY_PASSCODE_VALUE
import co.youverify.youhr.core.util.INPUT_ERROR_CODE
import co.youverify.youhr.core.util.NetworkResult
import co.youverify.youhr.data.model.CreateCodeRequest
import co.youverify.youhr.domain.repository.PreferencesRepository
import co.youverify.youhr.domain.use_case.CreateCodeUseCase
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
class ConfirmCodeViewModel @Inject constructor(
    private val navigator: Navigator,
    private val createCodeUseCase: CreateCodeUseCase,
    private val preferencesRepository: PreferencesRepository
) :ViewModel(){

    //initialize codeinputField variables
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

    var showSuccessDialog by mutableStateOf(false)
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



    fun createCode(createCodeViewModel: CreateCodeViewModel) {
        //navigator.navigatePopToInclusive(toRoute = CodeCreationSuccess.route, popToRoute = CreateCode.route)

        viewModelScope.launch {

            _uIStateFlow.value=_uIStateFlow.value.copy(loading = true)

            //val sb=StringBuilder().append(code1,code2,code3,code4,code5,code6)
            //val passcode=sb.toString().toInt()

            var passcode1= EMPTY_PASSCODE_VALUE
            var passcode2= EMPTY_PASSCODE_VALUE

            try {
                passcode1="${createCodeViewModel.code1}${createCodeViewModel.code2}${createCodeViewModel.code3}${createCodeViewModel.code4}${createCodeViewModel.code5}${createCodeViewModel.code6}".toInt()
                passcode2="$code1$code2$code3$code4$code5$code6".toInt()
            }
            catch (exception:NumberFormatException){
                exception.printStackTrace()
            }


            val createCodeRequest= CreateCodeRequest(passcode = passcode2)

            createCodeUseCase.invoke(createCodeRequest, passcode1).collect{ networkResult->
                when(networkResult){

                    is NetworkResult.Success->{

                        preferencesRepository.setUserPasscodeCreationStatus(passcodeCreated = true)

                        _uIStateFlow.value=_uIStateFlow.value.copy(loading = false,authenticated = true)
                        showSuccessDialog=true
                    }
                    is NetworkResult.Error->{

                        val authError=if (networkResult.code== INPUT_ERROR_CODE) networkResult.message.toString() else "An unexpected error occurred! try again! "
                        isErrorCode=true
                        _uIStateFlow.value=_uIStateFlow.value.copy(loading = false, authenticationError =authError )
                    }
                    is NetworkResult.Exception->{
                        _uIStateFlow.value=_uIStateFlow.value.copy(loading = false)
                        isErrorCode=false
                        _uIEventFlow.send(UiEvent.ShowSnackBar(message = networkResult.genericMessage))
                    }
                }
            }
        }

    }

    fun onLoginRedirectClicked(){

        navigator.navigatePopToInclusive(toRoute = LoginWithCode.route, popToRoute = CodeCreationSuccess.route)

    }

    fun onProceedButtonClicked() {

        navigator.navigatePopToInclusive(
            toRoute = BottomNavGraph.route, popToRoute = CreateCode.route)

    }



}






