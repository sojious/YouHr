package co.youverify.youhr.presentation.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import co.youverify.youhr.presentation.ui.components.ActionButton
import co.youverify.youhr.presentation.ui.components.TitleText
import co.youverify.youhr.presentation.ui.components.TitledTextField
import co.youverify.youhr.presentation.ui.theme.yvColor2

@Composable
fun LoginWithEmailScreen(
    modifier: Modifier = Modifier,
    emailValue: String,
    onEmailValueChanged: (String) -> Unit,
    onNextButtonClicked: () -> Unit,
    onLoginWithCodeOptionClicked: () -> Unit
){
    Box(modifier = modifier.fillMaxSize()){
        Column(
            modifier = Modifier.padding(horizontal = 4.dp).align(Alignment.Center)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleText(text = stringResource(id = R.string.login_to_your_account))

            //Text(text = stringResource(id = R.string.sign_in_to_your_account))
            
            TitledTextField(
                modifier=Modifier.padding(top=60.dp, bottom = 36.dp, start = 20.dp, end = 20.dp),
                fieldTitle = stringResource(id = R.string.work_email),
                fieldValue =emailValue ,
                fieldPlaceHolder =stringResource(id = R.string.work_email_placeholder) ,
                hideValue =false ,
                onFieldValueChanged =onEmailValueChanged ,
                isPasswordConfirmationField = false,
                isPasswordField = false
            )
            
            ActionButton(
                text = stringResource(id = R.string.next),
                onButtonClicked = onNextButtonClicked,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Text(
                text="Log In With Code",
                modifier = Modifier.padding(top=16.dp, start = 20.dp)
                    .align(Alignment.Start)
                    .clickable {  onLoginWithCodeOptionClicked()},
                fontSize = 12.sp,
                lineHeight=16.3.sp,
                color= yvColor2,
            )
            
        }
    }
}

@Preview
@Composable
fun LoginScreen(){
    Surface {
        LoginWithEmailScreen(emailValue ="", onEmailValueChanged ={}, onNextButtonClicked = {}, onLoginWithCodeOptionClicked = {})
    }
}