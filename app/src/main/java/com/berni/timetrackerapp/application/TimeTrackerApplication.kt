package com.berni.timetrackerapp.application

import android.app.Application
import com.berni.timetrackerapp.model.database.TimeTrackerRepository
import com.berni.timetrackerapp.model.database.TimeTrackerRoomDatabase

class TimeTrackerApplication : Application() {

    private val database by lazy {
        TimeTrackerRoomDatabase.getDatabase((this@TimeTrackerApplication))
    }

    val repository by lazy { TimeTrackerRepository(database.timeTrackerDao()) }
}