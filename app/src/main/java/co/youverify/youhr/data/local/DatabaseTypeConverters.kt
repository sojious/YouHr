package co.youverify.youhr.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson

class DatabaseTypeConverters {

    @TypeConverter
    fun docListToJSON(dbDocList: DbDocList):String = Gson().toJson(dbDocList,DbDocList::class.java)

    @TypeConverter
    fun JSONToDocList(jsonString: String):DbDocList = Gson().fromJson(jsonString,DbDocList::class.java)
}