package co.youverify.youhr.presentation.ui

import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import co.youverify.youhr.presentation.HomePageGraph
import co.youverify.youhr.presentation.BottomNavGraph
import co.youverify.youhr.presentation.ui.home.HomeViewModel
import co.youverify.youhr.presentation.ui.task.TaskViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState


@OptIn( ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
fun NavGraphBuilder.BottomNavGraph(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    taskViewModel: TaskViewModel,
    pagerState: PagerState,
    drawerState: DrawerState
){

   navigation(startDestination = HomePageGraph.route,route=BottomNavGraph.route){
       HomePageGraph(homeViewModel, pagerState = pagerState,drawerState=drawerState)
       TaskGraph(navController,taskViewModel)
       SettingsGraph(navController)
   }
}