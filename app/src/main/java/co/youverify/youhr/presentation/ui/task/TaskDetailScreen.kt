package co.youverify.youhr.presentation.ui.task

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import co.youverify.youhr.R
import co.youverify.youhr.core.util.capitalizeWords
import co.youverify.youhr.core.util.toOrdinalDateString
import co.youverify.youhr.presentation.ui.components.MultiColoredText
import co.youverify.youhr.presentation.ui.components.TextAvatar
import co.youverify.youhr.presentation.ui.components.YouHrTitleBar
import co.youverify.youhr.presentation.ui.home.announcementItemAvatarColors
import co.youverify.youhr.presentation.ui.theme.*
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TaskDetailScreen(
    modifier: Modifier = Modifier,
    //navController: NavHostController,
    //taskDetailViewModel: TaskDetailViewModel,
    taskId:Int,
    currentTaskList:List<Task>,
    taskMessage: String,
    onTaskMessageChanged:(String)->Unit,
    onSendMessageButtonClicked:()->Unit,
){
   // val taskId=taskDetailViewModel.taskId
    val task= pendingTasks[0]// should be gotten from the database using the id
    val scrollState= rememberScrollState()
    Column(modifier= modifier
        .fillMaxSize()
        .verticalScroll(scrollState)) {
        YouHrTitleBar(
            title = "Interview With Candidate For Product Design Role",
            navController = rememberAnimatedNavController(),
            modifier = Modifier.padding(top=36.dp, start = 21.dp,end=20.dp)
        )
        Divider(thickness = 0.2.dp, color = codeInputUnfocused, modifier = Modifier
            .fillMaxWidth()
            .padding( top = 16.dp, bottom = 22.dp))
        AssigneeInfo(task=currentTaskList[taskId], modifier = Modifier.padding(start = 21.dp,end=34.dp,bottom=20.dp))
        DateInfo(task=currentTaskList[taskId], modifier = Modifier.padding(start = 21.dp,end=21.dp,bottom=20.dp))
        ProjectInfo(task=currentTaskList[taskId], modifier = Modifier.padding(start = 21.dp,end=21.dp,bottom=32.dp))
        TaskDescription(task=currentTaskList[taskId], modifier = Modifier.padding(start = 21.dp,end=21.dp,bottom=10.dp))
        AttachmentsSection(task = pendingTasks.first(),modifier = Modifier.padding(start = 21.dp,end=21.dp,bottom=23.dp))
        TaskActivitiesSection()
        Divider(thickness = 1.dp, color = taskActivitiesBackGroundColor, modifier = Modifier.fillMaxWidth())
        TaskMessageBox(
            textFieldValue =taskMessage,
            onTextFieldValueChanged =onTaskMessageChanged,
            onSendMessageButtonClicked =onSendMessageButtonClicked
        )


    }
}



@Composable
fun TaskActivitiesSection(modifier: Modifier=Modifier) {
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
                neutralColor = bodyTextColor,
                parts = arrayOf(
                    "Seth Samuel added to project ",
                    "Employee Onboarding"
                )
            )
            
            Text(text = " 57 mins ago", fontSize = 8.sp, color = bodyTextColor, modifier=Modifier.padding(top=2.dp))
        }

        Text(text = "Seth Samuel assigned this task to you", fontSize = 10.sp, color = bodyTextColor)
        Text(text = "Seth Samuel changed due date to Wednesday", fontSize = 10.sp, color = bodyTextColor)
    }
}


