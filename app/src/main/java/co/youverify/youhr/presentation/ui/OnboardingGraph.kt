package co.youverify.youhr.presentation.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.navigation
import co.youverify.youhr.R
import co.youverify.youhr.presentation.Blank
import co.youverify.youhr.presentation.OnBoardingGraph
import co.youverify.youhr.presentation.OnBoardingPager
import co.youverify.youhr.presentation.SignUpGraph
import co.youverify.youhr.presentation.ui.onboarding.BlankScreen
import co.youverify.youhr.presentation.ui.onboarding.OnboardingPagerScreen
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState

/**
 * Onboarding graph containing the Blank screen and the Swipable pager
 */

@OptIn(ExperimentalPagerApi::class, ExperimentalAnimationApi::class)
fun NavGraphBuilder.onboardingGraph(navController: NavController, density: Density){

    navigation(startDestination = Blank.route, route = OnBoardingGraph.route){

        composable(
            route=Blank.route,
            exitTransition = { scaleOut(animationSpec = tween(500)) }
        ){


            BlankScreen(
                navigate = {
                    navController.navigate(OnBoardingPager.route){
                        launchSingleTop=true
                        popUpTo("blank"){inclusive=true}
                    }
                }
            )

        }

        composable(
            route=OnBoardingPager.route,
            enterTransition = {
                slideInVertically(animationSpec = tween(1000)) {
                    with(density) { it/3.dp.roundToPx() } }
            }
        ){

            val pagerState= rememberPagerState()

            OnboardingPagerScreen(
                pagerState = pagerState,
                actionButtonText = stringResource(id = R.string.login),
                onLoginButtonClicked = {
                    navController.navigate(SignUpGraph.route){
                        launchSingleTop=true
                        popUpTo(OnBoardingPager.route){inclusive=true}
                    }
                }

            )
        }
    }
}