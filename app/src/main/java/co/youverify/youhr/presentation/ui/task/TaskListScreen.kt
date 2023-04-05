package co.youverify.youhr.presentation.ui.task

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import co.youverify.youhr.R
import co.youverify.youhr.core.util.toOrdinalDateString
import co.youverify.youhr.presentation.ui.theme.*
import java.util.*

@Composable
fun TaskListScreen(
    modifier: Modifier = Modifier,
    tasks: List<Task>,
    listState: LazyListState,
    categoryDropDownExpanded: Boolean,
    dateDropDownExpanded: Boolean,
    onCategorySpinnerClicked: () -> Unit,
    onDateSpinnerClicked: () -> Unit,
    categoryDropDownOnDismissCallBack: () -> Unit,
    dateDropDownOnDismissCallBack: () -> Unit,
    onTaskItemClicked: (Int) -> Unit,
    onPendingClicked: () -> Unit,
    onCompletedClicked: () -> Unit,
    categorySpinnerText: String,
    dateSpinnerText: String


){
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(21.dp)
    ) {
        //var task1 by remember{ mutableStateOf(pendingTasks) }


        TopSection(
            title = "My Tasks",
            categoryExpanded=categoryDropDownExpanded,
            dateExpanded=dateDropDownExpanded,
            onCategorySpinnerClicked=onCategorySpinnerClicked,
            onDateSpinnerClicked=onDateSpinnerClicked,
            dateDropDownOnDismissCallBack = dateDropDownOnDismissCallBack,
            categoryDropDownOnDismissCallBack = categoryDropDownOnDismissCallBack,
            onPendingClicked = onPendingClicked,
            onCompletedClicked = onCompletedClicked,
            categorySpinnerText = categorySpinnerText,
            dateSpinnerText = dateSpinnerText
        )
        Divider(thickness = 1.dp, color = codeInputUnfocused, modifier = Modifier.fillMaxWidth())
        TaskList(tasks = tasks, state =listState, onTaskItemClicked =onTaskItemClicked)
    }
}



@Composable
fun TopSection(
    modifier: Modifier = Modifier,
    title: String,
    categoryExpanded: Boolean,
    dateExpanded: Boolean,
    onCategorySpinnerClicked: () -> Unit,
    onDateSpinnerClicked: () -> Unit,
    dateDropDownOnDismissCallBack: () -> Unit,
    categoryDropDownOnDismissCallBack: () -> Unit,
    onPendingClicked: () -> Unit,
    onCompletedClicked: () -> Unit,
    categorySpinnerText: String,
    dateSpinnerText: String
) {
    ConstraintLayout(modifier = modifier
        .padding(top = 36.dp)
        .fillMaxWidth()) {
        val (titleText,categorySpinner,dateSpinner) = createRefs()

        Text(
            text = title,
            modifier = Modifier.constrainAs(titleText){
                top.linkTo(parent.top,16.dp)
                start.linkTo(parent.start,20.dp)
            },
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = yvText
        )


        DateSpinner(
            modifier= Modifier.constrainAs(dateSpinner){
                top.linkTo(categorySpinner.top)
                start.linkTo(categorySpinner.end,8.dp)
            },
            onDateSpinnerClicked=onDateSpinnerClicked,
            dateDropDownOnDismissCallBack = dateDropDownOnDismissCallBack,
            dropDownExpanded=dateExpanded,
            spinnerText = dateSpinnerText
        )

        CategorySpinner(
            modifier = Modifier.constrainAs(categorySpinner){
                top.linkTo(titleText.bottom,24.dp)
                bottom.linkTo(parent.bottom,16.dp)
                start.linkTo(titleText.start)
            },
            onCategorySpinnerClicked=onCategorySpinnerClicked,
            categoryDropDownOnDismissCallBack = categoryDropDownOnDismissCallBack,
            onPendingClicked = onPendingClicked,
            onCompletedClicked = onCompletedClicked,
            dropDownExpanded=categoryExpanded,
            spinnerText = categorySpinnerText
        )

    }
}

@Composable
fun TaskList(
    modifier: Modifier = Modifier,
    tasks: List<Task>,
    state: LazyListState,
    onTaskItemClicked: (Int) -> Unit
) {

    val listState= rememberLazyListState()
    LazyColumn(modifier = modifier
        .fillMaxSize(),
        //.background(color = Color.Yellow),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        state = listState
    ){
        val isCompletedTask=tasks.first().isCompleted


        itemsIndexed(items = tasks){index,task->

            if(isCompletedTask) CompletedTaskItem(
                modifier =Modifier.padding(horizontal = 20.dp),
                task = task,
                index =index,
                onTaskItemClicked =onTaskItemClicked)
            else PendingTaskItem(
                modifier =Modifier.padding(horizontal = 20.dp),
                task = task,
                index =index,
                onTaskItemClicked =onTaskItemClicked)
        }
    }
}

