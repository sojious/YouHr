package co.youverify.youhr.presentation.ui.home

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.text.SimpleDateFormat
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import co.youverify.youhr.R
import co.youverify.youhr.core.util.Result
import co.youverify.youhr.core.util.capitalizeWords
import co.youverify.youhr.core.util.getGreetingMessage
import co.youverify.youhr.core.util.getLeavePeriod
import co.youverify.youhr.core.util.toEpochMillis
import co.youverify.youhr.core.util.toOrdinalDateString
import co.youverify.youhr.core.util.toTimeAgo
import co.youverify.youhr.data.model.UpdateUserProfileRequest
import co.youverify.youhr.data.model.UserProfileResponse
import co.youverify.youhr.data.remote.TokenInterceptor
import co.youverify.youhr.domain.model.Announcement
import co.youverify.youhr.domain.model.EmployeeOnLeave
import co.youverify.youhr.domain.model.FilteredUser
import co.youverify.youhr.domain.model.User
import co.youverify.youhr.domain.repository.AnnouncementRepository
import co.youverify.youhr.domain.repository.ProfileRepository
import co.youverify.youhr.domain.use_case.ChangePasswordUseCase
import co.youverify.youhr.domain.use_case.CreateCodeUseCase
import co.youverify.youhr.domain.use_case.CreateLeaveRequestUseCase
import co.youverify.youhr.domain.use_case.GetAllAnnouncementUseCase
import co.youverify.youhr.domain.use_case.GetEmployeesOnLeaveUseCase
import co.youverify.youhr.domain.use_case.GetLeaveRequestsUseCase
import co.youverify.youhr.domain.use_case.GetLeaveSummaryUseCase
import co.youverify.youhr.domain.use_case.GetTasksUseCase
import co.youverify.youhr.domain.use_case.GetUserProfileUseCase
import co.youverify.youhr.domain.use_case.LoginWithPasswordUseCase
import co.youverify.youhr.presentation.ui.Navigator
import co.youverify.youhr.presentation.ui.components.ConnectionErrorScreen
import co.youverify.youhr.presentation.ui.components.TextAvatar
import co.youverify.youhr.presentation.ui.leave.AuthRepoMock
import co.youverify.youhr.presentation.ui.leave.LeaveManagementScreen
import co.youverify.youhr.presentation.ui.leave.LeaveManagementViewModel
import co.youverify.youhr.presentation.ui.leave.LeaveRepoMock
import co.youverify.youhr.presentation.ui.leave.PreferenceRepoMock
import co.youverify.youhr.presentation.ui.settings.SettingsViewModel
import co.youverify.youhr.presentation.ui.settings.TasKRepoMock
import co.youverify.youhr.presentation.ui.theme.*
import coil.compose.AsyncImage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.util.*

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomePageScreen(

    notificationCount: String,
    profilePhotoBitmap: Bitmap?,
    onNotificationIconClicked: (String) -> Unit,
    onHamburgerClicked: () -> Unit,
    pagerState: PagerState,
    onTabItemClicked: (Int) -> Unit,
    onSideNavItemClicked: (Int) -> Unit,
    //onProfilePicClicked: () -> Unit,
    activeSideNavItemIndex: Int,
    homeDrawerState: DrawerState,
    onQuickAccessItemClicked: (Int) -> Unit,
    leaveManagementViewModel: LeaveManagementViewModel,
    homeViewModel: HomeViewModel,
    userName: String,
    settingsViewModel: SettingsViewModel,
    announcementState: StateFlow<PagingData<Announcement>>,
    employeesOnLeaveState: StateFlow<PagingData<co.youverify.youhr.domain.model.EmployeeOnLeave>>,
    onAnnouncementItemClicked: (Announcement) -> Unit,
    showAnnouncementDetailDialog: Boolean,
    clickedAnnouncementItem: Announcement?,
    onAnnouncementDialogCloseButtonClicked:()->Unit
    //uiState: HomePageUiState

){


    val leaveManagementUiState by leaveManagementViewModel.uIStateFlow.collectAsState()
    val context=LocalContext.current.applicationContext
    LaunchedEffect(key1 = Unit){
        homeViewModel.synUser(settingsViewModel,leaveManagementViewModel)
        homeViewModel.getAnnouncements()
        homeViewModel.getEmployeesOnLeave()

    }


    ModalNavigationDrawer(
        drawerState = homeDrawerState,
        gesturesEnabled = true,
        drawerContent = { SideNav(onItemClicked = onSideNavItemClicked, activeIndex = activeSideNavItemIndex, drawerState = homeDrawerState) },
        content = {
            when(activeSideNavItemIndex){
                0->{
                    HomeScreenContent(
                        modifier =Modifier.fillMaxSize(),
                        userName = userName,
                        notificationCount =notificationCount,
                        profileImageBitmap =profilePhotoBitmap?.asImageBitmap()?: ImageBitmap.imageResource(id = R.drawable.placeholder_pic),
                        onNotificationClicked = onNotificationIconClicked,
                        //onProfilePicClicked = onProfilePicClicked,
                        onHamburgerClicked = onHamburgerClicked,
                        pagerState = pagerState,
                        onTabClicked =onTabItemClicked,
                        onQuickAccessItemClicked =onQuickAccessItemClicked,
                        announcementState =announcementState,
                        employeesOnLeaveState=employeesOnLeaveState,
                        onAnnouncementItemClicked = onAnnouncementItemClicked,
                        showAnnouncementDetail = showAnnouncementDetailDialog,
                        clickedAnnouncementItem = clickedAnnouncementItem,
                        onDialogCloseButtonClicked = onAnnouncementDialogCloseButtonClicked
                    )
                }
                1->{
                    LeaveManagementScreen(
                        onCreateRequestClicked = {leaveManagementViewModel.onCreateLeaveRequestClicked()},
                        filterDropDownOnDismiss = {leaveManagementViewModel.onDropDownODismissRequested() },
                        onFilterDropDownClicked = { leaveManagementViewModel.updateDropDownExpandedStatus() },
                        onFilterDropDownItemClicked ={leaveManagementViewModel.onDropDownItemClicked(it)},
                        onRefresh = {leaveManagementViewModel.onRefresh()},
                        uiState = leaveManagementUiState,
                        homeViewModel = homeViewModel,
                        onLeaveHistoryItemClicked = {leaveManagementViewModel.displayLeaveDetail(it)},
                        leaveManagementViewModel = leaveManagementViewModel,
                        userGender = leaveManagementViewModel.userGender
                    )
                }
                else->{}
            }
        }
    )
}

