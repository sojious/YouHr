package co.youverify.youhr.data

import co.youverify.youhr.core.util.Result
import co.youverify.youhr.data.mapper.DbToDomainProfileMapper
import co.youverify.youhr.data.mapper.DtoToDbProfileMapper
import co.youverify.youhr.data.model.UserProfileResponse
import co.youverify.youhr.data.repository.profile.ProfileLocalDataSource
import co.youverify.youhr.data.repository.profile.ProfileRemoteDataSource
import co.youverify.youhr.domain.model.User
import co.youverify.youhr.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val profileLocalDataSource: ProfileLocalDataSource,
    private val profileRemoteDataSource: ProfileRemoteDataSource,
    private val dtoToDbProfileMapper: DtoToDbProfileMapper,
    private val dbToDomainProfileMapper: DbToDomainProfileMapper
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
}