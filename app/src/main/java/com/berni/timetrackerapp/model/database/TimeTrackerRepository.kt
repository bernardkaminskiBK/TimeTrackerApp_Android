package com.berni.timetrackerapp.model.database

import androidx.annotation.WorkerThread
import com.berni.timetrackerapp.model.entities.Record
import com.berni.timetrackerapp.model.entities.RecordDateTime
import com.berni.timetrackerapp.model.entities.RecordTotalTime
import kotlinx.coroutines.flow.Flow

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

    fun getTotalTimeRecords(): Flow<List<RecordTotalTime>> =
        timeTrackerDao.getTotalTimeRecords()

    fun getAllDate(): Flow<List<String>> =
        timeTrackerDao.getAllDate()

    fun getAllRecordsByMonth(month: String): Flow<List<RecordTotalTime>> =
        timeTrackerDao.getAllRecordsByMonth(month)

    fun getAllRecordsDateByName(name: String) : Flow<List<String>> =
        timeTrackerDao.getAllRecordsDateByName(name)

    fun getRecordsByNameAndDate(name: String, date: String) : Flow<List<RecordDateTime>> =
        timeTrackerDao.getRecordsByNameAndDate(name, date)

    val getAllRecordsName = timeTrackerDao.getAllRecordNames()

}