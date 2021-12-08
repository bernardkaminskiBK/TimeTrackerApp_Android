package com.berni.timetrackerapp.model.database

import androidx.room.*
import com.berni.timetrackerapp.model.database.viewmodel.FilterOrder
import com.berni.timetrackerapp.model.entities.Progress
import com.berni.timetrackerapp.ui.fragments.add.SHOW_ALL
import kotlinx.coroutines.flow.Flow

@Dao
interface TimeTrackerDao {

    fun getProgresses(filterQuery: String, filterOrder: FilterOrder) =
        when (filterOrder) {
            FilterOrder.SHOW_ALL -> getProgressList()
            FilterOrder.BY_NAME -> getFilteredProgressList(filterQuery)
        }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimeTrackerProgressDetails(progress: Progress)

    @Update
    suspend fun updateTimeTrackerProgressDetails(progress: Progress)

    @Delete
    suspend fun deleteTimeTrackerProgress(progress: Progress)

    @Query("SELECT DISTINCT name FROM TIME_TRACKER_TABLE")
    fun getAllTimeTrackerProgressNames(): Flow<List<String>>

    @Query("SELECT * FROM TIME_TRACKER_TABLE ORDER BY ID")
    fun getProgressList(): Flow<List<Progress>>

    @Query("SELECT * FROM TIME_TRACKER_TABLE WHERE name = :filterName")
    fun getFilteredProgressList(filterName: String): Flow<List<Progress>>

    @Query("DELETE FROM TIME_TRACKER_TABLE")
    suspend fun deleteAllRecords()

}