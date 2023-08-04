
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import co.youverify.youhr.R
import co.youverify.youhr.core.util.getColor
import co.youverify.youhr.core.util.getDateRange
import co.youverify.youhr.core.util.getStatus
import co.youverify.youhr.core.util.toCardinalDateFormat2
import co.youverify.youhr.core.util.toFormattedDateString
import co.youverify.youhr.domain.model.AttachedDoc
import co.youverify.youhr.domain.model.Task
import co.youverify.youhr.presentation.ui.components.ActionButton
import co.youverify.youhr.presentation.ui.components.ConnectionErrorScreen
import co.youverify.youhr.presentation.ui.components.shimmerEffect
import co.youverify.youhr.presentation.ui.task.TaskListUiState
import co.youverify.youhr.presentation.ui.task.TaskStatus
import co.youverify.youhr.presentation.ui.task.TaskViewModel
import co.youverify.youhr.presentation.ui.theme.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun TaskListScreen(
    modifier: Modifier = Modifier,
    uiState: TaskListUiState,
    //listState: LazyListState,
    //categoryDropDownExpanded: Boolean,
    //showDatePicker: Boolean,
    onDatePickerOkClicked: (DatePickerState, Int) -> Unit,
    onDatePickerCancelClicked: () -> Unit,
    onCategoryFilterClicked: () -> Unit,
    categoryDropDownOnDismissCallBack: () -> Unit,
    onTaskItemClicked: (Int) -> Unit,
    //categorySpinnerText: String,
    dateRange: DateRange,
    onDateInputFieldClicked: (Int) -> Unit,
    onTaskProgressDropDownItemClicked: (TaskStatus) -> Unit,
    //currentEditableDateInputField: Int,

    fetchMore: () -> Unit,
    //isFetchingMore: Boolean,
    onRetry:()->Unit,
    getTaskOnFirstLoad:()->Unit,
    onDateFilterBottomSheetConfirmClicked:()->Unit


){

    /*val dateRangePickerState = rememberDateRangePickerState(
        //initialSelectedEndDateMillis = Instant.now().toEpochMilli(),
        //initialSelectedStartDateMillis = Instant.now().toEpochMilli()-(3600*24*7*1000),
        yearRange = IntRange(start = 2023, endInclusive = 2023)
    )*/

    LaunchedEffect(key1 = Unit){
        getTaskOnFirstLoad()
    }

    val myDatePickerState= rememberDatePickerState()
    val  bottomSheetState= rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val coroutineScope= rememberCoroutineScope()
    //val pullRefreshState= rememberPullRefreshState(refreshing = isRefreshing, onRefresh = { onRefresh() })


    /*LaunchedEffect(key1 = Unit){
        getTaskOnFirstLoad()
    }*/

    Box(modifier = modifier) {
        Column(
            modifier=Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(21.dp),
            content = {
                TopSection(
                    title = "My Tasks",
                    categoryExpanded =uiState.categoryDropdownExpanded,
                    onCategoryFilterClicked =onCategoryFilterClicked,
                    categoryDropDownOnDismissCallBack = categoryDropDownOnDismissCallBack,
                    categorySpinnerText = uiState.filterText,
                    //filteringDisabled =uiState.value.tasks.isNullOrEmpty(),
                    dateRange =dateRange,
                    onTaskProgressDropDownItemClicked = onTaskProgressDropDownItemClicked,
                    bottomSheetState =bottomSheetState
                )
                Box(
                    modifier = Modifier.fillMaxSize(),

                    content = {



                        if (uiState.loading){
                            ShimmerList()
                        }
                       else if(uiState.internetConnectionError){
                           ConnectionErrorScreen(description = "Ooops, your connection seems off...", onRetryButtonClicked = {onRetry()})
                       }
                        else if(uiState.taskListIsEmpty){
                            EmptyStateContent(contentMessage = uiState.emptyTaskContentMessage)
                        }

                        else if(uiState.unexpectedError){
                            ConnectionErrorScreen(description = "Ooops..An unexpected error occurred while connecting to the sever!", onRetryButtonClicked = {onRetry()})
                        }

                       //else if (!uiState.value.loading && uiState.value.tasks==null){ return}
                       else  {
                            TaskList(
                                //tasks = uiState.value.tasks!!,
                                //listState =listState,
                                onTaskItemClicked =onTaskItemClicked,
                                fetchMore = fetchMore,
                                //isFetchingMore = isFetchingMore,
                                 uiState=uiState
                            )
                        }


                    }
                )
            }
        )

        if (uiState.showDatePicker)
            BottomSheetDatePicker(
                state =myDatePickerState,
                onOkButtonClicked = onDatePickerOkClicked,
                onCancelButtonClicked = onDatePickerCancelClicked,
                currentIndex=uiState.currentEditableDateInputField,
                bottomSheetState=bottomSheetState,
                coroutineScope = coroutineScope,
                modifier = Modifier.align(Alignment.Center)
            )

        //if (uiState.value.loading)
           // CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))

        if(bottomSheetState.isVisible)
            ModalBottomSheet(
                onDismissRequest = {},
                dragHandle = {},
                shape = RoundedCornerShape(8.dp),
                containerColor = Color.White,
                content = {
                    DateRangeInputBottomSheetContent(
                        sheetState =bottomSheetState ,
                        dateRange =dateRange ,
                        onInputFieldClicked =onDateInputFieldClicked ,
                        coroutineScope =coroutineScope,
                        onConfirmClicked=onDateFilterBottomSheetConfirmClicked
                    )
                          },
                sheetState = bottomSheetState
            )
    }




}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun EmptyStateContent(
    modifier: Modifier = Modifier,
    contentMessage:String
) {

    Box(
        modifier= modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {

            Image(
                painter = painterResource(id = R.drawable.task_list_empty_state),
                contentDescription =null ,
                modifier=Modifier.padding(bottom=24.dp)
            )

            Text(
                text = "No Tasks",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color= bodyTextDeepColor,
                modifier=Modifier.padding(bottom =8.dp ),
            )

            Text(
                text = contentMessage,
                fontSize = 12.sp,
                color= bodyTextLightColor,
                modifier=Modifier.padding(bottom =8.dp ),
                textAlign = TextAlign.Center
            )
        }

    }


}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun DateRangeInputBottomSheetContent(
    modifier: Modifier = Modifier,
    sheetState: SheetState,
    dateRange: DateRange,
    onInputFieldClicked: (Int) -> Unit,
    coroutineScope: CoroutineScope,
    onConfirmClicked: () -> Unit
) {



        val inputFieldValue1=dateRange.startDateMillis?.toFormattedDateString(pattern="dd - MM - yyyy")?:""
        val inputFieldValue2=dateRange.endDateMillis?.toFormattedDateString(pattern="dd - MM - yyyy")?:""

        Column(
            modifier = modifier

        ) {

            Box(
                modifier= Modifier
                    .padding(start = 20.dp, top = 24.dp)
                    .fillMaxWidth(),
                content = {
                    Text(
                        text = "Select Range",
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp,
                        color= bodyTextDeepColor,
                        modifier=Modifier.align(Alignment.CenterStart)
                    )

                    Image(
                        painter = painterResource(id = R.drawable.ic_close),
                        contentDescription = null,
                        modifier= Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 23.dp)
                            .clickable {
                                coroutineScope.launch {
                                    sheetState.hide()
                                }
                            }
                    )
                }
            )

            Divider(
                modifier= Modifier
                    .padding(top = 19.dp)
                    .fillMaxWidth(),
                thickness = 1.dp,
                color = deactivatedColorLight
            )

            DateInputField(
                fieldValue = inputFieldValue1,
                title="Start Date",
                modifier=Modifier.padding(top=21.dp, start = 20.dp, end = 20.dp),
                onClick = onInputFieldClicked,
                index = 1,
                sheetState=sheetState
            )

            DateInputField(
                inputFieldValue2,
                modifier=Modifier.padding(top=12.dp, start = 20.dp, end = 20.dp),
                index = 2,
                onClick = onInputFieldClicked,
                title="End Date",
                sheetState=sheetState,
            )

            ActionButton(
                text = "Confirm",
                modifier = Modifier
                    .padding(top = 28.dp, start = 20.dp, end = 20.dp, bottom = 30.dp)
                    .fillMaxWidth(),
                onButtonClicked = {
                    onConfirmClicked()
                    coroutineScope.launch { sheetState.hide() }
                }
            )

        }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateInputField(
    fieldValue: String,
    modifier: Modifier,
    index: Int,
    onClick: (Int) -> Unit,
    title: String,
    sheetState: SheetState,
) {
    val focusManager= LocalFocusManager.current
    val coroutineScope= rememberCoroutineScope()
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(text = title, fontSize = 12.sp, color= bodyTextDeepColor)
        OutlinedTextField(
            value = fieldValue, onValueChange = {},
            modifier= Modifier
                .requiredHeight(47.dp)
                .onFocusChanged { focusState ->
                    if (focusState.hasFocus) {
                        //coroutineScope.launch { sheetState.hide() }
                        focusManager.clearFocus()
                        coroutineScope.launch { sheetState.hide() }
                        onClick(index)
                    }

                }
                .fillMaxWidth(),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calendar_2),
                    contentDescription =null,
                    tint = bodyTextDeepColor
                )
            },
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = deactivatedColorDeep)
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSection(
    modifier: Modifier = Modifier,
    title: String,
    categoryExpanded: Boolean,
    categoryDropDownOnDismissCallBack: () -> Unit,
    categorySpinnerText: String,
    //filteringDisabled: Boolean,
    dateRange: DateRange,
    onTaskProgressDropDownItemClicked: (TaskStatus) -> Unit,
    bottomSheetState: SheetState,
    onCategoryFilterClicked: () -> Unit,
) {

    //val coroutineScpoe= rememberCoroutineScope()
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


        DateFilter(
            modifier = Modifier.constrainAs(dateSpinner){
                top.linkTo(categorySpinner.top)
                start.linkTo(categorySpinner.end,8.dp)
                bottom.linkTo(parent.bottom,16.dp)
            },
            dateRange=dateRange,
            //dateFilterDisabled =filteringDisabled,
            bottomSheetState=bottomSheetState,
        )

        CategoryFilter(
            modifier = Modifier.constrainAs(categorySpinner){
                top.linkTo(titleText.bottom,25.dp)
                start.linkTo(titleText.start)
                bottom.linkTo(parent.bottom,16.dp)
            },
            onClick =onCategoryFilterClicked,
            categoryDropDownOnDismissCallBack = categoryDropDownOnDismissCallBack,
            dropDownExpanded =categoryExpanded,
            spinnerText = categorySpinnerText,
            //categoryFilterDisabled=filteringDisabled,
            onDropDownMenuItemClicked = onTaskProgressDropDownItemClicked
        )

    }
}

