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





    fun updateCode(newValue:String, index:Int){
        if(newValue.length==1 || newValue.isEmpty()){
            when(index){
                1->code1=newValue
                2->code2=newValue
                3->code3=newValue
                4->{code4=newValue}

            }
        }
    }



    fun onSignUpOptionClicked() {
        //navigateSingleTopPopToInclusive(navController, SignUpGraph.route, LoginWithCode.route)
        navigator.navigatePopToInclusive(toRoute = CreatePassword.route, popToRoute = LoginWithCode.route)
    }

    fun onPasswordLoginOptionClicked() {
        //navigateSingleTopPopToInclusive(navController, LoginGraph.route,LoginWithCode.route)
        navigator.navigatePopTo(toRoute = LoginWithEmail.route, popToRoute = LoginWithCode.route)
    }

    fun logUserIn() {

        navigator.navigatePopToInclusive(toRoute = BottomNavGraph.route, popToRoute = LoginWithCode.route)

    }
}
