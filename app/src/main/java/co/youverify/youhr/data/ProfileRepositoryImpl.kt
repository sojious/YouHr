package co.youverify.youhr.data

import co.youverify.youhr.core.util.Result
import co.youverify.youhr.data.mapper.DbToDomainProfileMapper
import co.youverify.youhr.data.mapper.DtoToDbProfileMapper
import co.youverify.youhr.data.mapper.DtoToDomainFilteredUserListMapper
import co.youverify.youhr.data.model.FilterUserResponse
import co.youverify.youhr.data.model.UpdateUserProfileRequest
import co.youverify.youhr.data.model.UserProfileResponse
import co.youverify.youhr.data.repository.profile.ProfileLocalDataSource
import co.youverify.youhr.data.repository.profile.ProfileRemoteDataSource
import co.youverify.youhr.domain.model.FilteredUser
import co.youverify.youhr.domain.model.User
import co.youverify.youhr.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileLocalDataSource: ProfileLocalDataSource,
    private val profileRemoteDataSource: ProfileRemoteDataSource,
    private val dtoToDbProfileMapper: DtoToDbProfileMapper,
    private val dbToDomainProfileMapper: DbToDomainProfileMapper,
    private val dtoToDomainFilteredUserListMapper: DtoToDomainFilteredUserListMapper
):ProfileRepository {
    override suspend fun getUserProfile(isFirstLogin:Boolean): Flow<Result<User>> {

        //If the user just logged for the first time get their profile info from a network call otherwise from the local database
        if (isFirstLogin){
            val networkResult:Result<UserProfileResponse> = profileRemoteDataSource.getUserProfile()
            return when(networkResult){
                is Result.Success->{
                    profileLocalDataSource.saveUser(dtoToDbProfileMapper.map(networkResult.data))
                    flow {
                        emit(Result.Success(dbToDomainProfileMapper.map(profileLocalDataSource.getUser())))
                    }
                }

                is Result.Error->{
                    flow {
                        emit(Result.Error(code = networkResult.code,message = networkResult.message))
                    }
                }

                is Result.Exception->{
                    flow {
                        emit(Result.Exception(e = networkResult.e, genericMessage = networkResult.genericMessage))
                    }
                }
            }
        }else{
            return flow {
                emit(Result.Success(dbToDomainProfileMapper.map(profileLocalDataSource.getUser())))
            }
        }


    }

    override suspend fun filterAllUser(): Flow<Result<List<FilteredUser>>> {
        val result = profileRemoteDataSource.filterAllUser()
      return  when(result){
            is Result.Success->{
                flow {
                    emit(Result.Success(data = dtoToDomainFilteredUserListMapper.map(result.data.data)))
                }
            }
            is Result.Error->{
                flow {
                    emit(Result.Error(code = result.code,message = result.message))
                }
            }
            is Result.Exception->{
                flow {
                    emit(Result.Exception(e=result.e, genericMessage = result.genericMessage))
                }
            }
        }
    }

    override suspend fun updateUserProfile(request: UpdateUserProfileRequest): Result<UserProfileResponse> {

         return   profileRemoteDataSource.updateUserProfile(request)

    }

    override suspend fun updateUserProfilePic(imageFile: MultipartBody.Part): Result<UserProfileResponse> {
        return profileRemoteDataSource.updateUserProfilePic(imageFile)

    }

    override suspend fun filterAllLineManager(): Flow<Result<List<FilteredUser>>> {
        val result = profileRemoteDataSource.filterAllLineManager()
        return  when(result){
            is Result.Success->{
                flow {
                    emit(Result.Success(data = dtoToDomainFilteredUserListMapper.map(result.data.data)))
                }
            }
            is Result.Error->{
                flow {
                    emit(Result.Error(code = result.code,message = result.message))
                }
            }
            is Result.Exception->{
                flow {
                    emit(Result.Exception(e=result.e, genericMessage = result.genericMessage))
                }
            }
        }
    }

}