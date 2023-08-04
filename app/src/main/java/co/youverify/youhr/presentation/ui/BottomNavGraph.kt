package co.youverify.youhr.presentation.ui

import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import co.youverify.youhr.presentation.HomePageGraph
import co.youverify.youhr.presentation.BottomNavGraph
import co.youverify.youhr.presentation.ui.home.HomeViewModel
import co.youverify.youhr.presentation.ui.leave.LeaveManagementViewModel
import co.youverify.youhr.presentation.ui.login.InputEmailViewModel
import co.youverify.youhr.presentation.ui.settings.SettingsViewModel
import co.youverify.youhr.presentation.ui.settings.profile.ProfileViewModel
import co.youverify.youhr.presentation.ui.task.TaskDetailViewModel
import co.youverify.youhr.presentation.ui.task.TaskViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState


@OptIn( ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
fun NavGraphBuilder.BottomNavGraph(
    navController: NavHostController,
    homeViewModel: HomeViewModel,
    taskViewModel: TaskViewModel,
    taskDetailViewModel: TaskDetailViewModel,
    pagerState: PagerState,
    drawerState: DrawerState,
    settingsViewModel: SettingsViewModel,
    leaveManagementViewModel: LeaveManagementViewModel,
    profileViewModel: ProfileViewModel,
    inputEmailViewModel: InputEmailViewModel
){

   navigation(startDestination = HomePageGraph.route,route=BottomNavGraph.route){
       HomePageGraph(homeViewModel,leaveManagementViewModel,pagerState = pagerState,drawerState=drawerState, settingsViewModel = settingsViewModel, profileViewModel = profileViewModel)
       TaskGraph(taskViewModel=taskViewModel, taskDetailViewModel = taskDetailViewModel)
       SettingsGraph(navController, settingsViewModel = settingsViewModel,homeViewModel=homeViewModel,profileViewModel=profileViewModel,inputEmailViewModel=inputEmailViewModel)
   }
}