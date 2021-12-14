package com.berni.timetrackerapp.model.database

import androidx.annotation.WorkerThread
import com.berni.timetrackerapp.model.entities.Record

class TimeTrackerRepository(private val timeTrackerDao: TimeTrackerDao) {

    @WorkerThread
    suspend fun insertRecord(record: Record) {
        timeTrackerDao.insertRecord(record)
    }

    @WorkerThread
    suspend fun updateRecord(record: Record) {
        timeTrackerDao.updateRecord(record)
    }

    @WorkerThread
    suspend fun deleteRecord(record: Record) {
        timeTrackerDao.deleteRecord(record)
    }

    @WorkerThread
    suspend fun deleteAllRecords() {
        timeTrackerDao.deleteAllRecords()
    }

    fun getRecordsList(filterQuery: String, filterOrder: FilterOrder) =
        timeTrackerDao.getRecords(filterQuery, filterOrder)

    val getAllRecordsName = timeTrackerDao.getAllRecordNames()

}