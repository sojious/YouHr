package co.youverify.youhr.core.util

import TaskProgressColor
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.text.format.DateUtils
import androidx.compose.ui.graphics.Color
import co.youverify.youhr.domain.model.Task
import co.youverify.youhr.presentation.ui.task.TaskStatus
import com.google.gson.JsonSyntaxException
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.text.ParseException
import java.util.Locale


val timeZone: TimeZone =TimeZone.getTimeZone("Africa/Lagos")

private const val SECOND = 1
private const val MINUTE = 60 * SECOND
private const val HOUR = 60 * MINUTE
private const val DAY = 24 * HOUR
private const val MONTH = 30 * DAY
private const val YEAR = 12 * MONTH

fun getLeavePeriod(startDate:String,endDate:String): String {
    val split1=startDate.split(' ')
    val startMonth=split1[1]
    val startYear=split1[3]
    val formattedStartDay= getFormattedDay(split1[2])

    val split2=endDate.split(' ')
    val endMonth=split2[1]
    val endYear=split2[3]
    val formattedEndDay= getFormattedDay(split2[2])

    return if (startYear==endYear)
        "$formattedStartDay $startMonth - $formattedEndDay $endMonth $startYear"
    else "$formattedStartDay $startMonth $startYear - $formattedEndDay $endMonth $endYear"

}


fun String.toDobFormat(): String {
    if (this.isEmpty()) return "N/A"
    val split1=this.split(' ')
    val month = split1[1]
    val year = split1[3]
    val day = split1[2]
    val numericMonth=when(month){
          "Jan"-> "01"
          "Feb"-> "02"
          "Mar"-> "03"
          "Apr"-> "04"
          "May"-> "05"
          "Jun"-> "06"
          "Jul"-> "07"
          "Aug"-> "08"
          "Sep"-> "09"
          "Oct"-> "10"
          "Nov"-> "11"

        else -> "12"
    }

    return "$day/$numericMonth/$year"
}
fun getFormattedLeaveDate(dateString:String): String {
    val split=dateString.split(' ')
    return "${split[0]} ${split[1]} ${split[2]} ${split[3]}"

}

fun getGreetingMessage():String{

    return when (Calendar.getInstance(TimeZone.getDefault()).get(Calendar.HOUR_OF_DAY)) {
        in 0..11 -> "Good Morning"
        in 12..15 -> "Good Afternoon"
        in 16..20 -> "Good Evening"
        in 21..23 -> "Good Night"
        else -> "Hello"
    }
}
fun String.toTimeAgo(isApprovalDate: Boolean=false): String {
    val time=this.toEpochMillis(isApprovalDate)

    val now = System.currentTimeMillis()
    val diff: Long = now - time
    return if (diff < DateUtils.WEEK_IN_MILLIS) {
        if (diff <= 1000) "just now" else DateUtils.getRelativeTimeSpanString(
            time, now, DateUtils.MINUTE_IN_MILLIS,
            DateUtils.FORMAT_ABBREV_RELATIVE
        ).toString()
    } else if (diff <= 4 * DateUtils.WEEK_IN_MILLIS) {
        val week = (diff / DateUtils.WEEK_IN_MILLIS).toInt()
        if (week > 1) "$week weeks ago" else "$week week ago"
    } else if (diff < DateUtils.YEAR_IN_MILLIS) {
        val month = (diff / (4 * DateUtils.WEEK_IN_MILLIS)).toInt()
        if (month > 1) "$month months ago" else "$month month ago"
    } else {
        val year = (diff / DateUtils.YEAR_IN_MILLIS).toInt()
        if (year > 1) "$year years ago" else "$year year ago"
    }
}
fun Task.getStatus(): TaskStatus {
    return when(status){
        TaskStatus.COMPLETED.id -> TaskStatus.COMPLETED
        TaskStatus.IN_PROGRESS.id-> TaskStatus.IN_PROGRESS
        TaskStatus.OVER_DUE.id-> TaskStatus.OVER_DUE
        TaskStatus.TO_DO.id -> TaskStatus.TO_DO
        else ->TaskStatus.REVIEW
    }
}

