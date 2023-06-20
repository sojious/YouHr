package co.youverify.youhr.presentation.ui.settings.profile

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import co.youverify.youhr.R
import co.youverify.youhr.core.util.toEpochMillis
import co.youverify.youhr.core.util.toFormattedDateString
import co.youverify.youhr.domain.model.User
import co.youverify.youhr.presentation.ui.Navigator
import co.youverify.youhr.presentation.ui.components.ActionButton
import co.youverify.youhr.presentation.ui.components.ProfileEditBottomSheet
import co.youverify.youhr.presentation.ui.components.YouHrTitleBar
import co.youverify.youhr.presentation.ui.settings.SettingsViewModel
import co.youverify.youhr.presentation.ui.theme.YouHrTheme
import co.youverify.youhr.presentation.ui.theme.backGroundColor
import co.youverify.youhr.presentation.ui.theme.bodyTextDeepColor
import co.youverify.youhr.presentation.ui.theme.deactivatedColorDeep
import co.youverify.youhr.presentation.ui.theme.inputDeepTextColor
import co.youverify.youhr.presentation.ui.theme.primaryColor
import co.youverify.youhr.presentation.ui.theme.yvColor
import co.youverify.youhr.presentation.ui.theme.yvColor1
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    user: User?,
    onBackArrowClicked: () -> Unit,
    onEditProfileIconClicked: () -> Unit,
    onEditProfileFieldValueChanged: (String, EditableFieldType) -> Unit,
    onSaveProfileItemChanges: (EditableFieldType) -> Unit,
    onCancelProfileItemChanges: (EditableFieldType) -> Unit,
    showSuccessDialog: Boolean,
    settingsViewModel: SettingsViewModel,
    onSaveChangesButtonClicked: () -> Unit,
    profileViewModel: ProfileViewModel
){

    LaunchedEffect(key1 = Unit){
        profileViewModel.updateCurrentUser(settingsViewModel.currentUser)
    }
    val sheetState= rememberModalBottomSheetState(skipPartiallyExpanded = true)
    
   Column(
       modifier
           .fillMaxSize()
           .verticalScroll(state = rememberScrollState())
   ) {
       YouHrTitleBar(
           title = "My Profile",
           modifier = Modifier.padding(top = 32.dp, bottom = 43.dp,start = 24.42.dp),
           onBackArrowClicked = onBackArrowClicked
       )
       ProfileImageSection(
           profileBitmap =user?.displayPictureBitmap?.asImageBitmap()?:
           ImageBitmap.imageResource(id = R.drawable.placeholder_pic),
           onCameraIconClicked = onEditProfileIconClicked
       )
       ProfileInfoList(
           modifier=Modifier.padding(top=16.dp, start = 21.dp, end = 21.dp),
           onItemFieldValueChanged = onEditProfileFieldValueChanged,
           bottomSheetState=sheetState,
           onSaveButtonClicked = onSaveProfileItemChanges,
           onCancelButtonClicked = onCancelProfileItemChanges,
           name = "${user?.firstName} ${user?.lastName}",
           email = user?.email?:"",
           jobRole = user?.jobRole?:"",
           dob = user?.dob?:"",
           phone = user?.phoneNumber?:"",
           gender = user?.gender?:"",
           address =user?.address?:"" ,
           nextOfKin = user?.nextOfKin?:"",
           nextOfKinPhoneNumber = user?.nextOfKinNumber?:""
       )
       
       ActionButton(
           text = "Save Changes", modifier = Modifier.padding(top = 50.dp, bottom = 24.dp, start = 20.dp, end = 20.dp),
           onButtonClicked = onSaveChangesButtonClicked
       )

       if (showSuccessDialog)
           SuccessPopUpDialog()


   }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileInfoList(
    modifier: Modifier = Modifier,
    name: String,
    email: String,
    jobRole: String,
    dob: String,
    phone: String,
    gender: String,
    address: String,
    nextOfKin: String,
    nextOfKinPhoneNumber: String,
    onItemFieldValueChanged: (String, EditableFieldType) -> Unit,
    bottomSheetState: SheetState,
    onSaveButtonClicked: (EditableFieldType) -> Unit,
    onCancelButtonClicked: (EditableFieldType) -> Unit,

) {
    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        ProfileInfoItem(
            fieldTitle ="Name",
            fieldValue =name,
            editable =true,
            onFieldValueChanged =onItemFieldValueChanged,
            fieldType = EditableFieldType.NAME,
            sheetState =bottomSheetState,
            onSavedButtonClicked = onSaveButtonClicked,
            onCancelButtonClicked = onCancelButtonClicked
        )

        ProfileInfoItem(
            fieldTitle ="Email Address",
            fieldValue =email,
            editable =false,
            onFieldValueChanged =onItemFieldValueChanged,
            fieldType = EditableFieldType.NONE,
            sheetState = bottomSheetState,
            onCancelButtonClicked = onCancelButtonClicked,
            onSavedButtonClicked = onSaveButtonClicked
        )

        ProfileInfoItem(
            fieldTitle ="Job Role",
            fieldValue =jobRole,
            editable =false,
            onFieldValueChanged =onItemFieldValueChanged,
            fieldType = EditableFieldType.NONE,
            sheetState = bottomSheetState,
            onCancelButtonClicked = onCancelButtonClicked,
            onSavedButtonClicked = onSaveButtonClicked
        )

        ProfileInfoItem(
            fieldTitle ="Date Of Birth",
            fieldValue =dob.toEpochMillis().toFormattedDateString("dd/MM/yyyy"),
            editable =false,
            onFieldValueChanged =onItemFieldValueChanged,
            fieldType = EditableFieldType.NONE,
            sheetState = bottomSheetState,
            onCancelButtonClicked = onCancelButtonClicked,
            onSavedButtonClicked = onSaveButtonClicked
        )

        ProfileInfoItem(
            fieldTitle ="Phone Number",
            fieldValue =phone,
            editable =true,
            onFieldValueChanged =onItemFieldValueChanged,
            fieldType=EditableFieldType.PHONE,
            sheetState = bottomSheetState,
            onCancelButtonClicked = onCancelButtonClicked,
            onSavedButtonClicked = onSaveButtonClicked
        )

        ProfileInfoItem(
            fieldTitle ="Gender",
            fieldValue =gender,
            editable =false,
            onFieldValueChanged =onItemFieldValueChanged,
            fieldType=EditableFieldType.NONE,
            sheetState = bottomSheetState,
            onCancelButtonClicked = onCancelButtonClicked,
            onSavedButtonClicked = onSaveButtonClicked
        )

        ProfileInfoItem(
            fieldTitle ="Address",
            fieldValue =address,
            editable =true,
            onFieldValueChanged =onItemFieldValueChanged,
            fieldType=EditableFieldType.ADDRESS,
            sheetState = bottomSheetState,
            onCancelButtonClicked = onCancelButtonClicked,
            onSavedButtonClicked = onSaveButtonClicked
        )

        ProfileInfoItem(
            fieldTitle ="Next Of Kin",
            fieldValue =nextOfKin,
            editable =true,
            onFieldValueChanged =onItemFieldValueChanged,
            fieldType=EditableFieldType.NEXTOFKIN,
            sheetState = bottomSheetState,
            onCancelButtonClicked = onCancelButtonClicked,
            onSavedButtonClicked = onSaveButtonClicked
        )

        ProfileInfoItem(
            fieldTitle ="Next Of Kin's Phone Number",
            fieldValue =nextOfKinPhoneNumber,
            editable =true,
            onFieldValueChanged =onItemFieldValueChanged,
            fieldType=EditableFieldType.NEXTOFKINPHONE,
            sheetState = bottomSheetState,
            onCancelButtonClicked = onCancelButtonClicked,
            onSavedButtonClicked = onSaveButtonClicked
        )
    }
}

