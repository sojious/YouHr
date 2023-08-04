package co.youverify.youhr.presentation.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import co.youverify.youhr.presentation.ChangePasscode
import co.youverify.youhr.presentation.ChangePassword
import co.youverify.youhr.presentation.Profile
import co.youverify.youhr.presentation.Settings
import co.youverify.youhr.presentation.SettingsGraph
import co.youverify.youhr.presentation.ui.home.HomeViewModel
import co.youverify.youhr.presentation.ui.login.InputEmailViewModel
import co.youverify.youhr.presentation.ui.settings.ChangePasscodeScreen
import co.youverify.youhr.presentation.ui.settings.ChangePasswordScreen
import co.youverify.youhr.presentation.ui.settings.SettingsViewModel
import co.youverify.youhr.presentation.ui.settings.SettingsScreen
import co.youverify.youhr.presentation.ui.settings.profile.ProfileScreen
import co.youverify.youhr.presentation.ui.settings.profile.ProfileViewModel
import com.google.accompanist.navigation.animation.composable
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.SettingsGraph(
    navHostController: NavHostController,
    settingsViewModel: SettingsViewModel,
    homeViewModel: HomeViewModel,
    profileViewModel: ProfileViewModel,
    inputEmailViewModel:InputEmailViewModel
){

    navigation(startDestination = Settings.route , route =SettingsGraph.route ){

        composable(route= Settings.route){
            SettingsScreen(
                profileBitmap = settingsViewModel.currentUser?.displayPictureBitmap,
                name = settingsViewModel.currentUser?.firstName?:"",
                email = settingsViewModel.currentUser?.email?:"",
                onSettingsItemClicked = {index->
                    settingsViewModel.onSettingsItemClicked(index, inputEmailViewModel)
                },
                onProfilePicClicked = {settingsViewModel.onProfilePicClicked()},
                settingsViewModel = settingsViewModel,
                homeViewModel=homeViewModel,
                loading = settingsViewModel.settingsScreenLoading

                )
        }


        composable(route= Profile.route){
            val uiState by profileViewModel.uiStateFlow.collectAsState()
            val context= LocalContext.current
            ProfileScreen(
                user = profileViewModel.currentUser,
                onBackArrowClicked = {profileViewModel.onBackArrowClicked()},
                onSaveProfileItemChanges = {fieldType,newValue->
                    profileViewModel.updateProfileField(fieldType,newValue)
                                           },
                settingsViewModel =settingsViewModel,
                profileViewModel=profileViewModel,
                onSaveChangesButtonClicked = { uri->
                    profileViewModel.updateProfile(uri,homeViewModel,profileViewModel,context)
                                             },
                profileFieldsValue = profileViewModel.profileFieldValues,
                uiState = uiState
            )
        }


        composable(route= ChangePassword.route){
            //val cts= rememberCoroutineScope()
            val uiState by settingsViewModel.changePasswordUiStateFlow.collectAsState()
           ChangePasswordScreen(
               //fieldsState = settingsViewModel.changePasswordScreenInputFieldsState,
               //onChangePasswordButtonClicked = { cts.launch { settingsViewModel.changePassword() } },
               uiState = uiState,
               onHideDialogRequest = { settingsViewModel.hideSuccessDialogForPasswordChange() },
               settingsViewModel = settingsViewModel,
               onBackArrowClicked = {settingsViewModel.onBackArrowClicked()}
           )
        }

        composable(route= ChangePasscode.route){
            val cts= rememberCoroutineScope()
            val uiState by settingsViewModel.changePasscodeUiStateFlow.collectAsState()
            ChangePasscodeScreen(
                //fieldsState = settingsViewModel.changePasscodeScreenInputFieldsState,
                //onChangePasscodeButtonClicked = {cts.launch { settingsViewModel.changePasscode() }},
                uiState = uiState,
                onHideDialogRequest = { settingsViewModel.hideSuccessDialogForPasscodeChange() },
                settingsViewModel = settingsViewModel,
                onBackArrowClicked = {settingsViewModel.onBackArrowClicked()}
            )
        }
    }
}