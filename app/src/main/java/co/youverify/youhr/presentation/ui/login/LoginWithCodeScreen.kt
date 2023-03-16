package co.youverify.youhr.presentation.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import co.youverify.youhr.presentation.ui.theme.errorMessage
import co.youverify.youhr.presentation.ui.theme.textLight
import co.youverify.youhr.presentation.ui.theme.yvColor

@Composable
fun LoginWithCodeScreen(
    modifier: Modifier = Modifier,
    codeValue1:String,
    codeValue2:String,
    codeValue3:String,
    codeValue4:String,
    codeValue5:String,
    codeValue6:String,
    onCodeValueChanged:(String, Int)->Unit,
    onPasswordLoginOptionClicked: () -> Unit,
    onLoginButtonClicked: () -> Unit,
    onSignUpClicked: () -> Unit
){
    Column(
        modifier = modifier.padding(horizontal = 8.dp).fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TitleText(
            modifier=Modifier.padding(top=64.dp),
            text = stringResource(id = R.string.login_to_your_account)
        )

        Text(
            modifier=Modifier.padding(bottom = 40.dp),
            text = stringResource(id = R.string.sign_in_to_your_account)
        )

        MultiColoredText(
            fontSize =12.sp ,
            modifier = modifier
                .padding(start = 16.dp, bottom = 16.dp)
                .align(Alignment.Start),
            colorPosition =1 ,
            secondColor = errorMessage ,
            neutralColor = textLight,
            "Enter Code ",
            "*"
        )

        CodeInputBox(
            modifier=Modifier.padding(start = 16.dp).align(Alignment.Start),
            codeValue1=codeValue1,
            codeValue2=codeValue2,
            codeValue3=codeValue3,
            codeValue4=codeValue4,
            codeValue5=codeValue5,
            codeValue6=codeValue6,
            onValueChanged =onCodeValueChanged
        )

        Text(
            text = stringResource(id = R.string.login_with_password_option),
            fontSize = 12.sp,
            modifier = Modifier
                .padding(start = 16.dp)
                .align(Alignment.Start)
                .clickable(onClick = onPasswordLoginOptionClicked),
            color = yvColor
        )

        ActionButton(
            modifier=Modifier.padding(top = 24.dp),
            text = stringResource(id = R.string.next),
            onButtonClicked = onLoginButtonClicked
        )
        
        ClickableMultiColoredText(
            modifier = modifier
                .padding(vertical = 4.dp, horizontal = 16.dp)
                .align(Alignment.Start),
            colorPosition = 1,
            secondColor = yvColor,
            fontSize = 12.sp,
            onColoredTextClicked = onSignUpClicked,
            stringResource(id = R.string.dont_have_account),
            stringResource(id = R.string.sign_up)
        )
    }
}

@Preview
@Composable
fun LoginWithCodePreview(){
   Surface {
       LoginWithCodeScreen(
           onPasswordLoginOptionClicked = {},
           onLoginButtonClicked = {},
           onSignUpClicked = {},
           codeValue1="1",
           codeValue2="2",
           codeValue3="3",
           codeValue4="4" ,
           codeValue5="5",
           codeValue6="6",
           onCodeValueChanged = {_,_->}
       )
   }
}