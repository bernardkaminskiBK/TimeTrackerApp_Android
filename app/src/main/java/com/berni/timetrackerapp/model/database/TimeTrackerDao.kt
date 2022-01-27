package com.berni.timetrackerapp.model.database

import androidx.room.*
import com.berni.timetrackerapp.model.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeTrackerDao {

    fun getRecords(filterQuery: String, filterOrder: FilterOrder) =
        when (filterOrder) {
            FilterOrder.SHOW_ALL -> getRecordsList()
            FilterOrder.BY_NAME -> getFilteredRecordsListByNameByDate(filterQuery)
        }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: Record)

    @Update
    suspend fun updateRecord(record: Record)

    @Delete
    suspend fun deleteRecord(record: Record)

    @Query("SELECT DISTINCT name FROM TIME_TRACKER_TABLE ORDER BY name ASC")
    fun getAllRecordNames(): Flow<List<String>>

    @Query("SELECT DISTINCT strftime('%Y',datetime(date/1000, 'unixepoch')) FROM time_tracker_table WHERE name = :name ORDER BY date ASC")
    fun getAllYearsByName(name: String): Flow<List<String>>

    @Query("SELECT * FROM time_tracker_table GROUP BY name")
    fun getEachRecord(): Flow<List<Record>>

    @Query("SELECT * FROM TIME_TRACKER_TABLE ORDER BY date")
    fun getRecordsList(): Flow<List<Record>>

    @Query("SELECT * FROM TIME_TRACKER_TABLE WHERE name LIKE '%' || :filterQuery || '%' OR strftime('%d.%m.%Y',datetime(date/1000, 'unixepoch')) LIKE '%' || :filterQuery || '%' ORDER BY date")
    fun getFilteredRecordsListByNameByDate(filterQuery: String): Flow<List<Record>>

    @Query("SELECT name AS name, SUM(time) AS totalTime FROM TIME_TRACKER_TABLE GROUP BY name")
    fun getTotalTimeRecords(): Flow<List<RecordTotalTime>>

    @Query("SELECT DISTINCT strftime('%m/%Y',datetime(date/1000, 'unixepoch')) FROM time_tracker_table ORDER BY date")
    fun getAllDate(): Flow<List<String>>

    @Query("SELECT name AS name, SUM(time) AS totalTime FROM time_tracker_table WHERE strftime('%m/%Y',datetime(date/1000, 'unixepoch')) = :month GROUP BY name")
    fun getAllRecordsByMonth(month: String): Flow<List<RecordTotalTime>>

    @Query("SELECT DISTINCT strftime('%m/%Y',datetime(date/1000, 'unixepoch')) FROM time_tracker_table WHERE name = :name ORDER BY date")
    fun getAllRecordsDateByName(name: String): Flow<List<String>>

    @Query("SELECT strftime('%d/%m',datetime(date/1000, 'unixepoch')) AS day, name AS name,  SUM(time) AS time FROM time_tracker_table WHERE name = :name AND  strftime('%m/%Y',datetime(date/1000, 'unixepoch')) = :date  GROUP BY day, name ORDER BY date")
    fun getRecordsByNameByDateSumTimeWhereIsSameDate(
        name: String,
        date: String
    ): Flow<List<RecordDateTime>>

    @Query("SELECT strftime('%d.%m',datetime(date/1000, 'unixepoch')) AS dayMonth, name AS name,  sum(time) AS time FROM time_tracker_table WHERE name = :name AND  strftime('%Y',datetime(date/1000, 'unixepoch')) = :year  GROUP BY dayMonth, name ORDER BY date DESC LIMIT 7")
    fun getLastSevenRecordByNameByYear(name: String, year: String): Flow<List<OverviewDetailLastWeek>>

    @Query("SELECT DISTINCT strftime('%m',datetime(date/1000, 'unixepoch')) FROM time_tracker_table WHERE name = :name AND strftime('%Y',datetime(date/1000, 'unixepoch')) = :year ORDER BY date ASC")
    fun getMonthsByNameByYear(name: String, year: String): Flow<List<String>>

    @Query("SELECT strftime('%m/%Y',datetime(date/1000, 'unixepoch')) AS year, name, SUM(time) AS totalTime FROM TIME_TRACKER_TABLE WHERE name = :name AND strftime('%m/%Y',datetime(date/1000, 'unixepoch')) = :date  GROUP BY year, name")
    fun getRecordTotalTimeByNameByDate(name: String, date: String): Flow<OverviewDetailTotalTime>

    @Query("SELECT AVG(time) AS averageTime FROM time_tracker_table WHERE name = :name AND strftime('%m/%Y',datetime(date/1000, 'unixepoch')) = :date")
    fun getRecordAvgTimeByNameByDate(name: String, date: String): Flow<OverviewDetailAverageTime>

    @Query("SELECT count(DISTINCT strftime('%d/%m/%Y',datetime(date/1000, 'unixepoch'))) AS totalDays FROM time_tracker_table WHERE name = :name AND strftime('%m/%Y',datetime(date/1000, 'unixepoch')) = :date")
    fun getRecordTotalDaysByNameByDate(name: String, date: String): Flow<OverviewDetailTotalDays>

    @Query("SELECT strftime('%d.%m.%Y',datetime(date/1000, 'unixepoch')) AS mostRecentRecord FROM time_tracker_table WHERE name = :name ORDER BY date DESC LIMIT 1")
    fun getMostRecentRecordByName(name: String): Flow<OverviewDetailMostRecent>

    @Query("SELECT strftime('%m',datetime(date/1000, 'unixepoch')) AS month, strftime('%Y',datetime(date/1000, 'unixepoch')) AS year FROM time_tracker_table WHERE name = :name ORDER BY date DESC LIMIT 1")
    fun getLastAddedRecordMonthYearByName(name: String): Flow<OverviewDetailLastRecord>

    @Query("DELETE FROM TIME_TRACKER_TABLE")
    suspend fun deleteAllRecords()

}