fun TaskStatus.getColor(): Color {
   return when (this) {
        TaskStatus.COMPLETED -> TaskProgressColor.COMPLETED.value
        TaskStatus.IN_PROGRESS -> TaskProgressColor.IN_PROGRESS.value
        TaskStatus.OVER_DUE -> TaskProgressColor.OVERDUE.value
        TaskStatus.TO_DO -> TaskProgressColor.TO_DO.value
        else -> TaskProgressColor.REVIEW.value
    }
}
fun String.toCardinalDateFormat(): String {
    val split=split('-')
    val month=when(split[1]){
        "01" -> "Jan"
        "02" -> "Feb"
        "03" -> "Mar"
        "04" -> "Apr"
        "05" -> "May"
        "06" -> "Jun"
        "07" -> "Jul"
        "08" -> "Aug"
        "09" -> "Sep"
        "10" -> "Oct"
        "11" -> "Nov"

        else -> "Dec"
    }
    return "$month ${split[2]},${split[0]}"
}

fun String.toCardinalDateFormat2(): String {
    val split1=split(' ')
    val split2=split1[0].split('-')
    val month=when(split2[1]){
        "01" -> "Jan"
        "02" -> "Feb"
        "03" -> "Mar"
        "04" -> "Apr"
        "05" -> "May"
        "06" -> "Jun"
        "07" -> "Jul"
        "08" -> "Aug"
        "09" -> "Sep"
        "10" -> "Oct"
        "11" -> "Nov"

        else -> "Dec"
    }

        val formattedDay= getFormattedDay(split2[2])

    return "$formattedDay $month ${split2[0]}"
}

fun getFormattedDay(dayString:String): String {
   return when(dayString){

        "04", "05", "06", "07", "08", "09" -> "${dayString.last()}th"
        "01"->"${dayString.last()}st"
        "21"->"${dayString}st"
        "31"->"${dayString}st"
        "02"->"${dayString.last()}nd"
        "22"->"${dayString}nd"
        "03"->"${dayString.last()}rd"
        "23"->"${dayString}rd"
        else -> "${dayString}th"
    }
}
fun Long.toCardinalDateString():String{

    val calendar = Calendar.getInstance(TimeZone.getDefault());
    calendar.timeInMillis = this
    calendar.timeZone = timeZone

    val month = calendar.get(Calendar.MONTH) + 1; // Note: Month value in Calendar API is 0-based
    val year = calendar.get(Calendar.YEAR);

    val formattedDay=when(val day=calendar.get(Calendar.DAY_OF_MONTH)){

        1,21 -> "${day}st"
        2,22 -> "${day}nd"
        3,23->"${day}rd"
        else -> "${day}th"
    }

    val formattedMonth = getFormattedMonth(month,capitalize=true)


    return "$formattedMonth $formattedDay,$year"

}

fun Long.toOrdinalDateString(includeOf:Boolean=true):String{

    val calendar = Calendar.getInstance(TimeZone.getDefault())
    calendar.timeInMillis = this

    val month= calendar.get(Calendar.MONTH) + 1 // Note: Month value in Calendar API is 0-based

    val year= calendar.get(Calendar.YEAR)




    val formattedDay=when(val day=calendar.get(Calendar.DAY_OF_MONTH)){

        1,21 -> "${day}st"
        2,22 -> "${day}nd"
        3,23->"${day}rd"
        else -> "${day}th"
    }

    val formattedMonth = getFormattedMonth(month, true)


    return if (includeOf)
        "$formattedDay of $formattedMonth,$year"
    else
        "$formattedDay $formattedMonth,$year"

}

private fun getFormattedMonth(month: Int, capitalize: Boolean=false):String{
    val monthString=when (month) {

        1 -> "Jan"
        2 -> "feb"
        3 -> "Mar"
        4 -> "Apr"
        5 -> "May"
        6 -> "Jun"
        7 -> "Jul"
        8 -> "Aug"
        9 -> "Sep"
        10 -> "Oct"
        11 -> "Nov"

        else -> "Dec"
    }
    return if (capitalize) monthString.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() } else monthString
}