@Composable
fun ProfileImageSection(
    modifier: Modifier=Modifier,
    profileBitmap: ImageBitmap,
    onCameraIconClicked:()->Unit
) {

    ConstraintLayout(
        modifier=Modifier.fillMaxWidth()
    ) {
        val (profileImage,cameraIcon)=createRefs()
        
        Image(
            bitmap = profileBitmap,
            contentDescription =null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(120.dp)
                .clip(shape = CircleShape)
                .constrainAs(profileImage) {
                    centerHorizontallyTo(parent)
                }
        )

        Box(
            modifier = Modifier
                .size(36.dp)
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(
                            yvColor, yvColor1
                        )
                    ), shape = CircleShape
                )
                .constrainAs(cameraIcon) {
                    //end.linkTo(profileImage.end)
                    //bottom.linkTo(profileImage.bottom)
                    circular(profileImage, 135f, 60.dp)
                }
                .clickable { onCameraIconClicked() },
            contentAlignment = Alignment.Center,
            content = {
                Image(
                    painter = painterResource(id = R.drawable.ic_camera_1) ,
                    contentDescription =null,
                )
            }
        )

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileInfoItem(
    modifier: Modifier = Modifier,
    fieldTitle: String,
    fieldValue: String,
    editable: Boolean,
    onFieldValueChanged: (String, EditableFieldType) -> Unit,
    fieldType: EditableFieldType,
    sheetState: SheetState,
    onSavedButtonClicked: (EditableFieldType) -> Unit,
    onCancelButtonClicked: (EditableFieldType) -> Unit
){
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {


        val coroutineScope= rememberCoroutineScope()
        Text(
            text = fieldTitle,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = inputDeepTextColor,

        )

        OutlinedTextField(
            value = fieldValue,
            onValueChange = { onFieldValueChanged(it,fieldType) },
            enabled = editable,
            modifier= Modifier
                .fillMaxWidth()
                .onFocusEvent {
                    if (it.isFocused) {
                        coroutineScope.launch {
                            delay(500)
                            sheetState.show()
                        }

                    }
                },
                //.requiredHeight(if (editable) 48.dp else 40.dp),
            trailingIcon = {
                if (editable)
                    Icon(
                        painter = painterResource(id = R.drawable.ic_pencil) ,
                        contentDescription = null
                    )
            },
            shape = RoundedCornerShape(4.dp),
            textStyle = TextStyle.Default.copy(fontSize = 12.sp),
            colors = OutlinedTextFieldDefaults.colors(
                disabledLeadingIconColor = primaryColor,
                disabledBorderColor = deactivatedColorDeep,
                disabledTextColor = inputDeepTextColor,
                unfocusedBorderColor = deactivatedColorDeep,
                disabledContainerColor = backGroundColor,
                unfocusedContainerColor = backGroundColor
            )
        )
    }

    if (sheetState.isVisible)
        ProfileEditBottomSheet(
            title = fieldTitle,
            textFieldValue = fieldValue,
            fieldType = fieldType,
            onTextFieldValueChanged = onFieldValueChanged,
            onSaveButtonClicked = onSavedButtonClicked,
            onCancelButtonClicked =onCancelButtonClicked,
            sheetState = sheetState
        )
}

