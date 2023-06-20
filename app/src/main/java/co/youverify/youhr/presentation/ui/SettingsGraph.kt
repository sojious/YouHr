package co.youverify.youhr.presentation.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import co.youverify.youhr.presentation.Profile
import co.youverify.youhr.presentation.Settings
import co.youverify.youhr.presentation.SettingsGraph
import co.youverify.youhr.presentation.ui.home.HomeViewModel
import co.youverify.youhr.presentation.ui.settings.SettingsViewModel
import co.youverify.youhr.presentation.ui.settings.SettingsScreen
import co.youverify.youhr.presentation.ui.settings.profile.ProfileScreen
import co.youverify.youhr.presentation.ui.settings.profile.ProfileViewModel
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.SettingsGraph(
    navHostController: NavHostController,
    settingsViewModel: SettingsViewModel,
    homeViewModel: HomeViewModel,
    profileViewModel: ProfileViewModel
){

    navigation(startDestination = Settings.route , route =SettingsGraph.route ){

        composable(route= Settings.route){
            SettingsScreen(
                profileBitmap = settingsViewModel.currentUser?.displayPictureBitmap,
                onSettingsItemClicked = {index->
                    settingsViewModel.onSettingsItemClicked(index)
                },
                onProfilePicClicked = {settingsViewModel.onProfilePicClicked()},
                email = settingsViewModel.currentUser?.email?:"",
                name = settingsViewModel.currentUser?.firstName?:"",
                settingsViewModel = settingsViewModel,
                homeViewModel=homeViewModel

            )
        }


        composable(route= Profile.route){
            ProfileScreen(
                user = profileViewModel.currentUser,
                onBackArrowClicked = {},
                onEditProfileIconClicked = {},
                onEditProfileFieldValueChanged = { _, _->},
                onSaveProfileItemChanges = {},
                onCancelProfileItemChanges = {},
                showSuccessDialog = false,
                settingsViewModel =settingsViewModel,
                profileViewModel=profileViewModel,
                onSaveChangesButtonClicked = { profileViewModel.updateProfile() }
            )
        }
    }
}