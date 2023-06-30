package co.youverify.youhr.presentation.ui


import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.expandIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.runtime.*
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager

import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import co.youverify.youhr.presentation.*
import co.youverify.youhr.presentation.ui.login.*
import co.youverify.youhr.presentation.ui.login.CreateCodeScreen
import co.youverify.youhr.presentation.ui.login.CreateCodeViewModel
import co.youverify.youhr.presentation.ui.settings.SettingsViewModel

import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.loginGraph(
    loginWithCodeViewModel: LoginWithCodeViewModel,
    inputEmailViewModel: InputEmailViewModel,
    loginWithPassWordViewModel: LoginWithPassWordViewModel,
    resetPassWordViewModel: ResetPassWordViewModel,
    createCodeViewModel: CreateCodeViewModel,
    confirmCodeViewModel: ConfirmCodeViewModel,
    settingsViewModel: SettingsViewModel

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
                onLoginButtonClicked = {loginWithPassWordViewModel.logUserIn(settingsViewModel)},
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
                isErrorCode = loginWithCodeViewModel.isErrorCode,
                activeCodeInputFieldIndex = loginWithCodeViewModel.activeCodeInputFieldIndex,
                uiState = uiState,
                onCodeValueChanged = {newValue, codeIndex->


                    loginWithCodeViewModel.updateCode(newValue,codeIndex)

                    if (codeIndex in 2..5){
                        if (newValue.length==1) {
                            loginWithCodeViewModel.updateActiveCodeInputFieldIndex(codeIndex+1)
                            //focusManager.moveFocus(FocusDirection.Next)
                        }
                        if (newValue.isEmpty()){
                            loginWithCodeViewModel.updateActiveCodeInputFieldIndex(codeIndex-1)
                            //focusManager.moveFocus(FocusDirection.Previous)
                        }
                    }

                    if(codeIndex==1 && newValue.isEmpty()) return@LoginWithCodeScreen
                    if(codeIndex==1 && newValue.isNotEmpty()) {
                        loginWithCodeViewModel.updateActiveCodeInputFieldIndex(codeIndex+1)
                        //focusManager.moveFocus(FocusDirection.Next)
                    }

                    if (codeIndex==6 && loginWithCodeViewModel.code6.isNotEmpty()) {
                        focusManager.clearFocus()
                        loginWithCodeViewModel.logUserIn()
                    }
                    if (codeIndex==6 && loginWithCodeViewModel.code6.isEmpty()) {
                        loginWithCodeViewModel.updateActiveCodeInputFieldIndex(codeIndex-1)
                        //focusManager.moveFocus(FocusDirection.Previous)
                    }
                },
                onPasswordLoginOptionClicked = { loginWithCodeViewModel.onPasswordLoginOptionClicked() },
                onBackSpaceKeyPressed = {codeInputFieldIndex->
                    loginWithCodeViewModel.onBackSpaceKeyPressed(codeInputFieldIndex)
                }

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
                uiState =uiState,
                isErrorCode = createCodeViewModel.isErrorCode,
                onNextButtonClicked = { createCodeViewModel.goToConfirmCodeScreen() },
                onCodeValueChanged = {newValue,codeIndex->

                    createCodeViewModel.updateCode(newValue,codeIndex)

                    if (codeIndex in 2..5){
                        if (newValue.length==1) {
                            createCodeViewModel.updateActiveCodeInputFieldIndex(codeIndex+1)
                            //focusManager.moveFocus(FocusDirection.Next)

                        }
                        if (newValue.isEmpty()){
                            createCodeViewModel.updateActiveCodeInputFieldIndex(codeIndex-1)
                            //focusManager.moveFocus(FocusDirection.Previous)
                        }
                    }

                    if(codeIndex==1 && newValue.isEmpty()) return@CreateCodeScreen
                    if(codeIndex==1 && newValue.isNotEmpty()) {
                        createCodeViewModel.updateActiveCodeInputFieldIndex(codeIndex+1)
                        //focusManager.moveFocus(FocusDirection.Next)
                    }

                    if (codeIndex==6 && createCodeViewModel.code6.isNotEmpty()) focusManager.clearFocus()
                    if (codeIndex==6 && createCodeViewModel.code6.isEmpty()) {
                        createCodeViewModel.updateActiveCodeInputFieldIndex(codeIndex-1)
                        //focusManager.moveFocus(FocusDirection.Previous)
                    }

                },
                activeCodeInputFieldIndex = createCodeViewModel.activeCodeInputFieldIndex,
                onBackSpaceKeyPressed = {codeInputFieldIndex->
                    createCodeViewModel.onBackSpaceKeyPressed(codeInputFieldIndex)
                                        },
            )

        }

        //Create Code Screen
        composable(
            route=ConfirmCode.route,
            enterTransition = { expandIn()}
        ){
            val focusManager= LocalFocusManager.current
            val context= LocalContext.current.applicationContext
            val uiState by confirmCodeViewModel.uIStatFlow.collectAsState()

            ConfirmCodeScreen(
                codeValue1 = confirmCodeViewModel.code1,
                codeValue2 = confirmCodeViewModel.code2,
                codeValue3 = confirmCodeViewModel.code3,
                codeValue4 = confirmCodeViewModel.code4,
                codeValue5 = confirmCodeViewModel.code5,
                codeValue6 = confirmCodeViewModel.code6,
                uiState =uiState,
                isErrorCode =confirmCodeViewModel.isErrorCode,
                activeCodeInputFieldIndex = confirmCodeViewModel.activeCodeInputFieldIndex,
                showSuccessDialog = confirmCodeViewModel.showSuccessDialog,
                onCreateCodeButtonClicked = { confirmCodeViewModel.createCode(createCodeViewModel, context,settingsViewModel) },
                onProceedButtonClicked = {confirmCodeViewModel.onProceedButtonClicked()},
                onCodeValueChanged = {newValue,codeIndex->
                    confirmCodeViewModel.updateCode(newValue,codeIndex)

                    if (codeIndex in 2..5){
                        if (newValue.length==1) {
                            confirmCodeViewModel.updateActiveCodeInputFieldIndex(codeIndex+1)
                            //focusManager.moveFocus(FocusDirection.Next)

                        }
                        if (newValue.isEmpty()){
                            confirmCodeViewModel.updateActiveCodeInputFieldIndex(codeIndex-1)
                            //focusManager.moveFocus(FocusDirection.Previous)
                        }
                    }

                    if(codeIndex==1 && newValue.isEmpty()) return@ConfirmCodeScreen
                    if(codeIndex==1 && newValue.isNotEmpty()) {
                        confirmCodeViewModel.updateActiveCodeInputFieldIndex(codeIndex+1)
                        //focusManager.moveFocus(FocusDirection.Next)
                    }

                    if (codeIndex==6 && confirmCodeViewModel.code6.isNotEmpty()) focusManager.clearFocus()
                    if (codeIndex==6 && confirmCodeViewModel.code6.isEmpty()) {
                        confirmCodeViewModel.updateActiveCodeInputFieldIndex(codeIndex-1)
                        //focusManager.moveFocus(FocusDirection.Previous)
                    }


                },
                onBackSpaceKeyPressed = {codeInputFieldIndex->
                    confirmCodeViewModel.onBackSpaceKeyPressed(codeInputFieldIndex)
                }
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