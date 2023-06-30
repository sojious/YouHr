package co.youverify.youhr.presentation.ui.leave

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.youverify.youhr.R
import co.youverify.youhr.core.util.Result
import co.youverify.youhr.core.util.getLeavePeriod
import co.youverify.youhr.data.model.LeaveApplicationRequest
import co.youverify.youhr.data.model.LeaveApplicationResponse
import co.youverify.youhr.domain.model.LeaveRequest
import co.youverify.youhr.domain.model.LeaveSummary
import co.youverify.youhr.domain.repository.LeaveRepository
import co.youverify.youhr.domain.use_case.CreateLeaveRequestUseCase
import co.youverify.youhr.domain.use_case.GetLeaveRequestsUseCase
import co.youverify.youhr.domain.use_case.GetLeaveSummaryUseCase
import co.youverify.youhr.domain.use_case.GetUserProfileUseCase
import co.youverify.youhr.presentation.ui.Navigator
import co.youverify.youhr.presentation.ui.components.ConnectionErrorScreen
import co.youverify.youhr.presentation.ui.components.YouHrTitleBar
import co.youverify.youhr.presentation.ui.components.shimmerEffect
import co.youverify.youhr.presentation.ui.home.HomeViewModel
import co.youverify.youhr.presentation.ui.home.ProfileRepoMock
import co.youverify.youhr.presentation.ui.theme.YouHrTheme
import co.youverify.youhr.presentation.ui.theme.bodyTextDeepColor
import co.youverify.youhr.presentation.ui.theme.bodyTextLightColor
import co.youverify.youhr.presentation.ui.theme.deactivatedColorDeep
import co.youverify.youhr.presentation.ui.theme.errorMessageColor
import co.youverify.youhr.presentation.ui.theme.primaryColor
import co.youverify.youhr.presentation.ui.theme.taskItemBorderColor
import co.youverify.youhr.presentation.ui.theme.yvColor1
import co.youverify.youhr.presentation.ui.theme.yvText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun LeaveManagementScreen(
    modifier: Modifier = Modifier,
    onCreateRequestClicked: () -> Unit,
    filterDropDownOnDismiss: () -> Unit,
    onFilterDropDownClicked: () -> Unit,
    onFilterDropDownItemClicked: (Int) -> Unit,
    uiState: LeaveManagementUiState,
    homeViewModel: HomeViewModel,
    userGender:String,
    leaveManagementViewModel: LeaveManagementViewModel,
    onLeaveHistoryItemClicked: (Int) -> Unit,
    onRefresh: () -> Unit
){


    LaunchedEffect(key1 = Unit){
        leaveManagementViewModel.getLeaveRequestsAndSummaryOnFirstLoad()
    }

    val pullRefreshState= rememberPullRefreshState(refreshing = uiState.isRefreshing, onRefresh =onRefresh)

    Box(
        modifier= modifier
            .fillMaxSize()
            .pullRefresh(state = pullRefreshState)
    ) {


        Column(modifier = Modifier.fillMaxSize()) {

            YouHrTitleBar(
                title = "Leave Management",
                onBackArrowClicked = {homeViewModel.updateActiveSideNavItem(0)},
                modifier = Modifier.padding(top=45.dp)
            )

            if (uiState.loading){
                LoadingScreen(modifier=Modifier.padding(top=36.dp))

            }
            else if (uiState.internetConnectionError){
                ConnectionErrorScreen(description = "Ooops..You seem to be offline!", onRetryButtonClicked = {leaveManagementViewModel.getLeaveRequestsAndSummaryOnFirstLoad()})
            }

            else if (uiState.unexpectedError){
                ConnectionErrorScreen(description = "Ooops..An unexpected error occured while connecting to the sever!", onRetryButtonClicked = {leaveManagementViewModel.getLeaveRequestsAndSummaryOnFirstLoad()})
            }

            else{
                AnalyticsAndHistorySection(
                    modifier =Modifier.padding(top=36.dp),
                    historyIsEmpty = uiState.historyIsEmpty,
                    filterDropDownExpanded = uiState.dropDownExpanded,
                    filterDropDownOnDismiss = filterDropDownOnDismiss,
                    onFilterDropDownClicked = onFilterDropDownClicked,
                    onFilterDropDownItemClicked = onFilterDropDownItemClicked,
                    filterText = uiState.filterText,
                    onLeaveHistoryItemClicked = onLeaveHistoryItemClicked,
                    filteredList=uiState.filteredList,
                    leaveSummary=uiState.leaveSummary,
                    userGender=userGender
                )
            }

        }

        FloatingActionButton(
            onClick =onCreateRequestClicked,
            modifier= Modifier
                //.size(56.dp)
                .align(Alignment.BottomEnd)
                .padding(end = 36.dp, bottom = 85.dp),
            containerColor = primaryColor,
            content = {
                Icon(painter = painterResource(id = R.drawable.ic_add), contentDescription =null, tint = Color.White )
            },
            shape = CircleShape
        )

        PullRefreshIndicator(uiState.isRefreshing, pullRefreshState, Modifier.align(Alignment.Center))
    }
}

