package co.youverify.youhr.presentation.ui.task

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import co.youverify.youhr.R
import co.youverify.youhr.core.util.capitalizeWords
import co.youverify.youhr.core.util.toCardinalDateString
import co.youverify.youhr.core.util.toOrdinalDateString
import co.youverify.youhr.presentation.ui.components.MultiColoredText
import co.youverify.youhr.presentation.ui.components.YouHrTitleBar
import co.youverify.youhr.presentation.ui.theme.*

@Composable
fun TaskDetailScreen(
    modifier: Modifier = Modifier,
    taskId:Int,
    currentTaskList:List<Task>,
    taskMessage: String,
    onTaskMessageChanged:(String)->Unit,
    onSendMessageButtonClicked:()->Unit,
    onBackArrowClicked:()->Unit
){
   // val taskId=taskDetailViewModel.taskId
    //val task= pendingTasks[0]// should be gotten from the database using the id

    val currentTask=currentTaskList[taskId]
    val scrollState= rememberScrollState()
    Column(modifier= modifier
        .fillMaxSize()
        .verticalScroll(scrollState)) {
        YouHrTitleBar(
            title = currentTask.title,
            modifier = Modifier.padding(top=36.dp, start = 21.dp,end=20.dp),
            onBackArrowClicked = onBackArrowClicked
        )
        Divider(thickness = 0.2.dp, color = codeInputUnfocused, modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, bottom = 22.dp))
        AssigneeInfo(task=currentTask, modifier = Modifier.padding(start = 20.dp,end=20.dp,bottom=20.dp))
        TaskProgress(task=currentTask, modifier = Modifier.padding(start = 21.dp,end=21.dp,bottom=20.dp))
        TaskDescription(task=currentTask, modifier = Modifier.padding(start = 21.dp,end=21.dp,bottom=23.dp))
        AttachmentList(task = currentTask,modifier = Modifier.padding(start = 21.dp,end=21.dp,bottom=23.dp))
        TaskActivitiesSection(task=currentTask)
        Divider(thickness = 1.dp, color = taskActivitiesBackGroundColor, modifier = Modifier.fillMaxWidth())
        TaskMessageBox(
            textFieldValue =taskMessage,
            onTextFieldValueChanged =onTaskMessageChanged,
            onSendMessageButtonClicked =onSendMessageButtonClicked
        )
    }
}

@Composable
fun TaskProgress(task: Task, modifier: Modifier) {

    Column(modifier=modifier){
        Text(
            text = "Task progress",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = bodyTextLightColor,
            modifier=Modifier.padding(bottom = 4.dp)
        )
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(21.dp)
                .background(color = Color(0XFFFEF7EA), shape = RoundedCornerShape(10.dp))
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(color = Color(0XFFF7B32B), shape = CircleShape)
                    .padding(start = 15.dp)
            )
            Text(
                text = "Pending",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color  = Color(0XFFF7B32B),
                modifier=Modifier.padding(end = 15.dp)
            )
        }
    }
}


