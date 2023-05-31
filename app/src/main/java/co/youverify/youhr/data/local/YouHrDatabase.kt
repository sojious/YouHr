package co.youverify.youhr.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [DBTask::class], version = 1)
@TypeConverters(DatabaseTypeConverters::class)
abstract class YouHrDatabase:RoomDatabase() {
    abstract fun taskDao():TaskDao
}