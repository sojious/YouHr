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


object BottomNavGraph:YouHrDestination{
    override val route="bottomNavGraph"
}
object LoginGraph:YouHrDestination{
    override val route="login"
}
object OnBoardingGraph:YouHrDestination{
    override val route="onboarding"
}
object Splash:YouHrDestination{
    override val route="splash"
}
object CodeCreationSuccess:YouHrDestination{
    override val route="codeCreationSuccess"
}
object Blank:YouHrDestination{
    override val route="blank"
}
object OnBoardingPager:YouHrDestination{
    override val route="pager"
}
object HomePageGraph:YouHrDestination{
    override val route="homePageGraph"
}
object TaskGraph:YouHrDestination{
    override val route="taskGraph"
}
object SettingsGraph:YouHrDestination{
    override val route="settingsGraph"
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

object InputEmail:YouHrDestination{
    override val route="inputEmail"
}
object LoginWithPassword:YouHrDestination{
    override val route="loginWithPassword"
}
object RecoveryEmailSent:YouHrDestination{
    override val route="recoveryEmailSent"
}
object ResetPassword:YouHrDestination{
    override val route="resetPassword"
}
object LoginWithCode:YouHrDestination{
    override val route="loginWithCode"
}
object TaskList:YouHrDestination{
    override val route="taskList"
}
object TaskDetail:YouHrDestination{
    override val route="taskDetail"
    val taskIdArg="taskId"
    val routWithArgs="$route/{$taskIdArg}"
    val args = listOf(
        navArgument(name = taskIdArg){
            type= NavType.IntType
        }
    )
}
object Settings:YouHrDestination{
    override val route="settings"
}
object Home:YouHrDestination{
    override val route="home"
}
object ConfirmCode:YouHrDestination{
    override val route="confirmCode"
}

object Profile:YouHrDestination{
    override val route="profile"
}



