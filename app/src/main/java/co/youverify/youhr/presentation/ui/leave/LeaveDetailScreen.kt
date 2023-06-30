package co.youverify.youhr.presentation.ui.leave

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import co.youverify.youhr.R
import co.youverify.youhr.core.util.getFormattedLeaveDate
import co.youverify.youhr.core.util.toEpochMillis
import co.youverify.youhr.core.util.toTimeAgo
import co.youverify.youhr.domain.model.LeaveRequest
import co.youverify.youhr.presentation.ui.components.YouHrTitleBar
import co.youverify.youhr.presentation.ui.theme.YouHrTheme
import co.youverify.youhr.presentation.ui.theme.bodyTextDeepColor
import co.youverify.youhr.presentation.ui.theme.bodyTextLightColor
import co.youverify.youhr.presentation.ui.theme.deactivatedColorDeep
import co.youverify.youhr.presentation.ui.theme.deactivatedColorLight
import co.youverify.youhr.presentation.ui.theme.errorMessageColor
import co.youverify.youhr.presentation.ui.theme.yvColor
@Composable
fun LeaveDetailScreen(
    modifier: Modifier = Modifier,
    leaveDetailExpanded: Boolean,
    onLeaveDetailContentChangeRequested: () -> Unit,
    //onContactInfoContentChangeRequested: () -> Unit,
    //contactInfoExpanded: Boolean,
    leaveRequest: LeaveRequest,
    onBackArrowClicked:()->Unit,

    ){
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        YouHrTitleBar(title = "Leave Details", modifier = Modifier.padding(top=51.dp, bottom = 37.dp)){onBackArrowClicked()}
        LeaveDetailsExpandable(
            leaveType =leaveRequest.leaveType ,
            startDate = getFormattedLeaveDate(leaveRequest.startDate) ,
            endDate =getFormattedLeaveDate(leaveRequest.endDate) ,
            lm =leaveRequest.linemanagerName ,
            lmEmail = leaveRequest.linemanagerEmail,
            leaveDays = leaveRequest.leaveDaysRequested,
            reliever =leaveRequest.relieverName ,
            reason = leaveRequest.reasonForLeave,
            expanded = leaveDetailExpanded,
            onContentChangeRequested = onLeaveDetailContentChangeRequested,
            modifier = Modifier
                .padding(horizontal = 20.dp)
                .fillMaxWidth()
        )


        Divider(modifier= Modifier
            .fillMaxWidth()
            .padding(bottom = 25.dp, top = 25.dp), thickness = 1.dp, color = deactivatedColorLight)
        LeaveStatus(
            modifier = Modifier.padding(bottom = 20.dp, start = 20.dp,end=20.dp), lmStatus =leaveRequest.linemanagerStatus, hrStatus =leaveRequest.hrStatus,
            lmName =leaveRequest.linemanagerName, hrComment =leaveRequest.hrComment, lmComment =leaveRequest.linemanagerComment,
            hrModificationDaysAgo = if (leaveRequest.hrApprovalDate=="Not yet") leaveRequest.hrApprovalDate  else leaveRequest.hrApprovalDate.toTimeAgo(),
            lmModificationDaysAgo = if (leaveRequest.linemanagerApprovalDate=="Not yet") leaveRequest.linemanagerApprovalDate else leaveRequest.linemanagerApprovalDate.toTimeAgo(),
            relieverName = leaveRequest.relieverName, relieverComment = leaveRequest.relieverComment,
            relieverModificationDaysAgo = if (leaveRequest.relieverApprovalDate=="Not yet") leaveRequest.relieverApprovalDate else leaveRequest.relieverApprovalDate.toTimeAgo(),
            relieverStatus = leaveRequest.relieverStatus

            )
    }
}
@Composable
fun LeaveStatus(
    modifier: Modifier = Modifier,
    lmStatus: String,
    hrStatus: String,
    relieverStatus:String,
    lmName: String,
    relieverName:String,
    hrComment: String?,
    lmComment: String?,
    relieverComment:String?,
    hrModificationDaysAgo: String,
    lmModificationDaysAgo: String,
    relieverModificationDaysAgo:String
){

    //val step2Color=if (lmStatus==LeaveStatus.APPROVED.id) yvColor else deactivatedColorDeep

    Column(modifier=modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(28.dp)) {
        
        Text(text = "Leave Status", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = bodyTextDeepColor)

        Row(
            modifier= Modifier
                .padding(start = 1.dp)
                .fillMaxWidth(),
        ) {

            var indicatorHeight by remember{ mutableStateOf(50.dp) }
            val density=LocalDensity.current


            Indicators(lmStatus = lmStatus, hrStatus = hrStatus, height = indicatorHeight, relieverStatus = relieverStatus)
            
           Column(
               verticalArrangement = Arrangement.spacedBy(54.dp),
               modifier=Modifier.fillMaxWidth().onGloballyPositioned {layoutCoordinates ->
                               indicatorHeight=with(density)  {
                                   layoutCoordinates.size.height.toDp()
                               }
               },
               content = {

                   LeaveStatusCard(
                       modifier = Modifier, status = relieverStatus, title =relieverName,
                       designation ="Reliever",
                       comment = relieverComment,
                       greyOut=false,
                       modificationDaysAgo = relieverModificationDaysAgo
                   )
                   LeaveStatusCard(
                       modifier = Modifier, status = lmStatus, title =lmName,
                       designation ="In-line manager ",
                       comment = lmComment,
                       greyOut=false,
                       modificationDaysAgo = lmModificationDaysAgo
                   )
                   LeaveStatusCard(
                       modifier = Modifier, status = hrStatus, title ="People Operation Specialist",
                       designation ="Human resource department",
                       comment = hrComment,
                       greyOut=lmStatus==LeaveStatus.REJECTED.id,
                       modificationDaysAgo = hrModificationDaysAgo
                   )


               }
           )
        }
    }
    
}