@Composable
fun SideNav(
    modifier: Modifier=Modifier,
    onItemClicked:(Int)->Unit,
    activeIndex:Int,
    drawerState: DrawerState

) {


    Column(
        modifier = modifier
            .width(266.dp)
            .fillMaxHeight(),

    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(63.dp)
                .background(
                    brush = Brush.horizontalGradient(listOf(yvColor, yvColor1)),
                    shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                )
        )

        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr ){
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(
                        color = primaryColor,
                        shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                    ),
                verticalArrangement = Arrangement.spacedBy(10.dp)

            ) {
                val cts= rememberCoroutineScope()

                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(61.5.dp)
                    .background(color = primaryColor))
                repeat(sideNavItems.size){index->
                    val item= sideNavItems[index]
                    SideNavItem(
                        imageResId = item.imageResId,
                        text = item.text,
                        onClick = onItemClicked,
                        position=index,
                        active = index==activeIndex,
                        coroutineScope=cts,
                        drawerState = drawerState
                    )
                }
            }
        }


    }
}

@Composable
fun SideNavItem(
    imageResId: Int,
    text: String,
    onClick:(Int)->Unit,
    position:Int,
    active:Boolean,
    coroutineScope: CoroutineScope,
    drawerState: DrawerState
){

    val iconTintColor= if (active) Color.White else sideNavIconTintColor.copy(alpha =0.5f )
    val textColor= if (active) Color.White else sideNavIconTextColor
    val dividerColor= if (active) Color.White else primaryColor
    val backgroundColor= if (active) yvColor else primaryColor

    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(35.dp)
            .background(color = backgroundColor)
            //.padding(firstItemPadding)
            .clickable {
                onClick(position)
                coroutineScope.launch { drawerState.close() }
            }
    ) {
        val (divider,icon,textField) = createRefs()

        
        Spacer(
            modifier = Modifier
                .fillMaxHeight()
                .width(3.dp)
                .background(color = dividerColor)
                .constrainAs(divider) {
                    start.linkTo(parent.start)
                }
        )

        Icon(
            painter = painterResource(id = imageResId),
            contentDescription =null,
            modifier = Modifier.constrainAs(icon){
                start.linkTo(divider.end,20.dp)
                centerVerticallyTo(parent)

            },
            tint = iconTintColor
           // colorFilter = ColorFilter.tint(Color.Red, BlendMode.SrcIn)

        )

        Text(
            text = text,
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 20.8.sp,
            color=textColor,
            modifier = Modifier.constrainAs(textField){
                start.linkTo(icon.end,8.dp)
                centerVerticallyTo(parent)
            }
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun HomeScreenContent(
    modifier: Modifier,
    userName: String,
    notificationCount: String,
    profileImageBitmap: ImageBitmap,
    onNotificationClicked: (String) -> Unit,
    //onProfilePicClicked: () -> Unit,
    onHamburgerClicked: () -> Unit,
    pagerState: PagerState,
    onTabClicked: (Int) -> Unit,
    onQuickAccessItemClicked: (Int) -> Unit,
    //uiState: HomePageUiState,
    announcementState: StateFlow<PagingData<Announcement>>,
    onAnnouncementItemClicked: (Announcement) -> Unit,
    showAnnouncementDetail: Boolean,
    clickedAnnouncementItem: Announcement?,
    onDialogCloseButtonClicked: () -> Unit,
    employeesOnLeaveState: StateFlow<PagingData<co.youverify.youhr.domain.model.EmployeeOnLeave>>
) {

    Box(modifier = modifier.fillMaxSize()){
        Column(modifier = modifier.fillMaxSize()) {
            TopSection(
                userName = userName,
                notificationCount =notificationCount ,
                profileImageBitmap =profileImageBitmap,
                onNotificationClicked = onNotificationClicked,
                onHamburgerClicked = onHamburgerClicked,
                onQuickAccessItemClicked =onQuickAccessItemClicked,
                //onProfilePicClicked=onProfilePicClicked
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .background(color = surfaceGrey)
            )
            TabSection(
                pagerState = pagerState,
                onTabClicked = onTabClicked,
                announcementState =announcementState,
                employeesOnLeaveState=employeesOnLeaveState,
                onAnnouncementItemClicked =  onAnnouncementItemClicked
            )

        }
        
        if (showAnnouncementDetail){
            AnnouncementDetailDialog(
                onButtonClicked = onDialogCloseButtonClicked,
                announcement =clickedAnnouncementItem,
            )
        }
    }
    
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabSection(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    onTabClicked: (Int) -> Unit,
    //uiState: HomePageUiState,
    announcementState: StateFlow<PagingData<Announcement>>,
    onAnnouncementItemClicked: (Announcement) -> Unit,
    employeesOnLeaveState: StateFlow<PagingData<co.youverify.youhr.domain.model.EmployeeOnLeave>>
) {


    Column(modifier=modifier) {
        TabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = Color.White,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    color = primaryColor,
                    height = 2.dp,
                    modifier = Modifier
                        .ownTabIndicatorOffset(
                            currentTabPosition = tabPositions[pagerState.currentPage],pagerState

                        )
                )
            }
        ) {



            Tab(selected = pagerState.currentPage==0, onClick = {onTabClicked(0)},modifier=Modifier.padding(vertical = 12.dp)) {
                Text(
                    text = "Announcements",
                    color = if (pagerState.currentPage==0) primaryColor else unselectedTabColor,
                    fontSize = 14.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Tab(selected = pagerState.currentPage==1, onClick = {onTabClicked(1)},modifier=Modifier.padding(vertical = 12.dp)) {
                Text(
                    text = "Employees On Leave",
                    color = if (pagerState.currentPage==1) yvColor1 else unselectedTabColor,
                    fontSize = 14.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }



        HorizontalPager(
            count = 2,
            state = pagerState,
            //modifier = Modifier.padding(start = 21.dp, end = 21.dp,top=24.dp)
        ) {currentPage->

            if (currentPage==0) AnnouncementList(announcementState =announcementState, onItemClicked = onAnnouncementItemClicked) else EmployeeOnLeaveList(employeesOnLeaveState=employeesOnLeaveState)

        }


    }

}

@Composable
fun AnnouncementItem(announcement: Announcement,index: Int,onItemClicked:(Announcement)->Unit) {
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClicked(announcement) }
    ) {
        val (textAvatar,announcerName,announcementContent, addresseeName, date) =createRefs()


        //Text Avatar
        val color= announcementItemAvatarColors[index%announcementItemAvatarColors.size]

        TextAvatar(
            modifier=Modifier.constrainAs(textAvatar) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
            },
            color = color,
            text = announcement.postedBy[0].uppercaseChar().toString()
        )


        //AnnouncerName

        Text(
            text =announcement.postedBy,
            fontSize=14.sp,
            fontWeight=FontWeight.Medium,
            lineHeight=18.2.sp,
            color= announcerColor,
            modifier = Modifier
                .constrainAs(announcerName){
                    start.linkTo(textAvatar.end,8.dp)
                    top.linkTo(parent.top)
                }
        )

        //Announcement
        Text(
            text =announcement.message,
            fontSize=12.sp,
            fontWeight=FontWeight.Normal,
            lineHeight=16.sp,
            //textAlign=TextAlign.Start,
            color= bodyTextLightColor,
            modifier = Modifier
                .constrainAs(announcementContent){
                    start.linkTo(announcerName.start)
                    end.linkTo(parent.end)
                    top.linkTo(announcerName.bottom,4.dp)
                    width= Dimension.fillToConstraints
                },
            maxLines = 2,
            overflow = TextOverflow.Ellipsis

        )


        //To Section

        /*Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = bodyTextLightColor)){
                    append(text="To ")
                }


                withStyle(SpanStyle(color = addresseeColonColor )){
                    append(text=": ")
                }


                withStyle(SpanStyle(color = addresseeColor , fontWeight = FontWeight.W600)){
                    append(text=announcement.addressee)
                }
            },

            fontSize = 10.sp,
            lineHeight=16.sp,
            modifier = Modifier.constrainAs(addresseeName){
                top.linkTo(announcementContent.bottom,4.dp)
                start.linkTo(announcerName.start)
            }

        )*/



        //Date
        Text(
            text = announcement.createdAt.toEpochMillis().toOrdinalDateString(includeOf = true),
            fontSize = 10.sp,
            lineHeight=13.sp,
            color = announcementDateColor,
            modifier = Modifier.constrainAs(date){
                end.linkTo(parent.end)
                top.linkTo(parent.top)
            }
        )
    }
}

@Composable
fun EmployeeOnLeaveItem(employeeOnLeave: EmployeeOnLeave){
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {

        val (profileImage,name,designation, relieverName, leavePeriod) =createRefs()

        val imageModifier=remember{
            Modifier
                .size(30.dp)
                .clip(shape = CircleShape)
                .constrainAs(profileImage, constrainBlock = {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                })}

        if (employeeOnLeave.displayPicture.isEmpty()){
            Image(
                painter = painterResource(id = R.drawable.placeholder_pic),
                contentDescription =null ,
                modifier = imageModifier,
                contentScale = ContentScale.Crop
            )
        }else{
            AsyncImage(
                modifier=imageModifier,
                model = employeeOnLeave.displayPicture,
                contentDescription =null,
                contentScale = ContentScale.Crop,
                placeholder = painterResource(id = R.drawable.profile_pic_placeholder),
                fallback =  painterResource(id = R.drawable.profile_pic_placeholder)
            )
        }


        Text(
            text =employeeOnLeave.name.capitalizeWords(),
            fontSize=14.sp,
            fontWeight=FontWeight.Medium,
            lineHeight=18.2.sp,
            color= announcerColor,
            modifier = Modifier
                .constrainAs(name){
                    start.linkTo(profileImage.end,8.dp)
                    top.linkTo(parent.top)
                }
        )



        Text(
            text =employeeOnLeave.jobRole.capitalizeWords(),
            fontSize=11.sp,
            fontWeight=FontWeight.Normal,
            lineHeight=16.sp,
            //textAlign=TextAlign.Start,
            color= bodyTextLightColor,
            modifier = Modifier
                .constrainAs(designation){
                    start.linkTo(name.start)
                    top.linkTo(name.bottom,4.dp)
                    //width= Dimension.fillToConstraints
                }

        )



        Text(
            text ="Reliever : ${employeeOnLeave.relieverName.capitalizeWords()}",
            fontSize=10.sp,
            fontWeight=FontWeight.Normal,
            lineHeight=16.sp,
            //textAlign=TextAlign.Start,
            color= bodyTextLightColor,
            modifier = Modifier
                .constrainAs(relieverName){
                    start.linkTo(name.start)
                    top.linkTo(designation.bottom,4.dp)
                    bottom.linkTo(parent.bottom,12.dp)
                }

        )




        Text(
            text = getLeavePeriod(employeeOnLeave.startDateString,employeeOnLeave.endDateString),
            fontSize=10.sp,
            fontWeight=FontWeight.Normal,
            lineHeight=13.sp,
            //textAlign=TextAlign.Start,
            color= announcementDateColor,
            modifier = Modifier
                .constrainAs(leavePeriod){
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }

        )
    }
}

@Composable fun AnnouncementList(
    modifier: Modifier = Modifier,
    announcementState: StateFlow<PagingData<Announcement>>,
    onItemClicked: (Announcement) -> Unit
){
    val announcementPagingItems = announcementState.collectAsLazyPagingItems()
    LazyColumn(
        modifier= modifier
            .padding(start = 21.dp, end = 21.dp, top = 24.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ){

        items(announcementPagingItems.itemCount){ index->
            AnnouncementItem(announcement =announcementPagingItems[index]!!,index=index,onItemClicked=onItemClicked)
        }
        handleRefreshState(announcementPagingItems.loadState.refresh,announcementPagingItems)
        handleAppendState(announcementPagingItems.loadState.append)

    }
}


@Composable fun EmployeeOnLeaveList(
    modifier: Modifier = Modifier,
    employeesOnLeaveState: StateFlow<PagingData<co.youverify.youhr.domain.model.EmployeeOnLeave>>
){

    val employeesOnLeavePagingItems = employeesOnLeaveState.collectAsLazyPagingItems()
    LazyColumn(
        modifier= modifier
            .padding(start = 21.dp, end = 21.dp, top = 24.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        items(employeesOnLeavePagingItems.itemCount){ index->
            EmployeeOnLeaveItem(employeeOnLeave = employeesOnLeavePagingItems[index]!!)
        }
        handleEmployeeOnLeaveListRefreshState(employeesOnLeavePagingItems.loadState.refresh,employeesOnLeavePagingItems)
        handleEmployeesOnLeaveListAppendState(employeesOnLeavePagingItems.loadState.refresh)
    }
}


@Composable
fun TopSection(
    modifier: Modifier = Modifier,
    userName: String,
    notificationCount: String,
    profileImageBitmap: ImageBitmap,
    onNotificationClicked: (String) -> Unit,
    onHamburgerClicked: () -> Unit,
    onQuickAccessItemClicked: (Int) -> Unit,
    //onProfilePicClicked: () -> Unit,

    ) {

    Column (
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 36.dp),
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ){
        ProfileSection(
            profileImageBitmap =  profileImageBitmap,
            userName = userName,
            badgeText = notificationCount,
            onNotificationIconClicked = onNotificationClicked,
            onHamburgerIconClicked = onHamburgerClicked,
            //onProfilePicClicked=onProfilePicClicked
        )

        QuickAccessSection(
            imageResIds = arrayOf(R.drawable.clarity_tools_solid,R.drawable.edit_document_sharp,R.drawable.frame),
            stringResIds = arrayOf(R.string.work_tool_request,R.string.document_upload,R.string.leave_request),
            onQuickAccessItemClicked=onQuickAccessItemClicked
        )
    }

}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSection(
    modifier: Modifier = Modifier,
    profileImageBitmap: ImageBitmap,
    userName: String,
    badgeText: String,
    onNotificationIconClicked: (String) -> Unit,
    onHamburgerIconClicked: () -> Unit,
    //onProfilePicClicked: () -> Unit
) {


    ConstraintLayout(modifier= modifier
        .fillMaxWidth()
        //.padding(top = 15.dp)
        //.height(48.dp)
    ) {

        val (profileImage,greetingText,profileName,badgedNotificationIcon,hamburgerIcon) = createRefs()


        IconButton(
            onClick = { onHamburgerIconClicked() },
            modifier = Modifier.constrainAs(hamburgerIcon, constrainBlock = {
                start.linkTo(parent.start,0.dp)
                top.linkTo(parent.top,0.dp)
            }),
            content = { Icon(painter = painterResource(id = R.drawable.ic_hamburger) , contentDescription ="" , tint = yvColor1, modifier = Modifier.size(18.dp,12.dp)) }
        )


            Text(
                text = getGreetingMessage(),
                modifier= Modifier
                    .padding(top = 3.dp)
                    .constrainAs(greetingText, constrainBlock = {
                        top.linkTo(hamburgerIcon.bottom, 18.dp)
                        start.linkTo(hamburgerIcon.start, 0.dp)
                    }),
                fontSize = 12.sp,
                color = yvText,
                //lineHeight = 14.sp,
                fontWeight = FontWeight.W400
            )

            Text(
                text =userName, //userName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                modifier= Modifier
                    .padding(top = 2.dp)
                    .constrainAs(profileName, constrainBlock = {
                        top.linkTo(greetingText.bottom, 4.dp)
                        start.linkTo(greetingText.start, 0.dp)
                    }),
                fontSize = 16.sp,
                color = yvText,
                //lineHeight = 19.sp,
                fontWeight = FontWeight.W600
            )


        BadgedBox(
            modifier = Modifier
                .constrainAs(badgedNotificationIcon, constrainBlock = {
                    end.linkTo(profileImage.start, 20.dp)
                    centerVerticallyTo(profileImage)

                })
                .clickable { onNotificationIconClicked(badgeText) },
             badge = {
                /*Text(
                    text = "5",
                    modifier= Modifier
                        .size(12.dp)
                        .background(color = notificationBadgeColor, shape = CircleShape),
                    fontSize = 8.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )*/
                    },
            content = {Image(painter = painterResource(id = R.drawable.notification), contentDescription ="" )}
        )



        Image(
            modifier= Modifier
                .size(32.dp)
                .clip(shape = CircleShape)
                .constrainAs(profileImage, constrainBlock = {
                    end.linkTo(parent.end, 0.dp)
                }),
                //.clickable { onProfilePicClicked() },
            bitmap =profileImageBitmap ,
            contentDescription ="",
            contentScale = ContentScale.Crop
        )





    }
}



@Composable
fun QuickAccessSection(
    modifier: Modifier = Modifier,
    imageResIds: Array<Int>,
    stringResIds: Array<Int>,
    onQuickAccessItemClicked: (Int) -> Unit
) {
    Row(
        modifier = modifier
            .padding(bottom = 28.dp)
            .fillMaxWidth()
            .border(
                width = 0.3.dp,
                color = quickAccessSectionBorderColor,
                shape = RoundedCornerShape(4.dp)
            ),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(3){
            QuickAccessItem(
                imageResId = imageResIds[it],
                textResId =stringResIds[it],
                index =it,
                onQuickAccessItemClicked=onQuickAccessItemClicked
            )
        }
    }
}




@Composable
fun QuickAccessItem(
    modifier: Modifier = Modifier,
    imageResId: Int,
    index: Int,
    textResId: Int,
    onQuickAccessItemClicked: (Int) -> Unit
){


    val paddingValues=when (index){
        0-> PaddingValues(start = 18.dp, top = 12.dp, bottom = 12.dp)
        1-> PaddingValues(top = 12.dp, bottom = 12.dp)
        else -> PaddingValues(end = 18.dp, top = 12.dp, bottom = 12.dp)
    }

  Column (
      modifier= modifier
          .padding(paddingValues)
          .clip(shape = RoundedCornerShape(12.dp))
          .clickable { onQuickAccessItemClicked(index) }
  ){



      Box(
          modifier = modifier
              .size(79.dp, 42.dp)
              .border(
                  width = 0.3.dp,
                  color = Color(0x3B46B2C8),
                  shape = RoundedCornerShape(
                      topStart = 12.dp,
                      topEnd = 12.dp,
                      bottomStart = 0.dp,
                      bottomEnd = 0.dp
                  )
              )
              .background(color = Color(0X1A46B2C8)),
          content = {
              Image(
                  modifier= modifier
                      .padding(start = 8.dp, top = 2.dp)
                      .align(Alignment.BottomStart),
                  painter = painterResource(id = imageResId) ,
                  contentDescription =""
              )
          }

      )

      Box(
          modifier = modifier
              .size(79.dp, 42.dp)
              .border(
                  width = 0.3.dp,
                  color = Color(0X3B46B2C8),
                  shape = RoundedCornerShape(
                      topStart = 0.dp,
                      topEnd = 0.dp,
                      bottomStart = 12.dp,
                      bottomEnd = 12.dp
                  )
              )
              .background(color = Color.White),
          content = {
              Text(
                  text = stringResource(id = textResId),
                  fontSize = 10.sp,
                  fontWeight=FontWeight.Medium,
                  color = yvText,
                  lineHeight = 13.sp,
                  modifier = Modifier
                      .padding(start = 8.dp, top = 1.dp)
                      .align(Alignment.TopStart)
              )
          }
      )
  }


}

@Composable
fun AnnouncementDetailDialog(
    modifier: Modifier = Modifier,
    onButtonClicked: () -> Unit,
    announcement: Announcement?

) {

    var animateTrigger by remember { mutableStateOf(false) }
    LaunchedEffect(key1 = Unit) {
        launch {
            delay(200)
            animateTrigger = true
        }
    }


    Dialog(
        onDismissRequest = {onButtonClicked()},
        properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false, usePlatformDefaultWidth = false)
    ) {

       // val context= LocalContext.current

        Box {

            AnimatedVisibility(
                //visible = animateTrigger,
                visible=true,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    //horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier
                        .width(305.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(12.dp))
                ) {

                    Row(
                        modifier=Modifier.padding(top=30.dp, start = 30.dp,end=30.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){

                        AsyncImage(
                            model=announcement!!.postedByDisplayPicUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop,
                            placeholder = painterResource(id = R.drawable.profile_pic_placeholder),
                            fallback =  painterResource(id = R.drawable.profile_pic_placeholder)
                        )
                        Text(
                            text = announcement.postedBy,
                            color = bodyTextDeepColor,
                            fontSize = 11.sp,
                            textAlign=TextAlign.Start,
                            modifier=modifier.padding(start = 4.dp, end = 8.dp),
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text ="Posted this",
                            fontSize = 11.sp, color = bodyTextLightColor,
                        )

                    }

                    Text(
                        text = announcement?.createdAt?.toTimeAgo()!!,
                        fontSize = 10.sp, color = bodyTextLightColor,
                        modifier=Modifier.padding(bottom = 8.dp, end = 30.dp).align(Alignment.End)
                    )

                    Image(
                        painter = painterResource(id = R.drawable.announcement_icon_1),
                        contentDescription =null,
                        modifier = Modifier
                            //.size(81.dp)
                            .padding(top = 32.dp, bottom = 32.dp)
                            .align(Alignment.CenterHorizontally),
                        //contentScale = ContentScale.Crop
                    )

                    Text(
                        text = announcement!!.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = bodyTextDeepColor,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )


                    Text(
                        text = announcement.message,
                        fontSize = 12.sp,
                        color = bodyTextLightColor,
                        modifier = Modifier.padding(top=4.dp,start = 30.dp,end=30.dp, bottom=16.dp),
                        lineHeight = 16.sp,
                        textAlign = TextAlign.Center,
                    )



                    Button(
                        onClick = onButtonClicked,
                        shape= RoundedCornerShape(4.dp),
                        modifier = modifier
                            //.padding(horizontal = 16.dp)
                            .padding(start = 30.dp, end = 30.dp, bottom = 26.dp)
                            .fillMaxWidth()
                            .height(40.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryColor)
                    ) {
                        Text(
                            text = "Close",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            lineHeight = 15.6.sp
                        )
                    }
                }
            }
        }
    }


}

@Composable
fun PagerLoaderScreen(modifier: Modifier=Modifier){
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        repeat(6){
            PagerLoaderItem()
        }
    }
}


