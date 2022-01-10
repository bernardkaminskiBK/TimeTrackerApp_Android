package com.berni.timetrackerapp.model.database

import androidx.room.*
import com.berni.timetrackerapp.model.entities.Record
import com.berni.timetrackerapp.model.entities.RecordDateTime
import com.berni.timetrackerapp.model.entities.RecordTotalTime
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

    @Query("SELECT * FROM TIME_TRACKER_TABLE ORDER BY date")
    fun getRecordsList(): Flow<List<Record>>

    @Query("SELECT * FROM TIME_TRACKER_TABLE WHERE name LIKE '%' || :filterQuery || '%' OR strftime('%d.%m.%Y',datetime(date/1000, 'unixepoch')) LIKE '%' || :filterQuery || '%' order by date")
    fun getFilteredRecordsListByNameByDate(filterQuery: String): Flow<List<Record>>

    @Query("SELECT name as name, SUM(time) as totalTime FROM TIME_TRACKER_TABLE GROUP BY name")
    fun getTotalTimeRecords(): Flow<List<RecordTotalTime>>

    @Query("SELECT DISTINCT strftime('%m/%Y',datetime(date/1000, 'unixepoch')) FROM time_tracker_table order by date")
    fun getAllDate(): Flow<List<String>>

    @Query("SELECT name as name, SUM(time) as totalTime FROM time_tracker_table WHERE strftime('%m/%Y',datetime(date/1000, 'unixepoch')) = :month GROUP BY name")
    fun getAllRecordsByMonth(month: String): Flow<List<RecordTotalTime>>

    @Query("SELECT DISTINCT strftime('%m/%Y',datetime(date/1000, 'unixepoch')) FROM time_tracker_table WHERE name = :name  order by date")
    fun getAllRecordsDateByName(name: String) : Flow<List<String>>

    @Query("SELECT strftime('%d/%m',datetime(date/1000, 'unixepoch')) as date, time as time FROM time_tracker_table WHERE name = :name AND  strftime('%m/%Y',datetime(date/1000, 'unixepoch')) = :date order by date")
    fun getRecordsByNameAndDate(name: String, date: String): Flow<List<RecordDateTime>>

    @Query("DELETE FROM TIME_TRACKER_TABLE")
    suspend fun deleteAllRecords()

}