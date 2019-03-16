package com.mdkashif.universalarm.alarm.misc.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Locations")
data class LocationsModel(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,

        var address: String = "",

        var city: String = "",

        var latitude: Double = 0.0,

        var longitude: Double = 0.0,

        var note: String = "",

        @ColumnInfo(name = "pid")
        var pIntentRequestCode: Long = 0,

        var status: Boolean = false)