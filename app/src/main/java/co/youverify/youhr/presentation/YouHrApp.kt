package co.youverify.youhr.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import co.youverify.youhr.R
import co.youverify.youhr.presentation.ui.Navigator
import co.youverify.youhr.presentation.ui.YouHrNavHost
import co.youverify.youhr.presentation.ui.components.YouHrBottomNav
import co.youverify.youhr.presentation.ui.home.HomeViewModel
import co.youverify.youhr.presentation.ui.theme.bottomNavBackgroundColor
import co.youverify.youhr.presentation.ui.theme.bottomNavSelectedItemColor
import co.youverify.youhr.presentation.ui.theme.bottomNavUnselectedItemColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YouHrApp(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navigator: Navigator,
){

    val currentBackStackEntry by navController.currentBackStackEntryAsState()


    Scaffold(
        modifier=modifier,
        content = {offsetPadding->
            YouHrNavHost(
                modifier = Modifier.padding(offsetPadding),
                navController =navController,
                navigator=navigator
            )
        },

        bottomBar = {
            YouHrBottomNav(currentBackStackEntry=currentBackStackEntry)
        }
    )

}

