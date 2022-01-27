package com.berni.timetrackerapp.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.berni.timetrackerapp.model.entities.Record
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.*
import kotlin.collections.ArrayList

@RequiresApi(Build.VERSION_CODES.O)
object TestData {

    private var i = 1

    fun randomTestDataToDB(numberOfItems: Int): ArrayList<Record> {
        val testDataset = ArrayList<Record>()

        for (i in 1..numberOfItems) {
            when ((1..6).shuffled().last()) {
                1 -> {
                    testDataset.add(
                        Record(
                            0,
                            randomDateGenerator(),
                            "Duolingo",
                            getRandomRecordTimeInSeconds()
                        )
                    )
                }
                2 -> {
                    testDataset.add(
                        Record(
                            0,
                            randomDateGenerator(),
                            "Programing",
                            getRandomRecordTimeInSeconds()
                        )
                    )
                }
                3 -> {
                    testDataset.add(
                        Record(
                            0,
                            randomDateGenerator(),
                            "Work",
                            getRandomRecordTimeInSeconds()
                        )
                    )
                }
                4 -> {
                    testDataset.add(
                        Record(
                            0,
                            randomDateGenerator(),
                            "Cooking",
                            getRandomRecordTimeInSeconds()
                        )
                    )
                }
                5 -> {
                    testDataset.add(
                        Record(
                            0,
                            randomDateGenerator(),
                            "Yoga",
                            getRandomRecordTimeInSeconds()
                        )
                    )
                }
                6 -> {
                    testDataset.add(
                        Record(
                            0,
                            randomDateGenerator(),
                            "Training",
                            getRandomRecordTimeInSeconds()
                        )
                    )
                }
            }
        }

        return testDataset
    }

    private fun getRandomRecordTimeInSeconds(): Long {
        val time = 3_600L * 5
        val randomTime = time / (1..10).shuffled().last()
        return randomTime
    }


    private fun randomDateGenerator(): Long {
        // dates between 01.01.2020 - 31.12.2020
        val min = 1577897548000
        val max = 1609433548000

       // dates between 01.01.2021 - 31.12.2021
//        val min = 1609514485000
//        val max = 1640964085000

        val random = (min..max).random()
        return random
    }

//    private fun getRandomRecordTimeInSeconds(): Long {
//        return 3600L
//    }

//    private fun getRandomProgressTime(): String {
//        val time = 3_600_000L * 5
//        val randomTime = time / (1..10).shuffled().last()
//        return android.text.format.DateFormat
//            .format("HH:mm:ss", Date(randomTime)).toString()
//    }

}