package com.berni.timetrackerapp.model.database

import com.berni.timetrackerapp.model.entities.Record

object RecordTestData {

    fun listOfRecords() : List<Record> {
        val list = mutableListOf<Record>()
        list.add(Record(id = 1, 5000000L, "Duolingo", "01:45:00"))
        list.add(Record(id = 2, 3265000L, "Duolingo", "00:25:32"))
        list.add(Record(id = 3, 3265000L, "Work", "00:25:32"))
        list.add(Record(id = 4, 250000L, "Work", "03:00:00"))
        list.add(Record(id = 5, 5000000L, "Cooking", "00:25:32"))
        list.add(Record(id = 6, 250000L, "Cooking", "01:45:00"))
        list.add(Record(id = 7, 3265000L, "Cooking", "03:00:00"))
        return list
    }

    fun listOfRecordsFilteredByName() : List<Record> {
        val list = mutableListOf<Record>()
        list.add(Record(id = 5, 5000000L, "Cooking", "00:25:32"))
        list.add(Record(id = 6, 250000L, "Cooking", "01:45:00"))
        list.add(Record(id = 7, 3265000L, "Cooking", "03:00:00"))
        return list
    }

}