package com.berni.timetrackerapp.utils

import com.berni.timetrackerapp.model.entities.Record
import kotlin.collections.ArrayList

object TestData {

    fun randomTestDataToDB(numberOfItems: Int): ArrayList<Record> {
       val testDataset = ArrayList<Record>()

        for(i in 1..numberOfItems) {
            when((1..4).shuffled().last()) {
                1 -> {
                    testDataset.add(Record(0, System.currentTimeMillis(), "Duolingo", getRandomRecordTimeInSeconds()))
                }
                2 -> {
                    testDataset.add(Record(0, System.currentTimeMillis(), "Programing", getRandomRecordTimeInSeconds()))
                }
                3 -> {
                    testDataset.add(Record(0, System.currentTimeMillis(), "Work", getRandomRecordTimeInSeconds()))
                }
                4 -> {
                    testDataset.add(Record(0, System.currentTimeMillis(), "Cooking", getRandomRecordTimeInSeconds()))
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