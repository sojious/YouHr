package co.youverify.youhr.presentation.ui.home


import android.content.Context
import android.graphics.BitmapFactory
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
import co.youverify.youhr.presentation.ui.Navigator
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
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

    fun getUserProfile(context: Context) {

        viewModelScope.launch {
            getUserProfileUseCase.invoke(isFirstLogin =false).collect{result->
                if(result is Result.Success){
                   val profileBitmap=BitmapFactory.decodeStream(context.openFileInput("profile_pic"))
                    user=result.data.copy(displayPictureBitmap =profileBitmap )
                }

            }
        }
    }

}