@Composable
fun Indicators(modifier: Modifier = Modifier, lmStatus: String, hrStatus: String,relieverStatus: String,height: Dp){


    val step2Color=if (relieverStatus==LeaveStatus.APPROVED.id) yvColor else deactivatedColorDeep
    val step3Color=if (lmStatus==LeaveStatus.APPROVED.id) yvColor else deactivatedColorDeep

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(11.dp),
        modifier= modifier
            .width(24.dp)
            .height(height),
        content = {
            StepIndicator(
                stepColor = yvColor , stepNumber =1,approved = relieverStatus==LeaveStatus.APPROVED.id,
                modifier=Modifier
            )
            Spacer(
                modifier= Modifier
                    .width(2.dp)
                    .weight(1f)
                    .background(
                        color = if (relieverStatus == LeaveStatus.APPROVED.id) yvColor else deactivatedColorDeep,
                    ),

                )

            StepIndicator(
                stepColor = step2Color , stepNumber =2,approved = lmStatus==LeaveStatus.APPROVED.id,
                modifier=Modifier
            )

            Spacer(
                modifier= Modifier
                    .width(2.dp)
                    .weight(1f)
                    .background(
                        color = if (lmStatus == LeaveStatus.APPROVED.id) yvColor else deactivatedColorDeep,
                    ),

                )

            StepIndicator(
                stepColor = step3Color , stepNumber =3,approved = hrStatus==LeaveStatus.APPROVED.id,
                modifier=Modifier
            )
        }
    )
}

@Composable
fun StepIndicator(modifier: Modifier,stepColor: Color, stepNumber: Int,approved:Boolean) {
    Box(modifier = modifier
        .size(24.dp)
        .border(width = 0.5.dp, color = stepColor, shape = CircleShape),
        contentAlignment = Alignment.Center
    ){
        Box(
            modifier = Modifier
                .size(18.dp)
                .background(color = stepColor, shape = CircleShape),
            contentAlignment = Alignment.Center,
        ){
            if (approved){
                Icon(
                    painter = painterResource(id = R.drawable.ic_check),
                    contentDescription =null, tint = Color.White, modifier = Modifier.size(9.dp,6.dp)
                )
            }else{Text(text = "$stepNumber", fontSize = 12.sp, fontWeight = FontWeight.Medium, color = Color.White) }
        }
    }
}
@Composable
fun LeaveDetailsExpandable(
    modifier: Modifier = Modifier,
    leaveType: String,
    startDate: String,
    endDate: String,
    lm: String,
    lmEmail: String,
    leaveDays: String,
    reliever: String,
    reason: String,
    expanded:Boolean,
    onContentChangeRequested:()->Unit
){
    Column(
        modifier
            .fillMaxWidth()
            .animateContentSize()) {

        Row(
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(12.dp)),
            verticalAlignment = Alignment.CenterVertically,
            content = {
                Text(text = "General Details", fontSize = 14.sp, fontWeight = FontWeight.Medium,
                    color = bodyTextDeepColor,modifier=Modifier.padding(start = 20.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = if (expanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down),
                    contentDescription =null, tint = bodyTextDeepColor ,
                    modifier = Modifier
                        .padding(end = 18.5.dp)
                        .clickable { onContentChangeRequested() }
                )
            }
        )

        if (expanded) {
            Column(
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(top = 33.dp),
                verticalArrangement = Arrangement.spacedBy(31.dp),
                content = {
                    SummaryItem(title = "Leave Type", value = leaveType)
                    SummaryItem(title = "Start Date", value = startDate)
                    SummaryItem(title = "End Date", value = endDate)
                    SummaryItem(title = "Line Manager", value = lm)
                    SummaryItem(title = "Line Manager's Email", value = lmEmail)
                    SummaryItem(title = "Days Requested", value = "$leaveDays Days")
                    SummaryItem(title = "Reliever", value = reliever)
                    SummaryItem(title = "Reason", value = reason)}
            )
        }

    }
}


