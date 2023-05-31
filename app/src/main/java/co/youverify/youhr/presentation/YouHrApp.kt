package co.youverify.youhr.presentation

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import co.youverify.youhr.domain.repository.PreferencesRepository
import co.youverify.youhr.presentation.ui.Navigator
import co.youverify.youhr.presentation.ui.UiEvent
import co.youverify.youhr.presentation.ui.YouHrNavHost
import co.youverify.youhr.presentation.ui.components.YouHrBottomNav
import co.youverify.youhr.presentation.ui.login.LoginWithCodeViewModel
import co.youverify.youhr.presentation.ui.login.LoginWithPassWordViewModel
import co.youverify.youhr.presentation.ui.login.ResetPassWordViewModel
import co.youverify.youhr.presentation.ui.login.CreateCodeViewModel
import co.youverify.youhr.presentation.ui.task.TaskViewModel

@Composable
fun YouHrApp(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navigator: Navigator,
    prefRepo: PreferencesRepository,
){

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val loginWithPassWordViewModel:LoginWithPassWordViewModel= hiltViewModel()
    val loginWithCodeViewModel:LoginWithCodeViewModel= hiltViewModel()
    val createCodeViewModel: CreateCodeViewModel = hiltViewModel()
    val resetPassWordViewModel:ResetPassWordViewModel= hiltViewModel()
    val taskViewModel:TaskViewModel= hiltViewModel()
    val snackBarHostState= remember{ SnackbarHostState() }
    val context= LocalContext.current


    createLaunchEffects(
        loginWithPassWordViewModel,loginWithCodeViewModel,
        createCodeViewModel,resetPassWordViewModel,taskViewModel,snackBarHostState,context
    )



    Scaffold(
        modifier=modifier,
        content = {offsetPadding->
            YouHrNavHost(
                modifier = Modifier.padding(offsetPadding),
                navController =navController,
                navigator=navigator,
                preferencesRepository = prefRepo
            )
        },

        bottomBar = {
            YouHrBottomNav(currentBackStackEntry=currentBackStackEntry)
        },
        snackbarHost = { SnackbarHost(hostState =snackBarHostState ) }
    )

}

@Composable
fun createLaunchEffects(
    loginWithPassWordViewModel: LoginWithPassWordViewModel,
    loginWithCodeViewModel: LoginWithCodeViewModel,
    createCodeViewModel: CreateCodeViewModel,
    resetPassWordViewModel: ResetPassWordViewModel,
    taskViewModel: TaskViewModel,
    snackBarHostState: SnackbarHostState,
    context: Context
) {
    LaunchedEffect(key1 = true){

        loginWithPassWordViewModel.uiEventFlow.collect {event->
            handleEvent(event,snackBarHostState,context)
        }
    }


    LaunchedEffect(key1 = true){

        loginWithCodeViewModel.uiEventFlow.collect {event->
            handleEvent(event,snackBarHostState,context)
        }
    }

    LaunchedEffect(key1 = true){

        createCodeViewModel.uiEventFlow.collect { event->
            handleEvent(event,snackBarHostState,context)
        }
    }

    LaunchedEffect(key1 = true){

        resetPassWordViewModel.uiEventFlow.collect {event->
            handleEvent(event,snackBarHostState,context)
        }
    }

    LaunchedEffect(key1 = true){

        taskViewModel.uiEventFlow.collect {event->
            handleEvent(event,snackBarHostState,context)
        }
    }

}

suspend fun handleEvent(event: UiEvent, snackBarHostState: SnackbarHostState, context: Context) {
    when (event){
        is UiEvent.ShowSnackBar -> snackBarHostState.showSnackbar(message =event.message, duration = SnackbarDuration.Long )
        is UiEvent.ShowToast-> Toast.makeText(context,event.message, Toast.LENGTH_LONG).show()
    }
}



