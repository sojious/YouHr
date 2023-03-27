package co.youverify.youhr.presentation.ui.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import co.youverify.youhr.core.util.MINIMUM_PASSWORD_LENGTH
import co.youverify.youhr.presentation.CreateCode
import co.youverify.youhr.presentation.SignUpGraph
import co.youverify.youhr.presentation.ui.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CreatePasswordViewModel  @Inject constructor(private val navigator: Navigator , stateHandle: SavedStateHandle) :ViewModel() {


    //initialize the state variables
    var email by mutableStateOf("")
    private set
    var password by mutableStateOf("")
    private set
    var confirmPasswordValue by mutableStateOf("")
    private set
    var resetPassword by mutableStateOf(stateHandle["resetPassword"]?:false)
    private set
    var hidePassword by mutableStateOf(true)
    private set
    var passwordHasUpperCase by mutableStateOf(false)
    private set
    var passwordHasLowerCase by mutableStateOf(false)
    private set
    var passwordHasNumber by  mutableStateOf(false)
    private set
    var passwordHasSpecialCharacter by mutableStateOf(false)
    private set
    var passwordIsEightCharacters by mutableStateOf(false)
    private set



    fun updatePassword(newValue: String) {
        password= newValue
        passwordHasUpperCase = "[A-Z]+".toRegex().containsMatchIn(newValue)
        passwordHasLowerCase = "[a-z]+".toRegex().containsMatchIn(newValue)
        passwordHasNumber = ("[0-9]".toRegex().containsMatchIn(newValue))
        passwordHasSpecialCharacter = "[\\\\#{*}^@~`+&%?<>'\":_=;.\\[\\],/|()!-]".toRegex().containsMatchIn(newValue)
        passwordIsEightCharacters = newValue.length >= MINIMUM_PASSWORD_LENGTH

    }

    fun updateConfirmPasswordValue(newValue: String) {
        confirmPasswordValue=newValue
    }

    fun togglePasswordVisibility() {
        hidePassword=!hidePassword
    }

    fun createPassword() {
        navigator.navigatePopToInclusive(toRoute = CreateCode.route, popToRoute = SignUpGraph.route)
    }

    fun onTermsAndConditionsClicked() {

    }

}