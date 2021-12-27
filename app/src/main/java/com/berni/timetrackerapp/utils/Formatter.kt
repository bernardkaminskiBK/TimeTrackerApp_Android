package com.berni.timetrackerapp.utils

import java.text.SimpleDateFormat
import java.util.*
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

//    fun dateTimeToSeconds(time: String): Long {
//        val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.ENGLISH)
//        val reference = dateFormat.parse("00:00:00")
//        val date = dateFormat.parse(time)
//        val seconds = (date!!.time - reference!!.time) / 1000L
//        return seconds / 3600
//    }

}