package com.berni.timetrackerapp.model.entities

import android.os.Parcelable
import android.text.format.DateFormat
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
@Entity(tableName = "time_tracker_table")
data class Record(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo val date: Long,
    @ColumnInfo val name: String,
    @ColumnInfo val time: Long,
    @ColumnInfo val imgUrl: String
    ) : Parcelable {
        val createdDateFormatted: String
            get() = DateFormat.format("dd.MM.yyyy HH:mm:ss", Date(date)).toString()
    }

