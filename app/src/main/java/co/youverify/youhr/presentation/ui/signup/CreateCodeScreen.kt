package co.youverify.youhr.presentation.ui.signup

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.youverify.youhr.R
import co.youverify.youhr.presentation.ui.components.ActionButton
import co.youverify.youhr.presentation.ui.components.CodeInputBox
import co.youverify.youhr.presentation.ui.components.TitleText
import co.youverify.youhr.presentation.ui.theme.*

@Composable
fun CreateCodeScreen(
    modifier: Modifier=Modifier,
    codeValue1:String,
    codeValue2:String,
    codeValue3:String,
    codeValue4:String,
    onSkipClicked:()->Unit,
    onCreateCodeButtonClicked:()->Unit,
    onCodeValueChanged: (String,Int) -> Unit
){
    Column(
        modifier = modifier
            .fillMaxSize()
            //.padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TitleText(modifier = Modifier.padding(top=60.dp), text = stringResource(id = R.string.you_got_mail))

        Text(
            text = stringResource(id = R.string.congratulations),
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            modifier=Modifier.padding(start = 20.dp,end=20.dp),
            color = textLight
        )

        CreateCodeBox(
            onTextButtonClicked = onSkipClicked,
            onActionButtonClicked = onCreateCodeButtonClicked,
            codeValue1=codeValue1,
            codeValue2=codeValue2,
            codeValue3=codeValue3,
            codeValue4=codeValue4,
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
    onValueChanged: (String, Int) -> Unit,

) {
    Column(
        modifier = Modifier
            .padding(top=46.dp, start = 33.dp,end=33.dp)
            .border(width = 0.2.dp, brush = SolidColor(codeInputUnfocused), shape = RoundedCornerShape(5.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextButton(
            modifier = Modifier.align(Alignment.End),
            onClick = onTextButtonClicked
        ) {
            Text(
                text = "Skip",
                color = primaryColor,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
        
        Text(
            text = stringResource(id = R.string.create_code),
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 20.8.sp,
            color = yvText,
            modifier = Modifier.padding(bottom = 12.dp, top = 24.dp)
        )
        Text(
            text = stringResource(id = R.string.create_code_message),
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            modifier = Modifier.padding(start = 29.5.dp,end=29.5.dp,bottom=24.dp),
            color = textLight
        )
        CodeInputBox(
            modifier = Modifier.padding(bottom = 36.dp),
            codeValue1 =codeValue1,
            codeValue2=codeValue2,
            codeValue3=codeValue3,
            codeValue4 = codeValue4,
            onValueChanged=onValueChanged,
        )
        ActionButton(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 24.dp), text = stringResource(id = R.string.create_code_btn), onButtonClicked = onActionButtonClicked)
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
        onValueChanged ={_,_ ->}
    )
}
