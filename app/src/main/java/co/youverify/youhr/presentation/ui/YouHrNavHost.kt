package co.youverify.youhr.presentation.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import co.youverify.youhr.core.util.navigateSingleTop
import co.youverify.youhr.core.util.navigateSingleTopPopTo
import co.youverify.youhr.core.util.navigateSingleTopPopToInclusive
import co.youverify.youhr.presentation.Splash
import co.youverify.youhr.presentation.ui.login.LoginWithCodeViewModel
import co.youverify.youhr.presentation.ui.login.LoginWithEmailViewModel
import co.youverify.youhr.presentation.ui.login.LoginWithPassWordViewModel
import co.youverify.youhr.presentation.ui.onboarding.SplashScreen
import co.youverify.youhr.presentation.ui.signup.CreateCodeViewModel
import co.youverify.youhr.presentation.ui.signup.CreatePasswordViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun YouHrNavHost(
    modifier: Modifier,
    navController: NavHostController,
    navigator: Navigator,
    createCodeViewModel: CreateCodeViewModel = hiltViewModel(),
    createPasswordViewModel: CreatePasswordViewModel = hiltViewModel(),
    loginWithCodeViewModel: LoginWithCodeViewModel = hiltViewModel(),
    loginWithEmailViewModel: LoginWithEmailViewModel = hiltViewModel(),
    loginWithPasswordViewModel: LoginWithPassWordViewModel = hiltViewModel(),

){

    val density= LocalDensity.current



    //Observe changes in the navigation Route
    val route by navigator.destinationRoute.collectAsState()

    //Launch effect that launches everytime navigation route changes
    LaunchedEffect(key1 =route){

        //only navigate if navController's current destination is not the same as route
        if (route!= navController.currentDestination?.route!!){

            when(navigator.popBackStackType){
                PopBackStackType.NONE -> navigateSingleTop(navController=navController, destinationRoute = route)
                PopBackStackType.POPTO -> navigateSingleTopPopTo(navController=navController, destinationRoute = route, popToRoute = navigator.popToRoute)
                PopBackStackType.POPTOINCLUSIVE -> navigateSingleTopPopToInclusive(navController=navController, destinationRoute = route, popToInclusiveRoute = navigator.popToRoute)
            }
        }


    }

    AnimatedNavHost(
        modifier=modifier,
        navController =navController ,
        startDestination ="splash"
    ){


        composable(route =Splash.route,){
            SplashScreen {
                navController.navigate("onboarding") {
                    launchSingleTop=true
                    popUpTo("splash"){inclusive=true}
                }
            }
        }

        onboardingGraph( navController,density)
        signUpGraph(navController,density, createPasswordViewModel,createCodeViewModel)
        loginGraph(loginWithCodeViewModel,loginWithEmailViewModel,loginWithPasswordViewModel)
    }
}