package co.youverify.youhr.presentation.ui.home


import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.youverify.youhr.R
import co.youverify.youhr.presentation.ui.Navigator
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor( private val navigator: Navigator) : ViewModel(){



    var userName ="Edith"
    private set
    var profilePhotoResId = R.drawable.profile_photo_edith
    private set
    var activeSideNavItem by mutableStateOf(0)
    private set
    @OptIn(ExperimentalPagerApi::class)
    val pagerState = PagerState(currentPage = 0)
    @OptIn(ExperimentalMaterial3Api::class)
    val drawerState = DrawerState(initialValue = DrawerValue.Closed)
    private var _shouldUpdateDrawerState = MutableStateFlow(false)
    val shouldUpdateDrawerState = _shouldUpdateDrawerState.asStateFlow()




    fun onBottomNavItemClicked(route: String) {

            navigator.navigatePopToForBottomNavItem(toRoute =route )
        //navigatePopToInclusive(toRoute = CreatePassword.route, popToRoute = LoginWithCode.route)
    }

    fun updateActiveSideNavItem(newIndex: Int) {
        activeSideNavItem=newIndex
    }

    fun onNotificationClicked(count: String) {
        
    }

    @OptIn(ExperimentalPagerApi::class)
    fun scrollToPage(pageIndex: Int) {
        viewModelScope.launch {
            pagerState.animateScrollToPage(pageIndex)
        }
    }

    fun updateDrawerState() {
        if (!_shouldUpdateDrawerState.value) _shouldUpdateDrawerState.value=true
    }

}