@Composable
fun AnalyticsAndHistorySection(
    modifier: Modifier = Modifier,
    historyIsEmpty: Boolean,
    filterDropDownExpanded: Boolean,
    filterDropDownOnDismiss: () -> Unit,
    onFilterDropDownClicked: () -> Unit,
    onFilterDropDownItemClicked: (Int) -> Unit,
    filterText: String,
    onLeaveHistoryItemClicked: (Int) -> Unit,
    filteredList: List<LeaveRequest>,
    leaveSummary: LeaveSummary,
    userGender: String
) {
    Column(modifier.fillMaxSize()) {
        LeaveSummarySection(modifier=Modifier.padding(start = 20.dp),leaveSummary=leaveSummary,userGender=userGender)
        Divider(modifier= Modifier
            .fillMaxWidth()
            .padding(top = 37.dp, bottom = 24.dp), thickness = 1.dp, color = deactivatedColorDeep)
        HistorySection(
            modifier =Modifier,
            isEmpty =historyIsEmpty,
            filterDropDownExpanded = filterDropDownExpanded,
            filterDropDownOnDismiss = filterDropDownOnDismiss,
            onFilterDropDownItemClicked = onFilterDropDownItemClicked,
            onFilterDropDownClicked = onFilterDropDownClicked,
            filterText = filterText,
            onLeaveHistoryItemClicked = onLeaveHistoryItemClicked,
            filteredList=filteredList,
        )
    }

}

@Composable
fun HistorySection(
    modifier: Modifier,
    isEmpty: Boolean,
    filterDropDownExpanded: Boolean,
    filterDropDownOnDismiss: () -> Unit,
    onFilterDropDownItemClicked: (Int) -> Unit,
    onFilterDropDownClicked: () -> Unit,
    filterText: String,
    onLeaveHistoryItemClicked: (Int) -> Unit,
    filteredList: List<LeaveRequest>,
) {
    if (isEmpty){
        EmptyState()
    }else{
        LeaveHistoryList(
            modifier = Modifier.padding(horizontal = 20.dp),
            onDropDownClicked = onFilterDropDownClicked,
            dropDownOnDismiss = filterDropDownOnDismiss,
            onDropDownItemClicked = onFilterDropDownItemClicked,
            dropDownExpanded = filterDropDownExpanded,
            text = filterText,
            onLeaveHistoryItemClicked = onLeaveHistoryItemClicked,
            filteredList=filteredList
        )
    }
}

