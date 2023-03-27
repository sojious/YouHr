package co.youverify.youhr.presentation.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import co.youverify.youhr.presentation.*
import co.youverify.youhr.presentation.ui.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginWithPassWordViewModel @Inject constructor( private val navigator: Navigator) : ViewModel(){


    var userPassword by  mutableStateOf("")
    private set

    var hideUserPassword by  mutableStateOf(true)
    private set

    var isErrorPassword by  mutableStateOf(false)
        private set

    fun updateUserPassword(newValue:String){
        userPassword=newValue
    }

    fun updateIsErrorPassword(newValue:String){
        isErrorPassword=!isErrorPassword
    }

    fun togglePasswordVisibility(){
        hideUserPassword=!hideUserPassword
    }

    fun logUserIn() {

        navigator.navigatePopToInclusive(
            //toRoute = "${CreatePassword.route}?createPassword=true",

            toRoute = CreateCode.route,
            popToRoute = LoginWithPassword.route
        )
    }

    fun onForgetPasswordClicked() {

        navigator.navigatePopTo(
            //toRoute = "${CreatePassword.route}?createPassword=true",

            toRoute = ResetPassword.route,
            popToRoute = LoginWithPassword.route
        )
    }

}