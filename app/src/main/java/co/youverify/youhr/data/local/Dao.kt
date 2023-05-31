package co.youverify.youhr.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao{

    @Query("SELECT * FROM assigned_task")
    fun getAll(): Flow<List<DBTask>>

    @Query("SELECT * FROM assigned_task WHERE :pageNumber=page")
    fun getPaginated(pageNumber:Int): Flow<List<DBTask>>

    @Query("DELETE FROM assigned_task")
    suspend fun clearAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(tasks: List<DBTask>)
}