@Composable
fun TaskActivitiesSection(modifier: Modifier = Modifier, task: Task) {
    Column(
        modifier = modifier
            .background(color = taskActivitiesBackGroundColor2)
            .padding(start = 20.dp, top = 11.dp, bottom = 17.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TaskCreatorSection(task = pendingTasks.first())
        
        Row {
            MultiColoredText(
                fontSize = 10.sp,
                colorPosition = 1,
                secondColor = yvColor,
                neutralColor = bodyTextLightColor,
                parts = arrayOf(
                    "${task.assigner} added to ",
                    task.project
                )
            )
            
            Text(text = " 57 mins ago", fontSize = 8.sp, color = bodyTextLightColor, modifier=Modifier.padding(top=2.dp))
        }

        Text(text = "${task.assigner} assigned this task to you", fontSize = 10.sp, color = bodyTextLightColor)
        Text(text = "${task.assigner} changed due date to Wednesday", fontSize = 10.sp, color = bodyTextLightColor)
    }
}


@Composable
fun AssigneeInfo(modifier: Modifier=Modifier, task: Task) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {


        Column(
            verticalArrangement =Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = "Assigned By",
                fontSize = 12.sp,
                fontWeight=FontWeight.Medium,
                color = bodyTextLightColor,
            )

            Image(
                painterResource(id = R.drawable.profile_photo_edna_ibeh),
                modifier = Modifier
                    .size(18.dp)
                    .clip(shape = CircleShape),
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
        }
        


       Column(
           verticalArrangement = Arrangement.spacedBy(5.33.dp)
       ) {
           Text(
               text = "Due date",
               fontSize = 12.sp,
               fontWeight = FontWeight.Medium,
               color = bodyTextLightColor,
           )


           Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {

               Image(
                   painterResource(id = R.drawable.ic_calendar_2),
                   modifier = Modifier
                       .clip(shape = RoundedCornerShape(18.dp)),
                   contentDescription = null,
                   contentScale = ContentScale.Crop
               )


               Text(
                   text = task.dueDate.toCardinalDateString(),
                   fontSize = 10.sp,
                   color = bodyTextLightColor,
               )
           }
       }




            Row(
                //horizontalArrangement = Arrangement.SpaceBetween,,
                    //.padding(start = 2.dp)
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier
                    .height(29.dp)
                    .background(color = Color.White, shape = RoundedCornerShape(4.dp))
                    .border(
                        BorderStroke(width = 1.dp, color = primaryColor),
                        shape = RoundedCornerShape(4.dp)
                    )

            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_plus_2),
                    contentDescription =null,
                    modifier=Modifier.padding(start = 4.dp)
                )

                Text(
                    text = "Reassign",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = primaryColor,
                    modifier=Modifier.padding( end=8.dp)
                )
            }

    }

}



@Composable
fun ProjectInfo(task: Task, modifier: Modifier=Modifier) {
    
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = "Project", fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp, color = inputDeepTextColor)
        Text(text = task.project, fontSize = 11.sp, lineHeight = 16.sp, color = bodyTextLightColor)

    }

}


@Composable
fun TaskDescription(task: Task, modifier: Modifier=Modifier) {

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = "Description", fontSize = 12.sp, lineHeight = 16.sp, color = inputDeepTextColor)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 0.5.dp,
                    color = deactivatedColorDeep,
                    shape = RoundedCornerShape(4.dp)
                ),
            content = {
                Text(text = task.description, fontSize = 11.sp, lineHeight = 16.sp,
                    color = bodyTextLightColor, modifier = Modifier.padding(top = 9.dp, start = 9.dp, bottom = 22.dp))
            }
        )

    }
}

@Composable
fun AttachmentList(task: Task, modifier: Modifier=Modifier) {

    Column(
        modifier=modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ){
        AttachmentItem(
            fileName = "List of new employees . xlxs",
            fileSize = "234kb",
            fileType = FileType.EXCELSHEET
        )

        AttachmentItem(
            fileName = "Schedule document . pdf",
            fileSize = "120kb",
            fileType = FileType.PDF
        )

        AttachmentItem(
            fileName = "Privacy policy . jpg",
            fileSize = "1.2mb",
            fileType = FileType.IMAGE
        )
    }

}

@Composable
fun AttachmentItem( fileName: String, fileSize: String,fileType:FileType) {

    val logosResId=when(fileType){
        FileType.EXCELSHEET->R.drawable.ms_excel_logo
        FileType.PDF->R.drawable.pdf_logo
        FileType.IMAGE->R.drawable.icons8_picture
    }
    ConstraintLayout {
        val (logo,name,size)=createRefs()
        Image(
            painter = painterResource(id =logosResId),
            contentDescription =null,
            modifier = Modifier
                .size(19.dp)
                .constrainAs(logo) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                },
            contentScale = ContentScale.Crop
        )

        Text(
            text = fileName,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = primaryColor,
            modifier = Modifier.constrainAs(name){
                start.linkTo(logo.end,4.dp)
                top.linkTo(logo.top)
            }
        )

        Text(
            text = fileSize,
            fontSize = 10.sp,
            color = bodyTextLightColor,
            modifier = Modifier.constrainAs(size){
                start.linkTo(name.start)
                top.linkTo(name.bottom,4.dp)
            }
        )
    }
}

