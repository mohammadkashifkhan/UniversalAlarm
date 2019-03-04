package com.mdkashif.universalarm.alarm.miscellaneous.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Days")
data class DaysModel(
        @PrimaryKey(autoGenerate = true)
        var id: Int=0,

        @ColumnInfo(name = "alarmId")
        var fkAlarmId: Long=0,

        @ColumnInfo(name = "day")
        var repeatDay: String="")