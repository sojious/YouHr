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
import androidx.paging.PagingData
import androidx.paging.cachedIn
import co.youverify.youhr.data.model.UserData
import co.youverify.youhr.domain.model.Announcement
import co.youverify.youhr.domain.model.EmployeeOnLeave
import co.youverify.youhr.domain.model.User
import co.youverify.youhr.domain.use_case.GetAllAnnouncementUseCase
import co.youverify.youhr.domain.use_case.GetEmployeesOnLeaveUseCase
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
import kotlinx.coroutines.NonCancellable.cancel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val navigator: Navigator,
    //private val getUserProfileUseCase: GetUserProfileUseCase,
    private val getAllAnnouncementUseCase: GetAllAnnouncementUseCase,
    private val getEmployeesOnLeaveUseCase: GetEmployeesOnLeaveUseCase
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

    var showAnnouncementDetailDialog by mutableStateOf(false)
    private set

    var clickedAnnouncement:Announcement?=null
        private set

    private val _uiStateFlow = MutableStateFlow(HomePageUiState())
    val uiStateFlow = _uiStateFlow.asStateFlow()

    private val _announcementState = MutableStateFlow<PagingData<Announcement>>(PagingData.empty())
    val announcementState = _announcementState.asStateFlow()

    private val _employeesOnLeaveState = MutableStateFlow<PagingData<EmployeeOnLeave>>(PagingData.empty())
    val employeesOnLeaveState = _employeesOnLeaveState.asStateFlow()

    private val _uiEventFlow = MutableSharedFlow<UiEvent>()
    val uiEventFlow = _uiEventFlow.asSharedFlow()


    init {
        //getUserProfile()
        //getAnnouncements()
        //getEmployeesOnLeave()
    }



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

    fun synUser(settingsViewModel: SettingsViewModel, leaveManagementViewModel: LeaveManagementViewModel) {
        settingsViewModel.updateCurrentUser(user)
        leaveManagementViewModel.updateUserGender(user?.gender?:"Male")
    }


    fun updateUserProfile(context: Context, profileViewModel: ProfileViewModel, userData: UserData) {

        //var processSuccessful=false

            // Get the updated user profile from a network call

                    //Load the profile pic from the url and cache it into the app internal storage directory
                    Glide.with(context)
                        .asBitmap()
                        .load(userData.displayPicture)
                        .override(600,200)
                        .into(
                            object : CustomTarget<Bitmap>(){
                                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                    //val imageSize=resource.allocationByteCount
                                    //context.openFileOutput("profile_pic",Context.MODE_PRIVATE).use {
                                        //resource.compress(Bitmap.CompressFormat.JPEG,100,it)
                                    //}

                                    //val profileBitmap=BitmapFactory.decodeStream(context.openFileInput("profile_pic"))
                                   // user=result.data.copy(displayPictureBitmap =profileBitmap )
                                    user=User(
                                        role = userData.role?:"",
                                        jobRole = userData.jobRole?:"",
                                        status = userData.status?:"",
                                        email = userData.email?:"",
                                        firstName = userData.firstName?:"",
                                        lastName = userData.lastName?:"",
                                        middleName = userData.middleName?:"",
                                        phoneNumber = userData.phoneNumber?:"",
                                        password = userData.password?:"",
                                        passcode = userData.passcode?:"",
                                        address = userData.address?:"",
                                        dob = userData.dob?:"",
                                        gender = userData.gender?:"",
                                        nextOfKin = userData.nextofKin?:"",
                                        nextOfKinContact = userData.nextofKinContact?:"",
                                        nextOfKinNumber = userData.nextofKinNumber?:"",
                                        displayPictureUrl = userData.displayPicture?:"",
                                        displayPictureBitmap = resource,
                                        id = userData.id
                                    )
                                    //notify profileviemodel that profile update was successful
                                    profileViewModel.setProfileUpdateSuccess(updateSuccessful=true)


                                }

                                override fun onLoadCleared(placeholder: Drawable?) {}

                                override fun onLoadFailed(errorDrawable: Drawable?) {
                                    profileViewModel.setProfileUpdateSuccess(updateSuccessful=false)

                                }
                            }
                        )





    }

    fun goToProfileScreen() {
        navigator.navigate(toRoute = Profile.route)
    }

    fun onRefreshTabSection() {

    }

    fun getAnnouncements(){
        viewModelScope.launch {
            getAllAnnouncementUseCase.invoke()
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collectLatest{

                    _announcementState.value=it
                }
        }
    }

    fun getEmployeesOnLeave(){
        viewModelScope.launch {
            getEmployeesOnLeaveUseCase.invoke()
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collectLatest{
                    _employeesOnLeaveState.value=it
                }
        }
    }

    fun showAnnouncementDetailDialog(clickedAnnouncementItem: Announcement) {
        clickedAnnouncement=clickedAnnouncementItem
        showAnnouncementDetailDialog=true
    }

    fun hideAnnouncementDetailDialog() {
        showAnnouncementDetailDialog=false
    }
  fun  setCurrentUser(currentUser: User){
      user=currentUser

  }


}

data class HomePageUiState(
    val pagerSectionLoading:Boolean=false,
    val pagerSectionRefreshing:Boolean=false,
    val pagerSectionError:Boolean=false,
    val errorMessage:String="An unexpected error occurred while connecting to the server",
    val announcementsEmpty:Boolean=false,
    val employeeOnLeaveEmpty:Boolean=false,
    val announcements:List<Announcement> = emptyList()
)
