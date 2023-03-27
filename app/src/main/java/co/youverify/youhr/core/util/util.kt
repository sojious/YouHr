package co.youverify.youhr.core.util

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