@Composable
fun PagerLoaderItem() {
    Row(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .size(30.dp)
                .background(color = pagerLoaderColor, shape = CircleShape)
        )
        
        Column(
            modifier = Modifier
                .padding(start = 8.dp, top = 8.dp)
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .padding(bottom = 22.dp)
                    .fillMaxWidth(),
                content = {
                    Box(modifier = Modifier
                        .size(61.5.dp, 8.dp)
                        .background(color = pagerLoaderColor))
                    Spacer(modifier = Modifier.weight(1f))
                    Box(modifier = Modifier
                        .size(36.dp, 4.dp)
                        .background(color = pagerLoaderColor)
                        .align(Alignment.CenterVertically))
                }
            )

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(color = pagerLoaderColor)
            )



        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Preview
@Composable
fun HomePageScreenPreview(){
    YouHrTheme {
        var clickedIndex by remember {mutableStateOf(0)}
        val context=LocalContext.current
        val bitmap=remember{
            BitmapFactory.decodeResource(
                context.resources,
                R.drawable.profile_photo_edith,
                BitmapFactory.Options()
            )
        }

        Surface {

            val pagerState= rememberPagerState()
            val drawerState=rememberDrawerState(initialValue =DrawerValue.Closed )
            val coroutineScope= rememberCoroutineScope()
            HomePageScreen(
                notificationCount = "5",
                profilePhotoBitmap = bitmap,
                onNotificationIconClicked = { },
                onHamburgerClicked = {
                                     coroutineScope.launch {
                                     if (drawerState.isClosed) drawerState.open() else drawerState.close()
                                     }
                },
                pagerState = pagerState,
                onTabItemClicked = { tabIndex->
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(tabIndex)
                    }
                },
                onSideNavItemClicked = {
                           clickedIndex=it
                },
                //onProfilePicClicked = {},
                activeSideNavItemIndex = clickedIndex,
                homeDrawerState = drawerState,
                onQuickAccessItemClicked = {},
                leaveManagementViewModel = LeaveManagementViewModel
                    (Navigator(), GetLeaveRequestsUseCase(LeaveRepoMock()),
                    GetLeaveSummaryUseCase(LeaveRepoMock()), CreateLeaveRequestUseCase(LeaveRepoMock())
                ),
                homeViewModel = HomeViewModel(
                    Navigator(), //GetUserProfileUseCase(ProfileRepoMock()),
                    GetAllAnnouncementUseCase(AnnouncementRepoMock()),
                    GetEmployeesOnLeaveUseCase(LeaveRepoMock())
                ),
                userName = "Edith",
                settingsViewModel = SettingsViewModel(
                    Navigator(), ChangePasswordUseCase(AuthRepoMock()),
                    CreateCodeUseCase(AuthRepoMock(),PreferenceRepoMock()),LoginWithPasswordUseCase(AuthRepoMock(),PreferenceRepoMock()),
                       PreferenceRepoMock(),GetLeaveRequestsUseCase(LeaveRepoMock()),
                    GetTasksUseCase(TasKRepoMock()) ,TokenInterceptor()
                ),
                //uiState = HomePageUiState()
                //profileViewModel = ProfileViewModel(Navigator(), UpdateUserProfileUseCase(ProfileRepoMock()))
                announcementState = MutableStateFlow(PagingData.from(announcements)),
                onAnnouncementItemClicked = {},
                clickedAnnouncementItem = Announcement("","","","",""),
                showAnnouncementDetailDialog = false,
                onAnnouncementDialogCloseButtonClicked = {},
                employeesOnLeaveState = MutableStateFlow(PagingData.from(employeesOnLeave))
            )
        }

    }
}


