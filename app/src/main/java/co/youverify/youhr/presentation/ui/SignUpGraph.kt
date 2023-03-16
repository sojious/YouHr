package co.youverify.youhr.presentation.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.Density
import androidx.navigation.*
import co.youverify.youhr.core.util.navigateSingleTopPopToInclusive
import co.youverify.youhr.presentation.CreateCode
import co.youverify.youhr.presentation.CreatePassword
import co.youverify.youhr.presentation.SignUpGraph
import co.youverify.youhr.presentation.ui.components.CodeInputFieldState
import co.youverify.youhr.presentation.ui.signup.*
import co.youverify.youhr.presentation.ui.theme.codeInputUnfocused
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

        //Create Code Screen
        composable(route=CreateCode.route){


            //List containing the remembered state holder class  for each codeInputField



            val focusManager= LocalFocusManager.current

            val focusDirection=if (createCodeViewModel.focusDirection==FocusDirection.FORWARD) androidx.compose.ui.focus.FocusDirection.Next else androidx.compose.ui.focus.FocusDirection.Previous

            LaunchedEffect(key1 = createCodeViewModel.moveFocus){
                if (createCodeViewModel.moveFocus) focusManager.moveFocus(focusDirection)
            }

            CreateCodeScreen(
                codeValue1 = createCodeViewModel.code1,
                codeValue2 = createCodeViewModel.code2,
                codeValue3 = createCodeViewModel.code3,
                codeValue4 = createCodeViewModel.code4,
                codeValue5 = createCodeViewModel.code5,
                codeValue6 = createCodeViewModel.code6,
                onSkipClicked = {createCodeViewModel.onSkipClicked()},
                onCreateCodeButtonClicked = { createCodeViewModel.createCode() },
                onCodeValueChanged = {newValue,codeIndex->
                    createCodeViewModel.updateCode(newValue,codeIndex)
                }
            )

        }
    }
}


