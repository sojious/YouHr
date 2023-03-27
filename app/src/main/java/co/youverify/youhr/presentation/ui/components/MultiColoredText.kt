package co.youverify.youhr.presentation.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import co.youverify.youhr.presentation.ui.theme.textLight

@Composable
fun MultiColoredText(
    fontSize: TextUnit,
    modifier: Modifier=Modifier,
    colorPosition: Int,
    secondColor: Color,
    neutralColor:Color,
    vararg parts: String,
){

        Text(
            modifier=modifier,
            text = buildAnnotatedString {
                for (i in parts.indices){
                    if (i==colorPosition)
                        withStyle(SpanStyle(color = secondColor)){
                        append(parts[i])
                    }

                    else
                        withStyle(SpanStyle(color = neutralColor)){
                            append(parts[i])
                        }
                }


            },
            fontSize = fontSize,
            lineHeight = 16.sp,
            textAlign = TextAlign.Center
        )
}