package co.youverify.youhr.presentation.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import co.youverify.youhr.presentation.Profile
import co.youverify.youhr.presentation.Settings
import co.youverify.youhr.presentation.SettingsGraph
import co.youverify.youhr.presentation.ui.settings.SettingsViewModel
import co.youverify.youhr.presentation.ui.settings.SettingsScreen
import co.youverify.youhr.presentation.ui.settings.profile.ProfileScreen
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.SettingsGraph(navHostController: NavHostController, settingsViewModel: SettingsViewModel){

    navigation(startDestination = Settings.route , route =SettingsGraph.route ){

        composable(route= Settings.route){
            SettingsScreen(
                user = settingsViewModel.currentUser,
                onSettingsItemClicked = {index->
                    settingsViewModel.onSettingsItemClicked(index)
                },
                onProfilePicClicked = {settingsViewModel.onProfilePicClicked()}
            )
        }


        composable(route= Profile.route){
            ProfileScreen(
                user = settingsViewModel.currentUser,
                onCancelProfileItemChanges = {},
                onSaveProfileItemChanges = {},
                onBackArrowClicked = {},
                onEditProfileIconClicked = {},
                onEditProfileFieldValueChanged = {_,_->},
                showSuccessDialog = false
            )
        }
    }
}