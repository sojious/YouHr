package co.youverify.youhr.presentation.ui.onboarding


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.youverify.youhr.domain.repository.PreferencesRepository
import co.youverify.youhr.presentation.*
import co.youverify.youhr.presentation.ui.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreferencesViewModel @Inject constructor(
    private val navigator: Navigator,
    val preferencesRepository: PreferencesRepository
) : ViewModel(){

    var isFirstRun=false
    var userHasCode=false
    var userEmail=""


    suspend fun navigateToAppropriateScreen() {

        //collect the first emission from each flow in parallel
            val job1 =viewModelScope.launch {
                isFirstRun=preferencesRepository.getAppFirstRunStatus().first()
            }

        val job2=viewModelScope.launch {
                userHasCode=preferencesRepository.getUserPasscodeCreationStatus().first()
            }
        val job3=viewModelScope.launch {
                userEmail=preferencesRepository.getUserEmail().first()
            }

        //wait for all flows to complete collection
        joinAll(job1,job2,job3)

        if (isFirstRun){
            preferencesRepository.setFirstRun(isFirstRun=false)
            //navigator.navigateSingleTopPopToInclusive(navController, OnBoardingPager.route, Splash.route)
            navigator.navigatePopToInclusive(OnBoardingPager.route,Splash.route)
        }


        else if (userEmail.isNotEmpty() && userHasCode) navigator.navigatePopToInclusive(LoginWithCode.route, Splash.route)

        else if (userEmail.isNotEmpty() && !userHasCode) navigator.navigatePopToInclusive(CreateCode.route, Splash.route)

        //it isn't first run and userEmail is empty
        else   navigator.navigatePopToInclusive( InputEmail.route, Splash.route)

    }
}
