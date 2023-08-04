package co.youverify.youhr.presentation.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
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
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.youverify.youhr.R
import co.youverify.youhr.core.util.Result
import co.youverify.youhr.data.remote.TokenInterceptor
import co.youverify.youhr.domain.model.Task
import co.youverify.youhr.domain.repository.TaskRepository
import co.youverify.youhr.domain.use_case.ChangePasswordUseCase
import co.youverify.youhr.domain.use_case.CreateCodeUseCase
import co.youverify.youhr.domain.use_case.GetLeaveRequestsUseCase
import co.youverify.youhr.domain.use_case.GetTasksUseCase
import co.youverify.youhr.domain.use_case.LoginWithCodeUseCase
import co.youverify.youhr.domain.use_case.LoginWithPasswordUseCase
import co.youverify.youhr.presentation.ui.Navigator
import co.youverify.youhr.presentation.ui.components.LoadingDialog
import co.youverify.youhr.presentation.ui.components.YouHrTitleBar
import co.youverify.youhr.presentation.ui.leave.AuthRepoMock
import co.youverify.youhr.presentation.ui.leave.LeaveRepoMock
import co.youverify.youhr.presentation.ui.leave.PreferenceRepoMock
import co.youverify.youhr.presentation.ui.settings.profile.SuccessPopUpDialog
import co.youverify.youhr.presentation.ui.theme.YouHrTheme
import co.youverify.youhr.presentation.ui.theme.bodyTextLightColor
import co.youverify.youhr.presentation.ui.theme.deactivatedColorDeep
import co.youverify.youhr.presentation.ui.theme.errorMessageColor
import co.youverify.youhr.presentation.ui.theme.inputDeepTextColor
import co.youverify.youhr.presentation.ui.theme.primaryColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
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
        modifier= modifier.fillMaxSize()
    ) {

        LaunchedEffect(key1 = Unit){
            settingsViewModel.resetState()
            settingsViewModel.showAppropriateInputFields()
        }

        val fieldsState = remember{ChangePasscodeInputFieldsState()}
        val cts= rememberCoroutineScope()
        val titleBarTitle=if (uiState.showOldPasscodeField) "Change Passcode" else "Create Passcode"
        YouHrTitleBar(title = titleBarTitle, modifier = Modifier.padding(top=36.dp, bottom = 24.dp)){onBackArrowClicked()}
        Text(
            text = buildAnnotatedString {
                                        append("Your new passcode must be ")
                withStyle(SpanStyle(color = Color.Black)){
                    append("6 digits ")
                }
                append("long")
            },
            fontSize = 12.sp,
            color = bodyTextLightColor,modifier=Modifier.padding(start = 20.dp,end=20.dp)
        )

       Column(
           modifier= Modifier
                   .padding(top= 36.dp, start=20.dp,end=20.dp)
               .verticalScroll(rememberScrollState()),
           verticalArrangement = Arrangement.spacedBy(24.dp)
       ) {



           if (uiState.showOldPasscodeField){
               InputField2(
                   fieldTitle = "Old Passcode", fieldValue = fieldsState.oldPasscodeValue,
                   fieldPlaceHolder ="Enter old passcode" , isErrorValue =fieldsState.isOldPasscodeError ,
                   onFieldValueChanged = {fieldsState.updateOldPasscodeValue(it)}, errorMessage ="Wrong Passcode!",
                   //modifier=Modifier.padding(start = 20.dp, end = 20.dp, bottom = 24.dp),
               )
           }


           InputField2(
               fieldTitle = "New Passcode", fieldValue = fieldsState.newPasscodeValue,
               fieldPlaceHolder ="Enter new passcode" , isErrorValue =fieldsState.isNewPasscodeError ,
               onFieldValueChanged = {fieldsState.updateNewPasscodeValue(it)}, errorMessage ="Passcode does not meet requirement!",
               //modifier=Modifier.padding(start = 20.dp, end = 20.dp, bottom = 24.dp),
           )

           InputField2(
               fieldTitle = "Confirm Passcode", fieldValue = fieldsState.confirmPasscodeValue,
               fieldPlaceHolder ="Enter new password" , isErrorValue =fieldsState.isConfirmPasscodeError ,
               onFieldValueChanged = {fieldsState.updateConfirmPasscodeValue(it)}, errorMessage ="Passcodes do not match!",
               //modifier=Modifier.padding(start = 20.dp, end = 20.dp),
           )

           val btnEnabled=if (uiState.showOldPasscodeField){
               fieldsState.oldPasscodeValue.isNotEmpty() && fieldsState.newPasscodeValue.isNotEmpty() &&
                       fieldsState.confirmPasscodeValue.isNotEmpty() && !fieldsState.isNewPasscodeError && !fieldsState.isConfirmPasscodeError
           }else{
               fieldsState.newPasscodeValue.isNotEmpty() &&
                       fieldsState.confirmPasscodeValue.isNotEmpty() && !fieldsState.isNewPasscodeError && !fieldsState.isConfirmPasscodeError
           }

           Button(
               onClick = {cts.launch{settingsViewModel.changePasscode(fieldsState)}},
               shape= RoundedCornerShape(4.dp),
               modifier = modifier
                   .padding(top = 12.dp)
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
               enabled = btnEnabled
           )
       }


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
    onTrailingIconClicked: () -> Unit={},
    errorMessage: String
){

    var hideValue by remember{ mutableStateOf(true) }

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
            modifier= Modifier
                .fillMaxWidth()
                .requiredHeight(48.dp),
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
            visualTransformation = if(hideValue) PasswordVisualTransformation('*') else VisualTransformation.None,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                placeholderColor = deactivatedColorDeep,
                focusedBorderColor =  deactivatedColorDeep,
                unfocusedBorderColor = deactivatedColorDeep,
                textColor = inputDeepTextColor
            ),
            trailingIcon = {

                    Icon(
                        painter = if (hideValue) painterResource(id = R.drawable.hide) else painterResource(id = R.drawable.show),
                        contentDescription =null,
                        modifier= Modifier
                            .size(18.dp)
                            .clickable { hideValue = !hideValue }
                    )
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
                    LoginWithPasswordUseCase(AuthRepoMock(),PreferenceRepoMock()),
                    //LoginWithCodeUseCase(PreferenceRepoMock(),AuthRepoMock()),
                    preferencesRepository = PreferenceRepoMock(),
                    GetLeaveRequestsUseCase(LeaveRepoMock()),
                    GetTasksUseCase(TasKRepoMock()),
                    tokenInterceptor = TokenInterceptor(),
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
    var isNewPasscodeError by mutableStateOf(false)
        private set
    var isConfirmPasscodeError by mutableStateOf(false)
        private set

    fun updateOldPasscodeValue(newValue:String){
        if (isOldPasscodeError){isOldPasscodeError=false}
        if (newValue.length<=6){oldPasscodeValue=newValue}

    }
    fun updateNewPasscodeValue(newValue:String){

        if (newValue.length<=6){ newPasscodeValue=newValue}
        isNewPasscodeError=newValue.length<6
        if (newPasscodeValue.isEmpty()){isNewPasscodeError=false}

    }
    fun updateConfirmPasscodeValue(newValue:String){
        //if (isConfirmPasscodeError){isConfirmPasscodeError=false}
        if (newValue.length<=6){ confirmPasscodeValue=newValue}
        isConfirmPasscodeError=newPasscodeValue!=newValue
        if (confirmPasscodeValue.isEmpty()){isConfirmPasscodeError=false}

    }
    fun updateIsOldPasscodeError(newValue:Boolean){ isOldPasscodeError=newValue }

}
class TasKRepoMock:TaskRepository{
    override suspend fun getAssignedTaskFirstLoad(): Flow<Result<List<Task>>> {
        return flow{}
    }

    override suspend fun getAssignedTasksPaginated(page: Int): Flow<Result<List<Task>>> {
        return flow{}
    }

    override suspend fun refreshTasks(): Flow<Result<List<Task>>> {
        return flow{}
    }

    override suspend fun clearTasks() {

    }

}