package com.mdkashif.universalarm.alarm.misc.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "Timings")
data class TimingsModel(
        @PrimaryKey(autoGenerate = true)
        var id: Long=0,

        var hour: String="",

        var minute: String="",

        var note: String="",

        @ColumnInfo(name = "type")
        var alarmType: String="",

        var repeat: Boolean = false,

        var status: Boolean = false,

        @Ignore
        var repeatDays: List<DaysModel>?=null,

        @ColumnInfo(name = "pid")
        var pIntentRequestCode: Long=0)