@Composable
fun TaskList(
    modifier: Modifier = Modifier,
    //tasks: List<Task>,
    //listState: LazyListState,
    onTaskItemClicked: (Int) -> Unit,
    //isFetchingMore: Boolean,
    fetchMore: () -> Unit,
    uiState: TaskListUiState
) {
    val listState= rememberLazyListState()
    LazyColumn(modifier = modifier
        .fillMaxSize()
        .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        state = listState
    ){



        itemsIndexed(items = uiState.filteredList){index,task->


            TaskItem(
                        modifier =Modifier.padding(horizontal = 20.dp),
                        task = task,
                        index =index,
                        onTaskItemClicked =onTaskItemClicked
                    )

        }

        if(uiState.isFetchingMore){
            item{
                Box(contentAlignment = Alignment.Center){CircularProgressIndicator(strokeWidth = 1.dp)}
            }
        }
    }

    if (uiState.filteredList.isNotEmpty()){
        listState.OnBottomReached(lastTask = uiState.filteredList.last(), loadMore = fetchMore)
    }
}

@Composable
fun CategoryFilter(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    categoryDropDownOnDismissCallBack: () -> Unit,
    onDropDownMenuItemClicked: (TaskStatus) -> Unit,
    dropDownExpanded: Boolean,
    spinnerText: String,
    //categoryFilterDisabled: Boolean
) {


    //val contentColor=if (!categoryFilterDisabled) primaryColor else deactivatedColorDeep

    Row(
        modifier = modifier
            .height(28.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .border(width = 1.dp, color = deactivatedColorDeep, shape = RoundedCornerShape(8.dp))
            .clickable { onClick() }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_drop_down_indicator), contentDescription =null ,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 12.dp),
            tint = primaryColor
            )
        
        Text(
            text = spinnerText,
            fontSize = 10.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 14.dp),
            color= primaryColor
        )




            Icon(
                painter = painterResource(id = R.drawable.ic_spinner),
                contentDescription =null,
                modifier= Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 16.6.dp, end = 11.74.dp),
                tint = primaryColor

            )


        DropdownMenu(
            expanded = dropDownExpanded,
            onDismissRequest = { categoryDropDownOnDismissCallBack()},
            modifier =Modifier.background(color= Color.White, shape = RoundedCornerShape(4.dp))
        ) {

            DropdownMenuItem(
                text = { Text(text = TaskStatus.ALL.id, fontSize = 12.sp, color = primaryColor)},
                onClick = { onDropDownMenuItemClicked(TaskStatus.ALL) },
                leadingIcon = {
                    Box(
                        modifier= Modifier
                            .size(6.dp)
                            .background(
                                color = TaskProgressColor.ALL.value,
                                shape = CircleShape
                            )
                    )
                }
            )

            DropdownMenuItem(
                text = { Text(text = TaskStatus.COMPLETED.id, fontSize = 12.sp, color = primaryColor)},
                onClick = { onDropDownMenuItemClicked(TaskStatus.COMPLETED) },
                leadingIcon = {
                    Box(
                        modifier= Modifier
                            .size(6.dp)
                            .background(
                                color = TaskProgressColor.COMPLETED.value,
                                shape = CircleShape
                            )
                    )
                }
            )
            DropdownMenuItem(
                text = { Text(text = TaskStatus.IN_PROGRESS.id, fontSize = 12.sp, color = primaryColor)},
                onClick = { onDropDownMenuItemClicked(TaskStatus.IN_PROGRESS) },
                leadingIcon = {
                    Box(
                        modifier= Modifier
                            .size(6.dp)
                            .background(
                                color = TaskProgressColor.IN_PROGRESS.value,
                                shape = CircleShape
                            )
                    )
                }
            )

            DropdownMenuItem(
                text = { Text(text = TaskStatus.OVER_DUE.id, fontSize = 12.sp, color = primaryColor)},
                onClick = { onDropDownMenuItemClicked(TaskStatus.OVER_DUE) },
                leadingIcon = {
                    Box(
                        modifier= Modifier
                            .size(6.dp)
                            .background(
                                color = TaskProgressColor.OVERDUE.value,
                                shape = CircleShape
                            )
                    )
                }
            )
            DropdownMenuItem(
                text = { Text(text = TaskStatus.TO_DO.id, fontSize = 12.sp, color = primaryColor)},
                onClick = { onDropDownMenuItemClicked(TaskStatus.TO_DO) },
                leadingIcon = {
                    Box(
                        modifier= Modifier
                            .size(6.dp)
                            .background(
                                color = TaskProgressColor.TO_DO.value,
                                shape = CircleShape
                            )
                    )
                }
            )

            DropdownMenuItem(
                text = { Text(text =TaskStatus.REVIEW.id, fontSize = 12.sp, color = primaryColor)},
                onClick = { onDropDownMenuItemClicked(TaskStatus.REVIEW) },
                leadingIcon = {
                    Box(
                        modifier= Modifier
                            .size(6.dp)
                            .background(
                                color = TaskProgressColor.REVIEW.value,
                                shape = CircleShape
                            )
                    )
                }
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateFilter(
    modifier: Modifier = Modifier,
    //dateFilterDisabled: Boolean,
    dateRange: DateRange,
    bottomSheetState: SheetState,

) {

    val coroutineScope= rememberCoroutineScope()
    //val contentColor=if (!dateFilterDisabled) primaryColor else deactivatedColorDeep
    val showDefaultFilterText= dateRange.startDateMillis==null || dateRange.endDateMillis==null
    val filterText=if (showDefaultFilterText) "Filter by date" else getDateRange(dateRange.startDateMillis!!,dateRange.endDateMillis!!)

    Row(
        modifier = modifier
            .height(28.dp)
            .background(color = Color.White, shape = RoundedCornerShape(8.dp))
            .border(width = 1.dp, color = deactivatedColorDeep, shape = RoundedCornerShape(8.dp))
            .clickable {
                coroutineScope.launch { bottomSheetState.show() }
            }
    ) {

        Icon(
            painter = painterResource(id = R.drawable.ic_calendar),
            contentDescription =null ,
            modifier= Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 7.33.dp),
            tint = primaryColor
        )

        Text(
            text = filterText,
            fontSize = 10.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 14.dp),
            color = primaryColor
        )


            Icon(
                painter = painterResource(id = R.drawable.ic_drop_down_indicator),
                contentDescription =null ,
                modifier= Modifier
                    .align(Alignment.CenterVertically)
                    .padding(start = 14.dp, end = 10.dp),
                tint = primaryColor
            )

    }
}

