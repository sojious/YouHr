package co.youverify.youhr.presentation.ui.login

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import co.youverify.youhr.R
import co.youverify.youhr.presentation.ui.components.ActionButton
import co.youverify.youhr.presentation.ui.components.CodeInputBox
import co.youverify.youhr.presentation.ui.theme.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun CreateCodeScreen(
    modifier: Modifier=Modifier,
    codeValue1:String,
    codeValue2:String,
    codeValue3:String,
    codeValue4:String,
    codeValue5:String,
    codeValue6:String,
    uiState: UiState,
    isErrorCode:Boolean,
    showSuccessDialog:Boolean,
    onCreateCodeButtonClicked:()->Unit,
    onProceedButtonClicked:()->Unit,
    onCodeValueChanged: (String,Int) -> Unit
){
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 49.dp)
                .fillMaxWidth()
                .align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Text(
                text = stringResource(id = R.string.create_code),
                fontSize = 20.sp,
                color = yvText,
                lineHeight = 26.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )


            Text(
                text = stringResource(id = R.string.create_code_message),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                modifier = Modifier.padding(horizontal = 29.5.dp,vertical=24.dp),
                color = bodyTextColor
            )

            CodeInputBox(
                modifier = Modifier.padding(bottom = 36.dp),
                codeValue1 =codeValue1,
                codeValue2=codeValue2,
                codeValue3=codeValue3,
                codeValue4 = codeValue4,
                codeValue5 = codeValue5,
                codeValue6 = codeValue6,
                onValueChanged=onCodeValueChanged,
                errorMessage = uiState.authenticationError,
                isError = isErrorCode
            )

            ActionButton(
                text = stringResource(id = R.string.create_code_btn),
                onButtonClicked = onCreateCodeButtonClicked
            )

        }

        if (uiState.loading)
            CircularProgressIndicator(modifier=Modifier.align(Alignment.Center))


        if (showSuccessDialog)
            SuccessDialog(onButtonClicked = onProceedButtonClicked)

    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SuccessDialog(
    modifier: Modifier = Modifier,
    onButtonClicked: () -> Unit,
) {

    var animateTrigger by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit) {
        launch {
            delay(200)
            animateTrigger = true
        }
    }


    Dialog(
        onDismissRequest = {},
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false, usePlatformDefaultWidth = false)
    ) {

       Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

           AnimatedVisibility(
               visible = animateTrigger,
               enter = scaleIn(animationSpec = tween(1000), initialScale = 0.5f)
           ) {
               Column(
                   horizontalAlignment = Alignment.CenterHorizontally,
                   modifier = modifier.width(289.dp).background(color = backGroundColor, shape = RoundedCornerShape(8.dp))
               ) {

                   Image(
                       painter = painterResource(id = R.drawable.ic_success),
                       contentDescription =null,
                       modifier = Modifier
                           .size(72.11.dp, 84.dp)
                           .padding(top = 36.dp, bottom = 24.dp)
                   )

                   Text(
                       text = "Great, all set!",
                       fontSize = 16.sp,
                       fontWeight = FontWeight.Bold,
                       color = yvText,
                       modifier = Modifier.padding(bottom=8.dp)
                   )


                   Text(
                       text = "You have successfully created a new login code for the next time you want to login.",
                       fontSize = 12.sp,
                       color = bodyTextColor,
                       modifier = Modifier.padding(start = 20.dp,end=20.dp, bottom=24.dp),
                       lineHeight = 16.sp,
                       textAlign = TextAlign.Center
                   )

                   ActionButton(text = "Proceed To Homepage",
                       onButtonClicked = onButtonClicked,
                       modifier = Modifier.padding(start = 49.dp,end=49.dp, bottom = 48.dp)
                   )
               }
           }
       }
    }


}

@Preview
@Composable
fun CodeScreenPreview(){
    Surface {
        CreateCodeScreen(
            onCreateCodeButtonClicked = {},
            codeValue1 ="1",
            codeValue2="2",
            codeValue3="3",
            codeValue4 = "4",
            codeValue5 = "5",
            codeValue6 = "6",
            onCodeValueChanged ={_,_ ->},
            showSuccessDialog = true,
            onProceedButtonClicked = {},
            isErrorCode = false,
            uiState = UiState()
        )
    }
}

@Preview
@Composable
fun CodeInputBoxPreview(){
    CodeInputBox(
        codeValue1 ="1",
        codeValue2="2",
        codeValue3="3",
        codeValue4 = "4",
        codeValue5 = "5",
        codeValue6 = "6",
        onValueChanged ={_,_ ->},
        errorMessage = "",
        isError = false
    )
}

@Preview
@Composable
fun SuccessDialogPreview(){
    Surface {
        SuccessDialog(onButtonClicked = {})
    }
}
