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

@Composable
fun ResetPasswordSuccessScreen(modifier: Modifier=Modifier, onLoginRedirectButtonClicked:()->Unit){

       Column(
           modifier=modifier
               .fillMaxSize()
               .padding(horizontal = 8.dp)
               .verticalScroll(rememberScrollState()),
           horizontalAlignment = Alignment.CenterHorizontally
       ) {

           TitleText(
               modifier=Modifier.padding(top=150.dp),
               text = stringResource(id = R.string.you_got_mail)
           )
           Text(
               modifier=Modifier.padding(start=8.dp,end=8.dp,bottom = 64.dp),
               text = stringResource(id = R.string.password_reset_success),
               fontSize = 12.sp,
               lineHeight = 16.sp,
               textAlign = TextAlign.Center
           )

           ActionButton(
               text = stringResource(id = R.string.login_redirect),
               onButtonClicked = onLoginRedirectButtonClicked
           )
       }

}

@Preview
@Composable
fun ResetPassWordSuccessScreenReview(){

    Surface {
        ResetPasswordSuccessScreen(onLoginRedirectButtonClicked = {})
    }
}