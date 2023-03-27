package co.youverify.youhr.presentation.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavHostController
import co.youverify.youhr.R
import co.youverify.youhr.presentation.ui.theme.yvText
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@Composable
fun YouHrTitleBar(modifier: Modifier = Modifier, title: String,navController: NavHostController){
    ConstraintLayout(
        modifier = modifier.fillMaxWidth(),
    ) {

        val (backArrow,titleText) = createRefs()
        IconButton(
            modifier= Modifier
                //.fillMaxWidth()
                .constrainAs(backArrow) {
                    start.linkTo(parent.start, 24.dp)
                    centerVerticallyTo(parent)
                },
            onClick = {
                      navController.popBackStack()
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
                start.linkTo(backArrow.end,14.dp)
                end.linkTo(parent.end,21.dp)
               width= Dimension.fillToConstraints
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
            navController = rememberAnimatedNavController()
        )
    }
}