fun getDateRange(startDateMillis:Long, endDateMillis:Long):String{


    val calendar1 = Calendar.getInstance(timeZone)
    calendar1.timeInMillis = startDateMillis

    val calendar2: Calendar = Calendar.getInstance(timeZone)
    calendar2.timeInMillis = endDateMillis

    val startYear = calendar1[Calendar.YEAR]
    val endYear = calendar2[Calendar.YEAR]

    val startMonth = calendar1[Calendar.MONTH] + 1 // Note: Month value in Calendar API is 0-based

    val endMonth = calendar2[Calendar.MONTH] + 1

    val startDay = calendar1[Calendar.DAY_OF_MONTH]
    val endDay = calendar2[Calendar.DAY_OF_MONTH]

    return if (startYear==endYear)
        "${getFormattedMonth(startMonth, true)} $startDay - ${getFormattedMonth(endMonth, true)} $endDay,$startYear"
    else
        "${getFormattedMonth(startMonth, true)} $startDay,$startYear - ${getFormattedMonth(
            endMonth,
            true
        )} $endDay,$endYear"

}



fun String.toEpochMillis(isApprovalDate: Boolean=false): Long {

    val formatter =
        if (isApprovalDate) SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss",Locale.getDefault())
    else
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",Locale.getDefault())

    formatter.timeZone = TimeZone.getTimeZone("GMT")

    val time:Long
    try {
        time = formatter.parse(this).time
    } catch (e: ParseException) {
        e.printStackTrace()
        return 0; // or handle the parse exception accordingly
    }

    //val calendar = Calendar.getInstance()
   // calendar.timeZone= timeZone
    //calendar.time = date;

    //val epochMillis = calendar.timeInMillis;

    return time

}


fun String.capitalizeWords():String =
    split(" ").joinToString(" "){ word ->
        word.replaceFirstChar {character->
        character.uppercase()
    }
}


fun Long.toFormattedDateString(pattern: String): String {

    val calendar = Calendar.getInstance(TimeZone.getDefault());
    calendar.timeInMillis = this
    val formatter = SimpleDateFormat(pattern, Locale.ENGLISH)
    formatter.timeZone = timeZone

    return formatter.format(calendar.time)
}

fun Long.toHrFormattedDateString(): String {

    val calendar = Calendar.getInstance(TimeZone.getDefault());
    calendar.timeInMillis = this
    val formattedDay= when(calendar[Calendar.DAY_OF_WEEK]){
        1->"Sun"
        2->"Mon"
        3->"Tue"
        4->"Wed"
        5->"Thu"
        6->"Fri"
        else->"Sat"
    }

    val month= getFormattedMonth(calendar[Calendar.MONTH]+1)
    val dayOfMonth=calendar[Calendar.DAY_OF_MONTH]
    val year=calendar[Calendar.YEAR]


    return "$formattedDay $month $dayOfMonth $year"
}



 suspend fun<T:Any> handleApi(callApi: suspend () -> Response<T>): Result<T> {


   return try{
        val response=callApi()
        val body=response.body()

       //for a response with a body
        if (response.isSuccessful && body!=null)
            Result.Success(data = body)
        else
            //for a response with error message
        {
            var errorMessage = ""
            try {
                val jsOnErrorObject= JSONObject(response.errorBody()?.string()!!)
                errorMessage=jsOnErrorObject.getString("message")
            }catch (e:Exception){
               e.printStackTrace()
            }
            Result.Error(code=response.code(), message = errorMessage)
        }


       //for exceptions
    }catch (exception:HttpException){
        Result.Exception(e=exception, genericMessage = "Oops..Something went wrong")
    }catch (exception: IOException){
        Result.Exception(e=exception, genericMessage = "Could not reach Server, Check your Internet Connection")
    }catch (exception:Throwable){
        Result.Exception(e=exception, genericMessage = "Oops..An error occurred from the server")
    }
   catch (exception:JsonSyntaxException){
       //Result.Exception(e=exception, genericMessage = "Oops..An error occurred from the server")
       throw exception
   }


}


//Extension function that encapsulates pagination logic
