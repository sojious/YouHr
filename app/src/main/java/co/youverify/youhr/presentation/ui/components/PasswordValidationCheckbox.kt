package co.youverify.youhr.presentation.ui.components


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.youverify.youhr.presentation.ui.theme.checkboxColor
import co.youverify.youhr.presentation.ui.theme.textLight

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PasswordValidationCheckbox(modifier: Modifier = Modifier,title:String, checked: Boolean){

   Row(
       horizontalArrangement = Arrangement.spacedBy(4.dp),
       verticalAlignment = Alignment.CenterVertically,
       modifier = modifier
   ) {
       Checkbox(
           modifier=Modifier.size(16.dp),
           checked =checked ,
           onCheckedChange ={},
           colors = CheckboxDefaults.colors(
               checkedColor = checkboxColor,
               uncheckedColor = textLight,
               checkmarkColor = Color.White
           )
       )

       Text(
           text = title,
           color = textLight,
           fontSize = 12.sp,
           lineHeight = 15.sp
       )

   }
}

