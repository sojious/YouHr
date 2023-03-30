package co.youverify.youhr.presentation.ui.home

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import co.youverify.youhr.R
import co.youverify.youhr.core.util.capitalizeWords
import co.youverify.youhr.core.util.getLeavePeriod
import co.youverify.youhr.core.util.toOrdinalDateString
import co.youverify.youhr.presentation.ui.components.TextAvatar
import co.youverify.youhr.presentation.ui.theme.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomePageScreen(
    //modifier: Modifier = Modifier,
    userName: String,
    notificationCount: String,
    profilePhotoResId: Int,
    onNotificationIconClicked: (String) -> Unit,
    onHamburgerClicked: () -> Unit,
    pagerState: PagerState,
    onTabItemClicked: (Int) -> Unit,
    onSideNavItemClicked: (Int) -> Unit,
    activeSideNavItemIndex: Int,
    homeDrawerState:DrawerState

    ){


    ModalNavigationDrawer(
       // modifier = modifier.fillMaxSize(),
        drawerState = homeDrawerState,
        gesturesEnabled = true,
        drawerContent = {
                SideNav(
                    onItemClicked = onSideNavItemClicked,
                    activeIndex = activeSideNavItemIndex
                )
        },
        content = {
            ScreenContent(
                modifier =Modifier.fillMaxSize(),
                userName = "Edith",
                notificationCount =notificationCount,
                profilePhotoResId =profilePhotoResId,
                onNotificationClicked = onNotificationIconClicked,
                onHamburgerClicked = onHamburgerClicked,
                pagerState = pagerState,
                onTabClicked =onTabItemClicked
            )
        }
    )

}

