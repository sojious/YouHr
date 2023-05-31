package co.youverify.youhr.presentation.ui.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.youverify.youhr.R
import co.youverify.youhr.presentation.ui.theme.YouHrTheme
import co.youverify.youhr.presentation.ui.theme.bodyTextDeepColor
import co.youverify.youhr.presentation.ui.theme.bodyTextLightColor
import co.youverify.youhr.presentation.ui.theme.deactivatedColorDeep
import co.youverify.youhr.presentation.ui.theme.inputDeepTextColor
import co.youverify.youhr.presentation.ui.theme.yvText

@Composable
fun SettingsScreen(
    modifier: Modifier=Modifier,
    user: User,
    onSettingsItemClicked: (Int) -> Unit,
    onProfilePicClicked:() -> Unit
){

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {

        Box(
            modifier = Modifier
                .padding(top = 32.dp, bottom = 32.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
            content = {
                Text(
                    text = "Settings",
                    color = yvText,
                    fontSize =16.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center,
                )
            }
        )

        UserInfo(
            modifier=Modifier.padding(start = 20.dp),
            name=user.name,email=user.email,
            profileResId=user.profileResId?: R.drawable.profile_pic_placeholder,
            onProfilePicClicked=onProfilePicClicked
        )

        Divider(
            modifier= Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 37.dp),
            thickness = 0.4.dp,
            color = deactivatedColorDeep
        )

        SettingsList(modifier=Modifier.padding(start = 20.dp, end = 37.dp), onItemClicked = onSettingsItemClicked)
    }
}

@Composable
fun SettingsList(modifier: Modifier = Modifier, onItemClicked: (Int) -> Unit) {
    Column(
        modifier=modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        repeat(4){
            SettingsItem(item=items[it], index = it, onItemClicked = onItemClicked)
        }
    }
}

@Composable
fun SettingsItem(index: Int, onItemClicked: (Int) -> Unit, item: Item) {
    Row(
        horizontalArrangement =Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth().clickable {
            onItemClicked(index+1)
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
    profileResId: Int,
    onProfilePicClicked: () -> Unit
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = profileResId), 
            contentDescription =null ,
            modifier= Modifier
                .size(55.dp)
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
           SettingsScreen(
               user = User(
                   name = "Edit",
                   email = "Edith@youverify.co",
                   role = "Project Manager",
                   dob = "12/12/1997",
                   phone = "08037582010",
                   gender = "Female",
                   address = "No 12, Akintola str Yaba Lagos",
                   nextOfKin = "Yvonne Johnson",
                   nextOfKinPhoneNumber = "08149502340"
               ) ,
               onSettingsItemClicked ={_->} ,
               onProfilePicClicked = {}
           )
        }
    }
}

@Preview
@Composable
fun UserInfoPreview(){
    YouHrTheme {
        Surface {
            UserInfo(
                name ="Edith Ibeh",
                email ="Edith@youverify.co",
                profileResId =R.drawable.profile_pic_placeholder,
                onProfilePicClicked = {}
            )
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


data class User(
    val name: String,
    val email: String,
    val profileResId: Int? = null,
    val role: String = "",
    val dob: String,
    val phone: String,
    val gender: String,
    val address: String,
    val nextOfKin: String,
    val nextOfKinPhoneNumber: String
) {

}

data class Item(val imageResId:Int,val text:String)
val items= listOf(
    Item(R.drawable.ic_paddlock,"Security settings"),
    Item(R.drawable.ic_key,"Account settings"),
    Item(R.drawable.ic_activity,"Activity & permissions"),
    Item(R.drawable.ic_support,"Support"),

)