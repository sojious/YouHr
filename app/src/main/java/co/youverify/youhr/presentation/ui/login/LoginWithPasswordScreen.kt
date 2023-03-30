package co.youverify.youhr.presentation.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
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
import co.youverify.youhr.presentation.ui.theme.primaryColor
import co.youverify.youhr.presentation.ui.theme.yvColor

@Composable
fun LoginWithPasswordScreen(
    modifier: Modifier = Modifier,
    passwordValue: String,
    isErrorPassword: Boolean,
    hidePassword: Boolean,
    onPasswordValueChanged: (String) -> Unit,
    onLoginButtonClicked: () -> Unit,
    onHidePasswordIconClicked: () -> Unit,
    onForgotPasswordClicked: () -> Unit,
    uiState: UiState,
){

    Box(modifier = modifier.fillMaxSize()){





        Column(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleText(text = stringResource(id = R.string.login_to_your_account))


            TitledTextField(
                modifier=Modifier.padding(top=60.dp, start = 20.dp, end = 20.dp),
                fieldTitle = stringResource(id = R.string.password),
                fieldValue =passwordValue ,
                fieldPlaceHolder =stringResource(id = R.string.password_login_placeholder) ,
                hideValue =hidePassword ,
                isErrorValue = isErrorPassword,
                onFieldValueChanged =onPasswordValueChanged ,
                isPasswordConfirmationField = true,
                isPasswordField = true,
                passWordErrorMessage =uiState.authenticationError,
                onTrailingIconClicked = onHidePasswordIconClicked
            )


            Text(
                text = stringResource(id = R.string.forgot_password),
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(end = 20.dp, top = 16.dp, bottom = 36.dp)
                    .align(Alignment.End)
                    .clickable(onClick = onForgotPasswordClicked),
                color = primaryColor
            )

            ActionButton(
                modifier=Modifier.padding(horizontal = 20.dp),
                text = stringResource(id = R.string.login),
                onButtonClicked = onLoginButtonClicked
            )

        }

        if (uiState.loading)
            CircularProgressIndicator(modifier=Modifier.align(Alignment.Center))
    }
}

@Preview
@Composable
fun LoginPassWordScreenPreview(){
    Surface {
        LoginWithPasswordScreen(
            passwordValue = "",
            isErrorPassword = false,
            onPasswordValueChanged = {},
            onLoginButtonClicked = {},
            onHidePasswordIconClicked = {},
            onForgotPasswordClicked = {},
            hidePassword = true,
            uiState = UiState()
        )
    }
}