@Composable
fun ContactInfoExpandable(
    modifier: Modifier = Modifier,
    contactName: String,
    email: String,
    phoneNumber: String,
    onContentChangeRequested: () -> Unit,
    expanded: Boolean,
) {
    Column(
        modifier
            .fillMaxWidth()
            .animateContentSize()) {

        Row(
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth()
                .background(color = Color.White, shape = RoundedCornerShape(12.dp))
                .clickable { onContentChangeRequested() },
            verticalAlignment = Alignment.CenterVertically,
            content = {
                Text(
                    text = "Contact Person Information",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = bodyTextDeepColor,
                    modifier = Modifier.padding(start = 20.dp)
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painter = painterResource(id = if (expanded) R.drawable.ic_arrow_up else R.drawable.ic_arrow_down),
                    contentDescription = null,
                    tint = bodyTextDeepColor,
                    modifier = Modifier.padding(end = 18.5.dp)
                )
            }
        )

        if (expanded) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 33.dp),
                verticalArrangement = Arrangement.spacedBy(31.dp),
                content = {
                    SummaryItem(title = "Contact Name", value = contactName)
                    SummaryItem(title = "Email Address", value = email)
                    SummaryItem(title = "Phone Number", value = phoneNumber)
                }
            )
        }
    }
}



    @Composable
    fun SummaryItem(modifier: Modifier = Modifier, title: String, value: String) {
        ConstraintLayout(
            modifier
                .fillMaxWidth()
                .padding(start = 20.dp)) {
            val (titleText, valueText) = createRefs()
            val guideline = createGuidelineFromAbsoluteRight(0.55f)
            Text(
                text = "$title:", fontSize = 12.sp, color = bodyTextLightColor,
                modifier = Modifier.constrainAs(titleText) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
            )

            Text(
                text = value,
                fontSize = 14.sp,
                color = bodyTextDeepColor,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.constrainAs(valueText) {
                    start.linkTo(guideline)
                    top.linkTo(parent.top)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
            )
        }
    }


@Composable
fun LeaveStatusCard(
    modifier: Modifier = Modifier,
    status: String,
    title: String,
    designation: String,
    comment: String?,
    greyOut: Boolean,
    modificationDaysAgo: String
){


    val statusColor=when(status){
        LeaveStatus.PENDING.id-> Color(0XFFDAA419)
        LeaveStatus.APPROVED.id-> leaveColors[1]
        else -> errorMessageColor
    }

        ConstraintLayout(
            modifier= Modifier
                .fillMaxWidth()
                .background(color = Color(0XFFF2F5F5), shape = RoundedCornerShape(12.dp))
        ) {
            val (group1,group2,group3)=createRefs()

            Column(
                modifier = Modifier
                    .constrainAs(group1){
                        start.linkTo(parent.start,11.dp)
                        top.linkTo(parent.top,14.dp)
                       //end.linkTo(group3.start,16.dp)

                    },
                content = {
                    Text(
                        text =title,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (greyOut) deactivatedColorDeep else bodyTextLightColor,
                    modifier = Modifier.padding(end = 8.dp)

                        )

                    Text(
                        text =designation,
                        fontSize = 12.sp,
                        color =if (greyOut) deactivatedColorDeep else bodyTextLightColor,
                        modifier=Modifier.padding(top = 4.dp, bottom = 13.dp)
                    )
                }
            )

            if (!comment.isNullOrEmpty()){
                Column(
                    modifier=Modifier.constrainAs(group2){
                        //top.linkTo(group1.bottom,20.dp)
                       // bottom.linkTo(parent.bottom,11.dp)
                        start.linkTo(group1.start)
                        end.linkTo(parent.end,12.dp)
                        top.linkTo(group1.bottom,7.dp)
                        width= Dimension.fillToConstraints
                    },
                    content = {
                        Text(
                            text ="Comment-",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = if (greyOut) deactivatedColorDeep else bodyTextDeepColor,
                            modifier=Modifier.padding(bottom = 8.dp)
                        )

                        Text(
                            text =comment.toString(),
                            fontSize = 12.sp,
                            color = if (greyOut) deactivatedColorDeep else bodyTextLightColor,
                            modifier=Modifier.padding(bottom = 11.dp)

                        )
                    }
                )
            }


            Column(
                modifier = Modifier
                    .constrainAs(group3){
                        top.linkTo(parent.top,18.5.dp)
                        //bottom.linkTo(parent.bottom,13.5.dp)
                        end.linkTo(parent.end,12.dp)
                    },
                verticalArrangement = Arrangement.spacedBy(9.dp),
                content = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(2.5.dp),
                        modifier=Modifier.background(color = statusColor.copy(alpha = 0.1f), shape = RoundedCornerShape(4.dp)),
                        content = {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_clock), contentDescription =null,
                                tint = if (greyOut) deactivatedColorDeep else statusColor,
                                modifier = Modifier
                                    //.size(11.dp)
                                    .padding(start = 12.5.dp, top = 4.dp, bottom = 4.dp)
                            )
                            Text(
                                text = status, fontSize = 10.sp,
                                fontWeight = FontWeight.Medium,color=if (greyOut) deactivatedColorDeep else statusColor,
                                modifier = Modifier.padding(end=12.dp, top = 4.dp, bottom = 4.dp)
                            )
                        }
                    )

                    Text(
                        text = modificationDaysAgo, fontSize = 10.sp, color = bodyTextLightColor,
                        modifier= Modifier
                            .padding(bottom = 13.5.dp)
                            .align(Alignment.CenterHorizontally),
                    )
                }
            )
        }

        //Spacer(modifier = modifier.weight(1f))

}

