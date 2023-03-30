package co.youverify.youhr.presentation.ui.task



import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import co.youverify.youhr.presentation.Home
import co.youverify.youhr.presentation.TaskDetail
import co.youverify.youhr.presentation.ui.Navigator
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor( private val navigator: Navigator) : ViewModel(){


    var  currentTaskList by mutableStateOf(pendingTasks)
    private set
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
        //currentTaskList = if (currentTaskList[0].isCompleted) pendingTasks else completedTasks
    }

    fun updateDateDropDownState() {
        dateDropDownExpanded=!dateDropDownExpanded
    }

    fun showTaskDetail(taskId: Int) {
        navigator.navigatePopTo("${TaskDetail.route}/$taskId", popToRoute = Home.route)
    }

    fun categoryDropDownOnDismissCallBack() {
        categoryDropDownExpanded=!categoryDropDownExpanded
    }

    fun dateDropDownOnDismissCallBack() {
        dateDropDownExpanded=!dateDropDownExpanded
    }

    fun onPendingClicked() {
        currentTaskList= pendingTasks
    }

    fun onCompletedClicked() {
        currentTaskList= completedTasks
    }


}
