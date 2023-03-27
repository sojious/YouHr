package co.youverify.youhr.presentation.ui.login

import androidx.compose.ui.text.style.TextAlign
import co.youverify.youhr.presentation.ui.theme.textLight
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import co.youverify.youhr.presentation.ui.components.ActionButton
import co.youverify.youhr.presentation.ui.components.ClickableMultiColoredText
import co.youverify.youhr.presentation.ui.components.TitleText
import co.youverify.youhr.presentation.ui.theme.yvColor2
import co.youverify.youhr.presentation.ui.theme.yvText

@Composable
fun RecoveryEmailSentScreen(
    modifier: Modifier=Modifier,
    onContactSupportClicked: () -> Unit,
    onGotItButtonClicked: () -> Unit
){
    Box(modifier = modifier.fillMaxSize()){
        Column(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .align(Alignment.Center)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TitleText(text = stringResource(id = R.string.recovery_email_sent_title))

            Text(
                modifier=Modifier.padding(start = 20.dp,end=20.dp),
                text = stringResource(id = R.string.recovery_email_sent_message),
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = textLight,
                textAlign = TextAlign.Center
            )

            ClickableMultiColoredText(
                modifier = Modifier.padding(top=16.dp, bottom = 64.dp),
                colorPosition =1 ,
                secondColor = yvText ,
                fontSize = 12.sp,
                onColoredTextClicked = onContactSupportClicked,
                FontWeight.Bold,
                stringResource(id = R.string.didnt_get_recovery_link),
                stringResource(id = R.string.contact_support)
            )

            ActionButton(
                text = stringResource(id = R.string.got_it),
                onButtonClicked = onGotItButtonClicked,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

        }
    }
}

@Preview
@Composable
fun RecoveryEmailSentScreenPreview(){
    Surface {
        RecoveryEmailSentScreen(onContactSupportClicked = {}, onGotItButtonClicked = {})
    }
}