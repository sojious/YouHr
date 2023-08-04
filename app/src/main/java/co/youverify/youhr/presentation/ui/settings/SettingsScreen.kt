package co.youverify.youhr.presentation.ui.settings

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.youverify.youhr.R
import co.youverify.youhr.data.remote.TokenInterceptor
import co.youverify.youhr.domain.use_case.ChangePasswordUseCase
import co.youverify.youhr.domain.use_case.CreateCodeUseCase
import co.youverify.youhr.domain.use_case.GetAllAnnouncementUseCase
import co.youverify.youhr.domain.use_case.GetEmployeesOnLeaveUseCase
import co.youverify.youhr.domain.use_case.GetLeaveRequestsUseCase
import co.youverify.youhr.domain.use_case.GetTasksUseCase
import co.youverify.youhr.domain.use_case.GetUserProfileUseCase
import co.youverify.youhr.domain.use_case.LoginWithPasswordUseCase
import co.youverify.youhr.presentation.ui.Navigator
import co.youverify.youhr.presentation.ui.components.LoadingDialog
import co.youverify.youhr.presentation.ui.components.YouHrTitleBarNoArrow
import co.youverify.youhr.presentation.ui.home.AnnouncementRepoMock
import co.youverify.youhr.presentation.ui.home.HomeViewModel
import co.youverify.youhr.presentation.ui.home.ProfileRepoMock
import co.youverify.youhr.presentation.ui.leave.AuthRepoMock
import co.youverify.youhr.presentation.ui.leave.LeaveRepoMock
import co.youverify.youhr.presentation.ui.leave.PreferenceRepoMock
import co.youverify.youhr.presentation.ui.theme.YouHrTheme
import co.youverify.youhr.presentation.ui.theme.bodyTextDeepColor
import co.youverify.youhr.presentation.ui.theme.bodyTextLightColor
import co.youverify.youhr.presentation.ui.theme.deactivatedColorDeep
import co.youverify.youhr.presentation.ui.theme.inputDeepTextColor
import co.youverify.youhr.presentation.ui.theme.yvText

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    profileBitmap: Bitmap?,
    name: String,
    email: String,
    onSettingsItemClicked: (Int) -> Unit,
    onProfilePicClicked: () -> Unit,
    settingsViewModel: SettingsViewModel,
    homeViewModel: HomeViewModel,
    loading: Boolean
){

    LaunchedEffect(key1 = Unit){
        settingsViewModel.updateCurrentUser(homeViewModel.user)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {



        YouHrTitleBarNoArrow(title = "Settings", modifier = Modifier.padding(top = 32.dp, bottom = 32.dp))

        UserInfo(
            modifier =Modifier.padding(start = 20.dp),
            name =name, email =email,
            profileBitmap =profileBitmap,
            onProfilePicClicked =onProfilePicClicked
        )

        Divider(
            modifier= Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 37.dp),
            thickness = 0.4.dp,
            color = deactivatedColorDeep
        )

        SettingsList(modifier=Modifier.padding(start = 20.dp, end = 37.dp), onItemClicked = onSettingsItemClicked)

        if (loading){
            LoadingDialog(message = "Logging you out..")
        }
    }
}

@Composable
fun SettingsList(modifier: Modifier = Modifier, onItemClicked: (Int) -> Unit) {
    Column(
        modifier=modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        repeat(3){
            SettingsItem(item=items[it], index = it, onItemClicked = onItemClicked)
        }
    }
}

@Composable
fun SettingsItem(index: Int, onItemClicked: (Int) -> Unit, item: Item) {
    Row(
        horizontalArrangement =Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onItemClicked(index + 1)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(id = item.imageResId), contentDescription =null )
        Text(text = item.text, fontSize = 14.sp, color = inputDeepTextColor)
        Spacer(Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.stroke_1), contentDescription =null,

        )
    }
}

@Composable
fun UserInfo(
    modifier: Modifier = Modifier,
    name: String,
    email: String,
    profileBitmap: Bitmap?,
    onProfilePicClicked: () -> Unit
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Image(
            bitmap = profileBitmap?.asImageBitmap()?: ImageBitmap.imageResource(id = R.drawable.placeholder_pic),
            contentDescription =null ,
            modifier= Modifier
                .size(55.dp)
                .clip(shape = CircleShape)
                .clickable { onProfilePicClicked() },
            contentScale = ContentScale.Crop
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(start = 16.dp)
        ) {
            Text(text = name, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = bodyTextDeepColor)
            Text(text = email, fontSize = 12.sp, color = bodyTextLightColor)
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview(){
    YouHrTheme {
        Surface {
            val context= LocalContext.current
            val bitmap= remember{
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.profile_photo_edith,
                    BitmapFactory.Options()
                )
            }
           SettingsScreen(
               profileBitmap = bitmap,
               name = "Edith",
               email = "edith@youverify.co",
               onSettingsItemClicked ={_->},
               onProfilePicClicked = {},
               settingsViewModel = SettingsViewModel(
                   Navigator(),
                   ChangePasswordUseCase(AuthRepoMock()),
                   CreateCodeUseCase(AuthRepoMock(),PreferenceRepoMock()),
                   LoginWithPasswordUseCase(AuthRepoMock(),PreferenceRepoMock()),
                   PreferenceRepoMock(),
                   GetLeaveRequestsUseCase(LeaveRepoMock()),
                   GetTasksUseCase(TasKRepoMock()),
                   TokenInterceptor(),
               ),
               homeViewModel = HomeViewModel(
                   Navigator(),// GetUserProfileUseCase(ProfileRepoMock()),
                   GetAllAnnouncementUseCase(AnnouncementRepoMock()), GetEmployeesOnLeaveUseCase(LeaveRepoMock())
               ),
               loading = false
           )
        }
    }
}

@Preview
@Composable
fun UserInfoPreview(){
    YouHrTheme {
        Surface {
            val context= LocalContext.current
            val bitmap= remember{
                BitmapFactory.decodeResource(
                    context.resources,
                    R.drawable.profile_photo_edith,
                    BitmapFactory.Options()
                )
            }
            UserInfo(
                name ="Edith Ibeh",
                email ="Edith@youverify.co",
                profileBitmap =bitmap
            ) {}
        }
    }
}

@Preview
@Composable
fun SettingsListPreview(){
    YouHrTheme {
        Surface {
            SettingsList(onItemClicked = { _ -> })
        }
    }
}

@Preview
@Composable
fun SettingsItemPreview(){
    YouHrTheme {
       Surface {
           SettingsItem(
               item = items[0],
               index = 0,
               onItemClicked = {_->}
           )
       }
    }
}



data class Item(val imageResId:Int,val text:String)
val items= listOf(
    Item(R.drawable.ic_paddlock,"Change password"),
    Item(R.drawable.ic_key,"Change passcode"),
    Item(R.drawable.ic_log_out,"Logout"),
    //Item(R.drawable.ic_support,"Support"),

)