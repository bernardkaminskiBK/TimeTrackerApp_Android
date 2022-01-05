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
            when ((1..4).shuffled().last()) {
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

        // January
//        val min = 1609519948000
//        val max = 1612111948000

        // February
//        val min = 1612198348000
//        val max = 1614531148000

        // March
//        val min = 1614617548000
//        val max = 1617209548000

        // April
//        val min = 1617295948000
//        val max = 1619801548000

        // May
//        val min = 1619887948000
//        val max = 1622479948000

        // June
//        val min = 1622566348000
//        val max = 1625071948000

        // July
//        val min = 1625158348000
//        val max = 1627750348000

        // August
//        val min = 1627836748000
//        val max = 1630428748000

        // September
//        val min = 1630515148000
//        val max = 1633020748000

        // October
//        val min = 1633107148000
//        val max = 1635699148000

        // November
//        val min = 1635799340000
//        val max = 1638304940000

//         December
//        val min = 1638391340000
//        val max = 1640983340000

        // dates between 01.01.2020 - 31.12.2020
//        val min = 1577897548000
//        val max = 1609433548000

//       // dates between 01.01.2021 - 31.12.2021
        val min = 1609514485000
        val max = 1640964085000

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