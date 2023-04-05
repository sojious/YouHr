package co.youverify.youhr.presentation.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.youverify.youhr.R
import co.youverify.youhr.presentation.ui.components.ActionButton
import co.youverify.youhr.presentation.ui.components.TitleText
import co.youverify.youhr.presentation.ui.components.TitledTextField
import co.youverify.youhr.presentation.ui.theme.textLight

@Composable
fun ResetPasswordScreen(
    modifier: Modifier = Modifier,
    emailValue: String,
    onEmailValueChanged: (String) -> Unit,
    onResetPasswordButtonClicked: () -> Unit,
    onBackArrowClicked: () -> Unit,
    isErrorValue: Boolean,
    uiState: UiState,

    ){
    Box(modifier = modifier.fillMaxSize()){

        IconButton(onClick = onBackArrowClicked, modifier = Modifier
            .align(Alignment.TopStart)
            .padding(top = 52.dp, start = 23.42.dp)) {
            Icon(painter = painterResource(id = R.drawable.ic_back_arrow), contentDescription =null )
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleText(text = stringResource(id = R.string.reset_password_title),)

            Text(
                text = stringResource(id = R.string.reset_password_message),
                fontSize = 12.sp,
                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top=16.dp),
                lineHeight = 16.sp,
                textAlign = TextAlign.Center,
                color = textLight
            )

            TitledTextField(
                modifier=Modifier.padding(start = 20.dp,end=20.dp, top=58.dp, bottom = 64.dp),
                fieldTitle = stringResource(id = R.string.work_email),
                fieldValue =emailValue ,
                fieldPlaceHolder =stringResource(id = R.string.work_email_placeholder) ,
                hideValue =false ,
                onFieldValueChanged =onEmailValueChanged ,
                isPasswordField = false,
                isErrorValue = isErrorValue,
                errorMessage = uiState.authenticationError
            )



            ActionButton(
                modifier=Modifier.padding(horizontal = 20.dp),
                text = stringResource(id = R.string.send_password_reset),
                onButtonClicked = onResetPasswordButtonClicked
            )

        }

        if(uiState.loading)
            CircularProgressIndicator(modifier=Modifier.align(Alignment.Center))
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
            onBackArrowClicked = {},
            isErrorValue = true,
            uiState = UiState(authenticationError = "The email is wrong!!")
        )
    }
}