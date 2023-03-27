package co.youverify.youhr.presentation.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.youverify.youhr.R
import co.youverify.youhr.presentation.ui.components.ActionButton
import co.youverify.youhr.presentation.ui.components.TitleText
import co.youverify.youhr.presentation.ui.components.TitledTextField

@Composable
fun ResetPasswordScreen(
    modifier: Modifier = Modifier,
    emailValue: String,
    onEmailValueChanged: (String) -> Unit,
    onResetPasswordButtonClicked: () -> Unit,
){
    Box(modifier = modifier.fillMaxSize()){
        Column(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleText(text = stringResource(id = R.string.reset_password_title))

            Text(
                text = stringResource(id = R.string.reset_password_message),
                fontSize = 12.sp,
                lineHeight = 16.sp,
                textAlign = TextAlign.Center
            )

            TitledTextField(
                modifier=Modifier.padding(top=64.dp, bottom = 10.dp),
                fieldTitle = stringResource(id = R.string.work_email),
                fieldValue =emailValue ,
                fieldPlaceHolder =stringResource(id = R.string.work_email_placeholder) ,
                hideValue =false ,
                onFieldValueChanged =onEmailValueChanged ,
                isPasswordConfirmationField = false,
                isPasswordField = false,
            )



            ActionButton(
                modifier=Modifier.padding(top = 24.dp),
                text = stringResource(id = R.string.next),
                onButtonClicked = onResetPasswordButtonClicked
            )

        }
    }
}

@Preview
@Composable
fun ResetPassWordScreenPreview(){
    Surface {
        ResetPasswordScreen(
            emailValue = "",
            onEmailValueChanged = {},
            onResetPasswordButtonClicked = {},
        )
    }
}