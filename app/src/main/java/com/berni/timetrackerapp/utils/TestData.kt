package com.berni.timetrackerapp.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.berni.timetrackerapp.model.entities.Record

@RequiresApi(Build.VERSION_CODES.O)
object TestData {

    private var i = 1

    fun randomTestDataToDB(numberOfItems: Int): ArrayList<Record> {
        val testDataset = ArrayList<Record>()

        for (i in 1..numberOfItems) {
            when ((1..11).shuffled().last()) {
                1 -> {
                    testDataset.add(
                        Record(
                            0,
                            randomDateGenerator(),
                            "Duolingo",
                            getRandomRecordTimeInSeconds(),
                            " "
                        )
                    )
                }
                2 -> {
                    testDataset.add(
                        Record(
                            0,
                            randomDateGenerator(),
                            "Programing",
                            getRandomRecordTimeInSeconds(),
                            " "
                        )
                    )
                }
                3 -> {
                    testDataset.add(
                        Record(
                            0,
                            randomDateGenerator(),
                            "Work",
                            getRandomRecordTimeInSeconds(),
                            " "
                        )
                    )
                }
                4 -> {
                    testDataset.add(
                        Record(
                            0,
                            randomDateGenerator(),
                            "Cooking",
                            getRandomRecordTimeInSeconds(),
                            " "
                        )
                    )
                }
                5 -> {
                    testDataset.add(
                        Record(
                            0,
                            randomDateGenerator(),
                            "Yoga",
                            getRandomRecordTimeInSeconds(),
                            " "
                        )
                    )
                }
                6 -> {
                    testDataset.add(
                        Record(
                            0,
                            randomDateGenerator(),
                            "Training",
                            getRandomRecordTimeInSeconds(),
                            " "
                        )
                    )
                }
               7 -> {
                    testDataset.add(
                        Record(
                            0,
                            randomDateGenerator(),
                            "Painting",
                            getRandomRecordTimeInSeconds(),
                            " "
                        )
                    )
                }
                8 -> {
                    testDataset.add(
                        Record(
                            0,
                            randomDateGenerator(),
                            "Reading",
                            getRandomRecordTimeInSeconds(),
                            " "
                        )
                    )
                }
                9 -> {
                    testDataset.add(
                        Record(
                            0,
                            randomDateGenerator(),
                            "Soccer",
                            getRandomRecordTimeInSeconds(),
                            " "
                        )
                    )
                }
                10 -> {
                    testDataset.add(
                        Record(
                            0,
                            randomDateGenerator(),
                            "Sleeping",
                            getRandomRecordTimeInSeconds(),
                            " "
                        )
                    )
                }
                11 -> {
                    testDataset.add(
                        Record(
                            0,
                            randomDateGenerator(),
                            "Boxing",
                            getRandomRecordTimeInSeconds(),
                            " "
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
//        val min = 1577897548000
//        val max = 1609433548000

        // dates between 01.01.2021 - 31.12.2021
//        val min = 1609514485000
//        val max = 1640964085000


        // dates between 01.01.2022 - 10.02.2022
        val min = 1641043794000
        val max = 1644499794000

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