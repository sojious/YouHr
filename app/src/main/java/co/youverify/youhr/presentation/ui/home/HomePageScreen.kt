package co.youverify.youhr.presentation.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import co.youverify.youhr.R
import co.youverify.youhr.presentation.ui.theme.*
import java.util.*

@Composable
fun HomePageScreen(
    //modifier: Modifier = Modifier,
    userName: String,
    notificationCount: String,
    profilePhotoResId: Int,
    onNotificationIconClicked: () -> Unit,
    onHamburgerClicked: (String) -> Unit
){

    ModalNavigationDrawer(
       // modifier = modifier.fillMaxSize(),
        gesturesEnabled = true,
        drawerContent = {

        },
        content = {
            ScreenContent(
                modifier=Modifier.fillMaxSize(),
                userName = userName,
                notificationCount =notificationCount ,
                profilePhotoResId=profilePhotoResId,
                onNotificationClicked = onNotificationIconClicked,
                onHamburgerClicked = onHamburgerClicked
            )
        }
    )

}

@Composable
fun ScreenContent(
    modifier: Modifier,
    userName: String,
    notificationCount: String,
    profilePhotoResId: Int,
    onNotificationClicked: () -> Unit,
    onHamburgerClicked: (String) -> Unit
) {

    Column(modifier = modifier.fillMaxSize()) {
        TopSection(
            userName = userName,
            notificationCount =notificationCount ,
            profilePhotoResId=profilePhotoResId,
            onNotificationClicked = onNotificationClicked,
            onHamburgerClicked = onHamburgerClicked
        )

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(12.dp)
                .background(color = surfaceGrey)
        )
        TabSection(selectedTabIndex = 0)

    }
}

@Composable
fun TabSection(modifier: Modifier = Modifier, selectedTabIndex: Int) {


    Column {
        TabRow(
            selectedTabIndex = selectedTabIndex,
            containerColor = Color.White
        ) {



            Tab(selected = selectedTabIndex==0, onClick = {},modifier=Modifier.padding(vertical = 12.dp)) {
                Text(
                    text = "Announcements",
                    color = if (selectedTabIndex==0) yvColor1 else unselectedTabColor,
                    fontSize = 14.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Tab(selected = selectedTabIndex==1, onClick = {},modifier=Modifier.padding(vertical = 12.dp)) {
                Text(
                    text = "Employees On Leave",
                    color = if (selectedTabIndex==1) yvColor1 else unselectedTabColor,
                    fontSize = 14.sp,
                    lineHeight = 24.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        LazyColumn(){
            items(items = listOf("")){

            }
        }
    }
}


@Composable
fun TopSection(
    modifier: Modifier = Modifier,
    userName: String,
    notificationCount: String,
    profilePhotoResId: Int,
    onNotificationClicked: () -> Unit,
    onHamburgerClicked: (String) -> Unit,

    ) {

    Column (
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp, top = 25.dp),
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
    onNotificationIconClicked:()->Unit,
    onHamburgerIconClicked:(String)->Unit
) {


    ConstraintLayout(modifier= modifier
        .fillMaxWidth()
        .padding(top = 30.dp)
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
            painter = painterResource(id = profilePhotoResId) ,
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
                text = userName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
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
                .clickable { onNotificationIconClicked() },
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
            onClick = { onHamburgerIconClicked(badgeText) },
            modifier = Modifier.constrainAs(hamburgerIcon, constrainBlock = {
                end.linkTo(parent.end,0.dp)
                centerVerticallyTo(parent)
            }),
            content = { Icon(imageVector =Icons.Default.Menu , contentDescription ="" , tint = yvColor1, modifier = Modifier.size(18.dp,12.dp)) }
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
            .border(width = 0.3.dp, color = yvColor, shape = RoundedCornerShape(4.dp)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        repeat(3){
            QuickAccessItem(imageResId = imageResIds[it], textResId =stringResIds[it], index =it,)
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


@Preview
@Composable
fun HomePageScreenPreview(){
    YouHrTheme {

        Surface {
            HomePageScreen(
                userName = "Oluwayemisi",
                notificationCount = "5",
                profilePhotoResId = R.drawable.profile_photo,
                onNotificationIconClicked = { },
                onHamburgerClicked = {}
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