@Preview
@Composable
fun AnnouncementDetailDialogPreview(){
    YouHrTheme {
        Surface(modifier = Modifier.fillMaxSize()) {

                AnnouncementDetailDialog(
                    onButtonClicked = {},
                    announcement = Announcement(
                        postedBy = "Anita Duru",
                        postedByDisplayPicUrl = "",
                        message = "Ann Baker is getting married on the 30th of May 2023. Transportation arrangements are being made and will be communicated shortly. In the meantime feel free to wish Ann well as she takes on a new journey in life",
                        createdAt = "2023-06-05T13:19:34.525Z",
                        title = "Say Congratulations!"
                    )

                )
        }
    }
}


@Preview
@Composable
fun PagerLoaderScreenPreview(){
    YouHrTheme {
        Surface {
            PagerLoaderScreen(modifier = Modifier.padding(horizontal = 24.dp))
        }
    }
}



/*@Preview
@Composable
fun QuickAccessSectionPreview(){
    YouHrTheme {

        Surface {
            QuickAccessSection(
                imageResIds = arrayOf(R.drawable.clarity_tools_solid,R.drawable.edit_document_sharp,R.drawable.frame),
                stringResIds = arrayOf(R.string.work_tool_request,R.string.document_upload,R.string.leave_request),
                onQuickAccessItemClicked = {}
            )
        }

    }
}

@Preview
@Composable
fun QuickAccessItemPreview(){
    YouHrTheme {

        Surface {
            QuickAccessItem(
                imageResId = R.drawable.clarity_tools_solid,
                index = 1,
                textResId = R.string.work_tool_request,
                onQuickAccessItemClicked = {}
            )
        }

    }
}


@Preview
@Composable
fun AnnouncementItemPreview(){
    YouHrTheme {

        Surface {
            AnnouncementItem(announcement = Announcement(
                announcer = "Richard Cole",
                message = "Good Morning, our get together party is today. Please talk to HR.",
                addressee = "Everyone",
                date = getDateInMillis(2023,2,20)
            ) , index = 0)
        }

    }
}

@Preview
@Composable
fun EmployeeOnLeaveItemPreview(){
    YouHrTheme {

        Surface {
            EmployeeOnLeaveItem(
                employeeOnLeave = EmployeeOnLeave(
                    R.drawable.profile_photo_edith,
                    "sharon chigorom",
                    "product designer",
                    "Abidat akinyele",
                    getDateInMillis(2023,2,20),
                    System.currentTimeMillis()
                )
            )
        }

    }
}


@Preview
@Composable
fun AnnouncementListPreview(){
    YouHrTheme {

        Surface {

            AnnouncementList()
        }

    }
}


@Preview
@Composable
fun EmployeeOnLeaveListPreview(){
    YouHrTheme {

        Surface {

            EmployeeOnLeaveList()
        }

    }
}


@Preview
@Composable
fun SideNavPreview(){
    YouHrTheme {

        Surface {

            SideNav(
                onItemClicked ={} ,
                activeIndex = 1,
                drawerState= rememberDrawerState(initialValue = DrawerValue.Closed)
            )
        }

    }
}*/


