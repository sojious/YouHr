package co.youverify.youhr.presentation.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import co.youverify.youhr.presentation.*
import co.youverify.youhr.presentation.ui.Navigator
import co.youverify.youhr.presentation.ui.components.CodeInputFieldState
import co.youverify.youhr.presentation.ui.signup.FocusDirection
import co.youverify.youhr.presentation.ui.theme.codeInputUnfocused
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginWithCodeViewModel @Inject constructor( private val navigator: Navigator) : ViewModel(){


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
    var focusDirection= FocusDirection.FORWARD

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
            focusDirection= FocusDirection.FORWARD
            moveFocus=true
        }

        if (newValue.isEmpty()) {
            focusDirection= FocusDirection.BACKWARD
            moveFocus=true
        }
    }



    fun onSignUpOptionClicked() {
        //navigateSingleTopPopToInclusive(navController, SignUpGraph.route, LoginWithCode.route)
        navigator.navigatePopToInclusive(toRoute = CreatePassword.route, popToRoute = LoginWithCode.route)
    }

    fun onPasswordLoginOptionClicked() {
        //navigateSingleTopPopToInclusive(navController, LoginGraph.route,LoginWithCode.route)
        navigator.navigatePopToInclusive(toRoute = LoginWithEmail.route, popToRoute = LoginWithCode.route)
    }

    fun logUserIn() {

    }
}
