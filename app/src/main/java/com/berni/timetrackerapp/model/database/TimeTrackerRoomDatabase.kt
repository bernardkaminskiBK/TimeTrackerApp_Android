package com.berni.timetrackerapp.model.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.berni.timetrackerapp.model.entities.Record

@Database(entities = [Record::class], version = 2)
abstract class TimeTrackerRoomDatabase : RoomDatabase() {

    abstract fun timeTrackerDao() : TimeTrackerDao

    companion object {
        @Volatile
        private var INSTANCE: TimeTrackerRoomDatabase? = null

        fun getDatabase(context: Context) : TimeTrackerRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TimeTrackerRoomDatabase::class.java,
                    "time_tracker_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }


}