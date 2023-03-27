package co.youverify.youhr.presentation.ui.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import co.youverify.youhr.presentation.*
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

    fun onSkipClicked() {
        navigator.navigatePopToInclusive(toRoute = BottomNavGraph.route, popToRoute = LoginGraph.route)
    }

    fun createCode() {
        navigator.navigatePopToInclusive(toRoute = CodeCreationSuccess.route, popToRoute = CreateCode.route)
    }

    fun onLoginRedirectClicked(){

        navigator.navigatePopToInclusive(toRoute = LoginWithCode.route, popToRoute = CodeCreationSuccess.route)

    }
}






