package co.youverify.youhr.presentation.ui.settings.profile

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.youverify.youhr.core.util.PROFILE_PIC_UPDATE_SUCCESS_MESSAGE
import co.youverify.youhr.core.util.PROFILE_UPDATE_SUCCESS_MESSAGE
import co.youverify.youhr.core.util.Result
import co.youverify.youhr.data.model.UpdateUserProfileRequest
import co.youverify.youhr.domain.model.User
import co.youverify.youhr.domain.use_case.UpdateUserProfileUseCase
import co.youverify.youhr.presentation.ui.Navigator
import co.youverify.youhr.presentation.ui.UiEvent
import co.youverify.youhr.presentation.ui.home.HomeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val navigator: Navigator,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
):ViewModel() {


    var profileFieldValues by mutableStateOf(ProfileFieldsValue())
        private set

    private var profileUpdateSuccessful: Boolean = false
    private var profilePicUpdateSuccessful:Boolean = false
    var currentUser: User? by mutableStateOf(null)
    private set

    private val _uiStateFlow = MutableStateFlow(ProfileScreenUiState())
    val uiStateFlow = _uiStateFlow.asStateFlow()

    private val _uiEventFlow = MutableSharedFlow<UiEvent>()
    val uiEventFlow  = _uiEventFlow.asSharedFlow()

    fun updateProfile(imageUri: Uri?, homeViewModel: HomeViewModel,profileViewModel: ProfileViewModel, context: Context) {

        viewModelScope.launch {

            if (currentUser?.nextOfKin.isNullOrEmpty()||currentUser?.nextOfKinContact.isNullOrEmpty()||
                currentUser?.address.isNullOrEmpty()||currentUser?.nextOfKinNumber.isNullOrEmpty()||
                currentUser?.phoneNumber.isNullOrEmpty()){
                _uiEventFlow.emit(UiEvent.ShowToast("Empty fields are not allowed!!"))
            }else{
                //create update profile request
                _uiStateFlow.value=_uiStateFlow.value.copy(loading = true)


                val updateProfileRequest = UpdateUserProfileRequest(
                    nextofKin = currentUser?.nextOfKin!!,
                    nextofKinContact = currentUser?.nextOfKinContact!!,
                    address = currentUser?.address!!,
                    nextofKinNumber = currentUser?.nextOfKinNumber!!,
                    phoneNumber = currentUser?.phoneNumber!!
                )

                val file:File

                //if the user did not change the profile picture
                if (imageUri==null){
                     file= withContext(Dispatchers.IO) {
                         File.createTempFile("temp_pic", ".jpeg", context.externalCacheDir)
                     }
                    context.openFileInput("profile_pic").use {input->
                        file.outputStream().use {output->
                            input.copyTo(output)
                        }
                    }

                }else{ file= getFileFromUri(context,imageUri,"temp_pic.jpeg")!! }

                val a=file.extension

                val requestFile=file.asRequestBody("image/jpeg".toMediaType())
                val imageFile=MultipartBody.Part.createFormData("displayPicture",file.name,requestFile)

                updateUserProfileUseCase.invoke(updateProfileRequest,imageFile).collect{updateProfileResult->
                    when(updateProfileResult){
                        is Result.Success ->{
                            if (updateProfileResult.data.message == PROFILE_UPDATE_SUCCESS_MESSAGE){
                                profileUpdateSuccessful = true

                            }
                            if (updateProfileResult.data.message == PROFILE_PIC_UPDATE_SUCCESS_MESSAGE){
                               profilePicUpdateSuccessful = true

                            }

                            if (profileUpdateSuccessful && profilePicUpdateSuccessful){

                                homeViewModel.updateUserProfile(userData=updateProfileResult.data.data,context=context, profileViewModel = profileViewModel)
                                   // _uiStateFlow.value=_uiStateFlow.value.copy(loading = false,profileUpdateSuccessful=true)
                                //_uiStateFlow.value=_uiStateFlow.value.copy(loading = false)
                                //_uiEventFlow.emit(UiEvent.ShowToast("An error occurred..check your internet connection and try again"))

                            }


                        }
                        is Result.Error ->{
                            _uiStateFlow.value=_uiStateFlow.value.copy(loading = false)
                            _uiEventFlow.emit(UiEvent.ShowToast(updateProfileResult.message.toString()))
                            cancel()
                        }

                        is Result.Exception ->{
                            _uiStateFlow.value=_uiStateFlow.value.copy(loading = false)
                            _uiEventFlow.emit(UiEvent.ShowToast("Connection error..check your internet connection and try again"))
                            cancel()
                        }
                    }
                }

            }
        }



    }
    fun updateCurrentUser(user: User?) {
        currentUser=user
    }

    fun updateProfileField(fieldType: EditableFieldType, newValue: String) {
        when(fieldType){
            EditableFieldType.NEXTOFKIN->{ currentUser= currentUser?.copy(nextOfKin = newValue) }
            EditableFieldType.PHONE->{currentUser= currentUser?.copy(phoneNumber = newValue)}
            EditableFieldType.NEXTOFKINADDRESS->{currentUser= currentUser?.copy(nextOfKinContact = newValue)}
            EditableFieldType.NEXTOFKINPHONE->{currentUser= currentUser?.copy(nextOfKinNumber = newValue)}
            EditableFieldType.ADDRESS->{currentUser= currentUser?.copy(address = newValue)}
            EditableFieldType.NONE->{}
        }
    }

    fun updateDisplayImage(bitMap: Bitmap) {
        currentUser=currentUser?.copy(displayPictureBitmap = bitMap)
    }

    fun hideSuccessDialog() {
       _uiStateFlow.value= _uiStateFlow.value.copy(profileUpdateSuccessful=false)
    }

    @SuppressLint("Recycle")
    private fun getFileFromUri(context: Context, imageUri: Uri, fileName: String): File? {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val file = inputStream?.let {
            val file = File(context.externalCacheDir, fileName)
            FileOutputStream(file).use { outputStream ->
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read: Int
                while (inputStream.read(buffer).also { read = it } != -1) {
                    outputStream.write(buffer, 0, read)
                }
                outputStream.flush()
            }
            file
        }


        return file
    }

     fun setProfileUpdateSuccess(updateSuccessful: Boolean) {
        if (updateSuccessful){
            _uiStateFlow.value=_uiStateFlow.value.copy(loading = false,profileUpdateSuccessful=true)
        }
        else{
          viewModelScope.launch {
              _uiStateFlow.value=_uiStateFlow.value.copy(loading = false)
              _uiEventFlow.emit(UiEvent.ShowToast("An error occurred..check your internet connection and try again"))
          }
        }

    }

    fun onBackArrowClicked() {
        navigator.navigateBack()
    }
}

data class ProfileScreenUiState(
    val loading:Boolean = false,
    val profileUpdateSuccessful:Boolean =false

)