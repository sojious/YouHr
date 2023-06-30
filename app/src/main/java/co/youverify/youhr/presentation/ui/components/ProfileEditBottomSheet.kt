package co.youverify.youhr.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.youverify.youhr.presentation.ui.settings.profile.EditableFieldType
import co.youverify.youhr.presentation.ui.theme.YouHrTheme
import co.youverify.youhr.presentation.ui.theme.backGroundColor
import co.youverify.youhr.presentation.ui.theme.backGroundColor2
import co.youverify.youhr.presentation.ui.theme.bodyTextLightColor
import co.youverify.youhr.presentation.ui.theme.inputDeepTextColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileEditBottomSheet(
    modifier: Modifier = Modifier,
    title: String,
    textFieldValue: String,
    fieldType: EditableFieldType,
    onTextFieldValueChanged: (String) -> Unit,
    onSaveButtonClicked: (EditableFieldType, String) -> Unit,
    sheetState: SheetState,
){
    val coroutineScope= rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = {},
        sheetState = sheetState,
        dragHandle = {},
        shape = RoundedCornerShape(8.dp),
        //scrimColor = Color.Transparent
    ) {
        Column(modifier=modifier.fillMaxWidth()) {

            val boardOptions=remember{
                if (fieldType==EditableFieldType.PHONE || fieldType==EditableFieldType.NEXTOFKINPHONE)
                    KeyboardOptions(keyboardType = KeyboardType.NumberPassword) else KeyboardOptions.Default
            }

            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = inputDeepTextColor,
                modifier=Modifier.padding(start = 22.dp,end=22.dp,top=36.dp, bottom = 22.dp)
            )

            TextField(
                value = textFieldValue,
                onValueChange = {
                    onTextFieldValueChanged(it)
                },
                modifier= Modifier
                    .padding(start = 22.dp, end = 25.dp, bottom = 40.dp)
                    .fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                ),
                keyboardOptions = boardOptions
            )

            Row(
                modifier= Modifier
                    .align(Alignment.End)
                    .padding(end = 28.dp),
                horizontalArrangement = Arrangement.spacedBy(52.dp)
            ) {
                TextButton(
                    onClick = {
                        coroutineScope.launch { sheetState.hide() }
                    },
                    content = {Text(text = "Cancel", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = bodyTextLightColor) }
                )


                TextButton(
                    onClick = {
                        onSaveButtonClicked(fieldType,textFieldValue)
                        coroutineScope.launch { sheetState.hide() }

                    },
                    content = { Text(text = "Save", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = bodyTextLightColor) }
                )

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ProfileEditBottomSheetPreview(){
    YouHrTheme {
        Surface {
            ProfileEditBottomSheet(

                title = "Enter Your Full Name",
                textFieldValue ="Edith Ibeh",
                fieldType = EditableFieldType.NEXTOFKIN,
                onTextFieldValueChanged ={},
                onSaveButtonClicked = {_,_->},
                sheetState =  rememberModalBottomSheetState()
            )
        }
    }
}