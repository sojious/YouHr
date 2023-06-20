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
    onContactInfoContentChangeRequested: () -> Unit,
    contactInfoExpanded: Boolean,
    leaveRequest: LeaveRequest
){
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        YouHrTitleBar(title = "Leave Details", modifier = Modifier.padding(top=51.dp, bottom = 37.dp))
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

        ContactInfoExpandable(
            contactName =leaveRequest.contactName ,
            email =leaveRequest.contactEmail,
            phoneNumber =leaveRequest.contactNumber,
            onContentChangeRequested =onContactInfoContentChangeRequested,
            expanded =contactInfoExpanded ,
            modifier = Modifier
                .padding(horizontal = 20.dp, vertical = 36.dp)
                .fillMaxWidth()
        )

        Divider(modifier= Modifier
            .fillMaxWidth()
            .padding(bottom = 25.dp), thickness = 1.dp, color = deactivatedColorLight)
        LeaveStatus(
            modifier = Modifier.padding(20.dp), lmStatus =leaveRequest.linemanagerStatus, hrStatus =leaveRequest.hrStatus,
            lmName =leaveRequest.linemanagerName, hrComment =leaveRequest.hrComment, lmComment =leaveRequest.linemanagerComment,
            hrModificationDaysAgo = if (leaveRequest.hrApprovalDate=="Not yet") leaveRequest.hrApprovalDate  else leaveRequest.hrApprovalDate.toTimeAgo(),
            lmModificationDaysAgo = if (leaveRequest.linemanagerApprovalDate=="Not yet") leaveRequest.linemanagerApprovalDate else leaveRequest.linemanagerApprovalDate.toTimeAgo()

            )
    }
}
@Composable
fun LeaveStatus(
    modifier: Modifier = Modifier,
    lmStatus: String,
    hrStatus: String,
    lmName: String,
    hrComment: String?,
    lmComment: String?,
    hrModificationDaysAgo: String,
    lmModificationDaysAgo: String
){

    val step2Color=if (lmStatus==LeaveStatus.APPROVED.id) yvColor else deactivatedColorDeep

    Column(modifier=modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(28.dp)) {
        
        Text(text = "Leave Status", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = bodyTextDeepColor)

        Row(
            modifier= Modifier
                .padding(start = 1.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(11.dp),
                modifier=Modifier.width(24.dp),
                content = {
                    StepIndicator(
                        stepColor = yvColor , stepNumber =1,approved = lmStatus==LeaveStatus.APPROVED.id,
                        modifier=Modifier
                    )
                    Spacer(
                        modifier= Modifier
                            .size(2.dp, 70.dp)
                            .background(
                                color = if (lmStatus == LeaveStatus.APPROVED.id) yvColor else deactivatedColorDeep,
                            ),

                    )

                    StepIndicator(
                        stepColor = step2Color , stepNumber =2,approved = hrStatus==LeaveStatus.APPROVED.id,
                        modifier=Modifier
                    )
                }
            )
            
           Column(
               verticalArrangement = Arrangement.spacedBy(54.dp),
               modifier=Modifier.fillMaxWidth(),
               content = {
                   LeaveStatusCard(
                       modifier = Modifier, status = lmStatus, title =lmName,
                       designation ="In-line manager ",
                       comment = lmComment,
                       greyOut=lmStatus==LeaveStatus.REJECTED.id,
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
            val guideline = createGuidelineFromAbsoluteRight(0.35f)
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
    Row(
        modifier= Modifier
            .fillMaxWidth()
            .background(color = Color(0XFFF2F5F5), shape = RoundedCornerShape(12.dp))
    ) {
        Column( modifier = Modifier.padding(start = 11.dp,top=14.dp)) {

            Column(
                modifier = Modifier.padding(bottom = 11.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                content = {
                    Text(
                        text =title,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (greyOut) deactivatedColorDeep else bodyTextLightColor
                    )

                    Text(
                        text =designation,
                        fontSize = 12.sp,
                        color =if (greyOut) deactivatedColorDeep else bodyTextLightColor,
                    )
                }
            )

            if (!comment.isNullOrEmpty())
                Column(
                modifier = Modifier.padding(top = 9.dp, bottom = 11.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                content = {
                    Text(
                        text ="Comment-",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = bodyTextDeepColor
                    )

                    Text(
                        text =comment.toString(),
                        fontSize = 12.sp,
                        color = if (greyOut) deactivatedColorDeep else bodyTextLightColor,
                        modifier= Modifier
                            .fillMaxWidth()
                            .padding(end = 12.dp)
                    )
                }
            )
        }


        Spacer(modifier = modifier.weight(1f))

        Column(
            modifier = Modifier.padding(top = 18.5.dp, bottom = 13.5.dp,end=12.dp),
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
                                .size(11.dp)
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
                    modifier=Modifier.align(Alignment.CenterHorizontally)
                )
            }
        )


    }
}

@Preview
@Composable
fun LeaveStatusCardPreview(){
    YouHrTheme {
        Surface {
            LeaveStatusCard(
                status ="Pending", title ="Famous Echichioya",
                designation ="In-line manager",
                comment ="Abeg shey na me go do your work", greyOut =false,
                modificationDaysAgo = "2 days ago"
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
                lmModificationDaysAgo = "Not yet"
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
                onContactInfoContentChangeRequested = {},
                contactInfoExpanded = false,
                leaveRequest = LeaveRequest(
                    id = "", linemanagerStatus = "Approved", hrStatus = "Pending", linemanagerApprovalDate = "",
                    hrApprovalDate = "", leaveType = "Annual", startDate = "Wed Nov 20 2023", endDate = "Mon Jan 2024",
                    relieverName = "Tobi Onasanya", hrComment = "Dey play oo. shey na me go do your work",
                    linemanagerComment = "Enjoy your leave. See me before you proceed", reasonForLeave = "I really need time of.I'm bored of work",
                    contactName = "Adesoji Olowa", contactNumber = "08037582010", contactEmail = "adesoji@youverify.ci",
                    linemanagerName = "Timothy Akinyelu", linemanagerEmail = "timothy@youverify.co", status = "",
                    email = "", leaveDaysRequested = "8"
                )
            )
        }
    }
}