data class EmployeeOnLeave(val photoResId:Int, val name:String, val designation:String, val reliever:String, val leaveStartDate:Long, val leaveEndDate:Long)
data class SideNavItem(val imageResId:Int, val text:String)


val announcementItemAvatarColors= listOf(
    Color(0xFF023A59),
    Color(0xFF6941C6),
    Color(0xFFB54708),
    Color(0xFF175CD3)
)

val sideNavItems= listOf(
    SideNavItem(R.drawable.material_symbols_line_start_square_rounded,"Get Started"),
   // SideNavItem(R.drawable.ic_baseline_home,"Homepage"),
    //SideNavItem(R.drawable.fluent_task_list_square_settings_20_filled,"Worktool Management"),
    SideNavItem(R.drawable.clarity_administrator_solid,"Leave Management")
)


val dummyAnnouncement=Announcement(
    postedBy = "Richard Cole",
    message = "Good Morning, our get together party is today. Please talk to HR.",
    createdAt = "2023-07-06T13:19:34.525Z",
    title = "Say Congratulations",
    postedByDisplayPicUrl = ""
    )

val announcements= listOf(
    dummyAnnouncement,
    dummyAnnouncement.copy(postedBy = "Tehila Smith", message = "Today is Tehila’s birthday, say happy birthday to her!!!"),
    dummyAnnouncement.copy(postedBy = "Ann Baker", message = "Getting Married soon, Congratulations!!!!!"),
    dummyAnnouncement.copy(postedBy = "Yetunde Banjo", message = "Yetunde just lost her mum, Please pay her a condolence visit"),
    dummyAnnouncement.copy(postedBy = "Favour Ceasar", message = "Favor just put to birth, say Congratulations!!!"),
    dummyAnnouncement.copy(postedBy = "Isreal Davochi", message = "Wishing Everyone a great day ahead"),

    )

