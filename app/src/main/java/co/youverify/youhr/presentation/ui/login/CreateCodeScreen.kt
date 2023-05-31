package co.youverify.youhr.presentation.ui.login


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.youverify.youhr.R
import co.youverify.youhr.presentation.ui.components.ActionButton
import co.youverify.youhr.presentation.ui.components.CodeInputBox
import co.youverify.youhr.presentation.ui.theme.*


@Composable
fun CreateCodeScreen(
    modifier: Modifier = Modifier,
    codeValue1: String,
    codeValue2: String,
    codeValue3: String,
    codeValue4: String,
    codeValue5: String,
    codeValue6: String,
    uiState: UiState,
    isErrorCode: Boolean,
    onNextButtonClicked: () -> Unit,
    onCodeValueChanged: (String, Int) -> Unit,
    activeCodeInputFieldIndex: Int,
    onBackSpaceKeyPressed: (Int) -> Unit
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
                color = bodyTextLightColor
            )

            CodeInputBox(
                modifier = Modifier.padding(bottom = 36.dp),
                codeValue1 =codeValue1,
                codeValue2=codeValue2,
                codeValue3=codeValue3,
                codeValue4 = codeValue4,
                codeValue5 = codeValue5,
                codeValue6 = codeValue6,
                errorMessage = uiState.authenticationError,
                isError = isErrorCode,
                activeFieldIndex = activeCodeInputFieldIndex,
                onValueChanged=onCodeValueChanged,
                onBackSpaceKeyPressed = onBackSpaceKeyPressed
            )

            ActionButton(
                text = stringResource(id = R.string.next),
                onButtonClicked = onNextButtonClicked
            )

        }

        if (uiState.loading)
            CircularProgressIndicator(modifier=Modifier.align(Alignment.Center))
            //LoadingDialog(message = "Creating passcode...")
    }
}


@Preview
@Composable
fun CodeScreenPreview(){
    Surface {
        CreateCodeScreen(
            codeValue1 ="1",
            codeValue2="2",
            codeValue3="3",
            codeValue4 = "4",
            codeValue5 = "5",
            codeValue6 = "6",
            uiState = UiState(),
            isErrorCode = false,
            onNextButtonClicked = {},
            onCodeValueChanged ={_,_ ->},
            activeCodeInputFieldIndex = 1,
            onBackSpaceKeyPressed = {}
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
        errorMessage = "",
        isError = false,
        activeFieldIndex = 1,
        onValueChanged ={_,_ ->},
        onBackSpaceKeyPressed = {}
    )
}