@Composable
fun CategorySpinner(
    modifier: Modifier = Modifier,
    onCategorySpinnerClicked: () -> Unit,
    categoryDropDownOnDismissCallBack: () -> Unit,
    onPendingClicked: () -> Unit,
    onCompletedClicked: () -> Unit,
    dropDownExpanded: Boolean,
    spinnerText:String
) {


    Box(
        modifier = modifier
            .size(96.dp, 28.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .border(width = 1.dp, color = dividerColor, shape = RoundedCornerShape(8.dp))
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_drop_down_indicator), contentDescription =null ,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 10.dp)
            )
        
        Text(
            text = spinnerText,
            fontSize = 10.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 28.dp)
        )

        IconButton(
            onClick = {

                onCategorySpinnerClicked()
            },
            modifier= Modifier
                .align(Alignment.CenterStart)
                .padding(start = 70.67.dp)
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_spinner), contentDescription =null )
        }

        DropdownMenu(expanded = dropDownExpanded, onDismissRequest = { categoryDropDownOnDismissCallBack()}) {
            DropdownMenuItem(text = { Text(text = "Pending", fontSize = 12.sp, color = yvText)}, onClick = {
                onPendingClicked()
            })
            DropdownMenuItem(text = { Text(text = "Completed", fontSize = 12.sp, color = yvText)}, onClick = {
                onCompletedClicked()
            })
        }
    }
}

@Composable
fun DateSpinner(
    modifier: Modifier = Modifier,
    onDateSpinnerClicked: () -> Unit,
    dateDropDownOnDismissCallBack: () -> Unit,
    dropDownExpanded: Boolean,
    spinnerText: String
) {

    Box(
        modifier = modifier
            .size(139.dp, 28.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .border(width = 1.dp, color = dividerColor, shape = RoundedCornerShape(8.dp))
    ) {


        Text(
            text = "9th-16 March 2023",
            fontSize = 10.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 8.dp)
        )

        IconButton(
            onClick = {
                      onDateSpinnerClicked()
            },
            modifier= Modifier
                .align(Alignment.CenterStart)
                .padding(start = 115.67.dp)
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_spinner), contentDescription =null )
        }

        DropdownMenu(expanded = dropDownExpanded, onDismissRequest = {dateDropDownOnDismissCallBack() }) {
        }
    }
}

@Composable
fun PendingTaskItem(
    modifier: Modifier = Modifier,
    task: Task,
    index: Int,
    onTaskItemClicked: (Int) -> Unit,
){





    Row(
        modifier = modifier
            //.padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(72.dp)
            .clickable {
                onTaskItemClicked(index)
            }

    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(4.dp)
                .background(
                    color = taskItemSpacerColors[index % taskItemSpacerColors.size],
                    shape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp)
                )

        )

        ConstraintLayout(

            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    color = taskItemBorderColor,
                    shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp)
                )
                .background(color = taskItemFillColor)
                .padding(start = 8.dp),

            content = {

                val (clockIcon,dueDateText,descriptionText,checkBox) = createRefs()



                Image(
                    painter = painterResource(id = R.drawable.ic_clock), contentDescription = null,
                    modifier = Modifier.constrainAs(clockIcon){
                        start.linkTo(parent.start)
                        top.linkTo(parent.top,9.33.dp)
                    }
                )

                Text(
                    text = "Due ${task.dueDate.toOrdinalDateString(includeOf = false)}",
                    modifier = Modifier.constrainAs(dueDateText){
                        start.linkTo(clockIcon.end,9.33.dp)
                        top.linkTo(parent.top,8.dp)
                    },
                    fontSize = 10.sp,
                    lineHeight = 16.sp,
                    color = bodyTextColor
                )


                Text(
                    text = task.title,
                    modifier = Modifier.constrainAs(descriptionText){
                        start.linkTo(clockIcon.start)
                        top.linkTo(dueDateText.bottom,8.dp)
                        bottom.linkTo(parent.bottom,8.dp)
                        end.linkTo(checkBox.start,60.dp)
                        width= Dimension.fillToConstraints
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 16.sp,
                    color = inputDeepTextColor
                )

                Checkbox(
                    checked = false,
                    onCheckedChange ={} ,
                    modifier = Modifier
                        .size(18.dp)
                        .constrainAs(checkBox) {
                            end.linkTo(parent.end, 15.dp)
                            centerVerticallyTo(parent)
                        },
                    colors = CheckboxDefaults.colors(
                        checkedColor = yvColor,
                        uncheckedColor = bodyTextColor,
                        checkmarkColor = Color.White
                    )
                )
            }

        )
    }

}


