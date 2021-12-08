package com.berni.timetrackerapp.model.database

import androidx.annotation.WorkerThread
import com.berni.timetrackerapp.model.database.viewmodel.FilterOrder
import com.berni.timetrackerapp.model.entities.Progress

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

    @WorkerThread
    suspend fun deleteAllProgressRecords() {
        timeTrackerDao.deleteAllRecords()
    }

    fun getProgressesList(filterQuery: String, filterOrder: FilterOrder) =
        timeTrackerDao.getProgresses(filterQuery, filterOrder)

    val allTimeTrackerProgressNames = timeTrackerDao.getAllTimeTrackerProgressNames()

}