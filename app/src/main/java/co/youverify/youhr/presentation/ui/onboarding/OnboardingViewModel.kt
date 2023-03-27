package co.youverify.youhr.presentation.ui.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor( private val stateHandle: SavedStateHandle) : ViewModel(){

    //variable to change the viewpager current page and triger scroll animation to the specified page
    var currentPage = 0
    var onBoardingTourCompleted by mutableStateOf(false)



}
