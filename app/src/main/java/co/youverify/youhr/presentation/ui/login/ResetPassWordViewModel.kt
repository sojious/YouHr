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
class ResetPassWordViewModel @Inject constructor( private val navigator: Navigator) : ViewModel(){


    var userEmail by  mutableStateOf("")
        private set



    fun updateUserEmail(newValue:String){
        userEmail=newValue
    }



    fun resetPassword() {
        //navigateSingleTopPopToInclusive(navController, SignUpGraph.route, LoginWithCode.route)
        navigator.navigatePopToInclusive(toRoute = RecoveryEmailSent.route, popToRoute = ResetPassword.route)
    }

    fun onGotItClicked() {

        navigator.navigatePopToInclusive(toRoute = BottomNavGraph.route, popToRoute = RecoveryEmailSent.route)
    }


}