package co.youverify.youhr.presentation.ui

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import co.youverify.youhr.presentation.LeaveDetail
import co.youverify.youhr.presentation.LeaveManagement
import co.youverify.youhr.presentation.LeaveManagementGraph
import co.youverify.youhr.presentation.LeaveRequest
import co.youverify.youhr.presentation.ui.home.HomeViewModel
import co.youverify.youhr.presentation.ui.leave.LeaveDetailScreen
import co.youverify.youhr.presentation.ui.leave.LeaveManagementScreen
import co.youverify.youhr.presentation.ui.leave.LeaveManagementViewModel
import co.youverify.youhr.presentation.ui.leave.LeaveRequestScreen
import co.youverify.youhr.presentation.ui.login.ConfirmCodeViewModel
import co.youverify.youhr.presentation.ui.login.LoginWithCodeViewModel
import co.youverify.youhr.presentation.ui.login.LoginWithPassWordViewModel
import com.google.accompanist.navigation.animation.composable

@OptIn(ExperimentalAnimationApi::class)
fun NavGraphBuilder.leaveManagementGraph(
    leaveManagementViewModel:LeaveManagementViewModel,
    homeViewModel: HomeViewModel,
    loginWithCodeViewModel: LoginWithCodeViewModel,
    loginWithPassWordViewModel: LoginWithPassWordViewModel
){
    navigation(
        startDestination = LeaveManagement.route,
        route=LeaveManagementGraph.route
    ){
        composable(route=LeaveManagement.route){
            val uiState by leaveManagementViewModel.uIStateFlow.collectAsState()
            LeaveManagementScreen(
                onCreateRequestClicked = {leaveManagementViewModel.onCreateLeaveRequestClicked()},
                filterDropDownOnDismiss = {leaveManagementViewModel.onDropDownODismissRequested() },
                onFilterDropDownClicked = {leaveManagementViewModel.updateDropDownExpandedStatus() },
                onFilterDropDownItemClicked = {leaveManagementViewModel.onDropDownItemClicked(it)},
                uiState = uiState,
                homeViewModel = homeViewModel,
                leaveManagementViewModel = leaveManagementViewModel,
                onLeaveHistoryItemClicked = {leaveManagementViewModel.displayLeaveDetail(it)},
                onRefresh = {leaveManagementViewModel.onRefresh()},
                userGender = leaveManagementViewModel.userGender

            )
        }




        composable(route=LeaveDetail.routWithArgs, arguments = LeaveDetail.args){navBackStackEntry->
             val leaveDetailUiState by leaveManagementViewModel.detailUiState.collectAsState()
            val leaveManagementUiState by leaveManagementViewModel.uIStateFlow.collectAsState()
            val leaveId=navBackStackEntry.arguments?.getInt(LeaveDetail.leaveIdArg)!!
            val currentLeave= leaveManagementUiState.filteredList[leaveId]

            LeaveDetailScreen(
                leaveDetailExpanded = leaveDetailUiState.leaveDetailExpanded,
                onLeaveDetailContentChangeRequested = {leaveManagementViewModel.updateLeaveDetailExpanded()},
                leaveRequest= currentLeave,
                onBackArrowClicked = {leaveManagementViewModel.onBackArrowClicked()}
            )
        }

        composable(route=LeaveRequest.route){
            LeaveRequestScreen(
                onSubmit = {leaveManagementViewModel.createNewLeaveRequest(it)},
                onDialogCloseClicked = {leaveManagementViewModel.onDialogCloseClicked()},
                showDialog =leaveManagementViewModel.showNewLeaveRequestSuccessDialog,
                creatingRequest = leaveManagementViewModel.creatingNewLeaveRequest,
                leaveManagementViewModel = leaveManagementViewModel,
                loginWithCodeViewModel = loginWithCodeViewModel,
                loginWithPassWordViewModel = loginWithPassWordViewModel,
                onBackArrowClicked = {leaveManagementViewModel.onBackArrowClicked()}

            )
        }
    }
}