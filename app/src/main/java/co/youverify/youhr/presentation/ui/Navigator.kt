package co.youverify.youhr.presentation.ui


import co.youverify.youhr.presentation.Home
import co.youverify.youhr.presentation.Splash
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Helper class that allow the viewmodels handle navigation with a navController.
 * it wraps the app navigation destination in a MutableStateFlow and allows it to
 * be observed as a state in the App NavHost
 *
 */

class Navigator{

    private val _destinationRoute: MutableStateFlow<String> = MutableStateFlow(Splash.route)
    val destinationRoute: StateFlow<String> = _destinationRoute.asStateFlow()

    var popBackStackType: PopBackStackType=PopBackStackType.POPTOINCLUSIVE
    private set

    var popToRoute: String=Splash.route
        private set



    fun navigate(toRoute: String){

        popBackStackType=PopBackStackType.NONE


        _destinationRoute.value=toRoute

    }

    fun navigatePopToInclusive(toRoute: String, popToRoute:String){

        this.popToRoute=popToRoute
        popBackStackType=PopBackStackType.POPTOINCLUSIVE
        _destinationRoute.value=toRoute

    }

    fun navigatePopTo(toRoute: String, popToRoute:String){

        this.popToRoute=popToRoute
        popBackStackType=PopBackStackType.POPTO
        _destinationRoute.value=toRoute

    }
    fun navigatePopToForBottomNavItem(toRoute:String ){
        this.popToRoute= Home.route
        popBackStackType=PopBackStackType.POPTO
        _destinationRoute.value=toRoute
    }
    fun updateRouteIfOutdated(updatedRoute: String) {
        if (_destinationRoute.value!=updatedRoute) _destinationRoute.value=updatedRoute
    }
}

enum class PopBackStackType{
    POPTO,
    POPTOINCLUSIVE,
    NONE
}