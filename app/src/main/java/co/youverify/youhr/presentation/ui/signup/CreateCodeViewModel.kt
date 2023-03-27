package co.youverify.youhr.presentation.ui.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import co.youverify.youhr.core.util.navigateSingleTopPopToInclusive
import co.youverify.youhr.presentation.CreateCode
import co.youverify.youhr.presentation.LoginWithCode
import co.youverify.youhr.presentation.LoginWithEmail
import co.youverify.youhr.presentation.ui.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreateCodeViewModel @Inject constructor(private val navigator: Navigator) :ViewModel(){

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

    //initialize variables to manage focus
    var moveFocus by mutableStateOf(false)
    var focusDirection=FocusDirection.FORWARD

    fun updateCode(newValue:String, index:Int){
        when(index){
            1->code1=newValue
            2->code2=newValue
            3->code3=newValue
            4->code4=newValue
            5->code5=newValue
            6->code6=newValue
        }
        if (newValue.length==1) {
            focusDirection=FocusDirection.FORWARD
            moveFocus=true
        }

        if (newValue.isEmpty()) {
            focusDirection=FocusDirection.BACKWARD
            moveFocus=true
        }
    }

    fun onSkipClicked() {
        navigator.navigatePopToInclusive(toRoute = LoginWithEmail.route, popToRoute = CreateCode.route)
    }

    fun createCode() {
        navigator.navigatePopToInclusive(toRoute = LoginWithCode.route, popToRoute = CreateCode.route)
    }
}






enum class FocusDirection{
    FORWARD,
    BACKWARD
}
