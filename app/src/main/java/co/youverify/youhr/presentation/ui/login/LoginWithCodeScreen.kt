package co.youverify.youhr.presentation.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.youverify.youhr.R
import co.youverify.youhr.presentation.ui.components.*
import co.youverify.youhr.presentation.ui.theme.*

@Composable
fun LoginWithCodeScreen(
    modifier: Modifier = Modifier,
    codeValue1:String,
    codeValue2:String,
    codeValue3:String,
    codeValue4:String,
    codeValue5:String,
    codeValue6:String,
    isErrorCode:Boolean,
    uiState: UiState,
    onCodeValueChanged:(String, Int)->Unit,
    onPasswordLoginOptionClicked: () -> Unit,
    //onLoginButtonClicked: () -> Unit,
){
   Box(modifier = modifier.fillMaxSize()) {

       Column(
           modifier = Modifier.align(Alignment.Center),
           horizontalAlignment = Alignment.CenterHorizontally
       ) {

           TitleText(
               modifier=Modifier.padding( bottom = 55.dp),
               text = stringResource(id = R.string.login_to_your_account)
           )



           Text(
               modifier=Modifier.padding(bottom = 16.dp),
               text = "Enter Code",
               fontSize =16.sp ,
               lineHeight = 20.8.sp,
               fontWeight = FontWeight.Medium,
               color = bodyTextLightColor

           )

           CodeInputBox(
               // modifier=Modifier.padding(start = 16.dp).align(Alignment.Start),
               codeValue1=codeValue1,
               codeValue2=codeValue2,
               codeValue3=codeValue3,
               codeValue4=codeValue4,
               codeValue5=codeValue5,
               codeValue6=codeValue6,
               onValueChanged =onCodeValueChanged,
               errorMessage = uiState.authenticationError,
               isError = isErrorCode
           )

           Text(
               text = stringResource(id = R.string.login_with_password_option),
               fontSize = 12.sp,
               modifier = Modifier
                   .padding(vertical = 48.dp)
                   // .align(Alignment.Start)
                   .clickable(onClick = onPasswordLoginOptionClicked),
               color = primaryColor,
               fontWeight = FontWeight.Medium
           )

           /*ActionButton(
               modifier=Modifier.padding(horizontal = 28.dp),
               text = stringResource(id = R.string.login),
               onButtonClicked = onLoginButtonClicked
           )*/

       }

       if (uiState.loading)
           //CircularProgressIndicator(modifier=Modifier.align(Alignment.Center))
           LoadingDialog(message = "Authenticating...")

   }
}

@Preview
@Composable
fun LoginWithCodePreview(){
   Surface {
       LoginWithCodeScreen(
           onPasswordLoginOptionClicked = {},
           //onLoginButtonClicked = {},
           //onSignUpClicked = {},
           codeValue1="1",
           codeValue2="2",
           codeValue3="3",
           codeValue4="4" ,
           codeValue5 = "5",
           codeValue6 = "6",
           onCodeValueChanged = {_,_->},
           isErrorCode = true,
           uiState = UiState(authenticationError = "You entered the wrong passcode, try again!!",)
       )
   }
}