package com.berni.timetrackerapp.model.database

import androidx.room.*
import com.berni.timetrackerapp.model.entities.Progress
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeTrackerDao {

    @Insert
    suspend fun insertTimeTrackerProgressDetails(progress: Progress)

    @Update
    suspend fun updateTimeTrackerProgressDetails(progress: Progress)

    @Delete
    suspend fun deleteTimeTrackerProgress(progress: Progress)

    @Query("SELECT name FROM TIME_TRACKER_TABLE")
    fun getAllTimeTrackerProgressNames(): Flow<List<String>>

    @Query("SELECT * FROM TIME_TRACKER_TABLE ORDER BY ID")
    fun getProgressList(): Flow<List<Progress>>

    @Query("SELECT * FROM TIME_TRACKER_TABLE WHERE name = :filterName")
    fun getFilteredProgressList(filterName: String): Flow<List<Progress>>

    @Query("DELETE FROM TIME_TRACKER_TABLE")
    suspend fun deleteAllRecords()

}