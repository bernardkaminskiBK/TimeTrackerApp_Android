package com.berni.timetrackerapp.utils

import kotlin.math.roundToInt

object Converter {

    fun String?.convertTimeToSeconds(): Long {
        if (this.isNullOrEmpty().not()) {
            val units = this?.split(":")?.toTypedArray()
            if (units?.isNotEmpty() == true && units.size >= 3) {
                val hours = units[0].toInt()
                val minutes = units[1].toInt()
                val seconds = units[2].toInt()
                return (3600L * hours) + (60L * minutes) + seconds
            }
        }
        return 0
    }

    fun Long?.convertSecondsToDateTime() : String {
        val secs = this
        if (secs != null) return String.format("%02d:%02d:%02d", secs / 3600L, (secs % 3600) / 60, secs % 60)
        return "00:00:00"
    }

    fun Long?.convertSecondsToHours() : Float {
        val seconds = this
        if (seconds != null) {
            val hours = seconds / 3600f
            return (hours * 100).roundToInt() / 100.0f
        }
        return 0.0f
    }

    fun Float?.roundTwoDecimalPlaces() : Float {
        if (this != null) {
            return (this * 100).roundToInt() / 100.0f
        }
        return 0.0f
    }

}