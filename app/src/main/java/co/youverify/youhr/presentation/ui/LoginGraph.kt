package co.youverify.youhr.presentation.ui


import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.material.Snackbar
import androidx.compose.material.SnackbarHost
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import co.youverify.youhr.presentation.*
import co.youverify.youhr.presentation.ui.login.*
import co.youverify.youhr.presentation.ui.signup.CreateCodeScreen
import co.youverify.youhr.presentation.ui.signup.CreateCodeViewModel

import com.google.accompanist.navigation.animation.composable
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.loginGraph(
    loginWithCodeViewModel: LoginWithCodeViewModel,
    loginWithEmailViewModel: LoginWithEmailViewModel,
    loginWithPassWordViewModel: LoginWithPassWordViewModel,
    resetWithPassWordViewModel: ResetPassWordViewModel,
    createCodeViewModel: CreateCodeViewModel
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

                onLoginWithCodeOptionClicked = { loginWithEmailViewModel.onLoginWithCodeButtonClicked() }
            )


        }

        //Create Code Screen
        composable(
            route=LoginWithPassword.routWithArgs,
            arguments = LoginWithPassword.args
        ){



            val uiState by loginWithPassWordViewModel.uIStatFlow.collectAsState()

            val snackBarHostState=remember{ SnackbarHostState()}

            LaunchedEffect(key1 = true){
                loginWithPassWordViewModel.uiEventFlow.collectLatest {event->
                    if (event is UiEvent.ShowSnackBar)
                        snackBarHostState.showSnackbar(message =event.message, duration = SnackbarDuration.Long )
                }
            }


            LoginWithPasswordScreen(
                passwordValue =loginWithPassWordViewModel.userPassword,
                isErrorPassword =loginWithPassWordViewModel.isErrorPassword ,
                hidePassword =loginWithPassWordViewModel.hideUserPassword,
                onPasswordValueChanged ={newValue->
                    loginWithPassWordViewModel.updateUserPassword(newValue)
                } ,
                onLoginButtonClicked = {loginWithPassWordViewModel.logUserIn()},
                onHidePasswordIconClicked = {loginWithPassWordViewModel.togglePasswordVisibility()},
                onForgotPasswordClicked = { loginWithPassWordViewModel.onForgetPasswordClicked() },
                uiState = uiState
            )
        }
        
        
        composable(route=LoginWithCode.route){


            val focusManager= LocalFocusManager.current


            LoginWithCodeScreen(
                codeValue1 = loginWithCodeViewModel.code1,
                codeValue2 = loginWithCodeViewModel.code2,
                codeValue3 = loginWithCodeViewModel.code3,
                codeValue4 = loginWithCodeViewModel.code4,

                onCodeValueChanged = {newValue, index->
                             loginWithCodeViewModel.updateCode(newValue,index)

                    if (index<4){
                        if (newValue.length==1) focusManager.moveFocus(FocusDirection.Next)
                        if (newValue.isEmpty()) focusManager.moveFocus(FocusDirection.Previous)
                    }

                    if (index==4 && loginWithCodeViewModel.code4.isNotEmpty()) focusManager.clearFocus()
                    if (index==4 && loginWithCodeViewModel.code4.isEmpty()) focusManager.moveFocus(FocusDirection.Previous)


                },
                onPasswordLoginOptionClicked = { loginWithCodeViewModel.onPasswordLoginOptionClicked() },
                onLoginButtonClicked = {loginWithCodeViewModel.logUserIn()},

            )
        }


        //Create Code Screen
        composable(
            route=CreateCode.route,
            enterTransition = { expandIn()}
        ){

            val focusManager= LocalFocusManager.current


            CreateCodeScreen(
                codeValue1 = createCodeViewModel.code1,
                codeValue2 = createCodeViewModel.code2,
                codeValue3 = createCodeViewModel.code3,
                codeValue4 = createCodeViewModel.code4,

                onSkipClicked = {createCodeViewModel.onSkipClicked()},
                onCreateCodeButtonClicked = { createCodeViewModel.createCode() },
                onCodeValueChanged = {newValue,codeIndex->

                    createCodeViewModel.updateCode(newValue,codeIndex)

                    if (codeIndex<4){
                        if (newValue.length==1) focusManager.moveFocus(FocusDirection.Next)
                        if (newValue.isEmpty()) focusManager.moveFocus(FocusDirection.Previous)
                    }

                    if (codeIndex==4 && createCodeViewModel.code4.isNotEmpty()) focusManager.clearFocus()
                    if (codeIndex==4 && createCodeViewModel.code4.isEmpty()) focusManager.moveFocus(FocusDirection.Previous)

                }
            )

        }

        composable(route = CodeCreationSuccess.route){

            CodeCreationSuccessScreen(
                onLoginRedirectButtonClicked = {createCodeViewModel.onLoginRedirectClicked()}
            )
        }
        
        
        composable(route = ResetPassword.route){
            
            ResetPasswordScreen(
                emailValue =resetWithPassWordViewModel.userEmail ,
                onEmailValueChanged ={newValue->
                    resetWithPassWordViewModel.updateUserEmail(newValue)
                },
                onResetPasswordButtonClicked ={resetWithPassWordViewModel.resetPassword()}
            )
        }
    }
    
    
    composable(route = RecoveryEmailSent.route){
        RecoveryEmailSentScreen(
            onContactSupportClicked = {},
            onGotItButtonClicked = {resetWithPassWordViewModel.onGotItClicked()}
        )
    }
}