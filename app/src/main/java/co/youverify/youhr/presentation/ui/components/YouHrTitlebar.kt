package co.youverify.youhr.presentation.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import co.youverify.youhr.R
import co.youverify.youhr.presentation.ui.theme.yvText

@Composable
fun YouHrTitleBar(
    modifier: Modifier = Modifier,
    title: String,
    onBackArrowClicked: () -> Unit = {},
){
    ConstraintLayout(
        modifier = modifier.fillMaxWidth(),
    ) {

        val (backArrow,titleText) = createRefs()
        IconButton(
            modifier= Modifier
                //.fillMaxWidth()
                .size(16.dp)
                .constrainAs(backArrow) {
                    start.linkTo(parent.start, 24.dp)
                    //centerVerticallyTo(parent)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    //width=Dimension.fillToConstraints
                },
            onClick = {
                      onBackArrowClicked()
            },
            content = {
                   Icon(
                       painter = painterResource(id = R.drawable.ic_back_arrow), contentDescription =null ,
                   )
            }
        )

        Text(
            text = title,
            color = yvText,
            fontSize =16.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 24.sp,
            textAlign = TextAlign.Center,
            modifier=Modifier.constrainAs(titleText){
                start.linkTo(parent.start)
                end.linkTo(parent.end)
                top.linkTo(parent.top)
               width= Dimension.value(200.dp)
            },

        )
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Preview
@Composable
fun YouHrTitleBarPreview(){
    Surface {
        YouHrTitleBar(
            title = "Interview With Candidate For Product Design Role",
            onBackArrowClicked = {},
        )
    }
}

fun Modifier.shimmerEffect(): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }
    val transition = rememberInfiniteTransition()
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(1000)
        )
    )

    background(
        brush = Brush.linearGradient(
            colors = listOf(
                //Color(0xFF949090),
                //Color(0xFFBBB9B9),
                Color(0xCCF7F7F9),
                Color(0XFFF7F7F9),
                //Color(0xFF635F5F),
                //Color(0xFF9B9797),

                //Color(0xFFB8B5B5),
                //Color(0xFF3F3C3C),
                Color(0x80F7F7F9)
            ),
            start = Offset(startOffsetX, 0f),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    )
        .onGloballyPositioned {
            size = it.size
        }
}
