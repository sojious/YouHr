package co.youverify.youhr.core.util

import co.youverify.youhr.domain.model.Task
import co.youverify.youhr.presentation.ui.task.TaskStatus
import com.github.marlonlom.utilities.timeago.TimeAgo
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*


val zoneId= ZoneId.of("Africa/Lagos")



fun String.toTimeAgo(): String {
    val formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
     val instant=Instant.from(formatter.parse(this))
    return TimeAgo.using(instant.toEpochMilli())
}
fun Task.getStatus(): TaskStatus {
    return when(status){
        "To-do"-> TaskStatus.PENDING
        else->TaskStatus.COMPLETED
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

        val formattedDay=when(split[2]){

            "04","05","06","07","08","09", -> "${split[2].last()}th"
            "01"->"${split[2].last()}st"
            "21"->"${split[2]}st"
            "31"->"${split[2]}st"
            "02"->"${split[2].last()}nd"
            "22"->"${split[2]}nd"
            "03"->"${split[2].last()}rd"
            "23"->"${split[2]}rd"
            else -> "${split[2]}th"
        }

    return "$formattedDay $month ${split[0]}"
}
fun Long.toCardinalDateString():String{



    val instant=Instant.ofEpochMilli(this)
    val zoneDateTime=ZonedDateTime.ofInstant(instant,zoneId)

    val month= zoneDateTime.month.value
    val year= zoneDateTime.year

    val formattedDay=when(val day=zoneDateTime.dayOfMonth){

        1,21 -> "${day}st"
        2,22 -> "${day}nd"
        3,23->"${day}rd"
        else -> "${day}th"
    }

    val formattedMonth = getFormattedMonth(month,capitalize=true)


    return "$formattedMonth $formattedDay,$year"

}

fun Long.toOrdinalDateString(includeOf:Boolean=true):String{



    val instant=Instant.ofEpochMilli(this)
    val zoneDateTime=ZonedDateTime.ofInstant(instant,zoneId)

    val month= zoneDateTime.month.value
    val year= zoneDateTime.year

    val formattedDay=when(val day=zoneDateTime.dayOfMonth){

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

        1 -> "jan"
        2 -> "feb"
        3 -> "mar"
        4 -> "apr"
        5 -> "may"
        6 -> "jun"
        7 -> "jul"
        8 -> "aug"
        9 -> "sep"
        10 -> "oct"
        11 -> "nov"

        else -> "dec"
    }
    return if (capitalize) monthString.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() } else monthString
}

fun getDateRange(startDateMillis:Long, endDateMillis:Long):String{


    //using New  Java date/time Api
    val instant1=Instant.ofEpochMilli(startDateMillis)
    val zoneDateTime1=ZonedDateTime.ofInstant(instant1,zoneId)
    val instant2=Instant.ofEpochMilli(endDateMillis)
    val zoneDateTime2=ZonedDateTime.ofInstant(instant2,zoneId)

    val startYear=zoneDateTime1.year
    val endYear=zoneDateTime2.year

    val startMonth=zoneDateTime1.month.value
    val endMonth=zoneDateTime2.month.value

    val startDay=zoneDateTime1.dayOfMonth
    val endDay=zoneDateTime2.dayOfMonth

    return if (startYear==endYear)
        "${getFormattedMonth(startMonth, true)} $startDay - ${getFormattedMonth(endMonth, true)} $endDay,$startYear"
    else
        "${getFormattedMonth(startMonth, true)} $startDay,$startYear - ${getFormattedMonth(
            endMonth,
            true
        )} $endDay,$endYear"

}

fun String.capitalizeWords():String =
    split(" ").joinToString(" "){ word ->
        word.replaceFirstChar {character->
        character.uppercase()
    }
}


 fun Long.toFormattedDateString(pattern: String): String {
    val instant=Instant.ofEpochMilli(this)
    val formatter=DateTimeFormatter.ofPattern(pattern)
    return formatter.format(instant.atZone(zoneId))
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
            Result.Error(code=response.code(), message = response.message())


       //for exceptions
    }catch (exception:HttpException){
        Result.Exception(e=exception, genericMessage = "Oops..Something went wrong")
    }catch (exception: IOException){
        Result.Exception(e=exception, genericMessage = "Could not reach Server, Check your Internet Connection")
    }catch (exception:Throwable){
        Result.Exception(e=exception, genericMessage = "Oops..An error occurred from the server")
    }


}


//Extension function that encapsulates pagination logic