val dummyEmployee=EmployeeOnLeave(
    name = "Edna Ibeh",
    jobRole = "Product Designer",
    relieverName = "Yusuf Babatunde",
    startDateString = "Mon Jul 03 2023 00:00:00 GMT+0000",
    endDateString = "Thu Jul 20 2023 00:00:00 GMT+0000",
    displayPicture = ""
)


val employeesOnLeave= listOf(
    dummyEmployee,
    dummyEmployee.copy( name = "Isreal Ajadi", jobRole = "Frontend Engineer", relieverName = "Fortune Goodluck"),
    dummyEmployee.copy( name = "Timothy John", jobRole = "Product Manager", relieverName = "Bolanle Fadehinde"),
    dummyEmployee.copy( name = "Donald Njaoguani", jobRole = "Human Resource Manager", relieverName = "Jacob Olumide"),
    dummyEmployee.copy(name = "Tracy Mark", jobRole = "Human Resource Manager", relieverName = "Joseph Daniel"),
    dummyEmployee.copy( name = "Kene Nsofor", jobRole = "Devops Engineer", relieverName = "Omolade Cynthia")




)






@OptIn(ExperimentalPagerApi::class)
fun Modifier.ownTabIndicatorOffset(
    currentTabPosition: TabPosition,
    pagerState: PagerState
): Modifier = composed(
    inspectorInfo = debugInspectorInfo {
        name = "tabIndicatorOffset"
        value = currentTabPosition
    }
) {

    val padding= if (pagerState.currentPage==0) ((currentTabPosition.width-117.dp).value/2.0).dp
    else ((currentTabPosition.width-141.dp).value/2.0).dp

    val currentTabWidth by animateDpAsState(
        targetValue = if (pagerState.currentPage==0) 117.dp else 141.dp,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    val indicatorOffset by animateDpAsState(
        targetValue = currentTabPosition.left,
        animationSpec = tween(durationMillis = 250, easing = FastOutSlowInEasing)
    )
    //fillMaxWidth()
    wrapContentSize(Alignment.BottomStart)
        .padding(horizontal = padding)
        .offset(x = indicatorOffset)
        .width(currentTabWidth)
}

class ProfileRepoMock:ProfileRepository{
    override suspend fun getUserProfile(): Flow<Result<User>> { return flow{} }
    override suspend fun filterAllUser(): Flow<Result<List<FilteredUser>>> {
        return flow {  }
    }

    override suspend fun updateUserProfile(request: UpdateUserProfileRequest): Result<UserProfileResponse> {
        return Result.Error(500,"")

    }

    override suspend fun updateUserProfilePic(imageFile: MultipartBody.Part): Result<UserProfileResponse> {
        return Result.Error(500,"")
    }

    override suspend fun filterAllLineManager(): Flow<Result<List<FilteredUser>>> {
        return flow {  }
    }

}
class AnnouncementRepoMock:AnnouncementRepository{
    override suspend fun getAllAnnouncement(): Flow<PagingData<Announcement>> {
        return flow{}
    }

}

fun LazyListScope.handleRefreshState(
    state: LoadState,
    announcementList: LazyPagingItems<Announcement>,
    //onItemClicked: (Announcement) -> Unit
){
    when(state){
        is LoadState.Loading->{
            item { PagerLoaderScreen(modifier = Modifier.fillMaxSize()) }
        }

        is LoadState.Error->{
            item {
                ConnectionErrorScreen(
                    description = state.error.message!!,
                    onRetryButtonClicked ={announcementList.retry()},
                    modifier = Modifier.fillMaxSize()
                )
            }
        }


        is LoadState.NotLoading -> {

        }
    }
}
fun LazyListScope.handleAppendState(
    state:LoadState,
    //announcementList: LazyPagingItems<Announcement>,
    //onItemClicked: (Announcement) -> Unit
){
    when(state){
        is LoadState.Loading->{
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){ CircularProgressIndicator() }
            }
        }

        is LoadState.Error->{
            item {
                var showErrorText by remember {mutableStateOf(true)}
                LaunchedEffect(key1 = Unit){
                    delay(2000)
                    showErrorText=false
                }
                if (showErrorText)
                    Text(text = state.error.message!!, modifier = Modifier.padding(8.dp), color = Color.Red)
            }
        }


        is LoadState.NotLoading -> {

        }
    }
}



