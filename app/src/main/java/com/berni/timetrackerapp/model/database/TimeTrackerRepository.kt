package com.berni.timetrackerapp.model.database

import androidx.annotation.WorkerThread
import com.berni.timetrackerapp.model.entities.Progress
import kotlinx.coroutines.flow.Flow

class TimeTrackerRepository(private val timeTrackerDao: TimeTrackerDao) {

    @WorkerThread
    suspend fun insertTimerTrackerProgressData(progress: Progress) {
        timeTrackerDao.insertTimeTrackerProgressDetails(progress)
    }

    @WorkerThread
    suspend fun updateTimeTrackerProgressData(progress: Progress) {
        timeTrackerDao.updateTimeTrackerProgressDetails(progress)
    }

    @WorkerThread
    suspend fun deleteTimeTrackerProgressData(progress: Progress) {
        timeTrackerDao.deleteTimeTrackerProgress(progress)
    }

    fun getFilteredTimeTrackerProgresses(value: String) : Flow<List<Progress>> =
        timeTrackerDao.getFilteredProgressList(value)

    val allTimeTrackerProgressList: Flow<List<Progress>> = timeTrackerDao.getProgressList()

}