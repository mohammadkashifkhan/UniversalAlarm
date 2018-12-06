package com.mdkashif.alarm.alarm.miscellaneous

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "Days")
data class DaysModel(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,

        @ColumnInfo(name = "alarmId")
        @ForeignKey(entity = TimingsModel::class, parentColumns = ["id"], childColumns = ["foreignAlarmId"])
        var foreignAlarmId: String? = null,

        @ColumnInfo(name = "days")
        var day: String? = null)