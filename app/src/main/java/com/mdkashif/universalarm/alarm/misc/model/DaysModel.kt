package com.mdkashif.universalarm.alarm.misc.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(tableName = "Days", foreignKeys =[ForeignKey(entity = TimingsModel::class, parentColumns=["id"], childColumns = ["alarmId"], onUpdate = CASCADE, onDelete=CASCADE)])
data class DaysModel(
        @PrimaryKey(autoGenerate = true)
        var id: Int=0,

        @ColumnInfo(name = "alarmId")
        var fkAlarmId: Long=0,

        @ColumnInfo(name = "day")
        var repeatDay: String="")