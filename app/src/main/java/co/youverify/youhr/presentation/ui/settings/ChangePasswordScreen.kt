package co.youverify.youhr.presentation.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.youverify.youhr.data.remote.TokenInterceptor
import co.youverify.youhr.domain.use_case.ChangePasswordUseCase
import co.youverify.youhr.domain.use_case.CreateCodeUseCase
import co.youverify.youhr.presentation.ui.Navigator
import co.youverify.youhr.presentation.ui.components.LoadingDialog
import co.youverify.youhr.presentation.ui.components.YouHrTitleBar
import co.youverify.youhr.presentation.ui.leave.AuthRepoMock
import co.youverify.youhr.presentation.ui.leave.PreferenceRepoMock
import co.youverify.youhr.presentation.ui.settings.profile.SuccessPopUpDialog
import co.youverify.youhr.presentation.ui.theme.YouHrTheme
import co.youverify.youhr.presentation.ui.theme.bodyTextLightColor
import co.youverify.youhr.presentation.ui.theme.deactivatedColorDeep
import co.youverify.youhr.presentation.ui.theme.errorMessageColor
import co.youverify.youhr.presentation.ui.theme.inputDeepTextColor
import co.youverify.youhr.presentation.ui.theme.primaryColor
import kotlinx.coroutines.launch

