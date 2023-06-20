package co.youverify.youhr.data.mapper

import co.youverify.youhr.data.local.DbUser
import co.youverify.youhr.data.model.UserProfileResponse
import co.youverify.youhr.domain.model.User

class DtoToDbProfileMapper:Mapper<UserProfileResponse, DbUser>{
    override fun map(input: UserProfileResponse): DbUser {

        return DbUser(
            id = input.data.id,
            role = input.data.role?:"user",
            jobRole = input.data.jobRole?:"NA",
            status = input.data.status?:"NA",
            email = input.data.email,
            firstName = input.data.firstName,
            lastName = input.data.lastName,
            password = input.data.password,
            middleName=input.data.middleName?:"",
            passcode = input.data.passcode?:"",
            address = input.data.address?:"",
            dob = input.data.dob?:"",
            gender = input.data.gender?:"",
            nextOfKin = input.data.nextofKin?:"",
            nextOfKinContact = input.data.nextofKinContact?:"",
            nextOfKinNumber = input.data.nextofKinNumber?:"",
            displayPicture = input.data.displayPicture?:"",
            phoneNumber = input.data.phoneNumber?:""
        )
    }

}

class DbToDomainProfileMapper:Mapper<DbUser, User>{
    override fun map(input: DbUser): User {

        return User(
            id = input.id,
            role = input.role,
            jobRole = input.jobRole,
            status = input.status,
            email = input.email,
            firstName = input.firstName,
            lastName = input.lastName,
            middleName=input.middleName,
            password = input.password,
            passcode = input.passcode,
            address = input.address,
            dob = input.dob,
            gender = input.gender,
            nextOfKin = input.nextOfKin,
            nextOfKinContact = input.nextOfKinContact,
            nextOfKinNumber = input.nextOfKinNumber,
            displayPictureUrl = input.displayPicture,
            phoneNumber = input.phoneNumber
        )
    }

}