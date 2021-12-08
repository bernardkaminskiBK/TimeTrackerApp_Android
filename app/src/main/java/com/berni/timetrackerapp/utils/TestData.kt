package com.berni.timetrackerapp.utils

import com.berni.timetrackerapp.model.entities.Progress
import java.util.*
import kotlin.collections.ArrayList

object TestData {

    fun randomTestDataToDB(numberOfItems: Int): ArrayList<Progress> {
       val testDataset = ArrayList<Progress>()

        for(i in 1..numberOfItems) {
            when((1..4).shuffled().last()) {
                1 -> {
                    testDataset.add(Progress(0, System.currentTimeMillis(), "Duolingo", getRandomProgressTime()))
                }
                2 -> {
                    testDataset.add(Progress(0, System.currentTimeMillis(), "Programing", getRandomProgressTime()))
                }
                3 -> {
                    testDataset.add(Progress(0, System.currentTimeMillis(), "Work", getRandomProgressTime()))
                }
                4 -> {
                    testDataset.add(Progress(0, System.currentTimeMillis(), "Cooking", getRandomProgressTime()))
                }
            }
        }

       return testDataset
    }

    private fun getRandomProgressTime(): String {
        val time = 3_600_000L * 5
        val randomTime = time / (1..10).shuffled().last()
        return android.text.format.DateFormat
            .format("HH:mm:ss", Date(randomTime)).toString()
    }

}