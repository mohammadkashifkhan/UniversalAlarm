package com.mdkashif.alarm.alarm.miscellaneous.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "Days", foreignKeys =[ForeignKey(entity = TimingsModel::class, parentColumns=["id"], childColumns = ["fkAlarmId"], onUpdate = CASCADE, onDelete=CASCADE)])
data class DaysModel(
        @PrimaryKey(autoGenerate = true)
        var id: Int,

        @ColumnInfo(name = "fkAlarmId")
        var alarmId: Int,

        @ColumnInfo(name = "days")
        var day: String)