package co.youverify.youhr.presentation.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import co.youverify.youhr.core.util.EMPTY_PASSCODE_VALUE
import co.youverify.youhr.data.model.CreateCodeRequest
import co.youverify.youhr.presentation.*
import co.youverify.youhr.presentation.ui.Navigator
import co.youverify.youhr.presentation.ui.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class CreateCodeViewModel @Inject constructor(private val navigator: Navigator, ) :ViewModel(){

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



    private val _uIStateFlow= MutableStateFlow(UiState())
    val uIStatFlow=_uIStateFlow.asStateFlow()


    private val _uIEventFlow = Channel<UiEvent>()
    val uiEventFlow =_uIEventFlow.receiveAsFlow()



    fun updateCode(newValue:String, index:Int){

        if (isErrorCode)
            isErrorCode=false

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




    fun goToConfirmCodeScreen() {

        _uIStateFlow.value=_uIStateFlow.value.copy(loading = true)

        var passcode= EMPTY_PASSCODE_VALUE

        try { passcode="$code1$code2$code3$code4$code5$code6".toInt() }
        catch (exception:NumberFormatException){
            exception.printStackTrace()
        }

        val createCodeRequest= CreateCodeRequest(passcode = passcode)


        if (createCodeRequest.passcode== EMPTY_PASSCODE_VALUE){
            isErrorCode=true
            _uIStateFlow.value=_uIStateFlow.value.copy(loading = false, authenticationError ="Passcode  cannot be empty!" )

        }

        else if (createCodeRequest.passcode.toString().length!=6) {
            isErrorCode=true
            _uIStateFlow.value=_uIStateFlow.value.copy(loading = false, authenticationError ="Passcode  cannot be less than 6 digits!" )

        }


       else{
            _uIStateFlow.value=_uIStateFlow.value.copy(loading = false)
            navigator.navigate(toRoute = ConfirmCode.route)
       }
    }


}






