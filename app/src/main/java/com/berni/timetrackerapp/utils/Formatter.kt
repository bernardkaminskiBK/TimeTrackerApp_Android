package com.berni.timetrackerapp.utils

import android.util.Log
import kotlin.math.roundToInt

object Formatter {

    fun getTimeStringFromDouble(time: Double): String {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60

        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hours: Int, minutes: Int, seconds: Int): String =
        String.format("%02d:%02d:%02d", hours, minutes, seconds)

    fun String?.dateToStringFormat(): String {
        val year = this!!.takeLast(4)
        when (this.take(2)) {
            "01" -> return "January $year"
            "02" -> return "February $year"
            "03" -> return "March $year"
            "04" -> return "April $year"
            "05" -> return "May $year"
            "06" -> return "June $year"
            "07" -> return "July $year"
            "08" -> return "August $year"
            "09" -> return "September $year"
            "10" -> return "October $year"
            "11" -> return "November $year"
            "12" -> return "December $year"
        }
        return "null"
    }

//    fun dateTimeToSeconds(time: String): Long {
//        val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
//        val reference = dateFormat.parse("00:00:00")
//        val date = dateFormat.parse(time)
//        val seconds = (date!!.time - reference!!.time) / 1000L
//        return seconds / 3600
//    }

}