package co.youverify.youhr.presentation.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
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
fun ChangePasscodeScreen(
    modifier: Modifier = Modifier,
    //fieldsState: ChangePasscodeInputFieldsState,
    //onChangePasscodeButtonClicked: () -> Unit,
    uiState: ChangePasscodeScreenUiState,
    onBackArrowClicked:()->Unit,
    onHideDialogRequest: () -> Unit,
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

        val fieldsState = remember{ChangePasscodeInputFieldsState()}
        val cts= rememberCoroutineScope()
        YouHrTitleBar(title = "Change Passcode", modifier = Modifier.padding(top=36.dp, bottom = 24.dp)){onBackArrowClicked()}
        Text(
            text = "Your new passcode must be 6 digits long",
            fontSize = 12.sp,
            color = bodyTextLightColor,modifier=Modifier.padding(start = 20.dp,end=20.dp,bottom=36.dp)
        )

        InputField2(
            fieldTitle = "Old Passcode", fieldValue = fieldsState.oldPasscodeValue,
            fieldPlaceHolder ="Enter old passcode" , isErrorValue =fieldsState.isOldPasscodeError ,
            onFieldValueChanged = {fieldsState.updateOldPasscodeValue(it)}, errorMessage ="Wrong Passcode!",
            modifier=Modifier.padding(start = 20.dp, end = 20.dp, bottom = 24.dp),
        )

        InputField2(
            fieldTitle = "New Passcode", fieldValue = fieldsState.newPasscodeValue,
            fieldPlaceHolder ="Enter new passcode" , isErrorValue =false ,
            onFieldValueChanged = {fieldsState.updateNewPasscodeValue(it)}, errorMessage ="",
            modifier=Modifier.padding(start = 20.dp, end = 20.dp, bottom = 24.dp),
        )

        InputField2(
            fieldTitle = "Confirm Passcode", fieldValue = fieldsState.confirmPasscodeValue,
            fieldPlaceHolder ="Enter new password" , isErrorValue =fieldsState.isConfirmPasscodeError ,
            onFieldValueChanged = {fieldsState.updateConfirmPasscodeValue(it)}, errorMessage ="Passwords do not match!",
            modifier=Modifier.padding(start = 20.dp, end = 20.dp, bottom = 38.dp),
            )
        Button(
            onClick = {cts.launch{settingsViewModel.changePasscode(fieldsState)}},
            shape= RoundedCornerShape(4.dp),
            modifier = modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
                .height(42.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryColor),
            content = {
                Text(
                    text = "Change Passcode",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 18.2.sp
                )
            },
            enabled = fieldsState.oldPasscodeValue.isNotEmpty() && fieldsState.newPasscodeValue.isNotEmpty() && fieldsState.confirmPasscodeValue.isNotEmpty()
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
fun InputField2(
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
            shape = RoundedCornerShape(8.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword)
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
fun ChangePasscodeScreenPreview(){
    YouHrTheme {
        Surface {
            ChangePasscodeScreen(
                //fieldsState = ChangePasscodeInputFieldsState(),
                //onChangePasscodeButtonClicked = {},
                onHideDialogRequest = {},
                uiState = ChangePasscodeScreenUiState(),
                settingsViewModel =  SettingsViewModel(
                    Navigator(), ChangePasswordUseCase(AuthRepoMock()),
                    CreateCodeUseCase(AuthRepoMock(), PreferenceRepoMock()),
                    preferencesRepository = PreferenceRepoMock(), tokenInterceptor = TokenInterceptor()
                ),
                onBackArrowClicked = {}
            )
        }
    }
}

class ChangePasscodeInputFieldsState{
    var oldPasscodeValue by mutableStateOf("")
        private set

    var newPasscodeValue by mutableStateOf("")
        private set

    var confirmPasscodeValue by mutableStateOf("")
        private set

    var isOldPasscodeError by mutableStateOf(false)
        private set
    var isConfirmPasscodeError by mutableStateOf(false)
        private set

    fun updateOldPasscodeValue(newValue:String){
        if (isOldPasscodeError){isOldPasscodeError=false}
        oldPasscodeValue=newValue
    }
    fun updateNewPasscodeValue(newValue:String){ newPasscodeValue=newValue }
    fun updateConfirmPasscodeValue(newValue:String){
        if (isConfirmPasscodeError){isConfirmPasscodeError=false}
        confirmPasscodeValue=newValue
    }
    fun updateIsOldPasscodeError(newValue:Boolean){ isOldPasscodeError=newValue }
    fun updateIsConfirmPasscodeError(newValue:Boolean){ isConfirmPasscodeError=newValue }
    fun clearFields() {
        oldPasscodeValue=""
        newPasscodeValue=""
        confirmPasscodeValue=""
        isOldPasscodeError=false
        isConfirmPasscodeError=false
    }
}