@Composable
fun LeaveHistoryList(
    modifier: Modifier = Modifier,
    onDropDownClicked: () -> Unit,
    dropDownOnDismiss: () -> Unit,
    onDropDownItemClicked: (Int) -> Unit,
    dropDownExpanded: Boolean,
    text: String,
    onLeaveHistoryItemClicked: (Int) -> Unit,
    filteredList: List<LeaveRequest>
) {
    Column(modifier = modifier.fillMaxSize()) {
        Row(modifier=Modifier.fillMaxWidth()){
            Text(
                text = "My Requests",modifier=Modifier.padding(start = 20.dp, top=5.5.dp, bottom = 5.5.dp),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = bodyTextDeepColor
            )
            Spacer(modifier=Modifier.weight(1f))
            LeaveStatusFilter(
                onClick =onDropDownClicked,
                categoryDropDownOnDismissCallBack = dropDownOnDismiss,
                onDropDownMenuItemClicked =onDropDownItemClicked ,
                dropDownExpanded = dropDownExpanded ,
                FilterText = text,
            modifier = Modifier.padding(end=20.dp)
            )
        }

        if (filteredList.isEmpty()){
            EmptyState(message = "No Requests", resolution = "", modifier = Modifier.padding(top = 20.dp))
        }else{
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(24.dp),
                modifier = Modifier.padding(top=20.dp, start = 20.dp,end=20.dp)
            ){

                    itemsIndexed(items=filteredList){index,leaveRequest->
                        LeaveHistoryItem(
                            type = leaveRequest.leaveType,
                            reliever =leaveRequest.relieverName ,
                            period = getLeavePeriod(leaveRequest.startDate,leaveRequest.endDate),
                            status = leaveRequest.status,
                            onLeaveHistoryItemClicked =onLeaveHistoryItemClicked ,
                            index=index
                        )
                    }

                /*item{
                    LeaveHistoryItem(
                        type = "Annual Leave",
                        reliever = "Joseph Michael",
                        period = " 11th Dec - 15th Dec 2023",
                        status = LeaveStatus.REJECTED,
                        onLeaveHistoryItemClicked = onLeaveHistoryItemClicked
                    )
                }

                item{
                    LeaveHistoryItem(
                        type = "Casual Leave",
                        reliever = "Joseph Michael",
                        period = " 5th Jun - 10th July 2023",
                        status = LeaveStatus.APPROVED,
                        onLeaveHistoryItemClicked = onLeaveHistoryItemClicked
                    )
                }*/

            }
        }

    }
}


@Composable
fun LeaveHistoryItem(
    modifier: Modifier = Modifier,
    type: String, reliever: String,
    period: String,
    status: String,
    onLeaveHistoryItemClicked: (Int) -> Unit,
    index: Int,
){

    val statusColor=when(status){
        LeaveStatus.PENDING.id-> Color(0XFFDAA419)
        LeaveStatus.APPROVED.id-> leaveColors[1]
        else -> errorMessageColor
    }
    Row(
        modifier= Modifier
            .fillMaxWidth()
            .background(color = Color(0XFFF2F5F7), shape = RoundedCornerShape(10.dp))
            .clickable { onLeaveHistoryItemClicked(index) }
    ) {
        Column(modifier = Modifier.padding(start = 20.dp,top=22.dp, bottom = 24.dp)) {
            Text(
                text =type,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = bodyTextDeepColor
            )

            Text(
                text ="Reliever : $reliever",
                fontSize = 12.sp,
                color = bodyTextLightColor,
                modifier=Modifier.padding(top=12.dp, bottom = 14.dp)
            )

            Row(horizontalArrangement = Arrangement.spacedBy(4.5.dp)) {
                Icon(painter = painterResource(id = R.drawable.ic_calendar), contentDescription =null, tint = bodyTextLightColor )
                Text(text = period, fontSize = 12.sp, color = bodyTextLightColor)
            }
        }
        Spacer(modifier = modifier.weight(1f))
        Row(
            modifier= Modifier
                .padding(end = 18.dp, top = 22.dp)
                .background(
                    shape = RoundedCornerShape(4.dp),
                    color = statusColor.copy(alpha = 0.1f)
                ),
            horizontalArrangement = Arrangement.spacedBy(2.5.dp)

        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_clock),
                contentDescription =null,
                tint = statusColor,
                modifier = Modifier
                    .padding(start = 12.5.dp, top = 5.dp, bottom = 5.dp)
                    .size(11.dp)

            )
            
            Text(
                text = status,
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                modifier=Modifier.padding(end=12.dp,top=4.dp, bottom = 4.dp),
                color = statusColor
            )
        }
    }
}