@Composable
fun TaskItem(
    modifier: Modifier = Modifier,
    task: Task,
    index: Int,
    onTaskItemClicked: (Int) -> Unit,
){


    val taskStatus=task.getStatus()

    val taskColor=taskStatus.getColor()

    Row(
        modifier = modifier
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
                    color = taskColor,
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
                .background(color = Color(0XFFF7F7F9))
                .padding(start = 8.dp),

            content = {

                val (clockIcon,dueDateText,descriptionText,progressIndicatorBox) = createRefs()



                Image(
                    painter = painterResource(id = R.drawable.ic_clock), contentDescription = null,
                    modifier = Modifier.constrainAs(clockIcon){
                        start.linkTo(parent.start)
                        top.linkTo(parent.top,9.33.dp)
                    }
                )

                Text(
                    text = "Due ${task.dueDate.toCardinalDateFormat2()}",
                    modifier = Modifier.constrainAs(dueDateText){
                        start.linkTo(clockIcon.end,9.33.dp)
                        top.linkTo(clockIcon.top)
                    },
                    fontSize = 10.sp,
                    lineHeight = 16.sp,
                    color = bodyTextDeepColor
                )


                Text(
                    text = task.title,
                    modifier = Modifier.constrainAs(descriptionText){
                        start.linkTo(clockIcon.start)
                        top.linkTo(dueDateText.bottom,8.dp)
                        bottom.linkTo(parent.bottom,8.dp)
                        end.linkTo(progressIndicatorBox.start,60.dp)
                        width= Dimension.fillToConstraints
                    },
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 16.sp,
                    color = Color(0XFF344054)
                )

                Box(

                    modifier = Modifier
                        .background(
                            color = taskColor.copy(alpha = 0.1f),
                            shape = RoundedCornerShape(10.dp)
                        )
                        //.size(18.dp)
                        .constrainAs(progressIndicatorBox) {
                            end.linkTo(parent.end, 15.dp)
                            top.linkTo(parent.top, 14.dp)
                        },

                    content = {
                        Text(
                            text = taskStatus.id,
                            fontSize = 10.sp,
                            lineHeight = 13.sp,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp),
                            color=taskColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                )
            }

        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetDatePicker(
    modifier: Modifier = Modifier,
    state: DatePickerState,
    onOkButtonClicked: (DatePickerState, Int) -> Unit,
    onCancelButtonClicked: () -> Unit,
    currentIndex: Int,
    bottomSheetState: SheetState,
    coroutineScope: CoroutineScope
){

        Column(
            modifier = modifier.background(color = Color.White, shape = RoundedCornerShape(12.dp)),
            content = {
                DatePicker(
                    state =state,
                    title = {},
                    showModeToggle = false,
                    colors = DatePickerDefaults.colors(
                        containerColor = Color.White,
                        selectedDayContentColor = Color(0XFFEDFBFF),
                        selectedDayContainerColor = yvColor,
                        weekdayContentColor = bodyTextDeepColor,
                        disabledDayContentColor = Color(0xFF181E30),
                    ),
                    modifier = Modifier
                        .height(460.dp)
                )

                Row(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(end = 23.dp, top = 9.dp, bottom = 23.dp),
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                    content = {

                        Text(
                            text = "Cancel",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,color= primaryColor,
                            modifier=Modifier.clickable {
                                onCancelButtonClicked()
                                coroutineScope.launch { bottomSheetState.show() }
                            }
                        )
                        Text(
                            text = "Ok",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,color= primaryColor,
                            modifier=Modifier.clickable {
                                onOkButtonClicked(state,currentIndex)
                                coroutineScope.launch { bottomSheetState.show() }
                            }
                        )
                    }
                )

            }
        )
}

@Composable
fun ShimmerList(
    modifier: Modifier=Modifier
) {
    LazyColumn(
        modifier= modifier
            .fillMaxSize()
            .padding(top = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ){
        items(count = 10){
            ShimmerTaskItem()
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TaskListScreenPreview(){
    YouHrTheme {
        Surface {
            val uiState by remember { mutableStateOf(TaskListUiState()) }
            TaskListScreen(
                uiState =uiState,
                //listState = rememberLazyListState(),
                //categoryDropDownExpanded = false,
                //showDatePicker = false,
                onDatePickerOkClicked = { _, _->},
                onCategoryFilterClicked = {},
                categoryDropDownOnDismissCallBack = {},
                onTaskItemClicked = {},
                //categorySpinnerText = "Pending",
                dateRange = DateRange(),
                onDateInputFieldClicked = { _->},
                onTaskProgressDropDownItemClicked = {},
                //currentEditableDateInputField = 0,
                onDatePickerCancelClicked = {},
                fetchMore = {},
                //isFetchingMore = true,
                //getTaskOnFirstLoad = {},
                onRetry = {},
                onDateFilterBottomSheetConfirmClicked = {},
                getTaskOnFirstLoad = {}
            )
        }

    }
}

@Composable
@Preview
fun PendingTaskItemPreview(){
    YouHrTheme {
        Surface {
            TaskItem(
                task = co.youverify.youhr.domain.model.Task(
                    id="1",
                    "Interview with candidates for product design role",
                    status = "To-do",
                    assignedBy = "Edet",
                     description ="Onboarding new employees help them familiarize themselves with the company’s structure as well as help them settle in faster",
                    type = "Employee Onboarding",
                    hasNextPage = false,
                    page = 1,
                    attachedDocs = listOf(AttachedDoc("company_policy.pdf",url="")),
                    dueDate = "2023-06-23",
                    timeStampCreated = "2023-04-28T06:40:50.808Z"
                ),
                index = 0
            ) {}
        }

    }
}






@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun DateFilterPreview(){
    YouHrTheme {
        Surface {
            DateFilter(
                //dateFilterDisabled = false,
                dateRange = DateRange(),
                bottomSheetState = rememberModalBottomSheetState()
            )
        }

    }
}


@Composable
@Preview
fun CategoryFilterPreview(){
    YouHrTheme {
        Surface {
            CategoryFilter(
                onClick = { },
                categoryDropDownOnDismissCallBack = {},
                dropDownExpanded =false ,
                spinnerText ="Pending" ,
                //categoryFilterDisabled =false,
                onDropDownMenuItemClicked = {_->}
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun DateInputFieldPreview(){
    YouHrTheme {
        Surface {
            DateInputField(
                fieldValue = "16 - 04 -2023",
                modifier = Modifier.padding(horizontal = 20.dp),
                index = 1,
                onClick = {_->},
                title ="Start Date",
                sheetState = SheetState(skipPartiallyExpanded = true)
            )
        }

    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun EmptyStateContentPreview(){
    YouHrTheme {
        Surface {
            EmptyStateContent(contentMessage = "You haven’t been assigned any tasks yet")
        }

    }
}

/*@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun DatePickerPreview(){
    YouHrTheme {

        Surface {
            val bottomSheetScaffoldState= rememberBottomSheetScaffoldState(
                 bottomSheetState = SheetState(
                    initialValue = SheetValue.Hidden,
                    skipPartiallyExpanded = true,
                    skipHiddenState = false
                )
            )
            BottomSheetDatePicker(
                state = rememberDatePickerState(),
                onOkButtonClicked = {_,_->},
                onCancelButtonClicked = {},
                currentIndex = 1,
                bottomSheetState = bottomSheetScaffoldState.bottomSheetState,
                coroutineScope = rememberCoroutineScope()
            )
        }



    }
}*/


@Composable
@Preview
fun TaskItemPreview(){
    YouHrTheme {

        Surface {
            TaskItem(
                task = Task(
                    id = "1",
                    "Interview with candidates for product design role",
                    status = "To-do",
                    assignedBy = "Edet",
                    description = "Onboarding new employees help them familiarize themselves with the company’s structure as well as help them settle in faster",
                    type = "Employee Onboarding",
                    hasNextPage = false,
                    page = 1,
                    attachedDocs = listOf(AttachedDoc("company_policy.pdf", url = "")),
                    dueDate = "2023-06-23",
                    timeStampCreated = "2023-04-28T06:40:50.808Z"
                ), index = 1, onTaskItemClicked = {}
            )
        }
    }
}

@Composable
@Preview
fun ShimmerTaskItem(){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(61.dp)
            .padding(horizontal = 20.dp)
            .shimmerEffect()


    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .width(4.dp)
                .background(
                    color = Color.White,
                    shape = RoundedCornerShape(topStart = 4.dp, bottomStart = 4.dp)
                )

        )

        ConstraintLayout(

            modifier = Modifier
                .fillMaxSize()
                //.border(
                //  width = 1.dp,
                // color = Color.White,
                // shape = RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp)
                // )
                //.background(color = Color(0XFFF7F7F9))
                //.background(Color.White)
                .shimmerEffect()
                .clip(RoundedCornerShape(topEnd = 4.dp, bottomEnd = 4.dp))
                .padding(start = 8.dp),

            content = {

                val (clockIcon,dueDateText,descriptionText,progressIndicatorBox) = createRefs()



                Box(
                    modifier = Modifier
                        .constrainAs(clockIcon) {
                            start.linkTo(parent.start)
                            top.linkTo(parent.top, 9.33.dp)
                        }
                        .size(13.dp)
                        .background(color = taskItemBorderColor, shape = CircleShape)
                )

                Box(
                    modifier = Modifier
                        .constrainAs(dueDateText) {
                            start.linkTo(clockIcon.end, 9.33.dp)
                            top.linkTo(clockIcon.top)
                        }
                        .size(112.dp, 12.dp)
                        .background(color = taskItemBorderColor),

                )


                Box(
                    modifier = Modifier
                        .constrainAs(descriptionText) {
                            start.linkTo(clockIcon.start)
                            top.linkTo(dueDateText.bottom, 8.dp)
                            bottom.linkTo(parent.bottom, 8.dp)
                            end.linkTo(progressIndicatorBox.start, 60.dp)
                            //width= Dimension.fillToConstraints
                        }
                        .size(200.dp, 14.dp)
                        .background(color = taskItemBorderColor)
                )

                Box(

                    modifier = Modifier

                        .size(45.dp, 13.dp)
                        .background(color = taskItemBorderColor, shape = RoundedCornerShape(10.dp))
                        .constrainAs(progressIndicatorBox) {
                            end.linkTo(parent.end, 15.dp)
                            top.linkTo(parent.top, 14.dp)
                        },

                )
            }

        )
    }
}
@Composable
@Preview
fun ShimmerTaskItemPreview(){
    YouHrTheme {

        Surface {
           ShimmerTaskItem( )
        }
    }
}

@Composable
@Preview
fun ShimmerListPreview(){
    YouHrTheme {

        Surface {
            ShimmerList()
        }
    }
}



data class Task(
    val dueDate: Long, val title:String, val assignee:String, val assigneeEmail: String, val isCompleted:Boolean, val assigner:String, val creator:String, val project:String, val description:String)
/*val pendingTasks= listOf(
    Task(Instant.now().toEpochMilli(), "Interview with candidates for product design role", isCompleted = false, assignee = "Edet",
        assigner = "Seth Samuel", creator = "Timothy John", assigneeEmail = "Edna@youverify.co", project = "Employee Onboarding", description ="Onboarding new employees help them familiarize themselves with the company’s structure as well as help them settle in faster" ),
    )*/

val pendingTasks= listOf(co.youverify.youhr.domain.model.Task(
    id="1",
    "Interview with candidates for product design role",
    status = "To-do",
    assignedBy = "Edet",
    description ="Onboarding new employees help them familiarize themselves with the company’s structure as well as help them settle in faster",
    type = "Employee Onboarding",
    hasNextPage = false,
    page = 1,
    attachedDocs = listOf(AttachedDoc("company_policy.pdf",url="")),
    dueDate = "2023-09-20 08:30",
    timeStampCreated = "2023-04-28T06:40:50.808Z"
))

val completedTasks= listOf(Task(
    id="1",
    "Interview with candidates for product design role",
    status = "noo",
    assignedBy = "Olayinka",
    description ="Onboarding new employees help them familiarize themselves with the company’s structure as well as help them settle in faster",
    type = "Employee Onboarding",
    hasNextPage = false,
    page = 1,
    attachedDocs = listOf(AttachedDoc("grow_plan.jpg",url="")),
    dueDate = "2023-06-23",
    timeStampCreated = "2023-04-28T06:40:50.808Z"
))

val taskItemSpacerColors= listOf(
    Color(0XFFFF5454),
    Color(0XFF547AFF),
    Color(0XFFDAA419),
    Color(0XFFD619DA),
    Color(0XFF93FD52),
)

enum class TaskProgressColor(val value:Color){
    ALL(Color.Transparent),
    IN_PROGRESS(Color(0XFFDAA419)),
    COMPLETED(Color(0XFF4C7A40)),
    OVERDUE(Color(0XFFFF5454)),
    TO_DO(Color(0XFFFF993C)),
    REVIEW(Color.Black)
}


data class DateRange( val startDateMillis: Long?=null,  val endDateMillis: Long?=null)
@Composable
fun LazyListState.OnBottomReached(lastTask: Task,loadMore:()-> Unit){



    val shouldLoadmore= remember{
        derivedStateOf{
            val lastVisibleItem=layoutInfo.visibleItemsInfo.lastOrNull()?:false
            lastVisibleItem==layoutInfo.totalItemsCount-1
        }
    }

    LaunchedEffect(shouldLoadmore){
        snapshotFlow{shouldLoadmore.value}
            .collect{
                if (it && lastTask.hasNextPage) loadMore()
            }
    }
}
