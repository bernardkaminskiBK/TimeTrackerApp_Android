package com.berni.timetrackerapp.utils

import java.util.*
import kotlin.math.roundToInt

object Formatter {

    fun dateFormat(dateInLong : Long) =
        android.text.format.DateFormat
            .format("MM.dd.yyyy HH:mm:ss", Date(dateInLong)).toString()

    fun getTimeStringFromDouble(time: Double): String {
        val resultInt = time.roundToInt()
        val hours = resultInt % 86400 / 3600
        val minutes = resultInt % 86400 % 3600 / 60
        val seconds = resultInt % 86400 % 3600 % 60

        return makeTimeString(hours, minutes, seconds)
    }

    private fun makeTimeString(hours: Int, minutes: Int, seconds: Int): String =
        String.format("%02d:%02d:%02d", hours, minutes, seconds)

}