package co.youverify.youhr.data.remote

import co.youverify.youhr.data.model.LeaveRequestsResponse
import co.youverify.youhr.data.model.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface YouHrService {

    @POST("user-service/v1/user/login")
    @Headers("No-Authentication: true")
    suspend fun loginWithPassword( @Body loginWithPassWordRequest: LoginWithPassWordRequest): Response<AuthResponse>

    @POST("user-service/v1/user/loginpasscode")
    @Headers("No-Authentication: true")
    suspend fun loginWithCode( @Body loginWithCodeRequest: LoginWithCodeRequest): Response<AuthResponse>

    @POST("user-service/v1/user/resetpassword")
    @Headers("No-Authentication: true")
    suspend fun resetPassword( @Body resetPasswordRequest: ResetPasswordRequest): Response<GenericResponse>

    @POST("user-service/v1/user/setpasscode")
    suspend fun createCode( @Body createCodeRequest:CreateCodeRequest): Response<GenericResponse>

    @GET("task-service/v1/task/taskassignedto")
    suspend fun getAssignedTask(@Query("page") page: Int =1):Response<AssignedTasksResponse>

    @GET("employee-service/v1/employee")
    suspend fun getUserProfile():Response<UserProfileResponse>
    @GET("employee-service/v1/employee/leave/employeerequests")
    suspend fun getLeaveRequests():Response<LeaveRequestsResponse>

    @GET("employee-service/v1/employee/leave/leavesummary")
    suspend fun getLeaveSummary():Response<LeaveSummaryResponse>

    @POST("employee-service/v1/employee/leave/request")
    suspend fun createLeaveRequest(@Body applicationRequest: LeaveApplicationRequest):Response<LeaveApplicationResponse>
}
