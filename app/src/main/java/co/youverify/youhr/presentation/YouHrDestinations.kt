package co.youverify.youhr.presentation

import androidx.navigation.NavType
import androidx.navigation.navArgument

/**
 * Interface whose implementations holds navigation specific information
 * for each destination in the app's navigation graph
 */


interface YouHrDestination{
    val route:String
}

object LoginGraph:YouHrDestination{
    override val route="login"
}

object OnBoardingGraph:YouHrDestination{
    override val route="onboarding"
}

object SignUpGraph:YouHrDestination{
    override val route="signUp"
}

object Splash:YouHrDestination{
    override val route="splash"
}

object Blank:YouHrDestination{
    override val route="blank"
}

object OnBoardingPager:YouHrDestination{
    override val route="pager"
}

object CreatePassword:YouHrDestination{

    override val route="createPassword"
    const val resetPasswordArg="resetPassword"
    const val routeWithArgs="createPassword?$resetPasswordArg={$resetPasswordArg}"
    val args= listOf(
        navArgument(name = resetPasswordArg){
            type= NavType.BoolType
            defaultValue=false
        }
    )
}

object CreateCode:YouHrDestination{
    override val route="createCode"
}

object LoginWithEmail:YouHrDestination{
    override val route="loginWithEmail"
}

object LoginWithPassword:YouHrDestination{
    override val route="loginWithPassword"
}

object LoginWithCode:YouHrDestination{
    override val route="loginWithCode"
}


