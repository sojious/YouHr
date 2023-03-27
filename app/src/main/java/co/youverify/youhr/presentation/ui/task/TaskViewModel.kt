package co.youverify.youhr.presentation.ui.task



import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.youverify.youhr.R
import co.youverify.youhr.presentation.ui.Navigator
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.PagerState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor( private val navigator: Navigator) : ViewModel(){




    var categoryDropDownExpanded by mutableStateOf(false)
        private set
    var dateDropDownExpanded by mutableStateOf(false)
        private set


    fun onBottomNavItemClicked(route: String) {

        navigator.navigatePopToForBottomNavItem(toRoute =route )
        //navigatePopToInclusive(toRoute = CreatePassword.route, popToRoute = LoginWithCode.route)
    }

    fun updateCategoryDropDownState() {
        categoryDropDownExpanded=!categoryDropDownExpanded
    }

    fun updateDateDropDownState() {
        dateDropDownExpanded=!dateDropDownExpanded
    }



}