@Composable
fun ChangePasswordScreen(
    modifier: Modifier = Modifier,
    //fieldsState: ChangePasswordInputFieldsState,
    //onChangePasswordButtonClicked: () -> Unit,
    uiState: ChangePasswordScreenUiState,
    onHideDialogRequest: () -> Unit,
    onBackArrowClicked:()->Unit,
    settingsViewModel: SettingsViewModel
){
    Column(
        modifier= modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        LaunchedEffect(key1 = Unit){
            settingsViewModel.resetState()
        }

        val fieldsState = remember{ChangePasswordInputFieldsState()}
        val cts= rememberCoroutineScope()
        YouHrTitleBar(title = "Change Password", modifier = Modifier.padding(top=36.dp, bottom = 24.dp)){ onBackArrowClicked()}
        Text(
            text = "Your new password must contain at least 8 characters, an uppercase letter, a lower case letter and a special character",
            fontSize = 12.sp,
            color = bodyTextLightColor,modifier=Modifier.padding(start = 20.dp,end=20.dp,bottom=36.dp)
        )

        InputField(
            fieldTitle = "Old Password", fieldValue = fieldsState.oldPasswordValue,
            fieldPlaceHolder ="Enter old password" , isErrorValue =fieldsState.isOldPasswordError ,
            onFieldValueChanged = {fieldsState.updateOldPasswordValue(it)}, errorMessage ="Wrong Password!",
            modifier=Modifier.padding(start = 20.dp, end = 20.dp, bottom = 24.dp),
        )

        InputField(
            fieldTitle = "New Password", fieldValue = fieldsState.newPasswordValue,
            fieldPlaceHolder ="Enter new password" , isErrorValue =false ,
            onFieldValueChanged = {fieldsState.updateNewPasswordValue(it)}, errorMessage ="",
            modifier=Modifier.padding(start = 20.dp, end = 20.dp, bottom = 24.dp),
        )

        InputField(
            fieldTitle = "Confirm Password", fieldValue = fieldsState.confirmPasswordValue,
            fieldPlaceHolder ="Enter new password" , isErrorValue =fieldsState.isConfirmPasswordError ,
            onFieldValueChanged = {fieldsState.updateConfirmPasswordValue(it)}, errorMessage ="Passwords do not match!",
            modifier=Modifier.padding(start = 20.dp, end = 20.dp, bottom = 38.dp),
            )
        Button(
            onClick = {
                      cts.launch { settingsViewModel.changePassword(fieldsState) }
            },
            shape= RoundedCornerShape(4.dp),
            modifier = modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .height(42.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
            content = {
                Text(
                    text = "Change Password",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 18.2.sp
                )
            },
            enabled = fieldsState.oldPasswordValue.isNotEmpty() && fieldsState.newPasswordValue.isNotEmpty() && fieldsState.confirmPasswordValue.isNotEmpty()
        )

        if (uiState.loading){
            LoadingDialog()
        }
        if (uiState.success){
            SuccessPopUpDialog(onHideDialogRequest = onHideDialogRequest)
        }
    }
}

@Composable
fun InputField(
    modifier: Modifier = Modifier,
    fieldTitle: String,
    fieldValue: String,
    fieldPlaceHolder: String,
    isErrorValue: Boolean ,
    //hideValue: Boolean,
    //isPasswordField:Boolean=true,
    onFieldValueChanged: (String) -> Unit,
    //onTrailingIconClicked: () -> Unit={},
    errorMessage: String
){
    Column(
        modifier= modifier
            //.padding(horizontal = 16.dp)
            .fillMaxWidth(),
    ) {


            Text(
                text = fieldTitle, fontSize = 12.sp, color = Color(0xff200E32),
                fontWeight = FontWeight.Medium,modifier=Modifier.padding(bottom = 10.dp)
            )

        OutlinedTextField(
            modifier=Modifier.fillMaxWidth(),
            value =fieldValue ,
            onValueChange =onFieldValueChanged,
            singleLine = true,
            placeholder = {
                Text(
                    text = fieldPlaceHolder,
                    fontSize = 11.sp,
                    lineHeight = 13.sp,
                    color = deactivatedColorDeep,
                )
            },
            isError = isErrorValue,
           // visualTransformation = if(hideValue) PasswordVisualTransformation() else VisualTransformation.None,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                placeholderColor = deactivatedColorDeep,
                focusedBorderColor =  deactivatedColorDeep,
                unfocusedBorderColor = deactivatedColorDeep,
                textColor = inputDeepTextColor
            ),
            trailingIcon = {
                /*if (isPasswordField)
                    Icon(
                        painter = if (hideValue) painterResource(id = R.drawable.hide) else painterResource(id = R.drawable.show),
                        contentDescription =null,
                        modifier= Modifier
                            .size(18.dp)
                            .clickable(onClick = onTrailingIconClicked)
                    )*/
            },
            shape = RoundedCornerShape(8.dp)
        )

        if (isErrorValue)
            Text(
                text = errorMessage,
                color = errorMessageColor,
                modifier = Modifier.padding(top=8.dp),
                fontSize = 12.sp
            )
    }
}
@Preview
@Composable
fun ChangePasswordScreenPreview(){
    YouHrTheme {
        Surface {
            ChangePasswordScreen(
                //fieldsState = ChangePasswordInputFieldsState(),
                //onChangePasswordButtonClicked = {},
                onHideDialogRequest = {},
                uiState = ChangePasswordScreenUiState(),
                settingsViewModel = SettingsViewModel(
                    Navigator(), ChangePasswordUseCase(AuthRepoMock()),
                    CreateCodeUseCase(AuthRepoMock(),PreferenceRepoMock()),
                    preferencesRepository = PreferenceRepoMock(), tokenInterceptor = TokenInterceptor()
                ),
                onBackArrowClicked = {}
            )
        }
    }
}

class ChangePasswordInputFieldsState{
    var oldPasswordValue by mutableStateOf("")
    private set

    var newPasswordValue by mutableStateOf("")
        private set

    var confirmPasswordValue by mutableStateOf("")
        private set

    var isOldPasswordError by mutableStateOf(false)
        private set
    var isConfirmPasswordError by mutableStateOf(false)
        private set

    fun updateOldPasswordValue(newValue:String){
        if (isOldPasswordError){isOldPasswordError=false}
        oldPasswordValue=newValue
    }
    fun updateNewPasswordValue(newValue:String){ newPasswordValue=newValue }
    fun updateConfirmPasswordValue(newValue:String){
        if (isConfirmPasswordError){isConfirmPasswordError=false}
        confirmPasswordValue=newValue
    }
    fun updateIsOldPasswordError(newValue:Boolean){ isOldPasswordError=newValue }
    fun updateIsConfirmPasswordError(newValue:Boolean){ isConfirmPasswordError=newValue }
    fun clearFields() {
        oldPasswordValue=""
        newPasswordValue=""
        confirmPasswordValue=""
        isOldPasswordError=false
        isConfirmPasswordError=false
    }
}