fun LazyListScope.handleEmployeeOnLeaveListRefreshState(
    state: LoadState,
    employeesOnLeaveList: LazyPagingItems<EmployeeOnLeave>,
){
    when(state){
        is LoadState.Loading->{
            item { PagerLoaderScreen(modifier = Modifier.fillMaxSize()) }
        }

        is LoadState.Error->{
            item {
                ConnectionErrorScreen(
                    description = state.error.message!!,
                    onRetryButtonClicked ={employeesOnLeaveList.retry()},
                    modifier = Modifier.fillMaxSize()
                )
            }
        }


        is LoadState.NotLoading -> {

        }
    }
}
fun LazyListScope.handleEmployeesOnLeaveListAppendState(
    state:LoadState,
    //employeesOnLeaveList: LazyPagingItems<EmployeeOnLeave>,

    ){
    when(state){
        is LoadState.Loading->{
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){ CircularProgressIndicator() }
            }
        }

        is LoadState.Error->{
            item {
                var showErrorText by remember {mutableStateOf(true)}
                LaunchedEffect(key1 = Unit){
                    delay(2000)
                    showErrorText=false
                }
                if (showErrorText)
                    Text(text = state.error.message!!, modifier = Modifier.padding(8.dp), color = Color.Red)
            }
        }


        is LoadState.NotLoading -> {

        }
    }
}





