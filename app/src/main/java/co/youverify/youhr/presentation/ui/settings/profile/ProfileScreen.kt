package co.youverify.youhr.presentation.ui.settings.profile

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.core.content.FileProvider
import co.youverify.youhr.BuildConfig
import co.youverify.youhr.R
import co.youverify.youhr.core.util.toEpochMillis
import co.youverify.youhr.core.util.toFormattedDateString
import co.youverify.youhr.data.remote.TokenInterceptor
import co.youverify.youhr.domain.model.User
import co.youverify.youhr.domain.use_case.ChangePasswordUseCase
import co.youverify.youhr.domain.use_case.CreateCodeUseCase
import co.youverify.youhr.domain.use_case.UpdateUserProfileUseCase
import co.youverify.youhr.presentation.ui.Navigator
import co.youverify.youhr.presentation.ui.components.ActionButton
import co.youverify.youhr.presentation.ui.components.LoadingDialog
import co.youverify.youhr.presentation.ui.components.ProfileEditBottomSheet
import co.youverify.youhr.presentation.ui.components.YouHrTitleBar
import co.youverify.youhr.presentation.ui.home.ProfileRepoMock
import co.youverify.youhr.presentation.ui.leave.AuthRepoMock
import co.youverify.youhr.presentation.ui.leave.PreferenceRepoMock
import co.youverify.youhr.presentation.ui.settings.SettingsViewModel
import co.youverify.youhr.presentation.ui.theme.YouHrTheme
import co.youverify.youhr.presentation.ui.theme.backGroundColor
import co.youverify.youhr.presentation.ui.theme.bodyTextDeepColor
import co.youverify.youhr.presentation.ui.theme.deactivatedColorDeep
import co.youverify.youhr.presentation.ui.theme.inputDeepTextColor
import co.youverify.youhr.presentation.ui.theme.primaryColor
import co.youverify.youhr.presentation.ui.theme.yvColor
import co.youverify.youhr.presentation.ui.theme.yvColor1
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    user: User?,
    onBackArrowClicked: () -> Unit,
    onSaveProfileItemChanges: (EditableFieldType,String) -> Unit,
    settingsViewModel: SettingsViewModel,
    onSaveChangesButtonClicked: (Uri?) -> Unit,
    profileViewModel: ProfileViewModel,
    profileFieldsValue: ProfileFieldsValue,
    uiState: ProfileScreenUiState,

    ){

    LaunchedEffect(key1 = Unit){
        profileViewModel.updateCurrentUser(settingsViewModel.currentUser)
    }
    val editProfileSheetState= rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val editPictureSheetState=rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope= rememberCoroutineScope()
    val context= LocalContext.current
    val file=remember{createImageFile(context)}


    val cameraImageUri:Uri? = remember {
        FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID+".provider",file)
    }

    val imagePickerLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { uri ->

        uri?.let {uri->
            profileFieldsValue.updateDisplayImageUri(uri)

            val bitMap = try {
                val inputStream = context.contentResolver.openInputStream(uri)
                BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                null
            }
            bitMap?.let {
                profileFieldsValue.updateDisplayImageUri(uri)
                profileViewModel.updateDisplayImage(bitMap)
            }
        }
    }

    val takePictureLauncher= rememberLauncherForActivityResult(ActivityResultContracts.TakePicture() ){
        if (it){
            val bitMap=try {
                val inputStream = context.contentResolver.openInputStream(cameraImageUri!!)
                BitmapFactory.decodeStream(inputStream)
            } catch (e: Exception) {
                null
            }

            bitMap?.let {
                profileFieldsValue.updateDisplayImageUri(cameraImageUri)
                profileViewModel.updateDisplayImage(bitMap)
            }
        }

    }


    Column(
        modifier
            .fillMaxSize()
            .verticalScroll(state = rememberScrollState())
   ) {
       YouHrTitleBar(
           title = "My Profile",
           modifier = Modifier.padding(top = 32.dp, bottom = 43.dp,start = 24.42.dp),
           onBackArrowClicked = {onBackArrowClicked()}
       )
       ProfileImageSection(
           profileBitmap =user?.displayPictureBitmap?.asImageBitmap()?:
           ImageBitmap.imageResource(id = R.drawable.placeholder_pic),
           sheetState=editPictureSheetState,
           coroutineScope=coroutineScope

       )
       ProfileInfoList(
           modifier =Modifier.padding(top=16.dp, start = 21.dp, end = 21.dp),
           bottomSheetState =editProfileSheetState,
           onSaveButtonClicked = onSaveProfileItemChanges,
           user=user,
           values =profileFieldsValue
       )
       
       ActionButton(
           text = "Save Changes", modifier = Modifier.padding(top = 50.dp, bottom = 24.dp, start = 20.dp, end = 20.dp),
           onButtonClicked = {onSaveChangesButtonClicked(profileFieldsValue.displayImageUri)}
       )



       //if (showSuccessDialog) {SuccessPopUpDialog()}

        if (uiState.loading){
            LoadingDialog()
        }
        if (uiState.profileUpdateSuccessful){
            SuccessPopUpDialog(onHideDialogRequest = {profileViewModel.hideSuccessDialog()})
        }
        if (editProfileSheetState.isVisible){
            ProfileEditBottomSheet(
                title = profileFieldsValue.editableBottomSheetTitle,
                textFieldValue = profileFieldsValue.editableBottomSheetTextFieldValue,
                fieldType = profileFieldsValue.editableBottomSheetCurrentFieldType,
                onTextFieldValueChanged = {profileFieldsValue.updateSheetTextFieldValue(it)},
                onSaveButtonClicked = onSaveProfileItemChanges,
                sheetState = editProfileSheetState
            )
        }

       if (editPictureSheetState.isVisible){
           ModalBottomSheet(
               onDismissRequest = {
                   coroutineScope.launch {
                       editPictureSheetState.hide()
                   }
               },
               modifier=Modifier.height(398.dp),
               shape = RoundedCornerShape(8.dp),
               sheetState = editPictureSheetState,
               content = {
                   BottomSheetContent(
                       sheetState = editPictureSheetState,
                       crtScope=coroutineScope,
                       imageLauncher = imagePickerLauncher,
                       cameraLauncher =takePictureLauncher,
                       cameraImageUri=cameraImageUri!!
                   )
               }
           )
       }


   }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileInfoList(
    modifier: Modifier = Modifier,
    user: User?,
    bottomSheetState: SheetState,
    onSaveButtonClicked: (EditableFieldType,String) -> Unit,
    values: ProfileFieldsValue,

    ) {
    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        ProfileInfoItem(
            fieldTitle ="Name",
            fieldValue ="${user?.firstName} ${user?.lastName}",
            editable =false,
            //fieldType = EditableFieldType.NAME,
            sheetState =bottomSheetState,
            //onSavedButtonClicked = onSaveButtonClicked,
            values=values
        )

        ProfileInfoItem(
            fieldTitle ="Email Address",
            fieldValue =user?.email?:"",
            editable =false,
            sheetState = bottomSheetState,
            //onSavedButtonClicked = onSaveButtonClicked,
            values=values
        )

        ProfileInfoItem(
            fieldTitle ="Job Role",
            fieldValue =user?.jobRole?:"",
            editable =false,
            sheetState = bottomSheetState,
            //onSavedButtonClicked = onSaveButtonClicked,
            values=values
        )

        ProfileInfoItem(
            fieldTitle ="Date Of Birth",
            fieldValue =user?.dob?.toEpochMillis()?.toFormattedDateString("dd/MM/yyyy")?:"",
            editable =false,
            sheetState = bottomSheetState,
           // onSavedButtonClicked = onSaveButtonClicked,
            values=values
        )

        ProfileInfoItem(
            fieldTitle ="Phone Number",
            fieldValue =user?.phoneNumber?:"",
            editable =true,
            //onFieldValueChanged ={values.updatePhoneNumber(it)},
            fieldType=EditableFieldType.PHONE,
            sheetState = bottomSheetState,
            //onSavedButtonClicked = onSaveButtonClicked,
            values=values
        )

        ProfileInfoItem(
            fieldTitle ="Gender",
            fieldValue =user?.gender?:"",
            editable =false,
            sheetState = bottomSheetState,
            //onSavedButtonClicked = onSaveButtonClicked,
            //onFieldValueChanged = {  values.updateGender(it)},
            values=values
        )

        ProfileInfoItem(
            fieldTitle ="Address",
            fieldValue =user?.address?:"",
            editable =true,
            //onFieldValueChanged ={values.updateAddress(it)},
            fieldType=EditableFieldType.ADDRESS,
            sheetState = bottomSheetState,
            //onSavedButtonClicked = onSaveButtonClicked,
            values=values
        )

        ProfileInfoItem(
            fieldTitle ="Next Of Kin",
            fieldValue =user?.nextOfKin?:"",
            editable =true,
            //onFieldValueChanged ={values.updateNextOfKin(it)},
            fieldType=EditableFieldType.NEXTOFKIN,
            sheetState = bottomSheetState,
            //onSavedButtonClicked = onSaveButtonClicked,
            values=values

        )

        ProfileInfoItem(
            fieldTitle ="Next Of Kin's Phone Number",
            fieldValue =user?.nextOfKinNumber?:"",
            editable =true,
            //onFieldValueChanged ={values.updateNextOfKinPhoneNumber(it)},
            fieldType=EditableFieldType.NEXTOFKINPHONE,
            sheetState = bottomSheetState,
            //onSavedButtonClicked = onSaveButtonClicked,
            values=values
        )

        ProfileInfoItem(
            fieldTitle ="Next Of Kin's Address",
            fieldValue =user?.nextOfKinContact?:"",
            editable =true,
            //onFieldValueChanged ={values.updateNextOfKinAddress(it)},
            fieldType=EditableFieldType.NEXTOFKINADDRESS,
            sheetState = bottomSheetState,
            //onSavedButtonClicked = onSaveButtonClicked,
            values=values
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileImageSection(
    modifier: Modifier = Modifier,
    profileBitmap: ImageBitmap,
    sheetState: SheetState,
    coroutineScope: CoroutineScope
) {

    ConstraintLayout(
        modifier=modifier.fillMaxWidth()
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
                .clickable {
                    coroutineScope.launch { sheetState.show() }
                },
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
    //onFieldValueChanged: (String) -> Unit = {},
    fieldType: EditableFieldType = EditableFieldType.NONE,
    sheetState: SheetState,
    //onSavedButtonClicked: (EditableFieldType, String) -> Unit,
    values: ProfileFieldsValue,
) {
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
            onValueChange = {},
            enabled = false,
            modifier= Modifier
                .fillMaxWidth(),
            //.requiredHeight(if (editable) 48.dp else 40.dp),
            trailingIcon = {
                if (editable)
                    Icon(
                        painter = painterResource(id = R.drawable.ic_pencil) ,
                        contentDescription = null,
                        modifier=Modifier.clickable {
                            values.updateSheetTitle(fieldTitle)
                            values.updateSheetTextFieldValue(fieldValue)
                            values.updateSheetCurrentFieldType(fieldType)
                            coroutineScope.launch { sheetState.show() }
                        }
                    )
            },
            shape = RoundedCornerShape(4.dp),
            textStyle = TextStyle.Default.copy(fontSize = 12.sp),
            colors = OutlinedTextFieldDefaults.colors(
                disabledTrailingIconColor = primaryColor,
                disabledBorderColor = deactivatedColorDeep,
                disabledTextColor = inputDeepTextColor,
                unfocusedBorderColor = deactivatedColorDeep,
                disabledContainerColor = backGroundColor,
                unfocusedContainerColor = backGroundColor
            )
        )
    }



}

@Composable
fun SuccessPopUpDialog(
    modifier: Modifier = Modifier,
    //profileViewModel: ProfileViewModel,
    onHideDialogRequest:()->Unit
){
    Dialog(onDismissRequest = {}) {
        Box(modifier = modifier.fillMaxSize()){

            LaunchedEffect(key1 = Unit){
                delay(4000)
                //profileViewModel.hideSuccessDialog()
                onHideDialogRequest()
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.26.dp),
                modifier = Modifier
                    .align(Alignment.Center)
                    .background(color = Color(0XFFC2E2E9), shape = RoundedCornerShape(6.dp))
                    .height(150.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.mask_group),
                    contentDescription =null ,
                    modifier=Modifier.padding(start = 41.dp,top=10.dp, bottom = 10.dp),
                )
                
                Text(
                    text = "Successful!",
                    fontSize = 16.sp,
                    modifier=Modifier.padding(end = 70.dp),
                    fontWeight = FontWeight.Medium,
                    color = bodyTextDeepColor
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetContent(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    crtScope: CoroutineScope,
    imageLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
    cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>,
    cameraImageUri: Uri,
){


    Spacer(modifier = Modifier.height(1.dp))
    Column(modifier = modifier
        .fillMaxSize()
        .padding(start = 21.dp)) {

        Text(
            text = "Profile Photo",
            fontWeight = FontWeight.Medium,
            fontSize = 16.sp,
            lineHeight = 20.8.sp,
            color = inputDeepTextColor,
            modifier = Modifier.padding(bottom = 23.5.dp,top=33.dp)
        )

        TakePictureChoiceRow(
            modifier = Modifier.padding(bottom = 16.dp),
            imageResId = R.drawable.ic_camera,
            text="Take an instant picture",
            sheetState = sheetState,
            cameraLauncher=cameraLauncher,
            coroutineScope = crtScope,
            cameraImageUri=cameraImageUri,

        )

        PickFromGalleryChoiceRow(
            imageResId = R.drawable.ic_gallery_2, modifier = Modifier.padding(bottom = 16.dp),
            text="Choose a picture from your gallery",
            sheetState = sheetState,
            imageLauncher=imageLauncher,
            coroutineScope=crtScope
        )



    }

}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickFromGalleryChoiceRow(
    modifier: Modifier = Modifier,
    imageResId: Int,
    text: String,
    sheetState: SheetState,
    coroutineScope: CoroutineScope,
    imageLauncher: ManagedActivityResultLauncher<PickVisualMediaRequest, Uri?>,
){
    Row(
        modifier= modifier
            .height(37.dp)
            .clickable {
                coroutineScope.launch { sheetState.hide() }
                imageLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        Box(modifier = Modifier
            .size(37.dp)
            .border(width = 0.22.dp, color = bodyTextDeepColor, shape = CircleShape),
            contentAlignment = Alignment.Center,
            content = {
                Image(painter = painterResource(id = imageResId),contentDescription =null, modifier=Modifier)

            }
        )

        Text(
            text = text,
            fontSize = 12.sp,
            lineHeight = 15.6.sp,
            color = bodyTextDeepColor,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TakePictureChoiceRow(
    modifier: Modifier = Modifier,
    imageResId: Int,
    text: String,
    sheetState: SheetState,
    coroutineScope: CoroutineScope,
    cameraLauncher: ManagedActivityResultLauncher<Uri, Boolean>,
    cameraImageUri: Uri,
){
    Row(
        modifier= modifier
            .height(37.dp)
            .clickable {
                coroutineScope.launch { sheetState.hide() }
                cameraLauncher.launch(cameraImageUri)
            },
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        Box(modifier = Modifier
            .size(37.dp)
            .border(width = 0.22.dp, color = bodyTextDeepColor, shape = CircleShape),
            contentAlignment = Alignment.Center,
            content = {
                Image(painter = painterResource(id = imageResId),contentDescription =null, modifier=Modifier)

            }
        )

        Text(
            text = text,
            fontSize = 12.sp,
            lineHeight = 15.6.sp,
            color = bodyTextDeepColor,
            modifier = Modifier.align(Alignment.CenterVertically)
        )
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
                onSaveProfileItemChanges = {_,_->},
                settingsViewModel = SettingsViewModel(
                    Navigator(), ChangePasswordUseCase(AuthRepoMock()),
                    CreateCodeUseCase(AuthRepoMock(),PreferenceRepoMock()),
                    PreferenceRepoMock(),
                    TokenInterceptor()
                ),
                onSaveChangesButtonClicked = {},
                profileViewModel = ProfileViewModel(Navigator(), UpdateUserProfileUseCase(ProfileRepoMock())),
                profileFieldsValue = ProfileFieldsValue(),
                uiState = ProfileScreenUiState()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ProfileSectionPreview(){
    YouHrTheme {
        Surface {

            ProfileImageSection(
                profileBitmap = ImageBitmap.imageResource(id = R.drawable.placeholder_pic),
                sheetState = rememberModalBottomSheetState(),
                coroutineScope = rememberCoroutineScope()
            )
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
              user = User(
                  firstName = "Edit",
                  email = "Edith@youverify.co", passcode = "", password = "", nextOfKin = "",
                  jobRole = "Project Manager",middleName = "", phoneNumber = "",
                  dob = "12/12/1997", nextOfKinContact = "", displayPictureBitmap = null,
                  gender = "Female",displayPictureUrl = "",id="", nextOfKinNumber = "",
                  address = "", role = "", status = "", lastName = ""
              ),
               bottomSheetState = rememberModalBottomSheetState(),
               onSaveButtonClicked = {_,_->},
               values = ProfileFieldsValue(),
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
                //onFieldValueChanged ={},
                fieldType = EditableFieldType.PHONE,
                sheetState = rememberModalBottomSheetState(),
                //onSavedButtonClicked = {_,_->},
                values = ProfileFieldsValue()
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
                //onFieldValueChanged = {},
                fieldType = EditableFieldType.PHONE,
                sheetState = rememberModalBottomSheetState(),
                //onSavedButtonClicked = {_,_->},
                values = ProfileFieldsValue()
            )
        }
    }
}

enum class EditableFieldType{
    NONE,
    PHONE,
    ADDRESS,
    NEXTOFKIN,
    NEXTOFKINPHONE,
    NEXTOFKINADDRESS
}

class ProfileFieldsValue{
    var phoneNumber by mutableStateOf("")
    private set

    var displayImageUri: Uri? by mutableStateOf(null)
        private set


    //var address by mutableStateOf("")
        //private set


    var editableBottomSheetTitle by mutableStateOf("")
        private set
    var editableBottomSheetTextFieldValue by mutableStateOf("")

    var editableBottomSheetCurrentFieldType by mutableStateOf(EditableFieldType.NONE)

    fun updateDisplayImageUri(newValue: Uri?){ displayImageUri=newValue }

    fun updateSheetTitle(newValue: String){ editableBottomSheetTitle=newValue }

    fun updateSheetTextFieldValue(newValue: String){ editableBottomSheetTextFieldValue=newValue }
    fun updateSheetCurrentFieldType(newValue: EditableFieldType){ editableBottomSheetCurrentFieldType=newValue }
}
fun createImageFile(context: Context): File {
    val timeStamp= SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
    return File.createTempFile("img_$timeStamp",".jpeg",context.externalCacheDir)
}