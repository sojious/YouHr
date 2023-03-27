package co.youverify.youhr.presentation.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import co.youverify.youhr.presentation.Settings
import co.youverify.youhr.presentation.SettingsGraph
import co.youverify.youhr.presentation.TaskList
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.SettingsGraph(navHostController: NavHostController){

    navigation(startDestination = Settings.route , route =SettingsGraph.route ){
        composable(route= Settings.route){
            Box(modifier = Modifier.fillMaxSize().background(color= Color.Magenta))
        }
    }
}