@Composable
fun SideNav(
    modifier: Modifier=Modifier,
    onItemClicked:(Int)->Unit,
    activeIndex:Int

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
                    active = index==activeIndex
                )
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
    active:Boolean
){

    val iconTintColor= if (active) Color.White else sideNavIconTintColor.copy(alpha =0.5f )
    val textColor= if (active) Color.White else sideNavIconTextColor
    val dividerColor= if (active) Color.White else primaryColor
    val backgroundColor= if (active) yvColor else primaryColor
    val firstItemPadding=if (position==0) PaddingValues(top = 30.dp) else PaddingValues()
    ConstraintLayout(
        modifier = Modifier
            .fillMaxWidth()
            .height(35.dp)
            .background(color = backgroundColor)
            //.padding(firstItemPadding)
            .clickable { onClick(position) }
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
fun ScreenContent(
    modifier: Modifier,
    userName: String,
    notificationCount: String,
    profilePhotoResId: Int,
    onNotificationClicked: (String) -> Unit,
    onHamburgerClicked: () -> Unit,
    pagerState: PagerState,
    onTabClicked: (Int) -> Unit,
) {

    Column(modifier = modifier.fillMaxSize()) {
        TopSection(
            userName = userName,
            notificationCount =notificationCount ,
            profilePhotoResId =profilePhotoResId,
            onNotificationClicked = onNotificationClicked,
            onHamburgerClicked = onHamburgerClicked
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .background(color = surfaceGrey)
        )
        TabSection( pagerState = pagerState, onTabClicked = onTabClicked)

    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun TabSection(
    modifier: Modifier = Modifier,
    pagerState: PagerState,
    onTabClicked:(Int) -> Unit
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

        /*LazyColumn{
            items(items = listOf("")){

            }
        }*/

        HorizontalPager(
            count = 2,
            state = pagerState,
            //modifier = Modifier.padding(start = 21.dp, end = 21.dp,top=24.dp)
        ) {currentPage->

            if (currentPage==0) AnnouncementList() else EmployeeOnLeaveList()

        }


    }

   // AnnouncementItem()
    //EmployeeOnLeaveItem()
}

@Composable
fun AnnouncementItem(announcement: Announcement,index: Int) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
        val (textAvatar,announcerName,announcementContent, addresseeName, date) =createRefs()


        //Text Avatar
        val color= announcementItemAvatarColors[index%announcementItemAvatarColors.size]

        TextAvatar(
            modifier=Modifier.constrainAs(textAvatar) {
                start.linkTo(parent.start)
                top.linkTo(parent.top)
            },
            color = color,
            text = announcement.announcer[0].uppercaseChar().toString()
        )


        //AnnouncerName

        Text(
            text =announcement.announcer,
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
            color= bodyTextColor,
            modifier = Modifier
                .constrainAs(announcementContent){
                    start.linkTo(announcerName.start)
                    end.linkTo(parent.end)
                    top.linkTo(announcerName.bottom,4.dp)
                    width= Dimension.fillToConstraints
                }

        )


        //To Section

        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(color = bodyTextColor)){
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

        )



        //Date
        Text(
            text = announcement.date.toOrdinalDateString(),
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

        Image(
            modifier= Modifier
                .size(30.dp)
                .clip(shape = CircleShape)
                .constrainAs(profileImage, constrainBlock = {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }),
            painter = painterResource(id = employeeOnLeave.photoResId) ,
            contentDescription =null
        )

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
            text =employeeOnLeave.designation.capitalizeWords(),
            fontSize=11.sp,
            fontWeight=FontWeight.Normal,
            lineHeight=16.sp,
            //textAlign=TextAlign.Start,
            color= bodyTextColor,
            modifier = Modifier
                .constrainAs(designation){
                    start.linkTo(name.start)
                    top.linkTo(name.bottom,4.dp)
                    //width= Dimension.fillToConstraints
                }

        )



        Text(
            text ="Reliever : ${employeeOnLeave.reliever.capitalizeWords()}",
            fontSize=10.sp,
            fontWeight=FontWeight.Normal,
            lineHeight=16.sp,
            //textAlign=TextAlign.Start,
            color= bodyTextColor,
            modifier = Modifier
                .constrainAs(relieverName){
                    start.linkTo(name.start)
                    top.linkTo(designation.bottom,4.dp)
                    bottom.linkTo(parent.bottom,12.dp)
                }

        )




        Text(
            text = getLeavePeriod(employeeOnLeave.leaveStartDate,employeeOnLeave.leaveEndDate),
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

@Composable fun AnnouncementList(modifier: Modifier=Modifier){
    LazyColumn(
        modifier= modifier
            .padding(start = 21.dp, end = 21.dp, top = 24.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        itemsIndexed(items = announcements){ index,announcement->
            AnnouncementItem(announcement =announcement,index=index)
            //if (index< announcements.size-1) Divider(thickness = 0.2.dp, color = dividerColor)
        }
    }
}


@Composable fun EmployeeOnLeaveList(modifier: Modifier=Modifier){
    LazyColumn(
        modifier= modifier
            .padding(start = 21.dp, end = 21.dp, top = 24.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ){
        itemsIndexed(items = employeesOnLeave){ index,employee->
            EmployeeOnLeaveItem(employeeOnLeave = employee)
            if (index< employeesOnLeave.lastIndex) Divider(thickness = 0.2.dp, color = dividerColor)
        }
    }
}


@Composable
fun TopSection(
    modifier: Modifier = Modifier,
    userName: String,
    notificationCount: String,
    profilePhotoResId: Int,
    onNotificationClicked: (String) -> Unit,
    onHamburgerClicked: () -> Unit,

    ) {

    Column (
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 36.dp),
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ){
        ProfileSection(
            profilePhotoResId =  profilePhotoResId,
            userName = userName,
            badgeText = notificationCount,
            onNotificationIconClicked = onNotificationClicked,
            onHamburgerIconClicked = onHamburgerClicked
        )

        QuickAccessSection(
            imageResIds = arrayOf(R.drawable.clarity_tools_solid,R.drawable.edit_document_sharp,R.drawable.frame),
            stringResIds = arrayOf(R.string.work_tool_request,R.string.document_upload,R.string.leave_request)
        )
    }

}



/*@Composable
fun Announcements(modifier: Modifier=Modifier) {

}*/

/*@Composable
fun EmployeesOnLeave(modifier: Modifier=Modifier) {
}*/

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileSection(
    modifier: Modifier = Modifier,
    profilePhotoResId: Int,
    userName: String,
    badgeText: String,
    onNotificationIconClicked: (String) -> Unit,
    onHamburgerIconClicked: () -> Unit
) {


    ConstraintLayout(modifier= modifier
        .fillMaxWidth()
        //.padding(top = 5.dp)
        .height(48.dp)
    ) {

        val (profileImage,greetingText,profileName,badgedNotificationIcon,hamburgerIcon) = createRefs()
        Image(
            modifier= Modifier
                .size(48.dp)
                .clip(shape = CircleShape)
                .constrainAs(profileImage, constrainBlock = {
                    start.linkTo(parent.start, 0.dp)
                }),
            painter = painterResource(id =R.drawable.profile_photo_edith) ,
            contentDescription =""
        )


            Text(
                text = "Good Morning!",
                modifier= Modifier
                    .padding(top = 3.dp)
                    .constrainAs(greetingText, constrainBlock = {
                        top.linkTo(parent.top, 4.dp)
                        start.linkTo(profileImage.end, 2.dp)
                    }),
                fontSize = 12.sp,
                color = yvText,
                //lineHeight = 14.sp,
                fontWeight = FontWeight.W400
            )

            Text(
                text ="Edith", //userName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                modifier= Modifier
                    .padding(top = 2.dp)
                    .constrainAs(profileName, constrainBlock = {
                        top.linkTo(greetingText.bottom, 0.dp)
                        start.linkTo(profileImage.end, 2.dp)
                    }),
                fontSize = 16.sp,
                color = yvText,
                //lineHeight = 19.sp,
                fontWeight = FontWeight.W600
            )


        BadgedBox(
            modifier = Modifier
                .constrainAs(badgedNotificationIcon, constrainBlock = {
                    end.linkTo(hamburgerIcon.start, 20.dp)
                    centerVerticallyTo(parent)
                })
                .clickable { onNotificationIconClicked(badgeText) },
            badge = {
                Text(
                    text = "5",
                    modifier= Modifier
                        .size(12.dp)
                        .background(color = notificationBadgeColor, shape = CircleShape),
                    fontSize = 8.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                    },
            content = {Image(painter = painterResource(id = R.drawable.notification), contentDescription ="" )}
        )


        IconButton(
            onClick = { onHamburgerIconClicked() },
            modifier = Modifier.constrainAs(hamburgerIcon, constrainBlock = {
                end.linkTo(parent.end,0.dp)
                centerVerticallyTo(parent)
            }),
            content = { Icon(painter = painterResource(id = R.drawable.ic_hamburger) , contentDescription ="" , tint = yvColor1, modifier = Modifier.size(18.dp,12.dp)) }
        ) 


    }
}



@Composable
fun QuickAccessSection(
    modifier: Modifier=Modifier,
    imageResIds:Array<Int>,
    stringResIds:Array<Int>
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
            QuickAccessItem(imageResId = imageResIds[it], textResId =stringResIds[it], index =it)
        }
    }
}




@Composable
fun QuickAccessItem(modifier: Modifier = Modifier, imageResId: Int, index: Int, textResId: Int){


    val paddingValues=when (index){
        0-> PaddingValues(start = 18.dp, top = 12.dp, bottom = 12.dp)
        1-> PaddingValues(top = 12.dp, bottom = 12.dp)
        else -> PaddingValues(end = 18.dp, top = 12.dp, bottom = 12.dp)
    }

  Column (
      modifier= modifier
          .padding(paddingValues)
          .clip(shape = RoundedCornerShape(12.dp))
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


@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Preview
@Composable
fun HomePageScreenPreview(){
    YouHrTheme {
        var clickedIndex by remember {mutableStateOf(1)}

        Surface {

            val pagerState= rememberPagerState()
            val drawerState=rememberDrawerState(initialValue =DrawerValue.Closed )
            val coroutineScope= rememberCoroutineScope()
            HomePageScreen(
                userName = "Edith",
                notificationCount = "5",
                profilePhotoResId = R.drawable.profile_photo,
                homeDrawerState = drawerState,
                onNotificationIconClicked = { },
                onHamburgerClicked = {
                                     coroutineScope.launch {
                                     if (drawerState.isClosed) drawerState.open() else drawerState.close()
                                     }
                },
                pagerState = pagerState,
                onTabItemClicked = {tabIndex->
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(tabIndex)
                    }
                },
                onSideNavItemClicked = {
                           clickedIndex=it
                },
                activeSideNavItemIndex = clickedIndex
            )
        }

    }
}

@Preview
@Composable
fun QuickAccessSectionPreview(){
    YouHrTheme {

        Surface {
            QuickAccessSection(
                imageResIds = arrayOf(R.drawable.clarity_tools_solid,R.drawable.edit_document_sharp,R.drawable.frame),
                stringResIds = arrayOf(R.string.work_tool_request,R.string.document_upload,R.string.leave_request)
            )
        }

    }
}

@Preview
@Composable
fun QuickAccessItemPreview(){
    YouHrTheme {

        Surface {
            QuickAccessItem(imageResId = R.drawable.clarity_tools_solid, index = 1, textResId = R.string.work_tool_request)
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
                date = getDate()
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
                    R.drawable.profile_photo,
                    "sharon chigorom",
                    "product designer",
                    "Abidat akinyele",
                    getDate(),
                    Date()
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

            SideNav(onItemClicked ={} , activeIndex = 1)
        }

    }
}

data class Announcement(val announcer:String, val message:String, val addressee:String, val date:Date)

data class EmployeeOnLeave(val photoResId:Int, val name:String, val designation:String, val reliever:String, val leaveStartDate:Date, val leaveEndDate:Date)
data class SideNavItem(val imageResId:Int, val text:String)


val announcementItemAvatarColors= listOf(
    Color(0xFF023A59),
    Color(0xFF6941C6),
    Color(0xFFB54708),
    Color(0xFF175CD3)
)

val sideNavItems= listOf(
    SideNavItem(R.drawable.material_symbols_line_start_square_rounded,"Get Started"),
    SideNavItem(R.drawable.ic_baseline_home,"Homepage"),
    SideNavItem(R.drawable.fluent_task_list_square_settings_20_filled,"Resources"),
    SideNavItem(R.drawable.clarity_administrator_solid,"Leave Management")
)


val dummyAnnouncement=Announcement(
    announcer = "Richard Cole",
    message = "Good Morning, our get together party is today. Please talk to HR.",
    addressee = "Everyone",
    date = getDate()
)

val announcements= listOf(
    dummyAnnouncement,
    dummyAnnouncement.copy(announcer = "Tehila Smith", message = "Today is Tehila’s birthday, say happy birthday to her!!!"),
    dummyAnnouncement.copy(announcer = "Ann Baker", message = "Getting Married soon, Congratulations!!!!!"),
    dummyAnnouncement.copy(announcer = "Yetunde Banjo", message = "Yetunde just lost her mum, Please pay her a condolence visit"),
    dummyAnnouncement.copy(announcer = "Favour Ceasar", message = "Favor just put to birth, say Congratulations!!!"),
    dummyAnnouncement.copy(announcer = "Isreal Davochi", message = "Wishing Everyone a great day ahead"),

    )

val dummyEmployee=EmployeeOnLeave(
    R.drawable.profile_photo2,
    "Edna Ibeh",
    "Product Designer",
    "Yusuf Babatunde",
    getDate(),
    Date()
)

val employeesOnLeave= listOf(
    dummyEmployee,
    dummyEmployee.copy(photoResId = R.drawable.profile_photo_israel_ajadi, name = "Isreal Ajadi", designation = "Frontend Engineer", reliever = "Fortune Goodluck"),
    dummyEmployee.copy(photoResId = R.drawable.friend_profile, name = "Timothy John", designation = "Product Manager", reliever = "Bolanle Fadehinde"),
    dummyEmployee.copy(photoResId = R.drawable.profile_photo_donald_njaoguani, name = "Donald Njaoguani", designation = "Human Resource Manager", reliever = "Jacob Olumide"),
    dummyEmployee.copy(photoResId = R.drawable.profile_photo_tracy_mark, name = "Tracy Mark", designation = "Human Resource Manager", reliever = "Joseph Daniel"),
    dummyEmployee.copy(photoResId = R.drawable.profile_photo4, name = "Kene Nsofor", designation = "Devops Engineer", reliever = "Omolade Cynthia")




)

fun getDate(): Date {
    val cal=Calendar.getInstance()
    cal.set(2023,2,22)
    return cal.time
}


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


