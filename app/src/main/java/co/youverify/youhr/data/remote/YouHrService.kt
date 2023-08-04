package co.youverify.youhr.data.remote

import co.youverify.youhr.data.model.LeaveRequestsResponse
import co.youverify.youhr.data.model.*
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
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

    @PUT("user-service/v1/user/changepassword")
    suspend fun changePassword( @Body changePasswordRequest: ChangePasswordRequest): Response<GenericResponse>

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

    @GET("employee-service/v1/employee/leave/employeeonleave")
    suspend fun getEmployeesOnLeave():Response<EmployeeOnLeaveResponse>

    @POST("employee-service/v1/employee/leave/request")
    suspend fun createLeaveRequest(@Body applicationRequest: LeaveApplicationRequest):Response<LeaveApplicationResponse>

    @GET("employee-service/v1/employee/filtername?")
    suspend fun filterAllUser():Response<FilterUserResponse>

    @GET("employee-service/v1/employee/filtersupervisor")
    suspend fun filterAllLineManager():Response<FilterUserResponse>

    @PUT("employee-service/v1/employee/updatemyprofile")
    suspend fun updateProfile(@Body request:UpdateUserProfileRequest):Response<UserProfileResponse>

    @Multipart
    @POST("employee-service/v1/employee/uploadpic")
    suspend fun updateDisplayPic(@Part imageFile: MultipartBody.Part): Response<UserProfileResponse>


    @GET("employee-service/v1/employee/announcement")
    suspend fun getAllAnnouncement(): Response<AnnouncementListResponse>


}
