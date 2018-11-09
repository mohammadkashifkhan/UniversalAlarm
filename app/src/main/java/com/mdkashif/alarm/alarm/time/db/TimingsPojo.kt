package com.mdkashif.alarm.alarm.time.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Timings")
data class TimingsPojo(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,

        @ColumnInfo(name = "time")
        var time: String? = null,

        @ColumnInfo(name = "type")
        var type: String? = null,

        @ColumnInfo(name = "repeat")
        var repeat: Boolean = false,

        @ColumnInfo(name = "days")
        var days: String? = null)