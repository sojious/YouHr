package co.youverify.youhr.presentation.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import co.youverify.youhr.core.util.navigateSingleTop
import co.youverify.youhr.core.util.navigateSingleTopPopTo
import co.youverify.youhr.core.util.navigateSingleTopPopToInclusive
import co.youverify.youhr.domain.repository.PreferencesRepository
import co.youverify.youhr.presentation.*
import co.youverify.youhr.presentation.ui.home.HomeViewModel
import co.youverify.youhr.presentation.ui.login.LoginWithCodeViewModel
import co.youverify.youhr.presentation.ui.login.InputEmailViewModel
import co.youverify.youhr.presentation.ui.login.LoginWithPassWordViewModel
import co.youverify.youhr.presentation.ui.login.ResetPassWordViewModel
import co.youverify.youhr.presentation.ui.onboarding.SplashScreen
import co.youverify.youhr.presentation.ui.login.CreateCodeViewModel
import co.youverify.youhr.presentation.ui.onboarding.PreferencesViewModel
import co.youverify.youhr.presentation.ui.signup.CreatePasswordViewModel
import co.youverify.youhr.presentation.ui.task.TaskDetailViewModel
import co.youverify.youhr.presentation.ui.task.TaskViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState

@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class,
    ExperimentalMaterial3Api::class
)
@Composable
fun YouHrNavHost(
    modifier: Modifier,
    navController: NavHostController,
    navigator: Navigator,
    preferencesRepository: PreferencesRepository,

){


    //initialize viewmodels
    val createCodeViewModel: CreateCodeViewModel = hiltViewModel()
    val createPasswordViewModel: CreatePasswordViewModel = hiltViewModel()
    val loginWithCodeViewModel: LoginWithCodeViewModel = hiltViewModel()
    val inputEmailViewModel: InputEmailViewModel = hiltViewModel()
    val loginWithPasswordViewModel: LoginWithPassWordViewModel = hiltViewModel()
    val resetPasswordViewModel: ResetPassWordViewModel = hiltViewModel()
    val preferencesViewModel:PreferencesViewModel= hiltViewModel()
    val homeViewModel: HomeViewModel = hiltViewModel()
    val taskViewModel:TaskViewModel = hiltViewModel()
    val taskDetailViewModel:TaskDetailViewModel= hiltViewModel()

    val density= LocalDensity.current
    val pagerState = rememberPagerState()
    val drawerState= rememberDrawerState(initialValue = DrawerValue.Closed)



    //Observe changes in the navigation Route
    val route by navigator.destinationRoute.collectAsState()

    //Launch effect that launches everytime navigation route changes
    LaunchedEffect(key1 =route){


        //only navigate if navController's current destination is not the same as route


            // if the back arrow button was not pressed
            when (route) {
                CHECK_FOR_DESTINATION_ON_BACK_STACK -> destinationOnBackStack(navController,navigator)
                navigator.NAVIGATE_UP -> navController.popBackStack()
                else -> {
                    when(navigator.popBackStackType){
                        PopBackStackType.NONE -> navigateSingleTop(navController=navController, destinationRoute = route)
                        PopBackStackType.POPTO -> navigateSingleTopPopTo(navController=navController, destinationRoute = route, popToRoute = navigator.popToRoute)
                        PopBackStackType.POPTOINCLUSIVE -> navigateSingleTopPopToInclusive(navController=navController, destinationRoute = route, popToInclusiveRoute = navigator.popToRoute)
                    }
                }
            }


    }

    AnimatedNavHost(
        modifier=modifier,
        navController =navController ,
        startDestination ="splash"
    ){



        composable(route =Splash.route,){
            SplashScreen (prefViewModel=preferencesViewModel)
        }

        onboardingGraph( navController,density)
        signUpGraph(navController,density, createPasswordViewModel,createCodeViewModel)
        loginGraph(
            loginWithCodeViewModel,
            inputEmailViewModel,
            loginWithPasswordViewModel,
            resetPasswordViewModel,
            createCodeViewModel
        )
        BottomNavGraph(
            navController =navController,
           homeViewModel = homeViewModel,
            taskViewModel=taskViewModel,
            pagerState= pagerState,
            drawerState = drawerState,
            taskDetailViewModel = taskDetailViewModel
        )

    }
}











fun destinationOnBackStack(navController: NavHostController, navigator: Navigator){

    var destinationRoute=""

    try {
        destinationRoute = navController.getBackStackEntry(navigator.routeToCheckFor).destination.route!!
    }catch (exception:IllegalArgumentException){
        exception.printStackTrace()
    }

    navigator.destinationIsOnBackStack=destinationRoute.isNotEmpty()

}


