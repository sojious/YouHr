package co.youverify.youhr.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface TaskDao{

    @Query("SELECT * FROM assigned_task")
    suspend fun getAll(): List<DBTask>

    @Query("SELECT * FROM assigned_task WHERE :pageNumber=page")
    suspend fun getPaginated(pageNumber:Int): List<DBTask>

    @Query("DELETE FROM assigned_task")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<DBTask>)
}



@Dao
interface UserDao{

    @Query("SELECT * FROM user")
    suspend fun getUser():DbUser
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveUser(user: DbUser)
}

@Dao
interface LeaveDao{

    @Query("SELECT * FROM leave_request")
    suspend fun getLeaveRequests():List<DbLeaveRequest>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLeaveRequests(requests: List<DbLeaveRequest>)

    @Query("SELECT * FROM leave_summary")
    suspend fun getLeaveSummary():DbLeaveSummary?
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveLeaveSummary(summary: DbLeaveSummary)

    @Query("DELETE FROM leave_request")
    suspend fun clearAllLeaveRequests()

    @Query("DELETE FROM leave_summary")
    suspend fun clearAllLeaveSummary()

}