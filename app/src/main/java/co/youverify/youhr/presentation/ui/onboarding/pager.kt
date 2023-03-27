package co.youverify.youhr.presentation.ui.onboarding


import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import co.youverify.youhr.R
import co.youverify.youhr.presentation.ui.components.ActionButton
import co.youverify.youhr.presentation.ui.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.google.accompanist.pager.PagerState


@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingPagerScreen(
    modifier: Modifier=Modifier,
    pagerState: PagerState,
    actionButtonText:String,
    onLoginButtonClicked:()-> Unit
){



        Column(
            modifier= modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OnboardingPager(pagerState =pagerState )


            HorizontalPagerIndicator(
                pagerState =pagerState,
                activeColor= yvColor,
                inactiveColor = indicatorInactive,
                indicatorShape = RoundedCornerShape(4.dp),
                indicatorWidth = 24.dp,
                indicatorHeight = 8.dp,
                spacing = 4.dp,
                modifier = Modifier.padding(top = 36.dp)
            )

            //PagerIndicator()

            //Spacer(modifier = Modifier.weight(1f))

            ActionButton(
                text = actionButtonText,
                modifier = Modifier
                    .padding(start = 33.dp, end = 33.dp, top = 50.dp),
                onButtonClicked = onLoginButtonClicked
            )
    }


}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingPager(modifier: Modifier=Modifier, pagerState: PagerState){

    Column(
        modifier=modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HorizontalPager(
            count = pagerItems.size,
            state = pagerState
        ) {currentPagerIndex->
            val currentPagerItem=pagerItems[currentPagerIndex]

            PagerItem(imageResId =currentPagerItem.ImageResId, messageResId =currentPagerItem.messageResId)

        }


    }
}

@Composable
fun PagerItem(modifier: Modifier=Modifier,imageResId:Int,messageResId:Int){
    Column(
        modifier.padding(horizontal = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id =imageResId ),
            contentDescription =null,
            modifier=Modifier.size(351.dp,197.dp)
        )

        Text(
            text = stringResource(id =messageResId ),
            modifier=Modifier.padding(top=19.dp),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontFamily = sPProTextFont, fontWeight = FontWeight.Bold, color = textDim
            )
        )
    }
}



val pagerItems= listOf(
    PagerItemData(R.drawable.meeting, R.string.better_work_place),
    PagerItemData(R.drawable.interview, R.string.better_work_place),
    PagerItemData(R.drawable.interview, R.string.better_work_place),
)
data class PagerItemData(val ImageResId:Int, val messageResId: Int)

@OptIn(ExperimentalPagerApi::class)
@Preview()
@Composable
fun OnBoardingPreview(){
    YouHrTheme {
        Surface {
            OnboardingPagerScreen(pagerState = PagerState(), actionButtonText = "Login") {}
        }
    }

}