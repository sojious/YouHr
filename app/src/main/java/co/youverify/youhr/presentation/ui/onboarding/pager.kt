package co.youverify.youhr.presentation.ui.onboarding


import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import co.youverify.youhr.R
import co.youverify.youhr.presentation.ui.components.ActionButton
import co.youverify.youhr.presentation.ui.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.delay


@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingPagerScreen(
    modifier: Modifier=Modifier,
    pagerState: PagerState,
    actionButtonText:String,
    onLoginButtonClicked:()-> Unit,
    onboardingViewModel:OnboardingViewModel= hiltViewModel()
){



    //change the current page after every 3 seconds
    LaunchedEffect(key1 = pagerState.currentPage){


        onboardingViewModel.currentPage++

        if (onboardingViewModel.currentPage>2) onboardingViewModel.onBoardingTourCompleted=true

        if(!onboardingViewModel.onBoardingTourCompleted){
            delay(3000)

            pagerState.animateScrollToPage(onboardingViewModel.currentPage)
        }

    }



        Column(
            modifier= modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OnboardingPager(pagerState =pagerState,viewModel=onboardingViewModel )


            /*HorizontalPagerIndicator(
                pagerState =pagerState,
                activeColor= yvColor,
                inactiveColor = indicatorInactive,
                indicatorShape = RoundedCornerShape(4.dp),
                indicatorWidth = 24.dp,
                indicatorHeight = 8.dp,
                spacing = 4.dp,
                modifier = Modifier.padding(top = 36.dp)
            )*/

            //PagerIndicator()

            //Spacer(modifier = Modifier.weight(1f))
            
            WormPageIndicator(
                modifier = Modifier.padding(top = 31.17.dp),
                totalPages = pagerState.pageCount,
                pagerState=pagerState,
                inActiveIndicatorWidth = 23.37.dp,
                activeIndicatorWidth = 31.17.dp,
                indicatorHeight = 8.dp,
                activeColor = yvColor ,
                inactiveColor = indicatorInactive ,
                spacing = 8.dp,
                cornerRadius = 3.2.dp
            )

            ActionButton(
                text = actionButtonText,
                modifier = Modifier
                    .padding(start = 33.dp, end = 33.dp, top = 108.55.dp),
                onButtonClicked = onLoginButtonClicked
            )
    }


}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingPager(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    viewModel: OnboardingViewModel
){

    Column(
        modifier=modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        HorizontalPager(
            count = pagerItems.size,
            state = pagerState,
            userScrollEnabled = viewModel.onBoardingTourCompleted,
            reverseLayout = false
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
            modifier=Modifier.padding(top=18.7.dp),
            color = Color.Black,
            fontSize = 18.sp,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium
        )
    }
}



val pagerItems= listOf(
    PagerItemData(R.drawable.meeting, R.string.better_work_place),
    PagerItemData(R.drawable.interview, R.string.better_work_place),
    PagerItemData(R.drawable.meeting, R.string.better_work_place),
)
data class PagerItemData(val ImageResId:Int, val messageResId: Int)

@OptIn(ExperimentalPagerApi::class)
@Preview()
@Composable
fun OnBoardingPreview(){
    YouHrTheme {
        Surface {
            OnboardingPagerScreen(
                pagerState = PagerState(),
                actionButtonText = "Login",
                onLoginButtonClicked = {},
                onboardingViewModel = hiltViewModel()
            )
        }
    }

}



@OptIn(ExperimentalPagerApi::class)
@Composable
fun WormPageIndicator(
    totalPages: Int,
    modifier: Modifier = Modifier,
    inActiveIndicatorWidth: Dp,
    activeIndicatorWidth: Dp,
    indicatorHeight: Dp,
    activeColor: Color,
    inactiveColor: Color,
    spacing: Dp,
    cornerRadius: Dp,
    pagerState: PagerState
) {

    /*assert(
        value = currentPage in 0 until  totalPages,
        lazyMessage = { "Current page index is out of range." }
    )*/
    val rowWidth = (inActiveIndicatorWidth * (totalPages - 1)) + (spacing * (totalPages - 1) + activeIndicatorWidth)
    Row(
        modifier = modifier
            .requiredWidth(rowWidth),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        for (i in 0 until pagerState.pageCount) {
            val selected = i == pagerState.currentPage
            val height = indicatorHeight
            val width: Dp by animateDpAsState(
                if (selected) activeIndicatorWidth else inActiveIndicatorWidth
            )
            Canvas(
                modifier = Modifier
                    .size(width, height),
                onDraw = {
                    drawRoundRect(
                        color = if (selected) activeColor else inactiveColor,
                        cornerRadius = CornerRadius(cornerRadius.toPx()),
                        size = Size(width.toPx(), height.toPx())
                    )
                }
            )
        }
    }
}