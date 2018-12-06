package com.mdkashif.alarm.alarm.miscellaneous

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Timings")
data class TimingsModel(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,

        @ColumnInfo(name = "hour")
        var hour: String? = null,

        @ColumnInfo(name = "minute")
        var minute: String? = null,

        @ColumnInfo(name = "type")
        var type: String? = null,

        @ColumnInfo(name = "repeat")
        var repeat: Boolean = false)