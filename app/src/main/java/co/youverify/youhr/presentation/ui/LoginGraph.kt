package co.youverify.youhr.presentation.ui


import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalFocusManager

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import co.youverify.youhr.presentation.*
import co.youverify.youhr.presentation.ui.login.*
import co.youverify.youhr.presentation.ui.signup.FocusDirection

import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.loginGraph(
    loginWithCodeViewModel: LoginWithCodeViewModel,
    loginWithEmailViewModel: LoginWithEmailViewModel,
    loginWithPassWordViewModel: LoginWithPassWordViewModel
){
    navigation(startDestination =LoginWithEmail.route , route =LoginGraph.route ){



        //Create Password Screen
        composable(
            route = LoginWithEmail.route,
            enterTransition = { slideInHorizontally() }
        ){





            LoginWithEmailScreen(
                emailValue =loginWithEmailViewModel.userEmail,
                onEmailValueChanged ={newValue->
                  loginWithEmailViewModel.updateUserEmail(newValue)
                } ,

                onNextButtonClicked = { loginWithEmailViewModel.onNextButtonClicked() },

                onSignUpClicked = { loginWithEmailViewModel.onSignUpButtonClicked() }
            )


        }

        //Create Code Screen
        composable(route=LoginWithPassword.route){




            LoginWithPasswordScreen(
                passwordValue =loginWithPassWordViewModel.userPassword,
                isErrorPassword =loginWithPassWordViewModel.isErrorPassword ,
                hidePassword =loginWithPassWordViewModel.hideUserPassword,
                onPasswordValueChanged ={newValue->
                    loginWithPassWordViewModel.updateUserPassword(newValue)
                } ,
                onLoginButtonClicked = {loginWithPassWordViewModel.logUserIn()},
                onHidePasswordIconClicked = {loginWithPassWordViewModel.togglePasswordVisibility()},
                onForgotPasswordClicked = { loginWithPassWordViewModel.onForgetPasswordClicked() }
            )
        }
        
        
        composable(route=LoginWithCode.route){


            val focusManager= LocalFocusManager.current


            val focusDirection=if (loginWithCodeViewModel.focusDirection== FocusDirection.FORWARD) androidx.compose.ui.focus.FocusDirection.Next else androidx.compose.ui.focus.FocusDirection.Previous

            LaunchedEffect(key1 = loginWithCodeViewModel.moveFocus){
                if (loginWithCodeViewModel.moveFocus) focusManager.moveFocus(focusDirection)
            }

            LoginWithCodeScreen(
                codeValue1 = loginWithCodeViewModel.code1,
                codeValue2 = loginWithCodeViewModel.code2,
                codeValue3 = loginWithCodeViewModel.code3,
                codeValue4 = loginWithCodeViewModel.code4,
                codeValue5 = loginWithCodeViewModel.code5,
                codeValue6 = loginWithCodeViewModel.code6,
                onCodeValueChanged = {newValue, index->
                             loginWithCodeViewModel.updateCode(newValue,index)
                },
                onPasswordLoginOptionClicked = { loginWithCodeViewModel.onPasswordLoginOptionClicked() },
                onLoginButtonClicked = {loginWithCodeViewModel.logUserIn()},
                onSignUpClicked = { loginWithCodeViewModel.onSignUpOptionClicked() }
            )
        }
    }
}