@Composable
fun LeaveSummarySection(
    modifier: Modifier = Modifier,
    leaveSummary: LeaveSummary,
    userGender: String
){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .horizontalScroll(
                rememberScrollState(), enabled = true
            )

            .background(color = Color.White)
            .border(width = 0.5.dp, color = Color(0XFFF1F2F5), shape = RoundedCornerShape(8.dp))

    ) {
        LeaveSummaryItem(
            itemTintColor = leaveColors[0],
            leaveType = "Annual Leave",
            totalLeaveDays =20,
            usedLeaveDays = leaveSummary.annualLeaveTaken,
            modifier = Modifier.padding(start = 12.dp)
        )


        Spacer(modifier = Modifier
            .size(0.5.dp, 42.5.dp)
            .background(color = deactivatedColorDeep)
            .padding()
            .align(Alignment.CenterVertically))
        LeaveSummaryItem(
            itemTintColor = leaveColors[1],
            leaveType = "Casual",
            totalLeaveDays =5,
            usedLeaveDays = leaveSummary.casualLeaveTaken
        )
        Spacer(modifier = Modifier
            .size(0.5.dp, 42.5.dp)
            .background(color = deactivatedColorDeep)
            .padding()
            .align(Alignment.CenterVertically))

        /*Spacer(modifier = Modifier
            .size(0.5.dp, 42.5.dp)
            .background(color = deactivatedColorDeep)
            .padding()
            .align(Alignment.CenterVertically))*/
        LeaveSummaryItem(
            itemTintColor = leaveColors[2],
            leaveType = "Compassionate Leave",
            totalLeaveDays =5,
            usedLeaveDays = leaveSummary.compassionateLeaveTaken
        )
        Spacer(modifier = Modifier
            .size(0.5.dp, 42.5.dp)
            .background(color = deactivatedColorDeep)
            .padding()
            .align(Alignment.CenterVertically))

        LeaveSummaryItem(
            itemTintColor = leaveColors[3],
            leaveType = "Parental Leave",
            totalLeaveDays =if (userGender=="Male") 10 else 60,
            usedLeaveDays = leaveSummary.parentalLeaveTaken
        )
        Spacer(modifier = Modifier
            .size(0.5.dp, 42.5.dp)
            .background(color = deactivatedColorDeep)
            .padding()
            .align(Alignment.CenterVertically))

        LeaveSummaryItem(
            itemTintColor = leaveColors[4],
            leaveType = "Sick Leave",
            totalLeaveDays =5,
            usedLeaveDays = leaveSummary.sickLeaveTaken
        )
        Spacer(modifier = Modifier
            .size(0.5.dp, 42.5.dp)
            .background(color = deactivatedColorDeep)
            .padding()
            .align(Alignment.CenterVertically))

        LeaveSummaryItem(
            itemTintColor = leaveColors[5],
            leaveType = "Study Leave",
            totalLeaveDays =20,
            usedLeaveDays = leaveSummary.studyLeaveTaken
        )
        Spacer(modifier = Modifier
            .size(0.5.dp, 42.5.dp)
            .background(color = deactivatedColorDeep)
            .padding()
            .align(Alignment.CenterVertically))

    }
}



@Composable
fun LeaveSummaryItem(
    modifier: Modifier=Modifier,
    itemTintColor:Color,
    leaveType:String,
    totalLeaveDays:Int,
    usedLeaveDays:Int
){
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.background(color= Color.White)
    ) {


        Box(
            modifier=Modifier.padding( start = 11.dp, top = 18.dp, bottom = 18.dp)
        ){


            CircularProgressIndicator(
                progress = usedLeaveDays.toFloat() / totalLeaveDays,
                modifier=Modifier.size(32.dp),
                strokeWidth = 1.dp,
                strokeCap = StrokeCap.Round,
                trackColor = deactivatedColorDeep,
                color = itemTintColor
            )
            Icon(
                painter = painterResource(id = R.drawable.ic_briefcase),
                contentDescription =null,
                modifier = Modifier.align(Alignment.Center),
                tint = itemTintColor
            )
        }





        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(top = 18.dp, bottom = 14.dp, end = 14.dp),
            content = {
                Text(
                    text = leaveType,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = bodyTextDeepColor
                )

                Text(
                    text = "${usedLeaveDays}/${totalLeaveDays} days",
                    fontSize = 12.sp,
                    color = bodyTextLightColor
                )
            }
        )
        
    }
}



