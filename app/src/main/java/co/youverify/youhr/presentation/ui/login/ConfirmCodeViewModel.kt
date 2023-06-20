package co.youverify.youhr.presentation.ui.login


import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.youverify.youhr.core.util.EMPTY_PASSCODE_VALUE
import co.youverify.youhr.core.util.INPUT_ERROR_CODE
import co.youverify.youhr.core.util.Result
import co.youverify.youhr.data.model.CreateCodeRequest
import co.youverify.youhr.domain.repository.PreferencesRepository
import co.youverify.youhr.domain.use_case.CreateCodeUseCase
import co.youverify.youhr.domain.use_case.GetUserProfileUseCase
import co.youverify.youhr.presentation.*
import co.youverify.youhr.presentation.ui.Navigator
import co.youverify.youhr.presentation.ui.UiEvent
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmCodeViewModel @Inject constructor(
    private val navigator: Navigator,
    private val createCodeUseCase: CreateCodeUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val preferencesRepository: PreferencesRepository,
) :ViewModel(){

    var activeCodeInputFieldIndex by mutableStateOf(1)
        private set
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

    var isErrorCode by mutableStateOf(false)
        private set

    var showSuccessDialog by mutableStateOf(false)
        private set


    private val _uIStateFlow= MutableStateFlow(UiState())
    val uIStatFlow =_uIStateFlow.asStateFlow()


    private val _uIEventFlow = Channel<UiEvent>()
    val uiEventFlow =_uIEventFlow.receiveAsFlow()



    fun updateCode(newValue:String, index:Int){
       if (isErrorCode) isErrorCode = false

        if(newValue.length == 1 || newValue.isEmpty()){
            when(index){
                1->code1 = newValue
                2->code2 = newValue
                3->code3 = newValue
                4->code4 = newValue
                5->code5 = newValue
                6->code6 = newValue

            }
        }

    }



    fun createCode(createCodeViewModel: CreateCodeViewModel,context: Context) {
        //navigator.navigatePopToInclusive(toRoute = CodeCreationSuccess.route, popToRoute = CreateCode.route)

        viewModelScope.launch {

            _uIStateFlow.value = _uIStateFlow.value.copy(loading = true)

            //val sb=StringBuilder().append(code1,code2,code3,code4,code5,code6)
            //val passcode=sb.toString().toInt()

            var passcode1 = EMPTY_PASSCODE_VALUE
            var passcode2 = EMPTY_PASSCODE_VALUE

            try {
                passcode1 = "${createCodeViewModel.code1}${createCodeViewModel.code2}${createCodeViewModel.code3}${createCodeViewModel.code4}${createCodeViewModel.code5}${createCodeViewModel.code6}".toInt()
                passcode2 = "$code1$code2$code3$code4$code5$code6".toInt()
            }
            catch (exception:NumberFormatException){
                exception.printStackTrace()
            }


            val createCodeRequest= CreateCodeRequest(passcode = passcode2)

            createCodeUseCase.invoke(createCodeRequest, passcode1).collect{ networkResult->
                when(networkResult){

                   is Result.Success->{
                       // Download and save the user profile pic to the app internal storage folder
                       saveUserProfilePic(context)
                    }
                    is Result.Error->{

                        val authError = if (networkResult.code == INPUT_ERROR_CODE) networkResult.message.toString() else "An unexpected error occurred! try again! "
                        isErrorCode = true
                        _uIStateFlow.value = _uIStateFlow.value.copy(loading = false, authenticationError = authError )
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

    private suspend fun saveUserProfilePic(context: Context) {
        getUserProfileUseCase.invoke(isFirstLogin =true).collect{result->
            when(result){

                is Result.Success->{

                    Glide.with(context)
                        .asBitmap()
                        .load(result.data.displayPictureUrl)
                        .into(
                            object : CustomTarget<Bitmap>(){
                                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                    //val imageSize=resource.allocationByteCount
                                    context.openFileOutput("profile_pic",Context.MODE_PRIVATE).use {
                                        resource.compress(Bitmap.CompressFormat.JPEG,100,it)
                                    }

                                    viewModelScope.launch {
                                        preferencesRepository.setUserPasscodeCreationStatus(passcodeCreated = true)
                                        _uIStateFlow.value = _uIStateFlow.value.copy(loading = false,authenticated = true)
                                        showSuccessDialog = true
                                    }

                                }

                                override fun onLoadCleared(placeholder: Drawable?) {}

                                override fun onLoadFailed(errorDrawable: Drawable?) {
                                    _uIStateFlow.value = _uIStateFlow.value.copy(loading = false)
                                    viewModelScope.launch {
                                    _uIEventFlow.send(UiEvent.ShowToast(message = "No internet connection"))
                                    }
                                }
                            }
                        )

                    //preferencesRepository.setUserPasscodeCreationStatus(passcodeCreated = true)
                    //_uIStateFlow.value=_uIStateFlow.value.copy(loading = false,authenticated = true)
                    //showSuccessDialog=true
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

    fun onLoginRedirectClicked(){

        navigator.navigatePopToInclusive(toRoute = LoginWithCode.route, popToRoute = CodeCreationSuccess.route)

    }

    fun onProceedButtonClicked() {

        navigator.navigatePopToInclusive(
            toRoute = BottomNavGraph.route, popToRoute = CreateCode.route)

    }

    fun updateActiveCodeInputFieldIndex(newActiveIndex: Int) {
        activeCodeInputFieldIndex = newActiveIndex
    }

    fun onBackSpaceKeyPressed(codeInputFieldIndex: Int) {
        if(codeInputFieldIndex == 2 && code2.isEmpty())
            activeCodeInputFieldIndex -= 1

        if(codeInputFieldIndex == 3 && code3.isEmpty())
            activeCodeInputFieldIndex -= 1

        if(codeInputFieldIndex == 4 && code4.isEmpty())
            activeCodeInputFieldIndex -= 1

        if(codeInputFieldIndex == 5 && code5.isEmpty())
            activeCodeInputFieldIndex -= 1

        if(codeInputFieldIndex == 6 && code6.isEmpty())
            activeCodeInputFieldIndex -= 1

    }


}