@Composable
fun CompletedTaskItem(
    modifier: Modifier = Modifier,
    task: Task,
    index: Int,
    onTaskItemClicked: (Int) -> Unit
){


    Row(
        modifier = modifier
            //.padding(horizontal = 20.dp)
            .fillMaxWidth()
            .height(72.dp)
            .clickable {
                onTaskItemClicked(index)
            }

    ) {


        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(4.dp)
                .background(
                    color = taskItemSpacerColors[index % taskItemSpacerColors.size],
                    shape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp)
                )

        )


        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    color = taskItemBorderColor,
                    shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp)
                )
                .background(color = taskItemFillColor)
                .padding(start = 8.dp),

            content = {

                val (descriptionText,checkBox) = createRefs()

                Text(
                    text = task.title,
                    modifier = Modifier.constrainAs(descriptionText){
                        start.linkTo(parent.start)
                        centerVerticallyTo(parent)
                        end.linkTo(checkBox.start,60.dp)
                        width= Dimension.fillToConstraints

                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 16.sp,
                    color = inputDeepTextColor
                )

                Checkbox(
                    checked = true,
                    onCheckedChange ={} ,
                    modifier = Modifier
                        .size(18.dp)
                        .constrainAs(checkBox) {
                            end.linkTo(parent.end, 15.dp)
                            centerVerticallyTo(parent)
                        },
                    colors = CheckboxDefaults.colors(
                        checkedColor = yvColor,
                        uncheckedColor = bodyTextColor,
                        checkmarkColor = Color.White
                    )
                )
            }

        )
    }

}




@Composable
@Preview
fun TaskListScreenPreview(){
    YouHrTheme {
        Surface {
            TaskListScreen(
                tasks = completedTasks,
                listState = rememberLazyListState(),
                categoryDropDownExpanded = false,
                dateDropDownExpanded = false,
                onCategorySpinnerClicked = {},
                onDateSpinnerClicked = {},
                categoryDropDownOnDismissCallBack = {},
                dateDropDownOnDismissCallBack = {},
                onTaskItemClicked = {},
                onPendingClicked = {},
                onCompletedClicked = {},
                categorySpinnerText = "Pending",
                dateSpinnerText = ""
            )
        }

    }
}

@Composable
@Preview
fun PendingTaskItemPreview(){
    YouHrTheme {
        Surface {
            PendingTaskItem(
                task = Task(
                    Date(),
                    "Interview with candidates for product design role",
                    isCompleted = true,
                    assignee = "Edet", assigner = "Seth Samuel", creator = "Timothy John"
                    ,assigneeEmail = "Edna@youverify.co", description ="Onboarding new employees help them familiarize themselves with the company’s structure as well as help them settle in faster",
                    project = "Employee Onboarding"
                ),
                index = 0
            ) {}
        }

    }
}


@Composable
@Preview
fun CompletedTaskItemPreview(){
    YouHrTheme {
        Surface {
            CompletedTaskItem(
                task = Task(
                    Date(),
                    "Interview with candidates for product design role",
                    isCompleted = true,
                    assignee = "Edet", assigner = "Seth Samuel", creator = "Timothy John",
                    assigneeEmail = "Edna@youverify.co",
                    description ="Onboarding new employees help them familiarize themselves with the company’s structure as well as help them settle in faster",
                    project = "Employee Onboarding"
                ),
                index = 0
            ) {}
        }

    }
}

data class Task(
    val dueDate: Date, val title:String, val assignee:String, val assigneeEmail: String, val isCompleted:Boolean, val assigner:String, val creator:String, val project:String, val description:String)
