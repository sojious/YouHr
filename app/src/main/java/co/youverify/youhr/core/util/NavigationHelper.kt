package co.youverify.youhr.core.util

import androidx.navigation.NavHostController

fun navigateSingleTop(navController: NavHostController, destinationRoute:String){
    navController.navigate(route=destinationRoute){
        launchSingleTop=true
    }
}

fun navigateSingleTopPopToInclusive(navController: NavHostController, destinationRoute:String, popToInclusiveRoute:String){
    navController.navigate(route=destinationRoute){
        launchSingleTop=true
        popUpTo(route = popToInclusiveRoute){inclusive=true}
    }
}

fun navigateSingleTopPopTo(navController: NavHostController, destinationRoute:String, popToRoute:String){
    navController.navigate(route=destinationRoute){
        launchSingleTop=true
        popUpTo(route = popToRoute){
            inclusive=false
            saveState=true
        }
         restoreState=true
    }
}

