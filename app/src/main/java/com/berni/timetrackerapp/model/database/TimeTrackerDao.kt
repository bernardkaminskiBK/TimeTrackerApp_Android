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

    @Query("DELETE FROM TIME_TRACKER_TABLE")
    suspend fun deleteAllRecords()

    @Query("SELECT DISTINCT name FROM TIME_TRACKER_TABLE ORDER BY name ASC")
    fun getAllRecordNames(): Flow<List<String>>

    @Query("SELECT DISTINCT strftime('%Y',datetime(date/1000, 'unixepoch')) FROM time_tracker_table ORDER BY date ASC")
    fun getAllYears(): Flow<List<String>>

    @Query("SELECT DISTINCT strftime('%Y',datetime(date/1000, 'unixepoch')) FROM time_tracker_table WHERE name = :name ORDER BY date ASC")
    fun getAllYearsByName(name: String): Flow<List<String>>

    @Query("SELECT * FROM time_tracker_table GROUP BY name")
    fun getEachRecord(): Flow<List<Record>>

    @Query("SELECT imgUrl FROM time_tracker_table WHERE name = :name AND imgUrl LIKE 'h%'")
    suspend fun getRecordByName(name: String): String

    @Query("UPDATE time_tracker_table SET imgUrl = :updateImgUrl  WHERE name = :name")
    suspend fun updateEveryRecordsImgUrlByName(updateImgUrl: String, name: String)

    @Query("SELECT * FROM TIME_TRACKER_TABLE ORDER BY date DESC")
    fun getRecordsList(): Flow<List<Record>>

    @Query("SELECT * FROM TIME_TRACKER_TABLE WHERE name LIKE '%' || :filterQuery || '%' OR strftime('%d.%m.%Y',datetime(date/1000, 'unixepoch')) LIKE '%' || :filterQuery || '%' ORDER BY date")
    fun getFilteredRecordsListByNameByDate(filterQuery: String): Flow<List<Record>>

    @Query("SELECT strftime('%Y',datetime(date/1000, 'unixepoch')) AS year, name AS name, SUM(time) AS totalTime FROM TIME_TRACKER_TABLE WHERE year = :year GROUP BY name")
    fun getTotalTimeRecordsByYear(year: String): Flow<List<StatisticsPieChartData>>

    @Query("SELECT strftime('%m/%Y',datetime(date/1000, 'unixepoch')) AS monthYear, name AS name, SUM(time) AS totalTime FROM time_tracker_table WHERE strftime('%m/%Y',datetime(date/1000, 'unixepoch')) = :monthYear GROUP BY name, monthYear")
    fun getAllRecordsByMonth(monthYear: String): Flow<List<StatisticsBarChartData>>

    @Query("SELECT strftime('%d/%m',datetime(date/1000, 'unixepoch')) AS day, name AS name,  SUM(time) AS time FROM time_tracker_table WHERE name = :name AND  strftime('%m/%Y',datetime(date/1000, 'unixepoch')) = :date  GROUP BY day, name ORDER BY date")
    fun getRecordsByNameByDateSumTimeWhereIsSameDate(name: String, date: String): Flow<List<RecordDateTime>>

    @Query("SELECT strftime('%d.%m',datetime(date/1000, 'unixepoch')) AS dayMonth, name AS name,  sum(time) AS time FROM time_tracker_table WHERE name = :name AND  strftime('%Y',datetime(date/1000, 'unixepoch')) = :year  GROUP BY dayMonth, name ORDER BY date DESC LIMIT 7")
    fun getLastSevenRecordByNameByYear(name: String, year: String): Flow<List<OverviewDetailLastWeek>>

    @Query("SELECT DISTINCT strftime('%m',datetime(date/1000, 'unixepoch')) FROM time_tracker_table WHERE name = :name AND strftime('%Y',datetime(date/1000, 'unixepoch')) = :year ORDER BY date ASC")
    fun getMonthsByNameByYear(name: String, year: String): Flow<List<String>>

    @Query("SELECT strftime('%m/%Y',datetime(date/1000, 'unixepoch')) AS year, name, SUM(time) AS totalTime FROM TIME_TRACKER_TABLE WHERE name = :name AND strftime('%m/%Y',datetime(date/1000, 'unixepoch')) = :date  GROUP BY year, name")
    fun getRecordTotalTimeByNameByDate(name: String, date: String): Flow<OverviewDetailTotalTime>

    @Query("SELECT count(DISTINCT strftime('%d/%m/%Y',datetime(date/1000, 'unixepoch'))) AS totalDays FROM time_tracker_table WHERE name = :name AND strftime('%m/%Y',datetime(date/1000, 'unixepoch')) = :date")
    fun getRecordTotalDaysByNameByDate(name: String, date: String): Flow<OverviewDetailTotalDays>

    @Query("SELECT strftime('%d.%m.%Y',datetime(date/1000, 'unixepoch')) AS mostRecentRecord FROM time_tracker_table WHERE name = :name ORDER BY date DESC LIMIT 1")
    fun getMostRecentRecordByName(name: String): Flow<OverviewDetailMostRecent>

    @Query("SELECT strftime('%m',datetime(date/1000, 'unixepoch')) AS month, strftime('%Y',datetime(date/1000, 'unixepoch')) AS year FROM time_tracker_table WHERE name = :name ORDER BY date DESC LIMIT 1")
    fun getLastAddedRecordMonthYearByName(name: String): Flow<OverviewDetailLastRecord>

    @Query("SELECT COUNT(id) FROM time_tracker_table")
    fun getCountOfRecords(): Flow<Int>

}