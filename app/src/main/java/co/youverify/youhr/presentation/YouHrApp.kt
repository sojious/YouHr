package co.youverify.youhr.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import co.youverify.youhr.presentation.ui.Navigator
import co.youverify.youhr.presentation.ui.YouHrNavHost

@Composable
fun YouHrApp(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    navigator: Navigator,
){



    Scaffold(modifier=modifier) {offsetPadding->

        YouHrNavHost(
            modifier = Modifier.padding(offsetPadding),
            navController =navController,
            navigator=navigator
        )
    }

}