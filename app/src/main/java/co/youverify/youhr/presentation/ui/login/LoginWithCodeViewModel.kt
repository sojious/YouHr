package co.youverify.youhr.presentation.ui.login

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.youverify.youhr.core.util.*
import co.youverify.youhr.data.model.FilterUserDto
import co.youverify.youhr.data.model.LoginWithCodeRequest
import co.youverify.youhr.data.remote.TokenInterceptor
import co.youverify.youhr.domain.model.FilteredUser
import co.youverify.youhr.domain.model.User
import co.youverify.youhr.domain.repository.PreferencesRepository
import co.youverify.youhr.domain.use_case.FilterAllLineManagerUseCase
import co.youverify.youhr.domain.use_case.FilterAllUserUseCase
import co.youverify.youhr.domain.use_case.GetUserProfileUseCase
import co.youverify.youhr.domain.use_case.LoginWithCodeUseCase
import co.youverify.youhr.presentation.*
import co.youverify.youhr.presentation.ui.Navigator
import co.youverify.youhr.presentation.ui.UiEvent
import co.youverify.youhr.presentation.ui.home.HomeViewModel
import co.youverify.youhr.presentation.ui.settings.SettingsViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginWithCodeViewModel @Inject constructor(
    private val navigator: Navigator,
    val loginWithCodeUseCase: LoginWithCodeUseCase,
    val preferencesRepository: PreferencesRepository,
    private val tokenInterceptor: TokenInterceptor,
    private val filterAllUserUseCase: FilterAllUserUseCase,
    private val filterAllLineManagerUseCase: FilterAllLineManagerUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase
    ) : ViewModel(){




    //initialize codeinputField variables

    var allUsers: List<FilteredUser> = emptyList()
    private set

    var allLineManagers: List<FilteredUser> = emptyList()
        private set
    var activeCodeInputFieldIndex by mutableStateOf(1)
        private set
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
    var isErrorCode by mutableStateOf(false)
        private set


    private val _uIStateFlow = MutableStateFlow(UiState())
    val uIStatFlow=_uIStateFlow.asStateFlow()


    private val _uIEventFlow = Channel<UiEvent>()
    val uiEventFlow = _uIEventFlow.receiveAsFlow()





    fun updateCode(newValue:String, index:Int){

        if (isErrorCode) isErrorCode = false

        if(newValue.length == 1 || newValue.isEmpty()){
            when(index){
                1->code1 = newValue
                2->code2 = newValue
                3->code3 =newValue
                4->code4 = newValue
                5->code5 = newValue
                6->code6 = newValue

            }
        }
    }


     fun onPasswordLoginOptionClicked() { navigator.navigate(toRoute = LoginWithPassword.route) }

    fun logUserIn(homeViewModel: HomeViewModel,context: Context) {

        _uIStateFlow.value = _uIStateFlow.value.copy(loading = true)

        viewModelScope.launch {
            var passcode = EMPTY_PASSCODE_VALUE

            try { passcode = "$code1$code2$code3$code4$code5$code6".toInt() }
            catch (exception:NumberFormatException){
                exception.printStackTrace()
            }


            val loginRequest= LoginWithCodeRequest(passcode = passcode, email = "")

            loginWithCodeUseCase(loginRequest).collect{networkResult->
                when(networkResult){

                    is Result.Success->{

                        //settingsViewModel.setCurrentPasscode(passcode.toString())

                        val savedPasscode=preferencesRepository.getUserPasscode().first()
                        val userIsLoggedOut=preferencesRepository.getLogOutStatus().first()
                        if (savedPasscode.isEmpty()) preferencesRepository.saveUserPasscode(passcode.toString())



                        if(tokenInterceptor.getToken().isEmpty()){
                            tokenInterceptor.setToken(networkResult.data.data.token)
                        }
                        getAllUser()
                        getAllLineManager()


                        //check if LoginWithEmail Screen is presently on the BackStack pop up to it
                        //if (destinationOnBackStack(LoginWithEmail.route))
                        if (userIsLoggedOut){

                            preferencesRepository.setLogOutStatus(loggedOut = false)
                            preferencesRepository.saveUserToken(networkResult.data.data.token)
                            preferencesRepository.setUserPasscodeCreationStatus(passcodeCreated = true)
                            //clearCodeValues()
                            //_uIStateFlow.value = _uIStateFlow.value.copy(loading = false, authenticated = true)
                            //_uIEventFlow.send(UiEvent.ShowToast(message = networkResult.data.message))
                            //navigator.navigatePopToInclusive(toRoute = BottomNavGraph.route, popToRoute = InputEmail.route)
                            saveUserProfile(homeViewModel,context,BottomNavGraph.route,InputEmail.route,networkResult.data.message)

                        }else{
                            //clearCodeValues()
                            //_uIStateFlow.value = _uIStateFlow.value.copy(loading = false, authenticated = true)
                            //_uIEventFlow.send(UiEvent.ShowToast(message = networkResult.data.message))
                            //navigator.navigatePopToInclusive(toRoute = BottomNavGraph.route, popToRoute = LoginWithCode.route)
                            saveUserProfile(homeViewModel,context,BottomNavGraph.route,LoginWithCode.route,networkResult.data.message)

                        }

                    }

                    is Result.Error->{
                        val authError:String = when(networkResult.code){
                            INPUT_ERROR_CODE-> networkResult.message.toString()
                            RESOURCE_NOT_FOUND_ERROR_CODE-> "Invalid Email!"
                            BAD_REQUEST_ERROR_CODE-> "Wrong Passcode!"
                            else-> networkResult.message?:"oop!,Something went wrong, try again"
                        }
                        _uIStateFlow.value = _uIStateFlow.value.copy(loading = false, authenticationError =authError )
                        isErrorCode = true
                    }

                    is Result.Exception->{
                        _uIStateFlow.value = _uIStateFlow.value.copy(loading = false)
                        isErrorCode = false
                        _uIEventFlow.send(UiEvent.ShowSnackBar(message = networkResult.genericMessage))
                    }
                }
            }
        }

    }
    fun updateActiveCodeInputFieldIndex(newActiveIndex: Int) {
        if (newActiveIndex in 1..6)
            activeCodeInputFieldIndex=newActiveIndex
    }

    fun onBackSpaceKeyPressed(codeInputFieldIndex: Int) {
        if(codeInputFieldIndex == 2 && code2.isEmpty())
            activeCodeInputFieldIndex -= 1

        if(codeInputFieldIndex == 3 && code3.isEmpty())
            activeCodeInputFieldIndex -= 1

        if(codeInputFieldIndex == 4 && code4.isEmpty())
            activeCodeInputFieldIndex -= 1

        if(codeInputFieldIndex  ==5 && code5.isEmpty())
            activeCodeInputFieldIndex -= 1

        if(codeInputFieldIndex == 6 && code6.isEmpty())
            activeCodeInputFieldIndex -= 1

    }

    private suspend fun getAllUser() {
        filterAllUserUseCase.invoke().collect{result->
            when(result){

                is Result.Success->{
                    allUsers = result.data
                }
                is Result.Error->{

                    isErrorCode = false
                    _uIStateFlow.value =_uIStateFlow.value.copy(loading = false)
                    _uIEventFlow.send(UiEvent.ShowToast(message = "Unexpected error occurred,try again!"))

                }
                is Result.Exception->{
                    _uIStateFlow.value = _uIStateFlow.value.copy(loading = false)
                    isErrorCode = false
                    _uIEventFlow.send(UiEvent.ShowToast(message = "No internet connection"))
                }
            }
        }
    }

    private suspend fun getAllLineManager() {
        filterAllLineManagerUseCase.invoke().collect{result->
            when(result){

                is Result.Success->{
                    allLineManagers = result.data

                }
                is Result.Error->{

                    isErrorCode = false
                    _uIStateFlow.value =_uIStateFlow.value.copy(loading = false)
                    _uIEventFlow.send(UiEvent.ShowToast(message = "Unexpected error occurred,try again!"))

                }
                is Result.Exception->{
                    _uIStateFlow.value = _uIStateFlow.value.copy(loading = false)
                    isErrorCode = false
                    _uIEventFlow.send(UiEvent.ShowToast(message = "No internet connection"))
                }
            }
        }
    }

    private fun clearCodeValues(){
        code1 = ""
        code2 = ""
        code3 = ""
        code4 = ""
        code5 = ""
        code6 = ""
        activeCodeInputFieldIndex=1
    }

    private suspend fun saveUserProfile(
        homeViewModel: HomeViewModel,
        context: Context,
        navigateToRoute:String,
        popToRoute:String,
        loginSuccessMessage:String
    ) {
        val result= getUserProfileUseCase.invoke().first()
        //var fetchedUser:User?=null
        when(result){

            is Result.Success->{

                Glide.with(context)
                    .asBitmap()
                    .load(result.data.displayPictureUrl)
                    .override(600,200)
                    .into(
                        object : CustomTarget<Bitmap>(){
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                //val imageSize=resource.allocationByteCount
                                //context.openFileOutput("profile_pic", Context.MODE_PRIVATE).use {
                                // resource.compress(Bitmap.CompressFormat.JPEG,100,it)
                                //}
                                homeViewModel.setCurrentUser(result.data.copy(displayPictureBitmap = resource))

                                //fetchedUser=result.data
                                viewModelScope.launch {
                                    _uIStateFlow.value=_uIStateFlow.value.copy(loading = false,authenticated = true)
                                    _uIEventFlow.send(UiEvent.ShowToast(message = loginSuccessMessage))
                                    navigator.navigatePopToInclusive(toRoute = navigateToRoute, popToRoute = popToRoute)
                                    clearCodeValues()
                                }

                            }

                            override fun onLoadCleared(placeholder: Drawable?) {}

                            override fun onLoadFailed(errorDrawable: Drawable?) {
                                isErrorCode = false
                                _uIStateFlow.value = _uIStateFlow.value.copy(loading = false)
                                viewModelScope.launch {
                                    _uIEventFlow.send(UiEvent.ShowToast(message = "No internet connection"))
                                }
                            }
                        }
                    )

            }
            is Result.Error->{

                isErrorCode = false
                _uIStateFlow.value =_uIStateFlow.value.copy(loading = false)
                _uIEventFlow.send(UiEvent.ShowToast(message = "Unexpected error occurred,try again!"))

            }
            is Result.Exception->{
                _uIStateFlow.value = _uIStateFlow.value.copy(loading = false)
                isErrorCode = false
                _uIEventFlow.send(UiEvent.ShowToast(message = "No internet connection"))
            }


        }
        // return fetchedUser
    }
}