@Composable
fun AssigneeInfo(modifier: Modifier=Modifier, task: Task) {
    ConstraintLayout(modifier = modifier.fillMaxWidth()) {
        val (textAvatar,assignedToText,assigneeNameText,reassignButton)=createRefs()
        
        TextAvatar(
            color = announcementItemAvatarColors.random(),
            text = task.assignee.first().uppercaseChar().toString(),
            modifier = Modifier.constrainAs(textAvatar){
                start.linkTo(parent.start)
                top.linkTo(parent.top,2.dp)
            }
        )
        
        Text(
            text = "Assigned to",
            fontSize = 10.sp,
            lineHeight=16.sp,
            color = inputDeepTextColor,
            modifier = Modifier.constrainAs(assignedToText){
                start.linkTo(textAvatar.end,8.dp)
                top.linkTo(parent.top)
            }
        )

        Text(
            text = task.assigneeEmail,
            fontSize = 12.sp,
            lineHeight=16.sp,
            fontWeight = FontWeight.Medium,
            color = inputDeepTextColor,
            modifier = Modifier.constrainAs(assigneeNameText){
                start.linkTo(assignedToText.start)
                top.linkTo(assignedToText.bottom,2.dp)
            }
        )


        OutlinedIconButton(
            onClick = { },
            shape = RectangleShape,
            colors = IconButtonDefaults.iconButtonColors(containerColor = backGroundColor, contentColor = yvColor1),
            border = BorderStroke(width = 0.5.dp,color= yvColor1),
            modifier = Modifier
                .size(72.dp, 24.dp)
                .constrainAs(reassignButton) {
                    end.linkTo(parent.end)
                    centerVerticallyTo(parent)

                }
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.padding(start = 6.83.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painter = painterResource(id = R.drawable.ic_reassign_task), contentDescription =null )
                Text(text = "Reassign", fontSize = 10.sp, lineHeight = 16.sp, fontWeight = FontWeight.Medium, color = yvColor1,modifier=Modifier.padding(end=2.dp))
            }
        }
    }

}

@Composable
fun DateInfo(task: Task, modifier: Modifier=Modifier) {

    ConstraintLayout(modifier=modifier) {
        val (calendarIcon,dueDateText,dueDate) = createRefs()


        Box(
            modifier = Modifier
                .border(width = 1.dp, color = yvColor1, shape = CircleShape)
                .constrainAs(calendarIcon) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top,2.dp)
                }
                .padding(4.dp),
            contentAlignment = Alignment.Center,
            content = { Image(painter= painterResource(id = R.drawable.material_symbols_calendar_today_rounded), contentDescription = null) }
        )


        Text(
            text = "Due date",
            fontSize = 10.sp,
            lineHeight=16.sp,
            color = inputDeepTextColor,
            modifier = Modifier.constrainAs(dueDateText){
                start.linkTo(calendarIcon.end,4.dp)
                top.linkTo(parent.top)
            }
        )

        Text(
            text = task.dueDate.toOrdinalDateString(includeOf = false),
            fontSize = 12.sp,
            lineHeight=16.sp,
            fontWeight = FontWeight.Medium,
            color = yvColor1,
            modifier = Modifier.constrainAs(dueDate){
                start.linkTo(dueDateText.start)
                top.linkTo(dueDateText.bottom)
            }
        )
    }
}


@Composable
fun ProjectInfo(task: Task, modifier: Modifier=Modifier) {
    
    Column(
        verticalArrangement = Arrangement.spacedBy(2.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = "Project", fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp, color = inputDeepTextColor)
        Text(text = task.project, fontSize = 11.sp, lineHeight = 16.sp, color = bodyTextColor)

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
                .border(width = 0.5.dp, color = deactivatedColor, shape = RoundedCornerShape(4.dp)),
            content = {
                Text(text = task.description, fontSize = 11.sp, lineHeight = 16.sp,
                    color = bodyTextColor, modifier = Modifier.padding(top = 9.dp, start = 9.dp, bottom = 22.dp))
            }
        )

    }
}

@Composable
fun AttachmentsSection(task: Task, modifier: Modifier=Modifier) {

    val stroke = Stroke(width = 1f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )


    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Text(text = "Attachments", fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp, color = inputDeepTextColor)
        Box(
            modifier = Modifier
                .size(64.dp)
                .drawBehind {
                    drawRoundRect(
                        color = primaryColor,
                        style = stroke,
                        cornerRadius = CornerRadius(x = 20f, y = 20f)
                    )
                },
            contentAlignment = Alignment.Center
        ){
            Image(painter = painterResource(id = R.drawable.ic_attachments), contentDescription = null)
        }

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
            painter = painterResource(R.drawable.friend_profile) ,
            contentDescription =null
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
            color= bodyTextColor,
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
            currentTaskList = pendingTasks
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
fun DateInfoPreview(){
    Surface {
        DateInfo(task = pendingTasks.first(),modifier = Modifier.padding(horizontal = 21.dp))
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
        AttachmentsSection(task = pendingTasks.first(), modifier = Modifier.padding(horizontal = 21.dp))
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
        TaskActivitiesSection()
    }
}

@Preview
@Composable
fun TaskMessageBoxPreview(){
    Surface {
        TaskMessageBox(textFieldValue = "", onTextFieldValueChanged = {}, onSendMessageButtonClicked = {})
    }
}