@Composable
fun SuccessPopUpDialog(modifier: Modifier=Modifier){
    Dialog(onDismissRequest = {}) {
        Box(modifier = modifier.fillMaxSize()){
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.26.dp),
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(color = Color(0XFFC2E2E9))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.mask_group),
                    contentDescription =null ,
                    modifier=Modifier.padding(start = 41.dp,top=10.dp, bottom = 10.dp),
                )
                
                Text(
                    text = "Saved!",
                    fontSize = 16.sp,
                    modifier=Modifier.padding(end = 70.dp),
                    fontWeight = FontWeight.Medium,
                    color = bodyTextDeepColor
                )
            }
        }
    }
}


@Preview
@Composable
fun ProfileScreenPreview(){
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
            ProfileScreen(
                user = User(
                    firstName = "Edit", email = "Edith@youverify.co", role = "Project Manager",
                    dob = "12/12/1997", gender = "Female", address = "No 12, Akintola str Yaba Lagos",
                    nextOfKin = "Yvonne Johnson", nextOfKinNumber = "08149502340", passcode = "", password = "",
                    middleName = "", lastName = "Ibeh", status = "", jobRole = "Product Manager", nextOfKinContact = "",
                    displayPictureBitmap = bitmap, displayPictureUrl = "", id = "", phoneNumber = "08037582010"
                ),
                onBackArrowClicked = {},
                onEditProfileIconClicked = {},
                onEditProfileFieldValueChanged = { _, _->},
                onSaveProfileItemChanges = {},
                onCancelProfileItemChanges = {},
                showSuccessDialog = false,
                settingsViewModel = SettingsViewModel(Navigator()),
                onSaveChangesButtonClicked = {},
                profileViewModel = ProfileViewModel(Navigator()),
            )
        }
    }
}

@Preview
@Composable
fun ProfileSectionPreview(){
    YouHrTheme {
        Surface {

            ProfileImageSection(
                profileBitmap = ImageBitmap.imageResource(id = R.drawable.placeholder_pic)
            ) {}
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ProfileInfoListPreview(){
    YouHrTheme {
       Surface {
           ProfileInfoList(
                   name = "Edit",
                   email = "Edith@youverify.co",
                   jobRole = "Project Manager",
                   dob = "12/12/1997",
                   phone = "08037582010",
                   gender = "Female",
                   address = "No 12, Akintola str Yaba Lagos",
                   nextOfKin = "Yvonne Johnson",
                   nextOfKinPhoneNumber = "08149502340"
               ,
               onItemFieldValueChanged = {_,_->},
               bottomSheetState = rememberModalBottomSheetState(),
               onCancelButtonClicked = {},
               onSaveButtonClicked = {}
           )
       }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ProfileInfoItemPreview1(){
    YouHrTheme {
        Surface {
            ProfileInfoItem(
                modifier = Modifier.padding(horizontal = 21.dp),
                fieldTitle = "Name",
                fieldValue = "Edith Ibeh",
                editable =true,
                onFieldValueChanged ={_,_->},
                fieldType = EditableFieldType.PHONE,
                sheetState = rememberModalBottomSheetState(),
                onCancelButtonClicked = {},
                onSavedButtonClicked = {}
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ProfileInfoItemPreview2(){
    YouHrTheme {
        Surface {
            ProfileInfoItem(
                fieldTitle = "Email Adress",
                fieldValue = "edith@youverify.co",
                editable =false,
                onFieldValueChanged = {_,_->},
                fieldType = EditableFieldType.PHONE,
                sheetState = rememberModalBottomSheetState(),
                onCancelButtonClicked = {},
                onSavedButtonClicked = {}
            )
        }
    }
}

enum class EditableFieldType{
    NONE,
    NAME,
    JOBROLE,
    DOB,
    PHONENUMBER,
    GENDER,
    PHONE,
    ADDRESS,
    NEXTOFKIN,
    NEXTOFKINPHONE
}