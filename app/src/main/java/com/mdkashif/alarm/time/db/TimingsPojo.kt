package com.mdkashif.alarm.time.db

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

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