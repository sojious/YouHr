package co.youverify.youhr.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import co.youverify.youhr.presentation.ui.Navigator
import co.youverify.youhr.presentation.ui.theme.YouHrTheme
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var navigator:Navigator
    @OptIn(ExperimentalAnimationApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       // val loginViewModel: LoginViewModel by viewModel()



        setContent {

            val animatedNavController=rememberAnimatedNavController()
            animatedNavController.addOnDestinationChangedListener { controller, destination, arguments ->
                navigator.updateRouteIfOutdated(destination.route!!)
            }

            YouHrTheme {
                YouHrApp(
                    navController = animatedNavController,
                    navigator=navigator
                )
            }
        }
    }
}
