package co.youverify.youhr.presentation.ui


import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.*
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import co.youverify.youhr.presentation.*
import co.youverify.youhr.presentation.ui.login.*
import co.youverify.youhr.presentation.ui.login.CreateCodeScreen
import co.youverify.youhr.presentation.ui.login.CreateCodeViewModel

import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.loginGraph(
    loginWithCodeViewModel: LoginWithCodeViewModel,
    inputEmailViewModel: InputEmailViewModel,
    loginWithPassWordViewModel: LoginWithPassWordViewModel,
    resetPassWordViewModel: ResetPassWordViewModel,
    createCodeViewModel: CreateCodeViewModel,
    confirmCodeViewModel: ConfirmCodeViewModel
){
    navigation(startDestination =InputEmail.route , route =LoginGraph.route ){



        //Create Password Screen
        composable(
            route = InputEmail.route,
            enterTransition = { slideInHorizontally() }
        ){





            InputEmailScreen(
                emailValue =inputEmailViewModel.userEmail,
                onEmailValueChanged ={newValue->
                  inputEmailViewModel.updateUserEmail(newValue)
                },

                onNextButtonClicked = { inputEmailViewModel.onNextButtonClicked() },

                isErrorValue = inputEmailViewModel.isErrorValue,
                errorMessage = inputEmailViewModel.errorMessage,
                onBackArrowClicked ={inputEmailViewModel.navigateBack()}
            )


        }

        //Create Code Screen
        composable(route=LoginWithPassword.route){navBackStackEntry->

            //set the email from the LoginWithEmail screen
            loginWithPassWordViewModel.userEmail= inputEmailViewModel.userEmail

            val uiState by loginWithPassWordViewModel.uIStatFlow.collectAsState()





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
                uiState = uiState,
                onBackArrowClicked = {loginWithPassWordViewModel.navigateBack()}
            )
        }
        
        
        composable(route=LoginWithCode.route){


            val focusManager= LocalFocusManager.current
            val uiState by loginWithCodeViewModel.uIStatFlow.collectAsState()


            LoginWithCodeScreen(
                codeValue1 = loginWithCodeViewModel.code1,
                codeValue2 = loginWithCodeViewModel.code2,
                codeValue3 = loginWithCodeViewModel.code3,
                codeValue4 = loginWithCodeViewModel.code4,
                codeValue5 = loginWithCodeViewModel.code5,
                codeValue6 = loginWithCodeViewModel.code6,

                onCodeValueChanged = {newValue, index->
                             loginWithCodeViewModel.updateCode(newValue,index)

                    if (index in 2..5){
                        if (newValue.length==1) focusManager.moveFocus(FocusDirection.Next)
                        if (newValue.isEmpty()) focusManager.moveFocus(FocusDirection.Previous)
                    }

                    if(index==1 && newValue.isEmpty()) return@LoginWithCodeScreen
                    if(index==1 && newValue.isNotEmpty()) focusManager.moveFocus(FocusDirection.Next)

                    if (index==6 && loginWithCodeViewModel.code6.isNotEmpty()){
                        focusManager.clearFocus()
                        loginWithCodeViewModel.logUserIn()
                    }
                    if (index==6 && loginWithCodeViewModel.code6.isEmpty()) focusManager.moveFocus(FocusDirection.Previous)


                },
                onPasswordLoginOptionClicked = { loginWithCodeViewModel.onPasswordLoginOptionClicked() },
                //onLoginButtonClicked = {loginWithCodeViewModel.logUserIn()},
                isErrorCode = loginWithCodeViewModel.isErrorCode,
                uiState = uiState

            )
        }


        //Create Code Screen
        composable(
            route=CreateCode.route,
            enterTransition = { expandIn()}
        ){

            val focusManager= LocalFocusManager.current
            val uiState by createCodeViewModel.uIStatFlow.collectAsState()


            CreateCodeScreen(
                codeValue1 = createCodeViewModel.code1,
                codeValue2 = createCodeViewModel.code2,
                codeValue3 = createCodeViewModel.code3,
                codeValue4 = createCodeViewModel.code4,
                codeValue5 = createCodeViewModel.code5,
                codeValue6 = createCodeViewModel.code6,
                isErrorCode = createCodeViewModel.isErrorCode,
                uiState =uiState,

                onNextButtonClicked = { createCodeViewModel.goToConfirmCodeScreen() },
                onCodeValueChanged = {newValue,codeIndex->

                    createCodeViewModel.updateCode(newValue,codeIndex)

                    if (codeIndex in 2..5){
                        if (newValue.length==1) focusManager.moveFocus(FocusDirection.Next)
                        if (newValue.isEmpty()) focusManager.moveFocus(FocusDirection.Previous)
                    }

                    if(codeIndex==1 && newValue.isEmpty()) return@CreateCodeScreen
                    if(codeIndex==1 && newValue.isNotEmpty()) focusManager.moveFocus(FocusDirection.Next)

                    if (codeIndex==6 && createCodeViewModel.code6.isNotEmpty()) focusManager.clearFocus()
                    if (codeIndex==6 && createCodeViewModel.code6.isEmpty()) focusManager.moveFocus(FocusDirection.Previous)

                },
            )

        }


        //Create Code Screen
        composable(
            route=ConfirmCode.route,
            enterTransition = { expandIn()}
        ){

            val focusManager= LocalFocusManager.current
            val uiState by createCodeViewModel.uIStatFlow.collectAsState()


            ConfirmCodeScreen(
                codeValue1 = confirmCodeViewModel.code1,
                codeValue2 = confirmCodeViewModel.code2,
                codeValue3 = confirmCodeViewModel.code3,
                codeValue4 = confirmCodeViewModel.code4,
                codeValue5 = confirmCodeViewModel.code5,
                codeValue6 = confirmCodeViewModel.code6,
                isErrorCode =confirmCodeViewModel.isErrorCode,
                uiState =uiState,

                onCreateCodeButtonClicked = { confirmCodeViewModel.createCode(createCodeViewModel) },
                onCodeValueChanged = {newValue,codeIndex->

                    confirmCodeViewModel.updateCode(newValue,codeIndex)

                    if (codeIndex in 2..5){
                        if (newValue.length==1) focusManager.moveFocus(FocusDirection.Next)
                        if (newValue.isEmpty()) focusManager.moveFocus(FocusDirection.Previous)
                    }

                    if(codeIndex==1 && newValue.isEmpty()) return@ConfirmCodeScreen
                    if(codeIndex==1 && newValue.isNotEmpty()) focusManager.moveFocus(FocusDirection.Next)

                    if (codeIndex==6 && confirmCodeViewModel.code6.isNotEmpty()) focusManager.clearFocus()
                    if (codeIndex==6 && confirmCodeViewModel.code6.isEmpty()) focusManager.moveFocus(FocusDirection.Previous)


                },
                showSuccessDialog = confirmCodeViewModel.showSuccessDialog,
                onProceedButtonClicked = {confirmCodeViewModel.onProceedButtonClicked()}
            )

        }

        composable(route = CodeCreationSuccess.route){

            CodeCreationSuccessScreen(
                onLoginRedirectButtonClicked = {confirmCodeViewModel.onLoginRedirectClicked()}
            )
        }
        
        
        composable(route = ResetPassword.route){

            val uiState by resetPassWordViewModel.uIStatFlow.collectAsState()
            
            ResetPasswordScreen(
                emailValue =resetPassWordViewModel.userEmail,
                onEmailValueChanged ={newValue->
                    resetPassWordViewModel.updateUserEmail(newValue)
                },
                onResetPasswordButtonClicked ={resetPassWordViewModel.resetPassword()},
                isErrorValue = resetPassWordViewModel.isErrorValue,
                onBackArrowClicked = {resetPassWordViewModel.navigateBack()},
                uiState=uiState
            )
        }
    }
    
    
    composable(route = RecoveryEmailSent.route){

        RecoveryEmailSentScreen(
            onContactSupportClicked = {},
            onGotItButtonClicked = {
                resetPassWordViewModel.onGotItClicked()
            }
        )
    }


}