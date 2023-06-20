package co.youverify.youhr.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.BaselineShift
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.youverify.youhr.presentation.ui.theme.backGroundColor
import co.youverify.youhr.presentation.ui.theme.codeInputUnfocused
import co.youverify.youhr.presentation.ui.theme.yvColor

@Composable
fun CodeInputBox(
    modifier: Modifier = Modifier,
    codeValue1: String,
    codeValue2: String,
    codeValue3: String,
    codeValue4: String,
    codeValue5: String,
    codeValue6: String,
    errorMessage: String,
    isError: Boolean,
    activeFieldIndex: Int,
    onValueChanged: (String, Int) -> Unit,
    onBackSpaceKeyPressed: (Int) -> Unit,


    ) {


    Column {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = modifier
        ) {

            CodeInputField(codeValue1,1,activeFieldIndex,onValueChanged, onBackSpaceKeyPressed)
            CodeInputField(codeValue2,2,activeFieldIndex,onValueChanged,onBackSpaceKeyPressed)
            CodeInputField(codeValue3,3,activeFieldIndex,onValueChanged,onBackSpaceKeyPressed)
            CodeInputField(codeValue4,4,activeFieldIndex,onValueChanged,onBackSpaceKeyPressed)
            CodeInputField(codeValue5,5,activeFieldIndex,onValueChanged,onBackSpaceKeyPressed)
            CodeInputField(codeValue6,6,activeFieldIndex,onValueChanged,onBackSpaceKeyPressed)


        }


        if (isError)
            Text(
            text = errorMessage,
            color = co.youverify.youhr.presentation.ui.theme.errorMessageColor,
            modifier = Modifier.padding(top=8.dp),
            fontSize = 12.sp
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun CodeInputField(
    value: String,
    index: Int,
    activeIndex: Int,
    onValueChanged: (String,Int) -> Unit,
    onBackSpaceKeyPressed: (Int) -> Unit,

){

    val enabled=activeIndex==index
    val borderColor =if (enabled) yvColor else codeInputUnfocused
    val interactionSource=remember{ MutableInteractionSource() }
    val focusRequester=remember{FocusRequester()}

    LaunchedEffect(key1 = enabled){

        if (enabled)
            focusRequester.requestFocus()

    }

    BasicTextField(
        value = value,
        onValueChange ={
                       onValueChanged(it,index)
        },
        enabled=enabled,
        maxLines=1,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        textStyle = TextStyle( textAlign = TextAlign.Center, fontWeight = FontWeight.ExtraBold, baselineShift = BaselineShift(0.5f)),
        modifier = Modifier
            .size(36.dp, 42.dp)
            .background(color = Color.White, shape = RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                brush = SolidColor(borderColor),
                shape = RoundedCornerShape(12.dp)
            )
           .focusRequester(focusRequester)
            .onKeyEvent {
                if (it.key == Key.Backspace && value.isEmpty())
                    onBackSpaceKeyPressed(index)
                true
            },

        interactionSource = interactionSource,
        visualTransformation = PasswordVisualTransformation('.'),
        decorationBox = {
            TextFieldDefaults.DecorationBox(
                value = "1",
                innerTextField = it,
                enabled = enabled,
                singleLine = true,
                visualTransformation = PasswordVisualTransformation('.'),
                supportingText = null,
                shape = TextFieldDefaults.shape,
                contentPadding = PaddingValues(0.dp),
                interactionSource = interactionSource,
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = backGroundColor,
                    focusedContainerColor = backGroundColor,
                    disabledContainerColor = backGroundColor,
                ),
                container = {}
            )
        },
        keyboardActions = KeyboardActions(onPrevious = null),


    )
}
@Preview
@Composable
fun CodeInputBoxPreview(){
    Surface {
        CodeInputBox(
            codeValue1 = "1",
            codeValue2 = "2",
            codeValue3 = "3",
            codeValue4 = "4",
            codeValue5 = "5",
            codeValue6 = "6",
            errorMessage = "You entered the wrong passcode, try again!!",
            isError = true,
            activeFieldIndex = 1,
            onValueChanged = { _, _ ->},
            onBackSpaceKeyPressed = {}
        )
    }
}


@Preview
@Composable
fun CodeInputFieldPreview(){
    Surface {
        CodeInputField(
            value = "", index = 1,
            onValueChanged = {_,_->},
            activeIndex = 1,
            onBackSpaceKeyPressed = {}
        )
    }
}


data class CodeInputBoxUiState(
    val input1:String,
    val input2:String,
    val input3:String,
    val input4:String,
    ){

    var text1 by mutableStateOf(input1)
    var text2 by mutableStateOf(input2)
    var text3 by mutableStateOf(input3)
    var text4 by mutableStateOf(input4)
    val inputs= arrayOf(text1,text2,text3,text4)
}

data class CodeInputFieldState(val code:String,val borderColor:Color){
    var text by mutableStateOf(code)
    var color by mutableStateOf(code)
}