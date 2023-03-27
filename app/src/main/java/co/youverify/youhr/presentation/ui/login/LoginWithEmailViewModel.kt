package co.youverify.youhr.presentation.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import co.youverify.youhr.presentation.CreatePassword
import co.youverify.youhr.presentation.LoginWithCode
import co.youverify.youhr.presentation.LoginWithEmail
import co.youverify.youhr.presentation.LoginWithPassword
import co.youverify.youhr.presentation.ui.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginWithEmailViewModel @Inject constructor( private val navigator: Navigator):ViewModel(){


    var userEmail by  mutableStateOf("")
    private set


    fun updateUserEmail(newValue:String){
        userEmail=newValue
    }

    fun onNextButtonClicked() {

        navigator.navigate(toRoute = LoginWithPassword.route)
    }

    fun onLoginWithCodeButtonClicked() {
        navigator.navigatePopTo(toRoute = LoginWithCode.route, popToRoute = LoginWithEmail.route)
    }
}