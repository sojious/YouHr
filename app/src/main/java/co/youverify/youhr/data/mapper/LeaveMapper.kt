package co.youverify.youhr.data.mapper

import co.youverify.youhr.data.local.DbLeaveRequest
import co.youverify.youhr.data.local.DbLeaveSummary
import co.youverify.youhr.data.model.LeaveRequestDto
import co.youverify.youhr.data.model.LeaveSummaryDto
import co.youverify.youhr.domain.model.LeaveRequest
import co.youverify.youhr.domain.model.LeaveSummary

class DtoToDbLeaveMapper :Mapper<LeaveRequestDto,DbLeaveRequest>{
    override fun map(input: LeaveRequestDto): DbLeaveRequest {
        return DbLeaveRequest(
            linemanagerStatus = input.linemanagerStatus,
            hrStatus = input.hrStatus,
            status = input.status,
            email = input.email,
            leaveType = input.leaveType,
            startDate = input.startDate,
            endDate = input.endDate,
            reasonForLeave = input.reasonForLeave?:"",
            relieverName = input.relieverName?:"",
            linemanagerEmail = input.linemanagerEmail?:"",
            linemanagerComment = input.linemanagerComment?:"",
            hrComment = input.hrComment?:"",
            leaveDaysRequested = input.leaveDaysRequested,
            linemanagerName = input.linemanagerName?:"",
            id = input.id,
            linemanagerApprovalDate = input.linemanagerApprovalDate,
            hrApprovalDate = input.hrApprovalDate,
            alternativePhoneNumber = input.alternativeNumber?:"",
            relieverApprovalDate = input.relieverApprovalDate,
            relieverComment = input.relieverComment?:"",
            relieverEmail = input.relieverEmail?:"",
            relieverStatus = input.relieverStatus
        )
    }

}
class DbToDomainLeaveMapper:Mapper<DbLeaveRequest,LeaveRequest>{
    override fun map(input: DbLeaveRequest): LeaveRequest {
        return LeaveRequest(
            linemanagerStatus = input.linemanagerStatus,
            hrStatus = input.hrStatus,
            status = input.status,
            email = input.email,
            leaveType = input.leaveType,
            startDate = input.startDate,
            endDate = input.endDate,
            reasonForLeave = input.reasonForLeave,
            //contactName = input.contactName,
            //contactEmail = input.contactEmail,
            //contactNumber = input.contactNumber,
            relieverName = input.relieverName,
            linemanagerEmail = input.linemanagerEmail,
            linemanagerComment = input.linemanagerComment,
            hrComment = input.hrComment?:"",
            leaveDaysRequested = input.leaveDaysRequested,
            linemanagerName = input.linemanagerName,
            id = input.id,
            linemanagerApprovalDate = input.linemanagerApprovalDate,
            hrApprovalDate = input.hrApprovalDate,
            relieverApprovalDate = input.relieverApprovalDate,
            relieverComment = input.relieverComment,
            relieverEmail = input.relieverEmail,
            relieverStatus = input.relieverStatus,
            alternativePhoneNumber = input.alternativePhoneNumber
        )
    }
}

class DtoToDbLeaveSummaryMapper :Mapper<LeaveSummaryDto,DbLeaveSummary>{
    override fun map(input: LeaveSummaryDto): DbLeaveSummary {
        return DbLeaveSummary(
            id = input.id,
            annualLeaveTaken = input.annaulLeaveTaken,
            bereavementLeaveTaken = input.bereavementLeaveTaken,
            casualLeaveTaken = input.casualLeaveTaken,
            sickLeaveTaken = input.sickLeaveTaken,
            studyLeaveTaken = input.studyLeaveTaken,
            parentalLeaveTaken = input.parentalLeaveTaken,
            compassionateLeaveTaken = input.compassionateLeaveTaken

        )
    }

}


class DbToDomainLeaveSummaryMapper :Mapper<DbLeaveSummary,LeaveSummary>{
    override fun map(input: DbLeaveSummary): LeaveSummary {
        return LeaveSummary(
            id = input.id,
            annualLeaveTaken = input.annualLeaveTaken,
            bereavementLeaveTaken = input.bereavementLeaveTaken,
            casualLeaveTaken = input.casualLeaveTaken,
            sickLeaveTaken = input.sickLeaveTaken,
            studyLeaveTaken = input.studyLeaveTaken,
            parentalLeaveTaken = input.parentalLeaveTaken,
            compassionateLeaveTaken = input.compassionateLeaveTaken

        )
    }

}

class DtoToDbLeaveListMapper(val mapper: DtoToDbLeaveMapper):NullableInputListMapper<LeaveRequestDto,DbLeaveRequest>{
    override fun map(input: List<LeaveRequestDto>?): List<DbLeaveRequest> {
       return input?.map{
           mapper.map(it)
       }.orEmpty()
    }

}

class DbToDomainLeaveListMapper(val mapper: DbToDomainLeaveMapper):ListMapper<DbLeaveRequest,LeaveRequest>{
    override fun map(input: List<DbLeaveRequest>): List<LeaveRequest> {
      return  input.map{
            mapper.map(it)
        }
    }


}