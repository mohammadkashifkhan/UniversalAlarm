package com.mdkashif.alarm.alarm.miscellaneous

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Timings")
data class TimingsModel(
        @PrimaryKey(autoGenerate = true)
        var id: Int,

        @ColumnInfo(name = "hour")
        var hour: String,

        @ColumnInfo(name = "minute")
        var minute: String,

        @ColumnInfo(name = "type")
        var type: String,

        @ColumnInfo(name = "repeat")
        var repeat: Boolean = false)