package co.youverify.youhr.core.util

import co.youverify.youhr.data.model.GenericResponse
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.util.*

fun Date.toOrdinalDateString(includeOf:Boolean=true):String{

    val calendar=Calendar.getInstance()
    calendar.time=this

    val month= calendar[Calendar.MONTH]
    val year= calendar[Calendar.YEAR]

    val formattedDay=when(val day=calendar[Calendar.DAY_OF_MONTH]){

        1,21 -> "${day}st"
        2,22 -> "${day}nd"
        3,23->"${day}rd"
        else -> "${day}th"
    }

    val formattedMonth = getFormattedMonth(month)


    return if (includeOf)
        "$formattedDay of $formattedMonth,$year"
    else
        "$formattedDay $formattedMonth,$year"

}

private fun getFormattedMonth(month: Int)= when (month) {

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

fun getLeavePeriod(startDate:Date,endDate:Date):String{

    val calendar1=Calendar.getInstance()
    val calendar2=Calendar.getInstance()
    calendar1.time=startDate
    calendar2.time=endDate

    val startYear=calendar1[Calendar.YEAR]
    val endYear=calendar2[Calendar.YEAR]

    val startMonth=calendar1[Calendar.MONTH]
    val endMonth=calendar2[Calendar.MONTH]

    val startDay=calendar1[Calendar.DAY_OF_MONTH]
    val endDay=calendar2[Calendar.DAY_OF_MONTH]

    return if (startYear==endYear)
        "${getFormattedMonth(startMonth)} $startDay - ${getFormattedMonth(endMonth)} $endDay,$startYear"
    else
        "${getFormattedMonth(startMonth)} $startDay,$startYear - ${getFormattedMonth(endMonth)} $endDay,$endYear"

}

fun String.capitalizeWords():String =
    split(" ").joinToString(" "){ word ->
        word.replaceFirstChar {character->
        character.uppercase()
    }
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