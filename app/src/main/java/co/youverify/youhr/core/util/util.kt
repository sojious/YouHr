package co.youverify.youhr.core.util

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.toUpperCase
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*


 val zoneId= ZoneId.of("Africa/Lagos")


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

 suspend fun<T:Any> handleApi(callApi: suspend () -> Response<T>):NetworkResult<T>{


   return try{
        val response=callApi()
        val body=response.body()

       //for a response with a body
        if (response.isSuccessful && body!=null)
            NetworkResult.Success(data = body)
        else
            //for a response with error message
            NetworkResult.Error(code=response.code(), message = response.message())


       //for exceptions
    }catch (exception:HttpException){
        NetworkResult.Exception(e=exception, genericMessage = "Oops..Something went wrong")
    }catch (exception: IOException){
        NetworkResult.Exception(e=exception, genericMessage = "Could not reach Server, Check your Internet Connection")
    }catch (exception:Throwable){
        NetworkResult.Exception(e=exception, genericMessage = "Oops..An error occurred from the server")
    }


}