package co.youverify.youhr.presentation.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.youverify.youhr.domain.repository.PreferencesRepository
import co.youverify.youhr.presentation.LoginWithCode

import co.youverify.youhr.presentation.LoginWithPassword
import co.youverify.youhr.presentation.ui.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InputEmailViewModel @Inject constructor(
    private val navigator: Navigator,
    private val preferencesRepository: PreferencesRepository
    ):ViewModel(){


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
        viewModelScope.launch {

            if (userEmail.isEmpty()) {
                errorMessage="Email cannot be empty!"
                isErrorValue=true
            }
            else{
                val userIsLoggedOut=preferencesRepository.getLogOutStatus().first()
                preferencesRepository.saveUserEmail(userEmail)

                if (userIsLoggedOut){
                    preferencesRepository.setUserPasscodeCreationStatus(passcodeCreated = true)
                    navigator.navigate(LoginWithCode.route)
                }  else {navigator.navigate(toRoute = LoginWithPassword.route)}

            }

        }

    }

}