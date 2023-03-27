package co.youverify.youhr.presentation.ui.task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import co.youverify.youhr.presentation.TaskDetail
import co.youverify.youhr.presentation.ui.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskDetailViewModel @Inject constructor(private val stateHandle: SavedStateHandle, private val navigator: Navigator) : ViewModel(){



    var taskId: Int? =stateHandle[TaskDetail.taskIdArg]
        private set
    var dateDropDownExpanded by mutableStateOf(false)
        private set

    var categoryDropDownExpanded by mutableStateOf(false)
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
