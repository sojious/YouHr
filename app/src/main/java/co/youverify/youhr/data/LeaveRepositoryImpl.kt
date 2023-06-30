package co.youverify.youhr.data

import co.youverify.youhr.core.util.Result
import co.youverify.youhr.data.mapper.DbToDomainLeaveListMapper
import co.youverify.youhr.data.mapper.DbToDomainLeaveSummaryMapper
import co.youverify.youhr.data.mapper.DtoToDbLeaveListMapper
import co.youverify.youhr.data.mapper.DtoToDbLeaveSummaryMapper
import co.youverify.youhr.data.model.LeaveApplicationRequest
import co.youverify.youhr.data.model.LeaveApplicationResponse
import co.youverify.youhr.data.repository.leave.LeaveLocalDataSource
import co.youverify.youhr.data.repository.leave.LeaveRemoteDataSource
import co.youverify.youhr.domain.model.LeaveRequest
import co.youverify.youhr.domain.model.LeaveSummary
import co.youverify.youhr.domain.model.Task
import co.youverify.youhr.domain.repository.LeaveRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LeaveRepositoryImpl @Inject constructor(
    private val leaveLocalDataSource: LeaveLocalDataSource,
    private val leaveRemoteDataSource: LeaveRemoteDataSource,
    private val dtoToDbLeaveListMapper: DtoToDbLeaveListMapper,
    private val dbToDomainLeaveListMapper: DbToDomainLeaveListMapper,
    private val dtoToDbLeaveSummaryMapper: DtoToDbLeaveSummaryMapper,
    private val dbToDomainLeaveSummaryMapper: DbToDomainLeaveSummaryMapper
):LeaveRepository {


    override suspend fun getLeaveRequests(isFirstLoad: Boolean): Flow<Result<List<LeaveRequest>>> {
        return if (isFirstLoad) getLeaveRequestsFirstLoad() else getLeaveRequests()
    }

    override suspend fun getLeaveSummary(isFirstLoad: Boolean): Flow<Result<LeaveSummary>> {
        return if (isFirstLoad) getLeaveSummaryFirstLoad() else getLeaveSummary()

    }

    override suspend fun createLeaveRequest(request: LeaveApplicationRequest): Flow<Result<LeaveApplicationResponse>> {
        return flow {
            emit(leaveRemoteDataSource.createLeaveRequest(request))
        }
    }

    private suspend fun getLeaveSummary(): Flow<Result<LeaveSummary>> {

        //Get the leave requests by making a network call
        return when(val networkResult = leaveRemoteDataSource.getLeaveSummary()){
            is Result.Success->{
                //cache the response to the local database and return this cached response
                leaveLocalDataSource.saveLeaveSummary(dtoToDbLeaveSummaryMapper.map(networkResult.data.data))
                val domainLeaveRequests = dbToDomainLeaveSummaryMapper.map(leaveLocalDataSource.getLeaveSummary()!!)

                        flow {
                            emit(Result.Success(domainLeaveRequests))
                        }
            }

            is Result.Error->{
                flow { emit(Result.Error(code = networkResult.code,message = networkResult.message)) }
            }

            is Result.Exception->{
                flow { emit((Result.Exception(e = networkResult.e, genericMessage = networkResult.genericMessage))) } }
        }
    }

    private suspend fun getLeaveSummaryFirstLoad(): Flow<Result<LeaveSummary>> {


        val dataBaseLeaveSummary = leaveLocalDataSource.getLeaveSummary()

        //if the database contains no cached data, make a network call to get new data and cache it, otherwise return
        // the cached data from the database
        if (dataBaseLeaveSummary == null){
            return when(val networkResult = leaveRemoteDataSource.getLeaveSummary()){
                is Result.Success->{
                    leaveLocalDataSource.saveLeaveSummary(dtoToDbLeaveSummaryMapper.map(networkResult.data.data))
                    flow {emit(Result.Success(dbToDomainLeaveSummaryMapper.map(leaveLocalDataSource.getLeaveSummary()!!)))  }
                }

                is Result.Error->{
                    flow {emit(Result.Error(code = networkResult.code,message = networkResult.message))}
                }

                is Result.Exception->{
                    flow { emit((Result.Exception(e = networkResult.e, genericMessage = networkResult.genericMessage))) }
                }
            }
        }else{
            return  flow {emit(Result.Success(dbToDomainLeaveSummaryMapper.map(dataBaseLeaveSummary))) }
        }

    }

    private suspend fun getLeaveRequestsFirstLoad(): Flow<Result<List<LeaveRequest>>> {

        val leaveRequests:List<LeaveRequest>

        val dataBaseLeaveRequests = leaveLocalDataSource.getLeaveRequests()

        leaveRequests=dbToDomainLeaveListMapper.map(dataBaseLeaveRequests)

        //if the database contains no cached data, make a network call to get new data and cache it, otherwise return
        // the cached data from the database
        val result = if(leaveRequests.isEmpty()){
            fetchRemoteAndCacheResponse()
        } else{
            Result.Success(data = leaveRequests)
        }

        return flow { emit(result) }

    }


    private suspend fun fetchRemoteAndCacheResponse(): Result<List<LeaveRequest>> {
        return when(val networkResult = leaveRemoteDataSource.getLeaveRequests()){
            is Result.Success->{

                //Cache the data from the network call to the local database
                if (leaveLocalDataSource.getLeaveRequests().isNotEmpty()){ leaveLocalDataSource.clearAllLeaveRequest() }
                leaveLocalDataSource.saveLeaveRequests(
                    dtoToDbLeaveListMapper.map(networkResult.data.data?.docs)
                )
                Result.Success(dbToDomainLeaveListMapper.map(leaveLocalDataSource.getLeaveRequests()))
            }

            is Result.Error->{
                Result.Error(code = networkResult.code,message = networkResult.message)
            }

            is Result.Exception->{
                 (Result.Exception(e = networkResult.e, genericMessage = networkResult.genericMessage))
            }
        }
    }

    suspend fun getLeaveRequests(): Flow<Result<List<LeaveRequest>>> {
        val result = fetchRemoteAndCacheResponse()
        return flow {  emit(result)}
    }




}