@Composable
fun EmptyState(
    modifier: Modifier=Modifier,
    message:String="No Request Made",
    resolution:String="Click on the button below to request your preferred leave "
){
  Box(
      modifier = modifier.fillMaxSize(),
      contentAlignment = Alignment.Center,
  ){
      Column(
          horizontalAlignment = Alignment.CenterHorizontally
      ) {
          Image(
              painter = painterResource(id = R.drawable.ic_no_request), 
              contentDescription =null ,
              modifier= Modifier
                  .size(75.dp)
                  .padding(bottom = 24.dp),
              contentScale = ContentScale.Fit
          )
          Text(
              text = message,
              color = yvText,
              fontSize = 16.sp,
              fontWeight = FontWeight.Medium,
              modifier=Modifier.padding(bottom = 8.dp)
          )

          if (resolution.isNotEmpty())
              Text(
              text = resolution,
              color = bodyTextLightColor,
              fontSize = 12.sp,
          )
      }
  }
}

@Composable
fun LeaveStatusFilter(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    categoryDropDownOnDismissCallBack: () -> Unit,
    onDropDownMenuItemClicked:(Int)->Unit,
    dropDownExpanded: Boolean,
    FilterText: String,
    //categoryFilterDisabled: Boolean
) {


    ///val contentColor=if (!categoryFilterDisabled) primaryColor else deactivatedColorDeep

    Row(
        modifier = modifier
            .height(28.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .border(width = 1.dp, color = deactivatedColorDeep, shape = RoundedCornerShape(8.dp))
            .clickable {
                onClick()
            }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_drop_down_indicator), contentDescription =null ,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 12.dp),
            tint = primaryColor
        )

        Text(
            text = FilterText,
            fontSize = 12.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 14.dp),
            color= primaryColor
        )




        Icon(
            painter = painterResource(id = R.drawable.ic_spinner),
            contentDescription =null,
            modifier= Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 16.6.dp, end = 11.74.dp),
            tint = primaryColor

        )


        DropdownMenu(
            expanded = dropDownExpanded,
            onDismissRequest = { categoryDropDownOnDismissCallBack()},
            modifier =Modifier.background(color= Color.White, shape = RoundedCornerShape(4.dp))
        ) {

            DropdownMenuItem(
                text = { Text(text = "All", fontSize = 12.sp, color = primaryColor)},
                onClick = { onDropDownMenuItemClicked(1) },
            )

            DropdownMenuItem(
                text = { Text(text = "Pending", fontSize = 12.sp, color = primaryColor)},
                onClick = { onDropDownMenuItemClicked(2) },
            )
            DropdownMenuItem(
                text = { Text(text = "Approved", fontSize = 12.sp, color = primaryColor)},
                onClick = { onDropDownMenuItemClicked(3) },

            )

            DropdownMenuItem(
                text = { Text(text = "Rejected", fontSize = 12.sp, color = primaryColor)},
                onClick = { onDropDownMenuItemClicked(4) },
            )


        }
    }
}

@Composable
fun LoadingScreen(modifier: Modifier=Modifier){
    Column(modifier.fillMaxSize()) {
        Row(
            modifier = modifier
                .padding(start = 20.dp, bottom = 60.dp)
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState(), enabled = true)
                .background(color = Color.White, shape = RoundedCornerShape(8.dp)),
            content = {
            DummyLeaveSummaryItem()
            DummySpacer(modifier = Modifier.align(Alignment.CenterVertically))
            DummyLeaveSummaryItem()
            DummySpacer(modifier = Modifier.align(Alignment.CenterVertically))
            DummyLeaveSummaryItem()
            DummySpacer(modifier = Modifier.align(Alignment.CenterVertically))
            DummyLeaveSummaryItem()
            }
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(24.dp), modifier = Modifier.padding(top=20.dp, start = 20.dp,end=20.dp)){
            items(count = 5){ DummyLeaveHistoryItem()}
        }
    }
}

