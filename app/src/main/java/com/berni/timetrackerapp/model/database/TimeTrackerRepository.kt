package com.berni.timetrackerapp.model.database

import androidx.annotation.WorkerThread
import com.berni.timetrackerapp.model.entities.*
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

    fun getAllYearByName(name: String) : Flow<List<String>> =
        timeTrackerDao.getAllYearsByName(name)

    fun getRecordTotalTimeByNameByDate(name: String, date: String) : Flow<OverviewDetailTotalTime> =
        timeTrackerDao.getRecordTotalTimeByNameByDate(name, date)

    fun getRecordAvgTimeByNameByDate(name: String, date: String) : Flow<OverviewDetailAverageTime> =
        timeTrackerDao.getRecordAvgTimeByNameByDate(name, date)

    fun getRecordTotalDaysByNameByDate(name: String, date: String) : Flow<OverviewDetailTotalDays> =
        timeTrackerDao.getRecordTotalDaysByNameByDate(name, date)

    fun getMostRecentRecordByName(name: String): Flow<OverviewDetailMostRecent> =
        timeTrackerDao.getMostRecentRecordByName(name)

    fun getLastAddedRecordMonthYearByName(name: String): Flow<OverviewDetailLastRecord> =
        timeTrackerDao.getLastAddedRecordMonthYearByName(name)

    fun getRecordsList(filterQuery: String, filterOrder: FilterOrder) =
        timeTrackerDao.getRecords(filterQuery, filterOrder)

    fun getTotalTimeRecords(): Flow<List<RecordTotalTime>> =
        timeTrackerDao.getTotalTimeRecords()

    fun getAllDate(): Flow<List<String>> =
        timeTrackerDao.getAllDate()

    fun getEachRecord(): Flow<List<Record>> =
        timeTrackerDao.getEachRecord()

    fun getAllRecordsByMonth(month: String): Flow<List<RecordTotalTime>> =
        timeTrackerDao.getAllRecordsByMonth(month)

    fun getMonthsByNameByYear(name: String, year: String): Flow<List<String>> =
        timeTrackerDao.getMonthsByNameByYear(name, year)

    fun getAllRecordsDateByName(name: String) : Flow<List<String>> =
        timeTrackerDao.getAllRecordsDateByName(name)

    fun getLastSevenRecordsByNameByYear(name: String, year: String) : Flow<List<OverviewDetailLastWeek>> =
        timeTrackerDao.getLastSevenRecordByNameByYear(name, year)

    fun getRecordsByNameByDateSumTimeWhereIsSameDate(name: String, date: String) : Flow<List<RecordDateTime>> =
        timeTrackerDao.getRecordsByNameByDateSumTimeWhereIsSameDate(name, date)

    val getAllRecordsName = timeTrackerDao.getAllRecordNames()


}