package co.youverify.youhr.data.mapper

import co.youverify.youhr.data.local.DbUser
import co.youverify.youhr.data.model.FilterUserDto
import co.youverify.youhr.data.model.UserProfileResponse
import co.youverify.youhr.domain.model.FilteredUser
import co.youverify.youhr.domain.model.User

class DtoToDbProfileMapper:Mapper<UserProfileResponse, DbUser>{
    override fun map(input: UserProfileResponse): DbUser {

        return DbUser(
            id = input.data.id,
            role = input.data.role?:"",
            jobRole = input.data.jobRole?:"",
            status = input.data.status?:"",
            email = input.data.email?:"",
            firstName = input.data.firstName?:"",
            lastName = input.data.lastName?:"",
            password = input.data.password?:"",
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

class DtoToDomainProfileMapper:Mapper<UserProfileResponse, User>{
    override fun map(input: UserProfileResponse): User {

        return User(
            id = input.data.id,
            role = input.data.role?:"",
            jobRole = input.data.jobRole?:"",
            status = input.data.status?:"",
            email = input.data.email?:"",
            firstName = input.data.firstName?:"",
            lastName = input.data.lastName?:"",
            password = input.data.password?:"",
            middleName=input.data.middleName?:"",
            passcode = input.data.passcode?:"",
            address = input.data.address?:"",
            dob = input.data.dob?:"",
            gender = input.data.gender?:"",
            nextOfKin = input.data.nextofKin?:"",
            nextOfKinContact = input.data.nextofKinContact?:"",
            nextOfKinNumber = input.data.nextofKinNumber?:"",
            displayPictureUrl = input.data.displayPicture?:"",
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

class DtoToDomainFilteredUserMapper:Mapper<FilterUserDto, FilteredUser>{
    override fun map(input: FilterUserDto): FilteredUser {

       return FilteredUser(firstName = input.firstName?:"", lastName=input.lastName?:"",email = input.email, id = input.id)
    }

}

class DtoToDomainFilteredUserListMapper(private val filteredUserMapper: DtoToDomainFilteredUserMapper):Mapper<List<FilterUserDto>, List<FilteredUser>>{

    override fun map(input: List<FilterUserDto>): List<FilteredUser> {
        return  input.map {
            filteredUserMapper.map(it)
        }
    }

}