package co.youverify.youhr.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.youverify.youhr.R
import co.youverify.youhr.presentation.ui.theme.*

@Composable
fun TitledTextField(
    modifier: Modifier = Modifier,
    fieldTitle: String,
    fieldValue: String,
    fieldPlaceHolder: String,
    isErrorValue: Boolean ,
    hideValue: Boolean,
    isPasswordField:Boolean=true,
    onFieldValueChanged: (String) -> Unit,
    onTrailingIconClicked: () -> Unit={},
    errorMessage: String

    ){

   Column(
       modifier= modifier
           //.padding(horizontal = 16.dp)
           .fillMaxWidth(),
   ) {

       Row(modifier=Modifier.padding(bottom = 10.dp)) {
           Text(text = fieldTitle, fontSize = 12.sp, color = textFieldTitle, fontWeight = FontWeight.Normal)
           Text(text = "*", color = co.youverify.youhr.presentation.ui.theme.errorMessageColor, textAlign = TextAlign.Center,modifier=Modifier.padding(start = 2.dp), fontSize = 12.sp)
       }

       OutlinedTextField(
           modifier=Modifier.fillMaxWidth(),
           value =fieldValue ,
           onValueChange =onFieldValueChanged,
           singleLine = true,
           placeholder = {
               Text(
                   text = fieldPlaceHolder,
                   fontSize = 10.sp,
                   lineHeight = 13.sp,
                   color = deactivatedColorDeep,
               )
                         },
           isError = isErrorValue,
           visualTransformation = if(hideValue) PasswordVisualTransformation() else VisualTransformation.None,
           colors = TextFieldDefaults.outlinedTextFieldColors(
               placeholderColor = indicatorInactive,
               focusedBorderColor =  yvText,
               unfocusedBorderColor = inputFieldColor
           ),
           trailingIcon = {
               if (isPasswordField)
                   Icon(
                   painter = if (hideValue) painterResource(id = R.drawable.hide) else painterResource(id = R.drawable.show),
                   contentDescription =null,
                   modifier= Modifier
                       .size(18.dp)
                       .clickable(onClick = onTrailingIconClicked)
                   )
           },
           shape = RoundedCornerShape(4.dp)
       )

       if (isErrorValue)
           Text(
               text = errorMessage,
               color = errorMessageColor,
               modifier = Modifier.padding(top=8.dp),
               fontSize = 12.sp
           )
       

   }

}


@Preview
@Composable
fun TitledTextFieldPreview(){

    var hide by remember { mutableStateOf(true) }
    var value by remember { mutableStateOf("") }

    Surface() {
        TitledTextField(
            fieldTitle = "Password",
            fieldValue = value,
            fieldPlaceHolder = "Enter Password",
            hideValue = hide,
            onFieldValueChanged = { value=it },
            onTrailingIconClicked = { hide=!hide },
            isErrorValue = false,
            errorMessage = ""
        )
    }


}


