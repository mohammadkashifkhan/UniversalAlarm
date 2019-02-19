package com.mdkashif.universalarm.alarm.miscellaneous.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "Timings")
data class TimingsModel(
        @PrimaryKey(autoGenerate = true)
        var id: Int=0,

        var day: String="",

        var month: String="",

        var year: String="",

        var hour: String="",

        var minute: String="",

        @ColumnInfo(name = "type")
        var alarmType: String="",

        var repeat: Boolean = false,

        @Ignore
        var repeatDays: List<DaysModel>?=null)