@Composable
fun DummyLeaveSummaryItem(){
    Row(
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.shimmerEffect()
    ) {


        Box(
            modifier=Modifier.padding( start = 11.dp, top = 18.dp, bottom = 18.dp),
            contentAlignment = Alignment.Center
        ){
            Box(modifier= Modifier
                .size(32.dp)
                .background(color = taskItemBorderColor, shape = CircleShape))
            Box(modifier = Modifier
                .size(12.dp)
                .align(Alignment.Center)
                .background(color = taskItemBorderColor))
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(top = 18.dp, bottom = 14.dp, end = 14.dp, start = 4.dp),
            content = {
                Box(modifier = Modifier
                    .size(77.dp, 14.dp)
                    .background(color = taskItemBorderColor))
                Box(modifier = Modifier
                    .size(40.dp, 14.dp)
                    .background(color = taskItemBorderColor))
            }
        )

    }
}
@Composable
fun DummyLeaveHistoryItem(){
    Row(modifier= Modifier
        .fillMaxWidth()
        .shimmerEffect()
        .clip(shape = RoundedCornerShape(10.dp))) {
        Column(
            modifier = Modifier.padding(start = 20.dp,top=22.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(modifier = Modifier
                .size(100.dp, 20.dp)
                .background(color = taskItemBorderColor))
            Box(modifier = Modifier
                .size(120.dp, 14.dp)
                .background(color = taskItemBorderColor))
            Box(modifier = Modifier
                .size(150.dp, 14.dp)
                .background(color = taskItemBorderColor))
        }
        Spacer(modifier = Modifier.weight(1f))
        Box(modifier= Modifier
            .padding(end = 18.dp, top = 22.dp)
            .size(55.dp, 20.dp)
            .background(color = taskItemBorderColor, shape = RoundedCornerShape(4.dp))
        )
    }
}
@Composable
fun DummySpacer(modifier: Modifier){
    Box(modifier = modifier
        .size(0.5.dp, 42.5.dp)
        .background(color = taskItemBorderColor))
}
@SuppressLint("UnrememberedMutableState")
@Preview
@Composable
fun LeaveOverViewScreenPreview(){
    YouHrTheme {
        Surface {
            LeaveManagementScreen(
                onCreateRequestClicked = {},
                filterDropDownOnDismiss = {},
                onFilterDropDownClicked = {},
                onFilterDropDownItemClicked = {},
                uiState = LeaveManagementUiState(loading = false),
                homeViewModel = HomeViewModel(Navigator(), GetUserProfileUseCase(ProfileRepoMock())),
                leaveManagementViewModel = LeaveManagementViewModel(
                    Navigator(), GetLeaveRequestsUseCase(LeaveRepoMock()),
                    GetLeaveSummaryUseCase(LeaveRepoMock()), CreateLeaveRequestUseCase(LeaveRepoMock())
                ),
                onLeaveHistoryItemClicked = {},
                onRefresh = {},
                userGender = "Male"
            )
        }
    }
}
@Preview
@Composable
fun EmptyStatePreview(){
    YouHrTheme {
        Surface {
            EmptyState()
        }
    }
}

class LeaveRepoMock:LeaveRepository{
    override suspend fun getLeaveRequests(isFirstLoad: Boolean): Flow<Result<List<LeaveRequest>>> {
        return flow{}

    }


    override suspend fun getLeaveSummary(isFirstLoad: Boolean): Flow<Result<LeaveSummary>> {
        return flow{}

    }

    override suspend fun createLeaveRequest(request: LeaveApplicationRequest): Flow<Result<LeaveApplicationResponse>> {
        return flow{}
    }

}


@Preview
@Composable
fun LeaveSummaryItemPreview(){
    YouHrTheme {
        Surface {
            LeaveSummaryItem(
                itemTintColor = Color.Red,
                leaveType = "Annual Leave",
                totalLeaveDays =20 ,
                usedLeaveDays = 7
            )
        }
    }
}
val leaveColors= listOf(
    Color(0XFFFF993C),
    Color(0XFF4C7A40),
    Color(0XFFCC0B96),
    Color(0XFF3CC4FF),
    Color(0XFFB0E41A),
    Color(0XFF633CFF),
    yvColor1
)

enum class LeaveStatus(val id:String){
    ALL("All"),
    PENDING("Pending"),
    APPROVED("Approved"),
    REJECTED("Rejected")
}