package co.youverify.youhr.presentation.ui.leave

import android.icu.util.Calendar
import android.icu.util.TimeZone
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import co.youverify.youhr.R
import co.youverify.youhr.core.util.Result
import co.youverify.youhr.core.util.toFormattedDateString
import co.youverify.youhr.core.util.toHrFormattedDateString
import co.youverify.youhr.data.model.AuthResponse
import co.youverify.youhr.data.model.ChangePasswordRequest
import co.youverify.youhr.data.model.CreateCodeRequest
import co.youverify.youhr.data.model.GenericResponse
import co.youverify.youhr.data.model.LoginWithCodeRequest
import co.youverify.youhr.data.model.LoginWithPassWordRequest
import co.youverify.youhr.data.model.ResetPasswordRequest
import co.youverify.youhr.data.remote.TokenInterceptor
import co.youverify.youhr.domain.model.FilteredUser
import co.youverify.youhr.domain.repository.AuthRepository
import co.youverify.youhr.domain.repository.PreferencesRepository
import co.youverify.youhr.domain.use_case.CreateLeaveRequestUseCase
import co.youverify.youhr.domain.use_case.FilterAllLineManagerUseCase
import co.youverify.youhr.domain.use_case.FilterAllUserUseCase
import co.youverify.youhr.domain.use_case.GetLeaveRequestsUseCase
import co.youverify.youhr.domain.use_case.GetLeaveSummaryUseCase
import co.youverify.youhr.domain.use_case.GetUserProfileUseCase
import co.youverify.youhr.domain.use_case.LoginWithCodeUseCase
import co.youverify.youhr.domain.use_case.LoginWithPasswordUseCase
import co.youverify.youhr.presentation.ui.Navigator
import co.youverify.youhr.presentation.ui.components.LoadingDialog
import co.youverify.youhr.presentation.ui.components.SearchableExpandableDropDownMenu2
import co.youverify.youhr.presentation.ui.components.YouHrTitleBar
import co.youverify.youhr.presentation.ui.home.ProfileRepoMock
import co.youverify.youhr.presentation.ui.login.LoginWithCodeViewModel
import co.youverify.youhr.presentation.ui.login.LoginWithPassWordViewModel
import co.youverify.youhr.presentation.ui.login.SuccessDialog
import co.youverify.youhr.presentation.ui.theme.YouHrTheme
import co.youverify.youhr.presentation.ui.theme.bodyTextDeepColor
import co.youverify.youhr.presentation.ui.theme.bodyTextLightColor
import co.youverify.youhr.presentation.ui.theme.deactivatedColorDeep
import co.youverify.youhr.presentation.ui.theme.deactivatedColorLight
import co.youverify.youhr.presentation.ui.theme.errorMessageColor
import co.youverify.youhr.presentation.ui.theme.primaryColor
import co.youverify.youhr.presentation.ui.theme.textFieldTitle
import co.youverify.youhr.presentation.ui.theme.yvColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaveRequestScreen(
    modifier: Modifier = Modifier,
    //leaveApplicationFormState: LeaveApplicationFormState,
    //stepIndicatorBoxState:StepIndicatorBoxState,
    onSubmit: (LeaveApplicationFormState) -> Unit,
    onDialogCloseClicked: () -> Unit,
    showDialog: Boolean,
    creatingRequest:Boolean,
    leaveManagementViewModel: LeaveManagementViewModel,
    //confirmCodeViewModel: ConfirmCodeViewModel,
    loginWithCodeViewModel: LoginWithCodeViewModel,
    loginWithPassWordViewModel: LoginWithPassWordViewModel,
    onBackArrowClicked:()->Unit
){
    LaunchedEffect(key1 = Unit){
        leaveManagementViewModel.initializeUsers(
            loginWithCodeViewModel,loginWithPassWordViewModel
        )
    }


    val leaveApplicationFormState=remember{LeaveApplicationFormState()}
    val stepIndicatorBoxState=remember{StepIndicatorBoxState()}
    val datePickerState= rememberDatePickerState()
    //val leaveApplicationFormState=remember{LeaveApplicationFormState()}
    Box(modifier = modifier.fillMaxSize()){
        Column {
            YouHrTitleBar(title = "Request For Leave", modifier = Modifier.padding(top=45.dp, bottom = 36.dp)){onBackArrowClicked()}
            if(stepIndicatorBoxState.currentStep==2){Consent(modifier=Modifier)}
            StepIndicatorBox(modifier= Modifier
                .padding(bottom = 36.dp)
                .align(Alignment.CenterHorizontally),stepIndicatorBoxState)
            LeaveApplicationForm(
                formState=leaveApplicationFormState,
                onSubmit = onSubmit,
                stepIndicatorState=stepIndicatorBoxState,
                filteredUsers = leaveManagementViewModel.allUsers,
                filteredLineManagers = leaveManagementViewModel.allLineManagers
            )
        }

        if (leaveApplicationFormState.datePickerVisible)
            LeaveDatePicker(
                state = datePickerState,
                modifier=Modifier.align(Alignment.Center),
                onOkButtonClicked ={
                    leaveApplicationFormState.updateDateInputFieldValue(datePickerState.selectedDateMillis)
                    leaveApplicationFormState.updateDatePickerVisibility()
                                   } ,
                onCancelButtonClicked = {leaveApplicationFormState.updateDatePickerVisibility() },
                leaveType=leaveApplicationFormState.selectedLeaveType
            )

        if(showDialog){
            SuccessDialog(
                onButtonClicked = onDialogCloseClicked,
                title = "Successful!!",
                message = "Your leave application has been submitted succesfully",
                buttonText = "Close",
                modifier = Modifier,
            )
        }
        if (creatingRequest){
            LoadingDialog(message = "Creating new leave application..")
        }
    }

}

