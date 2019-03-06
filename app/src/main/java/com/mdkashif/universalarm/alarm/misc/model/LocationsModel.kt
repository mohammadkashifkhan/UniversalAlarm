package com.mdkashif.universalarm.alarm.misc.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Locations")
data class LocationsModel(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,

        var address: String = "",

        var city: String = "",

        var latitude: Long = 0,

        var longitude: Long = 0,

        var status: Boolean = false)