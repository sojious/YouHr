package co.youverify.youhr.presentation.ui.components

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import co.youverify.youhr.R
import co.youverify.youhr.presentation.HomePageGraph
import co.youverify.youhr.presentation.BottomNavGraph
import co.youverify.youhr.presentation.SettingsGraph
import co.youverify.youhr.presentation.TaskGraph
import co.youverify.youhr.presentation.ui.home.HomeViewModel
import co.youverify.youhr.presentation.ui.theme.bottomNavBackgroundColor
import co.youverify.youhr.presentation.ui.theme.bottomNavSelectedItemColor
import co.youverify.youhr.presentation.ui.theme.bottomNavUnselectedItemColor


@Composable
fun YouHrBottomNav(
    modifier: Modifier=Modifier,
    homeViewModel: HomeViewModel=hiltViewModel(),
    currentBackStackEntry: NavBackStackEntry?
){


    val currentDestination=currentBackStackEntry?.destination

    val showBottomNav=currentDestination?.route==BottomNavGraph.route || currentDestination?.hierarchy?.any{it.route==BottomNavGraph.route}==true


    if (showBottomNav && !homeViewModel.hideBottomNavBar.value){



        BottomNavigation(
            modifier=modifier,
            backgroundColor = bottomNavBackgroundColor
        ) {


            destinations.forEachIndexed { index, destination ->
                val labelText=when(index){
                    0 ->"Home"
                    1 ->"My Tasks"
                    else->"Settings"
                }

                val iconResId=when(index){
                    0 -> R.drawable.ic_baseline_home
                    1 -> R.drawable.ic_task
                    else-> R.drawable.ic_settings
                }

                val hr=currentDestination?.hierarchy
                //val hierachyList=hr?.toList().toString()
                //val b= hr?.toList()?.get(4).
             // val b=NavGraph().


                val selected=currentDestination?.hierarchy?.any{it.route==destination.route} ==true

                BottomNavigationItem(
                    selected =selected,
                    onClick = {
                        homeViewModel.onBottomNavItemClicked(destination.route)
                    },
                    label ={ Text(text = labelText) },
                    alwaysShowLabel = true,
                    icon = { Icon(painter = painterResource(id = iconResId), contentDescription =null ) },
                    selectedContentColor = bottomNavSelectedItemColor,
                    unselectedContentColor = bottomNavUnselectedItemColor
                )
            }
        }
    }

}

val destinations= listOf(HomePageGraph,TaskGraph, SettingsGraph)