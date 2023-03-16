package co.youverify.youhr.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import co.youverify.youhr.presentation.ui.theme.textLight
import co.youverify.youhr.presentation.ui.theme.yvColor1

@Composable
fun ClickableMultiColoredText(
    modifier: Modifier,
    colorPosition:Int,
    secondColor:Color,
    fontSize:TextUnit,
    onColoredTextClicked:() -> Unit,
    vararg parts:String
){
   Row(modifier = modifier) {
       repeat(parts.size){index->
           Text(
               modifier=Modifier.clickable(onClick = onColoredTextClicked),
               text =parts[index],
               fontSize=fontSize,
               color = if (index==colorPosition) secondColor else textLight
           )
       }
   }
}