@Composable
fun Consent(modifier: Modifier) {
    Column(modifier.fillMaxWidth()) {
        Text(
            text = buildAnnotatedString {
                append("By clicking ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)){
                    append("Apply")
                }
                append(" your request will be sent to your Reliever, the HR and the Line manager for confirmation.")
            },
            color = bodyTextDeepColor,
            fontSize = 12.sp,
            textAlign=TextAlign.Start,
            modifier=modifier.padding(horizontal = 21.dp)
        )
        Divider(modifier= Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 32.dp), color = deactivatedColorLight, thickness = 1.dp)
    }
}

@Composable
fun LeaveApplicationForm(
    modifier: Modifier = Modifier,
    formState: LeaveApplicationFormState,
    onSubmit: (LeaveApplicationFormState) -> Unit,
    stepIndicatorState: StepIndicatorBoxState,
    filteredUsers: List<FilteredUser>,
    filteredLineManagers: List<FilteredUser>
) {


    if (stepIndicatorState.currentStep==1){
        Column(
            modifier
                .padding(horizontal = 20.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(32.dp)) {

            val buttonEnabled=formState.selectedLeaveType.id.isNotEmpty()&&formState.leaveStartDateMillis!=null&&formState.leaveEndDateMillis!=null
                    && formState.lineManager.isNotEmpty() &&formState.lineManagerEmail.isNotEmpty() &&formState.reliever.isNotEmpty() && formState.reason.isNotEmpty()
                    && formState.relieverEmail.isNotEmpty() && formState.alternativePhoneNumber.isNotEmpty()

            LeaveTypeDropDown(state=formState)
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier =Modifier.fillMaxWidth() ) {
                LeaveDateField(
                    title ="Start Date" , selectedDate =formState.leaveStartDateMillis?.toFormattedDateString(("dd/MM/yy"))?:"",
                    index = 1, onFieldClicked = {formState.onDateFieldClicked(1)}
                )

                LeaveDateField(
                    title ="End Date" , selectedDate =formState.leaveEndDateMillis?.toFormattedDateString(("dd/MM/yy"))?:"",
                    index = 2, onFieldClicked = {formState.onDateFieldClicked(2)}
                )
            }

            
            SearchableDropDown(
                onFilteredUserSelected = {
                    formState.updateLineManager("${it.firstName} ${it.lastName}")
                    formState.updateLineManagerEmail(it.email)
                                         },
                fieldTitle = "Line Manager",
                filteredUsers =filteredLineManagers,
                selectedOption = formState.lineManager
            )
            LeaveRequestTextField(
                fieldTitle = "Line Manager’s Email address", fieldValue = formState.lineManagerEmail,
                enableMultiline =false , type =InputFieldType.EMAIL,onFieldValueChanged = {formState.updateLineManagerEmail(it)}
            )
            /*LeaveRequestTextField(
                fieldTitle = "Reliever", fieldValue = formState.reliever,
                enableMultiline =false , type =InputFieldType.TEXT,onFieldValueChanged = {formState.updateReliever(it)}
            )*/

            SearchableDropDown(
                onFilteredUserSelected = {
                    formState.updateReliever("${it.firstName} ${it.lastName}")
                    formState.updateRelieverEmail(it.email)
                    formState.updateRelieverId(it.id)
                },
                fieldTitle = "Reliever",
                filteredUsers =filteredUsers,
                selectedOption = formState.reliever
            )

            LeaveRequestTextField(
                fieldTitle = "Reliever’s Email ", fieldValue = formState.relieverEmail,
                enableMultiline =false , type =InputFieldType.EMAIL,onFieldValueChanged = {formState.updateRelieverEmail(it)}
            )

            LeaveRequestTextField(
                fieldTitle = "Alternative Phone Number ", fieldValue = formState.alternativePhoneNumber,
                enableMultiline =false , type =InputFieldType.NUMBER,onFieldValueChanged = {formState.updateAlternativePhoneNumber(it)}
            )

            LeaveRequestTextField(
                fieldTitle = "Reason", fieldValue = formState.reason,
                enableMultiline =true , type =InputFieldType.TEXT,onFieldValueChanged = {
                    formState.updateReason(it)
                }
            )

            Button(
                onClick = {stepIndicatorState.updateCurrentStep(2) }, enabled =buttonEnabled,
                colors = ButtonDefaults.buttonColors(containerColor = primaryColor, disabledContainerColor = deactivatedColorDeep),
                content = {Text(text = "Proceed", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp), shape = RoundedCornerShape(4.dp)
            )
        }
    }



    if (stepIndicatorState.currentStep==2){
        Column(modifier = modifier
            .padding( start = 20.dp, end = 20.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())) {

            /*Divider(modifier= Modifier
                .padding(top = 12.dp, bottom = 32.dp)
                .fillMaxWidth(), color = deactivatedColorLight, thickness = 1.dp)*/
           // ContactPersonInfo(formState)
            LeaveInfo(formState)
            ConstraintLayout( modifier = Modifier
                .fillMaxWidth()
                .padding(top = 45.dp, bottom = 16.dp)) {

                //val buttonEnabled=formState.contactName.isNotEmpty()&&formState.contactEmail.isNotEmpty()&&formState.phoneNumber.isNotEmpty()
                val (previousBtn,proceedBtn)=createRefs()
                val guideline=createGuidelineFromAbsoluteLeft(0.5f)

                OutlinedButton(
                    onClick = {stepIndicatorState.updateCurrentStep(1)},
                    shape = RoundedCornerShape(4.dp),
                    border = BorderStroke(width = 1.dp, color = primaryColor),
                    colors =ButtonDefaults.outlinedButtonColors(containerColor = Color.White, contentColor = primaryColor),
                    content = {Text(text = "Previous",fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.constrainAs(previousBtn){
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        end.linkTo(guideline,8.dp)
                        width= Dimension.fillToConstraints
                    }
                )


                Button(onClick = {onSubmit(formState)},
                    //enabled = true,
                    shape = RoundedCornerShape(4.dp),
                    colors =ButtonDefaults.buttonColors(containerColor = primaryColor, contentColor = Color.White),
                    content = {Text(text = "Submit",fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.constrainAs(proceedBtn){
                        top.linkTo(parent.top)
                        start.linkTo(guideline,8.dp)
                        end.linkTo(parent.end)
                        width= Dimension.fillToConstraints
                    }
                )


            }

        }
    }



}

/*@Composable
fun ContactPersonInfo(formState: LeaveApplicationFormState,modifier: Modifier=Modifier) {
    Column(modifier = modifier.fillMaxWidth()){
        Text(text = "Contact Person Information", fontSize = 14.sp, fontWeight = FontWeight.Bold,modifier=Modifier.padding(bottom = 33.dp))
        Column(modifier=Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(32.dp)) {
            ApplicationSummaryItem(title = "Contact Name", value = formState.contactName)
            ApplicationSummaryItem(title = "Phone Number", value = formState.phoneNumber)
            ApplicationSummaryItem(title = "Email Address", value = formState.contactEmail)
        }
    }
}*/

@Composable
fun LeaveInfo(formState: LeaveApplicationFormState,modifier: Modifier=Modifier) {
    Column(modifier = modifier.fillMaxWidth()){
        Text(text = "Leave Information", fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Column(
            modifier= Modifier
                .fillMaxWidth()
                .padding(top = 33.dp),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            ApplicationSummaryItem(title = "Leave Type", value = formState.selectedLeaveType.id)
            ApplicationSummaryItem(title = "Start Date", value = formState.leaveStartDateMillis?.toHrFormattedDateString()?:"")
            ApplicationSummaryItem(title = "End Date", value = formState.leaveEndDateMillis?.toHrFormattedDateString()?:"")
            ApplicationSummaryItem(title = "Line Manager", value = formState.lineManager)
            ApplicationSummaryItem(title = "Line Manager's Email ", value = formState.lineManagerEmail.replaceFirstChar { it.uppercase() })
            ApplicationSummaryItem(title = "Reliever", value = formState.reliever)
            ApplicationSummaryItem(title = "Reliever's Email", value = formState.relieverEmail.replaceFirstChar { it.uppercase() })
            ApplicationSummaryItem(title = "Alternative Phone Number", value = formState.alternativePhoneNumber)
            ApplicationSummaryItem(title = "Reason", value = formState.reason)
        }
    }
}

@Composable
fun StepIndicatorBox(modifier: Modifier=Modifier,state: StepIndicatorBoxState){
    Row(
        modifier=modifier.height(67.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        //verticalAlignment = Alignment.CenterVertically
    ) {

        StepIndicator(
            active =state.step1Active , completed =state.step1Completed ,
            stepNumber =1 ,text = "Leave\nInformation"
        )

        Spacer(
            modifier = Modifier
                .size(52.dp, 1.dp)
                .align(Alignment.CenterVertically)
                .background(color = if (state.step1Completed) primaryColor else deactivatedColorDeep)
        )

        /*StepIndicator(
            active =state.step2Active , completed =state.step2Completed ,
            stepNumber =2 ,text = "Contact\nPerson "
        )

        Spacer(
            modifier = Modifier
                .size(52.dp, 1.dp)
                .align(Alignment.CenterVertically)
                .background(color = if (state.step2Completed) primaryColor else deactivatedColorDeep)

        )*/

        StepIndicator(active =state.step2Active , completed =state.step2Completed ,
            stepNumber =2, text="Review"
        )

    }
}

@Composable
fun StepIndicator(
    modifier: Modifier=Modifier,
    active:Boolean,
    completed:Boolean,
    stepNumber:Int,
    text:String,
){


    Column(
        verticalArrangement = Arrangement.spacedBy(9.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

       // val (circle,line,titleText)=createRefs()
        IndicatorBox(
            modifier=Modifier,
            active = active,
            completed = completed,
            number = stepNumber
        )

        Text(
            text = text,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium, color = if (active || completed) primaryColor else deactivatedColorDeep,
            textAlign = TextAlign.Center,
        )
        
       /* if (includeLine) Spacer(modifier = Modifier
            .size(52.dp, 1.dp)
            .background(color = if (completed) primaryColor else deactivatedColorDeep)
            .padding(top = 16.dp, start = 53.dp)
        )*/
        
        //Text(text = "Leave\ninformation", fontSize = 10.sp, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)

    }
}

@Composable
fun IndicatorBox(active:Boolean,completed:Boolean,number:Int,modifier:Modifier){
    if (!active && !completed){
        Box(
            modifier = modifier
                .size(26.dp)
                .background(color = deactivatedColorDeep, shape = CircleShape),
            contentAlignment = Alignment.Center,
            content = { Text(text = "$number", fontSize = 14.sp, color = Color.White ) }
        )
    }

    if ( active && !completed){
        Box(
            modifier = Modifier
                .size(26.dp)
                .background(color = primaryColor, shape = CircleShape),
            contentAlignment = Alignment.Center,
            content = { Text(text = "$number", fontSize = 14.sp, color = Color.White ) }
        )
    }

    if (active && completed){
        Box(
            modifier = Modifier
                .size(26.dp)
                .border(width = 1.5.dp, color = primaryColor, shape = CircleShape),
            contentAlignment = Alignment.Center,
            content = { Image(painter = painterResource(id = R.drawable.ic_check), contentDescription =null ) }
        )
    }

}
@Composable
fun LeaveRequestTextField(
    modifier: Modifier = Modifier,
    fieldTitle: String,
    fieldValue: String,
    onFieldValueChanged: (String) -> Unit,
    enableMultiline:Boolean,
    type:InputFieldType,

){

    val keyboardType=when(type){
        InputFieldType.TEXT->KeyboardType.Text
        InputFieldType.NUMBER->KeyboardType.Phone
        InputFieldType.EMAIL->KeyboardType.Email
    }

    val textModifier=if (enableMultiline) Modifier
        .fillMaxWidth()
        .height(70.dp) else Modifier
        .fillMaxWidth()
        .heightIn(47.dp)
    val borderColor=if (fieldValue.isEmpty()) deactivatedColorDeep else primaryColor
    Column(modifier= modifier.fillMaxWidth(),) {

        Row(modifier=Modifier.padding(bottom = 8.dp)) {
            Text(
                text = fieldTitle,
                fontSize = 12.sp,
                color = textFieldTitle,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "*",
                color = errorMessageColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 2.dp),
                fontSize = 12.sp
            )
        }

        OutlinedTextField(
            modifier= textModifier,
            value =fieldValue ,
            onValueChange =onFieldValueChanged,
            singleLine = !enableMultiline,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            textStyle = TextStyle(fontSize = 12.sp, color = bodyTextDeepColor),
            shape = RoundedCornerShape(8.dp) ,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = borderColor,
                focusedBorderColor = primaryColor,
            ),
        )


    }

}

@Composable
fun LeaveDateField(
    modifier: Modifier=Modifier,
    title:String,
    selectedDate:String,
    index:Int,
    onFieldClicked:(Int)->Unit
){

    //var dropDownExpanded by remember{ mutableStateOf(false) }
    val textColor=if (selectedDate.isEmpty()) deactivatedColorDeep else bodyTextDeepColor
    val borderColor=if (selectedDate.isEmpty()) deactivatedColorDeep else primaryColor
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        //modifier = modifier.fillMaxWidth()
    ) {
        Row {
            Text(
                text = title,
                fontSize = 12.sp,
                color = textFieldTitle,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(start = 8.dp)
            )
            Text(
                text = "*",
                color = errorMessageColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 2.dp),
                fontSize = 12.sp
            )
        }


        Row(
            modifier= Modifier
                .height(40.dp)
                .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(8.dp))
                .clickable {
                    onFieldClicked(index)
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(11.5.dp)
        ) {
            Image(painter = painterResource(id = R.drawable.ic_calendar), contentDescription =null,modifier=Modifier.padding(start = 14.5.dp) )
            Text(text =selectedDate.ifEmpty { "DD/MM/YY" }, fontSize = 12.sp, color = textColor,modifier=Modifier.padding(end = 45.dp))
        }
    }

}

@Composable
fun LeaveTypeDropDown(
    modifier: Modifier = Modifier,
    state: LeaveApplicationFormState
){

    val borderColor=if (state.selectedLeaveType.id.isEmpty()) deactivatedColorDeep else primaryColor
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row {
            Text(
                text ="Leave Type",
                fontSize = 12.sp,
                color = textFieldTitle,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "*",
                color = errorMessageColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(start = 2.dp),
                fontSize = 12.sp
            )
        }


        Row(
            modifier= Modifier
                .fillMaxWidth()
                .height(47.dp)
                .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(8.dp))
                .clickable { state.toggleExpandedState() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = state.selectedLeaveType.id, fontSize = 12.sp, color = bodyTextDeepColor,modifier=Modifier.padding(start = 12.dp))
            Spacer(modifier = Modifier.weight(1f))
            Image(painter = painterResource(id = R.drawable.ic_arrow_down), contentDescription =null,modifier=Modifier.padding(end=20.dp))
            DropdownMenu(
                expanded = state.leaveTypeDropdownExpanded,
                onDismissRequest = { state.toggleExpandedState()},
                modifier =Modifier.background(color= Color.White, shape = RoundedCornerShape(4.dp))

            ) {

                DropdownMenuItem(
                    text = { Text(text = LeaveType.ANNUAL.id, fontSize = 12.sp, color = primaryColor)},
                    onClick = { state.updateSelectedLeaveType(LeaveType.ANNUAL);state.toggleExpandedState() },
                )

                DropdownMenuItem(
                    text = { Text(text = LeaveType.CASUAL.id, fontSize = 12.sp, color = primaryColor)},
                    onClick = { state.updateSelectedLeaveType(LeaveType.CASUAL);state.toggleExpandedState() },
                )
                DropdownMenuItem(
                    text = { Text(text = LeaveType.COMPASSIONATE.id, fontSize = 12.sp, color = primaryColor)},
                    onClick = { state.updateSelectedLeaveType(LeaveType.COMPASSIONATE);state.toggleExpandedState() },
                )

                DropdownMenuItem(
                    text = { Text(text =LeaveType.PARENTAL.id , fontSize = 12.sp, color = primaryColor)},
                    onClick = { state.updateSelectedLeaveType(LeaveType.PARENTAL);state.toggleExpandedState() },

                )

                DropdownMenuItem(
                    text = { Text(text =LeaveType.SICK.id , fontSize = 12.sp, color = primaryColor)},
                    onClick = { state.updateSelectedLeaveType(LeaveType.SICK);state.toggleExpandedState() }
                )

            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaveDatePicker(
    modifier: Modifier = Modifier,
    state: DatePickerState,
    onOkButtonClicked: () -> Unit,
    onCancelButtonClicked: () -> Unit,
    leaveType: LeaveType,
){

        Column(
            modifier = modifier.background(color = Color.White, shape = RoundedCornerShape(12.dp)),
            content = {
                val calendar= Calendar.getInstance(TimeZone.getDefault())
                DatePicker(
                    state =state,
                    title = {},
                    showModeToggle = false,
                    colors = DatePickerDefaults.colors(
                        containerColor = Color.White,
                        selectedDayContentColor = Color.White,
                        selectedDayContainerColor = yvColor,
                        //weekdayContentColor = bodyTextDeepColor,

                        disabledDayContentColor = deactivatedColorDeep,
                        dayContentColor =Color(0xFF181E30)
                    ),
                    modifier = Modifier.height(460.dp),
                   dateValidator = {dateToValidateInMillis->
                       calendar.timeInMillis=dateToValidateInMillis
                       if (leaveType==LeaveType.ANNUAL || leaveType==LeaveType.PARENTAL){
                           //calendar.add(Calendar.DAY_OF_YEAR, 7)
                           val currentCalendar=Calendar.getInstance(TimeZone.getDefault())
                           currentCalendar.add(Calendar.DAY_OF_YEAR, 7)
                           dateToValidateInMillis>=currentCalendar.timeInMillis && !calendar.isWeekend
                       }else{
                           //calendar.timeInMillis=dateToValidateInMillis
                           dateToValidateInMillis >= Calendar.getInstance(TimeZone.getDefault()).timeInMillis && !calendar.isWeekend
                       }

                    },
                )


                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 23.dp, top = 9.dp, bottom = 23.dp),
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    content = {
                        Text(text = "Cancel", fontSize = 12.sp, fontWeight = FontWeight.Medium,color= primaryColor, modifier=Modifier.clickable { onCancelButtonClicked() })
                        Text(text = "Ok", fontSize = 12.sp, fontWeight = FontWeight.Medium,color= primaryColor, modifier=Modifier.clickable { onOkButtonClicked() })
                    }
                )
            }
        )
}
@Composable
fun ApplicationSummaryItem(modifier: Modifier=Modifier,title: String,value:String){
    ConstraintLayout(modifier.fillMaxWidth()) {
        val(titleText,valueText)=createRefs()
        val guideline=createGuidelineFromAbsoluteRight(0.5f)
        Text(
            text = "$title:", fontSize =12.sp,color= bodyTextLightColor,
            modifier=Modifier.constrainAs(titleText){
                start.linkTo(parent.start)
                top.linkTo(parent.top)
            }
        )

        Text(
            text = value, fontSize =14.sp,color= bodyTextDeepColor,fontWeight=FontWeight.Medium,
            modifier=Modifier.constrainAs(valueText){
                start.linkTo(guideline)
                top.linkTo(parent.top)
                end.linkTo(parent.end)
                width= Dimension.fillToConstraints
            }
        )
    }
}


@Composable
fun SearchableDropDown(
    modifier: Modifier = Modifier,
    onFilteredUserSelected: (FilteredUser) -> Unit,
    fieldTitle: String,
    filteredUsers: List<FilteredUser>,
    selectedOption: String,
    //formState: LeaveApplicationFormState,
) {


   // val selectedUser=if (isLineManagerDropDown) formState.lineManager else formState.reliever
    //val borderColor = if (selectedUser.isEmpty()) deactivatedColorDeep else primaryColor

    Column(
        //verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            content = {
                Text(
                    text = fieldTitle, fontSize = 12.sp,
                    color = textFieldTitle, fontWeight = FontWeight.Medium
                )
                Text(
                    text = "*", color = errorMessageColor, textAlign = TextAlign.Center,
                    modifier = Modifier.padding(start = 2.dp), fontSize = 12.sp
                )
            },
            modifier = Modifier.padding(bottom = 8.dp)
        )


       // val sports = mutableListOf("Basketball", "Rugby", "Football", "MMA", "Motorsport", "Snooker", "Tennis")

        SearchableExpandableDropDownMenu2(
            listOfItems = filteredUsers,
            modifier = Modifier.fillMaxWidth(),
            onDropDownItemSelected = { item -> // Returns the item selected in the dropdown
                onFilteredUserSelected(item)
            },

                    openedIcon =ImageVector.vectorResource(id = R.drawable.ic_arrow_up),
                    closedIcon =ImageVector.vectorResource(id = R.drawable.ic_arrow_down),
            selectedOption = selectedOption
                    //parentTextFieldCornerRadius = 8.dp,

        )


           /* Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(47.dp)
                    .border(width = 1.dp, color = borderColor, shape = RoundedCornerShape(8.dp))
                    .clickable { onDropDownClicked() },
                verticalAlignment = Alignment.CenterVertically,
                content = {
                    Text(
                        text = selectedUser,
                        fontSize = 12.sp,
                        color = bodyTextDeepColor,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = painterResource(id = R.drawable.ic_arrow_down),
                        contentDescription = null,
                        modifier = Modifier.padding(end = 20.dp)
                    )
                }
            )




            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { onDismissRequest() },
                modifier = Modifier
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .height(200.dp),
                content = {

                    OutlinedTextField(
                        value = textValue,
                        onValueChange = { onTextValueChanged(it) },
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .requiredHeight(37.dp)
                            .padding(top = 16.dp, start = 20.dp, end = 20.dp, bottom = 20.dp),
                        placeholder = { Text(text = "Search by name", color = Color.Red) },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_calendar_2),
                                contentDescription = null,
                                tint = Color.Yellow
                            )
                        },
                        textStyle = TextStyle(color = primaryColor)
                    )

                    filteredUsers.forEach { filteredUser ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = "${filteredUser.firstName} ${filteredUser.lastName}",
                                    fontSize = 12.sp,
                                    color = primaryColor
                                )
                            },
                            onClick = { onFilteredUserSelected("${filteredUser.firstName} ${filteredUser.lastName}") },
                            modifier=Modifier.padding(horizontal = 20.dp),
                        )
                    }
                }

            )*/



    }
}


    @Preview
    @Composable
    fun SearchableDropDownPreview() {
        YouHrTheme {
            Surface {
                var textVal by remember { mutableStateOf("") }
                var isExpanded by remember { mutableStateOf(false) }
                val formState=remember{LeaveApplicationFormState()}

                SearchableDropDown(

                    onFilteredUserSelected = {user-> isExpanded = false;formState.updateLineManager("${user.firstName} ${user.lastName}") },
                    fieldTitle = "Line Manager",
                    filteredUsers = listOf(
                        FilteredUser("Adewole", "Akin", "akin@gmail.com",id=""),
                        FilteredUser("Adesoji", "Olowa", "akin@gmail.com",id=""),
                        FilteredUser("Sharon", "Chigorom", "akin@gmail.com",id=""),
                        FilteredUser("Oluwayemisi", "Balogun", "akin@gmail.com",id=""),
                        FilteredUser("Timothy", "Akinyelu", "akin@gmail.com",id=""),
                        FilteredUser("Seth", "Samuel", "akin@gmail.com",id=""),
                    ),
                    selectedOption = formState.lineManager,
                )
            }
        }
    }

    @Preview
    @Composable
    fun LeaveRequestScreenPreview() {
        YouHrTheme {
            Surface {
                LeaveRequestScreen(
                    //leaveApplicationFormState = LeaveApplicationFormState(),
                    //stepIndicatorBoxState = StepIndicatorBoxState(),
                    onSubmit = {},
                    onDialogCloseClicked = {},
                    showDialog = false,
                    creatingRequest = false,
                    leaveManagementViewModel = LeaveManagementViewModel(
                        Navigator(),
                        GetLeaveRequestsUseCase(LeaveRepoMock()),
                        GetLeaveSummaryUseCase(LeaveRepoMock()),
                        CreateLeaveRequestUseCase(LeaveRepoMock())
                    ),
                    loginWithCodeViewModel = LoginWithCodeViewModel(
                        Navigator(),
                        LoginWithCodeUseCase(PreferenceRepoMock(), AuthRepoMock()),
                        PreferenceRepoMock(),
                        TokenInterceptor(),
                        FilterAllUserUseCase(ProfileRepoMock()),
                        FilterAllLineManagerUseCase(ProfileRepoMock()),
                        GetUserProfileUseCase(ProfileRepoMock())
                    ),
                    loginWithPassWordViewModel = LoginWithPassWordViewModel(
                            Navigator(),LoginWithPasswordUseCase(AuthRepoMock(),PreferenceRepoMock()),
                            PreferenceRepoMock(),TokenInterceptor(),FilterAllUserUseCase(ProfileRepoMock()),
                            FilterAllLineManagerUseCase(ProfileRepoMock()),                        GetUserProfileUseCase(ProfileRepoMock())

                    ),
                    onBackArrowClicked = {}
                    )


            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Preview
    @Composable
    fun LeaveDatePickerPreview() {
        YouHrTheme {
            Surface {
                //StepIndicator(active = true, completed = false, stepNumber = 2, includeLine = true)
                LeaveDatePicker(
                    state = rememberDatePickerState(),
                    onOkButtonClicked = {},
                    onCancelButtonClicked = {},
                    leaveType = LeaveType.PARENTAL,
                )
            }
        }
    }

    @Preview
    @Composable
    fun LeaveDateFieldPreview() {
        YouHrTheme {
            Surface {
                //StepIndicator(active = true, completed = false, stepNumber = 2, includeLine = true)
                LeaveDateField(
                    title = "Start Date",
                    selectedDate = "23/09/2023",
                    index = 1,
                    onFieldClicked = {})
            }
        }
    }

    @Preview
    @Composable
    fun LeaveDropDownPreview() {
        YouHrTheme {
            Surface {
                //StepIndicator(active = true, completed = false, stepNumber = 2, includeLine = true)
                LeaveTypeDropDown(state = LeaveApplicationFormState())
            }
        }
    }

    @Preview
    @Composable
    fun StepIndicatorPreview() {
        YouHrTheme {
            Surface {
                StepIndicator(
                    active = true, completed = false, stepNumber = 2,
                    text = "Leave\nInformation"
                )
            }
        }
    }

    @Preview
    @Composable
    fun LeaveRequestInputFieldPreview() {
        YouHrTheme {
            Surface {
                LeaveRequestTextField(
                    fieldTitle = "Line Manager",
                    fieldValue = "Timothy Akinyelu",
                    onFieldValueChanged = {},
                    enableMultiline = false,
                    type = InputFieldType.TEXT
                )
            }
        }
    }

    enum class InputFieldType {
        TEXT,
        NUMBER,
        EMAIL
    }

    enum class LeaveType(val id: String) {
        ANNUAL("Annual"),
        CASUAL("Casual"),
        COMPASSIONATE("Compassionate"),
        PARENTAL("Parental"),
        SICK("Sick"),
        STUDY("Study"),
        NONE("")
    }

    class StepIndicatorBoxState {

        var currentStep by mutableStateOf(1)
            private set
        var step1Active by mutableStateOf(true)
            private set
        var step2Active by mutableStateOf(false)
            private set


        var step1Completed by mutableStateOf(false)
            private set
        var step2Completed by mutableStateOf(false)
            private set



        fun updateCurrentStep(newStepIndex: Int) {
            when (newStepIndex) {
                1 -> {
                    currentStep = newStepIndex;step1Active = true;step1Completed =
                        false;step2Active = false;step2Completed = false
                }

                2 -> {
                    currentStep = newStepIndex; step1Active = true;step1Completed =
                        true;step2Active = true;step2Completed = false
                }

                else -> {}
            }
        }
    }

    class LeaveApplicationFormState {

        var selectedLeaveType by mutableStateOf(LeaveType.NONE)
            private set
        var lineManager by mutableStateOf("")
            private set
        var lineManagerEmail by mutableStateOf("")
            private set
        var reliever by mutableStateOf("")
            private set

        var relieverEmail by mutableStateOf("")
            private set

        var alternativePhoneNumber by mutableStateOf("")
            private set
        var reason by mutableStateOf("")
            private set

        var clickedDateFieldIndex by mutableStateOf(1)
            private set
        var leaveStartDateMillis: Long? by mutableStateOf(null)
            private set
        var leaveEndDateMillis: Long? by mutableStateOf(null)
            private set
        var leaveTypeDropdownExpanded by mutableStateOf(false)
            private set
        var datePickerVisible by mutableStateOf(false)
            private set

        var relieverId=""
            private set


        fun updateSelectedLeaveType(newValue: LeaveType) {
            selectedLeaveType = newValue
        }

        fun updateLineManager(newValue: String) {
            lineManager = newValue
        }

        fun updateLineManagerEmail(newValue: String) {
            lineManagerEmail = newValue
        }

        fun updateReliever(newValue: String) {
            reliever = newValue
        }

        fun updateRelieverId(newValue: String) {
            relieverId = newValue
        }

        fun updateReason(newValue: String) {
            if (newValue.length<=250){ reason = newValue }

            }


        fun updateRelieverEmail(newValue: String) {
            relieverEmail = newValue
        }

        fun updateAlternativePhoneNumber(newValue: String) {
            if (newValue.length<=11){ alternativePhoneNumber = newValue }
        }

        fun onDateFieldClicked(index: Int) {
            clickedDateFieldIndex = index;datePickerVisible = true
        }

        fun toggleExpandedState() {
            leaveTypeDropdownExpanded = !leaveTypeDropdownExpanded
        }

        fun updateDatePickerVisibility() {
            datePickerVisible = !datePickerVisible
        }

        fun updateDateInputFieldValue(newValue: Long?) {
            if (clickedDateFieldIndex == 1)
                leaveStartDateMillis = newValue
            else
                leaveEndDateMillis = newValue
        }
    }




    class AuthRepoMock : AuthRepository {
        override suspend fun loginWithPassword(loginWithPassWordRequest: LoginWithPassWordRequest): Flow<Result<AuthResponse>> {
            return flow {}
        }

        override suspend fun loginWithCode(loginWithCodeRequest: LoginWithCodeRequest): Flow<Result<AuthResponse>> {
            return flow {}
        }

        override suspend fun resetPassword(resetPasswordRequest: ResetPasswordRequest): Flow<Result<GenericResponse>> {
            return flow {}
        }

        override suspend fun createCode(createCodeRequest: CreateCodeRequest): Flow<Result<GenericResponse>> {
            return flow {}
        }

        override suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): Flow<Result<GenericResponse>> {
            return flow{}
        }

    }

    class PreferenceRepoMock : PreferencesRepository {
        override suspend fun getUserEmail(): Flow<String> {
            return flow {}
        }

        override suspend fun saveUserEmail(userEmail: String) {}

        override suspend fun getUserToken(): Flow<String> {
            return flow {}
        }

        override suspend fun saveUserToken(userToken: String) {}

        override suspend fun setFirstRun(isFirstRun: Boolean) {}

        override suspend fun getAppFirstRunStatus(): Flow<Boolean> { return flow {}}

        override suspend fun getUserPasscodeCreationStatus(): Flow<Boolean> { return flow {} }

        override suspend fun setUserPasscodeCreationStatus(passcodeCreated: Boolean) {}
        override suspend fun getLogOutStatus(): Flow<Boolean> {
            return flow{}
        }

        override suspend fun setLogOutStatus(loggedOut: Boolean) {
            TODO("Not yet implemented")
        }

        override suspend fun saveUserPassword(userPassword: String) {}

        override fun getUserPassword(): Flow<String> { return flow {} }

        override suspend fun saveUserPasscode(userPasscode: String) { }

        override fun getUserPasscode(): Flow<String> { return flow {} }


    }



