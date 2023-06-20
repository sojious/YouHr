package co.youverify.youhr.presentation.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import co.youverify.youhr.presentation.Home
import co.youverify.youhr.presentation.HomePageGraph
import co.youverify.youhr.presentation.ui.home.HomePageScreen
import co.youverify.youhr.presentation.ui.home.HomeViewModel
import co.youverify.youhr.presentation.ui.leave.LeaveManagementViewModel
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class, ExperimentalPagerApi::class)
fun NavGraphBuilder.HomePageGraph(
    homePageViewModel: HomeViewModel,
    leaveManagementViewModel: LeaveManagementViewModel,
    pagerState: PagerState,
    drawerState: DrawerState,
){

    navigation(startDestination = Home.route , route =HomePageGraph.route ){
        composable(route= Home.route){
            val leaveManagementUiState by leaveManagementViewModel.uIStateFlow.collectAsState()
            val coroutineScope = rememberCoroutineScope()

            HomePageScreen(
                //userName =homePageViewModel.userName ,
                notificationCount ="5" ,
                profilePhotoBitmap =homePageViewModel.user?.displayPictureBitmap ,
                onNotificationIconClicked = {},
                onHamburgerClicked = {
                    coroutineScope.launch {
                    if (drawerState.isClosed) drawerState.open() else drawerState.close()
                }
                                     },
                pagerState = pagerState,
                onTabItemClicked = { tabIndex->
                    coroutineScope.launch {
                    pagerState.animateScrollToPage(tabIndex)
                }
                                   },
                onSideNavItemClicked ={
                    homePageViewModel.updateActiveSideNavItem(it)

                } ,
                activeSideNavItemIndex =homePageViewModel.activeSideNavItem ,
                homeDrawerState =drawerState,
                onQuickAccessItemClicked = {homePageViewModel.onQuickAccessItemClicked(it)},
                leaveManagementViewModel =leaveManagementViewModel,
                homeViewModel =homePageViewModel,
                userName = homePageViewModel.user?.firstName?:""
            )
        }
    }
}