@Composable
fun TaskCreatorSection(task: Task){
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {

        val (profileImage,taskCreatorNameText,elapsedTimeText) =createRefs()

        Image(
            modifier= Modifier
                .size(30.dp)
                .clip(shape = CircleShape)
                .constrainAs(profileImage, constrainBlock = {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }),
            painter = painterResource(R.drawable.profile_photo_timothy_john) ,
            contentDescription =null,
            contentScale = ContentScale.Crop
        )

        Text(
            text ="${task.creator.capitalizeWords()} created this task",
            fontSize=12.sp,
            fontWeight=FontWeight.Medium,
            lineHeight=16.sp,
            color= inputDeepTextColor,
            modifier = Modifier
                .constrainAs(taskCreatorNameText){
                    start.linkTo(profileImage.end,9.dp)
                    top.linkTo(parent.top)
                }
        )


        Text(
            text ="42 minutes ago",
            fontSize=10.sp,
            lineHeight=16.sp,
            color= bodyTextLightColor,
            modifier = Modifier
                .constrainAs(elapsedTimeText){
                    start.linkTo(taskCreatorNameText.start)
                    top.linkTo(taskCreatorNameText.bottom)
                }
        )

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskMessageBox(
    modifier: Modifier = Modifier,
    textFieldValue: String,
    onTextFieldValueChanged: (String) -> Unit,
    onSendMessageButtonClicked: () -> Unit
) {

    Box(modifier = modifier
        .fillMaxWidth()
        .height(115.dp)
        .background(color = Color.White)){
        TextField(
            value =textFieldValue ,
            onValueChange =onTextFieldValueChanged,
            trailingIcon = {
                IconButton(
                    onClick = onSendMessageButtonClicked,
                    content = { Icon(painter = painterResource(id = R.drawable.ic_send_message), contentDescription =null )},
                    modifier = Modifier.padding(end = 10.dp)
                )
            },
            placeholder = { Text(text = "Ask a question or post an update", fontSize = 12.sp, color = taskMessageBoxPlaceHolderColor)},
            modifier=Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(containerColor = Color.White, focusedIndicatorColor = Color.White, unfocusedIndicatorColor = Color.White)
        )
    }
}


@Preview
@Composable
fun TaskDetailScreenPreview(){
    Surface {
        TaskDetailScreen(
           // navController = rememberAnimatedNavController(),
           // taskDetailViewModel = ,
            taskMessage ="" ,
            onTaskMessageChanged ={},
            onSendMessageButtonClicked = {},
            taskId = 0,
            currentTaskList = pendingTasks,
            onBackArrowClicked = {}
        )
    }
}

@Preview
@Composable
fun AssigneeInfoPreview(){
    Surface {
        AssigneeInfo(task = pendingTasks.first(),modifier = Modifier.padding(horizontal = 21.dp))
    }
}


@Preview
@Composable
fun ProjectInfoPreview(){
    Surface {
        ProjectInfo(task = pendingTasks.first(),modifier = Modifier.padding(horizontal = 21.dp))
    }
}

@Preview
@Composable
fun TaskDescriptionPreview(){
    Surface {
        TaskDescription(task = pendingTasks.first(),modifier = Modifier.padding(horizontal = 21.dp))
    }
}

@Preview
@Composable
fun AttachmentsSectionPreview(){
    Surface {
        AttachmentList(task = pendingTasks.first(), modifier = Modifier.padding(horizontal = 21.dp))
    }
}


@Preview
@Composable
fun TaskCreatorSectionPreview(){
    Surface {
        TaskCreatorSection(task = pendingTasks.first())
    }
}

@Preview
@Composable
fun TaskActivitiesSectionPreview(){
    Surface {
        TaskActivitiesSection(task = pendingTasks.first())
    }
}

@Preview
@Composable
fun TaskMessageBoxPreview(){
    Surface {
        TaskMessageBox(textFieldValue = "", onTextFieldValueChanged = {}, onSendMessageButtonClicked = {})
    }
}

enum class FileType{
    PDF,EXCELSHEET,IMAGE
}