@Composable
fun VerticalLineBetweenComposables(modifier: Modifier=Modifier) {
    Box(
        modifier = modifier
            .padding(vertical = 32.dp)
            .fillMaxHeight()
            .width(1.dp)
            .background(Color.Red)
    )
}


@Preview
@Composable
fun LeaveStatusCardPreview(){
    YouHrTheme {
        Surface {
            LeaveStatusCard(
                status ="Pending", title ="Famous Echichioya",
                designation ="In-line manager",
                comment ="Dey play oo. Shey na me go do your work abi?", greyOut =false,
                modificationDaysAgo = "2023-06-29 00:04:10".toTimeAgo()
            )
        }
    }
}


@Preview
@Composable
fun StepIndicator2Preview(){
    YouHrTheme {
        Surface {
            StepIndicator(
                modifier = Modifier,
                stepColor = yvColor,
                stepNumber = 1,
                approved = true
            )
        }
    }
}

@Preview
@Composable
fun LeaveStatusPreview(){
    YouHrTheme {
        Surface {
            LeaveStatus(
                modifier = Modifier
                    .padding(20.dp)
                    .fillMaxWidth(),
                lmStatus = "Pending", hrStatus ="Pending",
                lmName ="Famous Echichioya", hrComment ="", lmComment ="",
                hrModificationDaysAgo = "1 day ago",
                lmModificationDaysAgo = "Not yet",
                relieverStatus = "Pending",
                relieverName = "Tobi Onasanya",
                relieverComment = "",
                relieverModificationDaysAgo = "Not yet"
            )
        }
    }
}

@Preview
@Composable
fun LeaveDetailScreenPreview(){
    YouHrTheme {
        Surface {
            LeaveDetailScreen(
                leaveDetailExpanded = false,
                onLeaveDetailContentChangeRequested = {},
                //onContactInfoContentChangeRequested = {},
                //contactInfoExpanded = false,
                leaveRequest = LeaveRequest(
                    id = "",
                    linemanagerStatus = "Approved",
                    hrStatus = "Pending",
                    linemanagerApprovalDate = "",
                    hrApprovalDate = "",
                    status = "",
                    email = "",
                    leaveType = "Annual",
                    startDate = "Wed Nov 20 2023",
                    endDate = "Mon Jan 30 2024",
                    reasonForLeave = "I really need time of.I'm bored of work",
                    //contactName = "Adesoji Olowa",
                    //contactEmail = "adesoji@youverify.ci",
                    //contactNumber = "08037582010",
                    relieverName = "Tobi Onasanya",
                    linemanagerEmail = "timothy@youverify.co",
                    linemanagerName = "Timothy Akinyelu",
                    leaveDaysRequested = "8",
                    linemanagerComment = "Enjoy your leave. See me before you proceed",
                    hrComment = "Dey play oo. shey na me go do your work",
                    relieverApprovalDate = "Not yet",
                    relieverComment = "",
                    relieverEmail = "ade@gmail.com",
                    relieverStatus = "Pending",
                    alternativePhoneNumber = "08088987385"
                ),
                onBackArrowClicked = {}
            )
        }
    }
}