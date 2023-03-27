package co.youverify.youhr.presentation.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.youverify.youhr.presentation.ui.theme.yvText

@Composable
fun TitleText(modifier: Modifier=Modifier,text:String){
    Text(
        modifier=modifier.padding(bottom = 16.dp),
        text = text,
        style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp, color = yvText, lineHeight = 26.sp),
        textAlign = TextAlign.Center
    )
}