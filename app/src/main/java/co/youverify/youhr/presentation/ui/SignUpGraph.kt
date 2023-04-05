package co.youverify.youhr.presentation.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.*
import androidx.compose.ui.unit.Density
import androidx.navigation.*
import co.youverify.youhr.presentation.CreatePassword
import co.youverify.youhr.presentation.SignUpGraph
import co.youverify.youhr.presentation.ui.login.CreateCodeViewModel
import co.youverify.youhr.presentation.ui.signup.*
import com.google.accompanist.navigation.animation.composable

/**
 * SignUp graph containing the CreatePassWord screen and the CreateCode screen
 */

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.signUpGraph(
    navController: NavHostController,
    density: Density,
    createPasswordViewModel: CreatePasswordViewModel,
    createCodeViewModel: CreateCodeViewModel
){
    navigation(startDestination =CreatePassword.routeWithArgs , route =SignUpGraph.route ){



            //Create Password Screen
        composable(
            route = CreatePassword.routeWithArgs,
            arguments = CreatePassword.args,
            enterTransition = {
                slideInHorizontally {
                    with(density){ it/3.toDp().roundToPx() }
                }
            }
        ){backStackEntry->

           // val args=backStackEntry.arguments?.getBoolean(CreatePassword.resetPasswordArg)!!



            CreatePasswordScreen(
                email =createPasswordViewModel.email ,
                passwordValue = createPasswordViewModel.password,
                confirmPasswordValue = createPasswordViewModel.confirmPasswordValue,
                resetPassword = createPasswordViewModel.resetPassword,
                lowerCaseCheckboxChecked = createPasswordViewModel.passwordHasLowerCase,
                upperCaseCheckboxChecked = createPasswordViewModel.passwordHasUpperCase,
                oneNumberCheckboxChecked = createPasswordViewModel.passwordHasNumber,
                eightCharactersCheckboxChecked = createPasswordViewModel.passwordIsEightCharacters,
                specialCharacterCheckboxChecked = createPasswordViewModel.passwordHasSpecialCharacter,
                hideTextFieldValue = createPasswordViewModel.hidePassword,
                onPasswordValueChanged = {createPasswordViewModel.updatePassword(it) },
                onConfirmPasswordValueChanged = {createPasswordViewModel.updateConfirmPasswordValue(it)},
                onPasswordVisibilityIconClicked = {createPasswordViewModel.togglePasswordVisibility()},
                onCreatePasswordButtonClicked = {createPasswordViewModel.createPassword()},
                onTermsAndConditionsClicked = {createPasswordViewModel.onTermsAndConditionsClicked()}
            )
        }


    }
}


