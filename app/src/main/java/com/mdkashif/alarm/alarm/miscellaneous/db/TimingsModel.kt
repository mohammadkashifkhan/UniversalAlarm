package com.mdkashif.alarm.alarm.miscellaneous.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "Timings")
data class TimingsModel(
        @PrimaryKey(autoGenerate = true)
        @ColumnInfo(name = "id")
        var id: Int=0,

        @ColumnInfo(name = "day")
        var day: String="",

        @ColumnInfo(name = "month")
        var month: String="",

        @ColumnInfo(name = "year")
        var year: String="",

        @ColumnInfo(name = "hour")
        var hour: String="",

        @ColumnInfo(name = "minute")
        var minute: String="",

        @ColumnInfo(name = "type")
        var type: String="",

        @ColumnInfo(name = "repeat")
        var repeat: Boolean = false,

        @Ignore
        var repeatDays: List<DaysModel>?=null)