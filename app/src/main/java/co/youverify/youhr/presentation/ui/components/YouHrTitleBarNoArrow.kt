package co.youverify.youhr.presentation.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.youverify.youhr.presentation.ui.theme.yvText

@Composable
fun YouHrTitleBarNoArrow(
    modifier: Modifier = Modifier,
    title: String,
){
    Box(
        modifier = modifier.fillMaxWidth(),
        //contentAlignment = Alignment.Center
    ) {


        Text(
            text = title,
            color = yvText,
            fontSize =16.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 24.sp,
            textAlign = TextAlign.Center,
            modifier=Modifier.width(200.dp).align(Alignment.Center)

            )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun YouHrTitleBarNoArrowPreview(){
    Surface {
        YouHrTitleBarNoArrow(
            title = "Interview With Candidate For Product Design Role",
        )
    }
}