val pendingTasks= listOf(
    Task(Date(), "Interview with candidates for product design role", isCompleted = false, assignee = "Edet",assigner = "Seth Samuel", creator = "Timothy John", assigneeEmail = "Edna@youverify.co", project = "Employee Onboarding", description ="Onboarding new employees help them familiarize themselves with the company’s structure as well as help them settle in faster" ),
    Task(Date(), "Meeting with the head of each department", isCompleted = false, assignee = "Bolaji", assigner = "Kene Nsofor", creator = "Adewusi Teni",assigneeEmail = "bolaji@youverify.co",project = "Client Meeting",description ="Onboarding new employees help them familiarize themselves with the company’s structure as well as help them settle in faster"),
    Task(Date(), "Meeting with the head of each department", isCompleted = false, assignee = "Yusuf",assigner = "Tracy Mark", creator = "Keneth Brown",assigneeEmail = "yusuf@youverify.co",project = "Product Exhibition",description ="Onboarding new employees help them familiarize themselves with the company’s structure as well as help them settle in faster"),
    Task(Date(), "Create new email address for new employees", isCompleted = false, assignee = "Paul",assigner = "Donald Njaoguani", creator = "Richard Fish",assigneeEmail = "paul@youverify.co",project = "Employee Onboarding",description ="Onboarding new employees help them familiarize themselves with the company’s structure as well as help them settle in faster"),
    Task(Date(), "Create new email address for new employees", isCompleted = false, assignee = "Nkechi",assigner = "Chioma Terena", creator = "Edna Ibeh",assigneeEmail = "nkechi@youverify.co",project = "Employee Onboarding",description ="Onboarding new employees help them familiarize themselves with the company’s structure as well as help them settle in faster"),
    Task(Date(), "Draft intern documents", isCompleted = false, assignee = "Abidemi",assigner = "Joseph Samuel", creator = "Danladi Abubakar",assigneeEmail = "abidemi@youverify.co",project = "Employee Onboarding",description ="Onboarding new employees help them familiarize themselves with the company’s structure as well as help them settle in faster"),
    Task(Date(), "Draft intern documents", isCompleted = false, assignee = "Zahra",assigner = "Praise Chima", creator = "Eniola Temidire",assigneeEmail = "zahra@youverify.co",project = "Employee Onboarding",description ="Onboarding new employees help them familiarize themselves with the company’s structure as well as help them settle in faster"),
    Task(Date(), "Prepare Appraisals", isCompleted = false, assignee = "Tina",assigner = "Bassey Kiriku", creator = "John Micheal",assigneeEmail = "tina@youverify.co",project = "Employee Onboarding",description ="Onboarding new employees help them familiarize themselves with the company’s structure as well as help them settle in faster")
)



val completedTasks= listOf(
    Task(Date(), "Interview with candidates for product design role", isCompleted = true, assignee = "Edet",assigner = "Seth Samuel", creator = "Timothy John", assigneeEmail = "Edna@youverify.co", project = "Employee Onboarding", description ="Onboarding new employees help them familiarize themselves with the company’s structure as well as help them settle in faster" ),
    Task(Date(), "Meeting with the head of each department", isCompleted = true, assignee = "Bolaji", assigner = "Kene Nsofor", creator = "Adewusi Teni",assigneeEmail = "bolaji@youverify.co",project = "Employee Onboarding",description ="Onboarding new employees help them familiarize themselves with the company’s structure as well as help them settle in faster"),
    Task(Date(), "Meeting with the head of each department", isCompleted = true, assignee = "Yusuf",assigner = "Tracy Mark", creator = "Keneth Brown",assigneeEmail = "yusuf@youverify.co",project = "Employee Onboarding",description ="Onboarding new employees help them familiarize themselves with the company’s structure as well as help them settle in faster"),
    Task(Date(), "Create new email address for new employees", isCompleted = true, assignee = "Paul",assigner = "Donald Njaoguani", creator = "Richard Fish",assigneeEmail = "paul@youverify.co",project = "Employee Onboarding",description ="Onboarding new employees help them familiarize themselves with the company’s structure as well as help them settle in faster"),
    Task(Date(), "Create new email address for new employees", isCompleted = true, assignee = "Nkechi",assigner = "Chioma Terena", creator = "Edna Ibeh",assigneeEmail = "nkechi@youverify.co",project = "Employee Onboarding",description ="Onboarding new employees help them familiarize themselves with the company’s structure as well as help them settle in faster"),
    Task(Date(), "Draft intern documents", isCompleted = true, assignee = "Abidemi",assigner = "Joseph Samuel", creator = "Danladi Abubakar",assigneeEmail = "abidemi@youverify.co",project = "Employee Onboarding",description ="Onboarding new employees help them familiarize themselves with the company’s structure as well as help them settle in faster"),
    Task(Date(), "Draft intern documents", isCompleted = true, assignee = "Zahra",assigner = "Praise Chima", creator = "Eniola Temidire",assigneeEmail = "zahra@youverify.co",project = "Employee Onboarding",description ="Onboarding new employees help them familiarize themselves with the company’s structure as well as help them settle in faster"),
    Task(Date(), "Prepare Appraisals", isCompleted = true, assignee = "Tina",assigner = "Bassey Kiriku", creator = "John Micheal",assigneeEmail = "tina@youverify.co",project = "Employee Onboarding",description ="Onboarding new employees help them familiarize themselves with the company’s structure as well as help them settle in faster")
)

val taskItemSpacerColors= listOf(
    Color(0XFFFF5454),
    Color(0XFF547AFF),
    Color(0XFFDAA419),
    Color(0XFFD619DA),
    Color(0XFF93FD52),
)