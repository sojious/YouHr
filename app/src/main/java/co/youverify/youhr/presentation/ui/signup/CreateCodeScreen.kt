package co.youverify.youhr.presentation.ui.signup

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.youverify.youhr.R
import co.youverify.youhr.presentation.ui.components.ActionButton
import co.youverify.youhr.presentation.ui.components.CodeInputBox
import co.youverify.youhr.presentation.ui.components.TitleText
import co.youverify.youhr.presentation.ui.theme.yvColor

@Composable
fun CreateCodeScreen(
    modifier: Modifier=Modifier,
    codeValue1:String,
    codeValue2:String,
    codeValue3:String,
    codeValue4:String,
    codeValue5:String,
    codeValue6:String,
    onSkipClicked:()->Unit,
    onCreateCodeButtonClicked:()->Unit,
    onCodeValueChanged: (String,Int) -> Unit
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TitleText(modifier = Modifier.padding(top=32.dp), text = stringResource(id = R.string.you_got_mail))

        Text(
            text = stringResource(id = R.string.congratulations),
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            modifier=Modifier.padding(start = 16.dp,end=16.dp, bottom = 64.dp)
        )

        CreateCodeBox(
            onTextButtonClicked = onSkipClicked,
            onActionButtonClicked = onCreateCodeButtonClicked,
            codeValue1=codeValue1,
            codeValue2=codeValue2,
            codeValue3=codeValue3,
            codeValue4=codeValue4,
            codeValue5=codeValue5,
            codeValue6=codeValue6,
            onValueChanged=onCodeValueChanged
        )
    }
}

@Composable
fun CreateCodeBox(
    onTextButtonClicked: () -> Unit,
    onActionButtonClicked: () -> Unit,
    codeValue1: String,
    codeValue2: String,
    codeValue3: String,
    codeValue4: String,
    codeValue5: String,
    codeValue6: String,
    onValueChanged: (String, Int) -> Unit,

) {
    Column(
        modifier = Modifier
            .border(width = 1.dp, brush = SolidColor(yvColor), shape = RectangleShape),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextButton(
            modifier = Modifier.align(Alignment.End),
            onClick = onTextButtonClicked
        ) {
            Text(text = "Skip", color = yvColor)
        }
        
        TitleText(text = stringResource(id = R.string.create_code))
        Text(
            text = stringResource(id = R.string.create_code_message),
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            modifier = Modifier.padding(start = 30.dp,end=30.dp,bottom=24.dp)
        )
        CodeInputBox(
            modifier = Modifier,
            codeValue1 =codeValue1,
            codeValue2=codeValue2,
            codeValue3=codeValue3,
            codeValue4 = codeValue4,
            codeValue5=codeValue5,
            codeValue6=codeValue6,
            onValueChanged=onValueChanged
        )
        ActionButton(modifier = Modifier.padding(bottom = 24.dp), text = stringResource(id = R.string.create_code_btn), onButtonClicked = onActionButtonClicked)
    }
}







@Preview
@Composable
fun CodeScreenPreview(){
    Surface {
        CreateCodeScreen(
            onSkipClicked = {},
            onCreateCodeButtonClicked = {},
            codeValue1 ="1",
            codeValue2="2",
            codeValue3="3",
            codeValue4 = "4",
            codeValue5="5",
            codeValue6 = "6",
            onCodeValueChanged ={_,_ ->}
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
        codeValue5="5",
        codeValue6 = "6",
        onValueChanged ={_,_ ->}
    )
}
