package co.youverify.youhr.presentation.ui.home


import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.youverify.youhr.core.util.Result
import co.youverify.youhr.domain.model.User
import co.youverify.youhr.domain.use_case.GetUserProfileUseCase
import co.youverify.youhr.presentation.LeaveRequest
import co.youverify.youhr.presentation.Profile
import co.youverify.youhr.presentation.ui.Navigator
import co.youverify.youhr.presentation.ui.UiEvent
import co.youverify.youhr.presentation.ui.leave.LeaveManagementViewModel
import co.youverify.youhr.presentation.ui.settings.SettingsViewModel
import co.youverify.youhr.presentation.ui.settings.profile.ProfileViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val navigator: Navigator,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    ) : ViewModel(){


    var  user: User? by mutableStateOf(null)
    private set
    var hideBottomNavBar= mutableStateOf(false)
    private set
    var activeSideNavItem by mutableStateOf(0)
        private set

    @OptIn(ExperimentalPagerApi::class)
    val pagerState = PagerState(currentPage = 0)

    var drawerState = DrawerState(initialValue = DrawerValue.Closed)
        private set

    private var _shouldUpdateDrawerState = MutableStateFlow(false)
    var shouldUpdateDrawerState = _shouldUpdateDrawerState.asStateFlow()
        private set

    private val _uiStateFlow = MutableStateFlow(HomePageUiState())
    val uiStateFlow = _uiStateFlow.asStateFlow()

    private val _uiEventFlow = MutableSharedFlow<UiEvent>()
    val uiEventFlow = _uiEventFlow.asSharedFlow()




    fun onBottomNavItemClicked(route: String) { navigator.navigatePopToForBottomNavItem(toRoute =route ) }
    fun updateActiveSideNavItem(newIndex: Int) {
        activeSideNavItem=newIndex
        hideBottomNavBar.value = newIndex!=0

    }
    fun onNotificationClicked(count: String) {}
    @OptIn(ExperimentalPagerApi::class)
    fun scrollToPage(pageIndex: Int) {
        viewModelScope.launch {
            pagerState.animateScrollToPage(pageIndex)
        }
    }
    fun updateDrawerState() {
        if (!_shouldUpdateDrawerState.value) _shouldUpdateDrawerState.value=true
    }

    fun onQuickAccessItemClicked(index: Int) {
        when(index){
            0->{}
            1->{}
            else->{navigator.navigate(LeaveRequest.route)}
        }
    }

    fun getUserProfile(
        context: Context,
        settingsViewModel: SettingsViewModel,
        leaveManagementViewModel: LeaveManagementViewModel
    ) {

        viewModelScope.launch {
            getUserProfileUseCase.invoke(isFirstLogin =false).collect{result->
                if(result is Result.Success){
                   val profileBitmap=BitmapFactory.decodeStream(context.openFileInput("profile_pic"))
                    user=result.data.copy(displayPictureBitmap =profileBitmap )
                    settingsViewModel.updateCurrentUser(user)
                    leaveManagementViewModel.updateUserGender(user?.gender?:"Male")
                }

            }
        }
    }


    fun updateUserProfile(context: Context, profileViewModel: ProfileViewModel) {

        var processSuccessful=false
        viewModelScope.launch {

            // Get the updated user profile from a network call
            getUserProfileUseCase.invoke(isFirstLogin =true).collect{result->
                if(result is Result.Success){
                    //Load the profile pic from the url and cache it into the app internal storage directory
                    Glide.with(context)
                        .asBitmap()
                        .load(result.data.displayPictureUrl)
                        .override(600,200)
                        .into(
                            object : CustomTarget<Bitmap>(){
                                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                    //val imageSize=resource.allocationByteCount
                                    context.openFileOutput("profile_pic",Context.MODE_PRIVATE).use {
                                        resource.compress(Bitmap.CompressFormat.JPEG,100,it)
                                    }

                                    val profileBitmap=BitmapFactory.decodeStream(context.openFileInput("profile_pic"))
                                    user=result.data.copy(displayPictureBitmap =profileBitmap )

                                    //notify profileviemodel that profile update was successful
                                    profileViewModel.setProfileUpdateSuccess(updateSuccessful=true)
                                    cancel()

                                }

                                override fun onLoadCleared(placeholder: Drawable?) {}

                                override fun onLoadFailed(errorDrawable: Drawable?) {
                                    profileViewModel.setProfileUpdateSuccess(updateSuccessful=false)
                                    cancel()
                                }
                            }
                        )
                }

            }
        }

    }

    fun goToProfileScreen() {
        navigator.navigate(toRoute = Profile.route)
    }

    fun onRefreshTabSection() {

    }

}

data class HomePageUiState(val pagerSectionLoading:Boolean=false,val pagerSectionRefreshing:Boolean=false)
