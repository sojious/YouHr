package co.youverify.youhr.presentation.ui.signup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.youverify.youhr.R
import co.youverify.youhr.presentation.ui.components.*
import co.youverify.youhr.presentation.ui.theme.YouHrTheme
import co.youverify.youhr.presentation.ui.theme.textDim
import co.youverify.youhr.presentation.ui.theme.textLight
import co.youverify.youhr.presentation.ui.theme.yvColor

@Composable
fun CreatePasswordScreen(
    modifier: Modifier = Modifier,
    email: String,
    passwordValue: String,
    confirmPasswordValue: String,
    resetPassword:Boolean,
    lowerCaseCheckboxChecked: Boolean,
    upperCaseCheckboxChecked: Boolean,
    oneNumberCheckboxChecked: Boolean,
    eightCharactersCheckboxChecked: Boolean,
    specialCharacterCheckboxChecked: Boolean,
    hideTextFieldValue:Boolean=false,
    onPasswordValueChanged: (String) -> Unit,
    onConfirmPasswordValueChanged: (String) -> Unit,
    onPasswordVisibilityIconClicked: () -> Unit,
    onCreatePasswordButtonClicked: () -> Unit,
    onTermsAndConditionsClicked: () -> Unit

){

    Column(
       // horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .padding(start = 8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        
        TitleText(
            text = stringResource(id = if (resetPassword)  R.string.create_new_password else R.string.create_password),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp),
        )

        if (!resetPassword)
            MultiColoredText(
            fontSize = 12.sp,
            modifier=Modifier.padding(horizontal = 16.dp),
            colorPosition = 1,
            secondColor = textDim,
                neutralColor = textLight,
            stringResource(id = R.string.welcome),
            email,
            stringResource(id = R.string.get_Started)

        )
        
        TitledTextField(
            modifier=Modifier.padding(vertical = 25.dp),
            fieldTitle = if (resetPassword)  "Enter New Password"  else "Password",
            fieldValue = passwordValue,
            fieldPlaceHolder = stringResource(id = R.string.password_field_placeholder),
            hideValue =hideTextFieldValue ,
            onFieldValueChanged =onPasswordValueChanged ,
            onTrailingIconClicked = onPasswordVisibilityIconClicked,
            isPasswordConfirmationField = false
        )



        TitledTextField(
            fieldTitle = "Confirm Password",
            fieldValue = confirmPasswordValue,
            fieldPlaceHolder = stringResource(id = R.string.password_field_placeholder),
            hideValue =hideTextFieldValue ,
            onFieldValueChanged =onConfirmPasswordValueChanged ,
            onTrailingIconClicked = onPasswordVisibilityIconClicked,
            isPasswordConfirmationField = true
        )

        Text(
            text = stringResource(id = R.string.password_validation_instruction),
            modifier= Modifier
                .align(Alignment.Start)
                .padding(start = 16.dp, top = 30.dp, bottom = 10.dp),
            color = textLight,
            fontSize = 12.sp
        )

        CheckboxesSection(booleanArrayOf(lowerCaseCheckboxChecked,upperCaseCheckboxChecked,oneNumberCheckboxChecked, eightCharactersCheckboxChecked,specialCharacterCheckboxChecked))
        
        ClickableMultiColoredText(
            modifier = Modifier
                .padding(start = 8.dp, end = 4.dp, top = 30.dp)
                .align(Alignment.CenterHorizontally),
            colorPosition = 1,
            secondColor = yvColor,
            fontSize=10.sp,
            onColoredTextClicked = onTermsAndConditionsClicked,
            stringResource(id = R.string.by_creating_account),
            stringResource(id = R.string.terms)
        )
        
        ActionButton(modifier=Modifier.padding(top = 10.dp, bottom = 16.dp),text = "Create Password", onButtonClicked =onCreatePasswordButtonClicked )

    }
}

@Composable
fun CheckboxesSection(checks:BooleanArray) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(start = 16.dp)
    ) {
        PasswordValidationCheckbox(title ="At least one lower case letter" , checked =checks[0] )
        PasswordValidationCheckbox(title ="At least one upper case letter" , checked =checks[1] )
        PasswordValidationCheckbox(title ="At least one number" , checked =checks[2] )
        PasswordValidationCheckbox(title ="Eight characters minimum" , checked =checks[3] )
        PasswordValidationCheckbox(title ="One special character (allowed characters !@#\$%&*)" , checked =checks[4] )
    }
}

@Preview
@Composable
fun SignUpScreenReview(){
   YouHrTheme {
       Surface {
           CreatePasswordScreen(
               email = "adesoji@youverify.co",
               passwordValue = "Ade$",
               confirmPasswordValue ="Ade$",
               lowerCaseCheckboxChecked =false,
               upperCaseCheckboxChecked =false,
               oneNumberCheckboxChecked =true,
               eightCharactersCheckboxChecked =true,
               specialCharacterCheckboxChecked =false,
               onPasswordValueChanged ={},
               onConfirmPasswordValueChanged ={},
               onPasswordVisibilityIconClicked = {},
               onCreatePasswordButtonClicked = {},
               onTermsAndConditionsClicked = {},
               resetPassword = false
           )
       }
   }
}

