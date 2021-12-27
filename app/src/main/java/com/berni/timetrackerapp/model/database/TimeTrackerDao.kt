package com.berni.timetrackerapp.model.database

import androidx.room.*
import com.berni.timetrackerapp.model.entities.Record
import com.berni.timetrackerapp.model.entities.RecordTotalTime
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeTrackerDao {

    fun getRecords(filterQuery: String, filterOrder: FilterOrder) =
        when (filterOrder) {
            FilterOrder.SHOW_ALL -> getRecordsList()
            FilterOrder.BY_NAME -> getFilteredRecordsListByName(filterQuery)
        }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: Record)

    @Update
    suspend fun updateRecord(record: Record)

    @Delete
    suspend fun deleteRecord(record: Record)

    @Query("SELECT DISTINCT name FROM TIME_TRACKER_TABLE ORDER BY name ASC")
    fun getAllRecordNames(): Flow<List<String>>

    @Query("SELECT * FROM TIME_TRACKER_TABLE ORDER BY id")
    fun getRecordsList(): Flow<List<Record>>

    @Query("SELECT * FROM TIME_TRACKER_TABLE WHERE name = :filterName")
    fun getFilteredRecordsListByName(filterName: String): Flow<List<Record>>

    @Query("SELECT name as name, SUM(time) as totalTime FROM TIME_TRACKER_TABLE GROUP BY name")
    fun getTotalTimeRecords(): Flow<List<RecordTotalTime>>

    @Query("DELETE FROM TIME_TRACKER_TABLE")
    suspend fun deleteAllRecords()

}