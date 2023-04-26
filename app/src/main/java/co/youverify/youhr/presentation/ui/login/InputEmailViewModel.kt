package co.youverify.youhr.presentation.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import co.youverify.youhr.presentation.LoginWithCode
import co.youverify.youhr.presentation.InputEmail
import co.youverify.youhr.presentation.LoginWithPassword
import co.youverify.youhr.presentation.ui.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InputEmailViewModel @Inject constructor(private val navigator: Navigator):ViewModel(){


    var userEmail by  mutableStateOf("")
    private set
    var errorMessage by  mutableStateOf("")
        private set
    var isErrorValue by  mutableStateOf(false)
        private set


    fun updateUserEmail(newValue:String){

        if (isErrorValue) isErrorValue=false
        userEmail=newValue
    }

    fun onNextButtonClicked() {

        if (userEmail.isEmpty()) {
            errorMessage="Email cannot be empty!"
            isErrorValue=true
        }
        else
            navigator.navigate(toRoute = LoginWithPassword.route)
    }


    fun navigateBack() =navigator.navigateBack()
}