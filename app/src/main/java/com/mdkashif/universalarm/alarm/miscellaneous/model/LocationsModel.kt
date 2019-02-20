package com.mdkashif.universalarm.alarm.miscellaneous.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Locations")
data class LocationsModel(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,

        @ColumnInfo(name = "address")
        var address: String? = null,

        @ColumnInfo(name = "latitude")
        var latitude: Long? = null,

        @ColumnInfo(name = "longitude")
        var longitude: Long? = null)