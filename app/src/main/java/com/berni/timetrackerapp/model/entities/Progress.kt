package com.berni.timetrackerapp.model.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "time_tracker_table")
data class Progress(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val date: Long,
    @ColumnInfo val name: String,
    @ColumnInfo val time: String
    ) : Parcelable
