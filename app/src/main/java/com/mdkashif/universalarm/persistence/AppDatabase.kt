package com.mdkashif.universalarm.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mdkashif.universalarm.alarm.misc.model.DaysModel
import com.mdkashif.universalarm.alarm.misc.model.LocationsModel
import com.mdkashif.universalarm.alarm.misc.model.TimingsModel

@Database(entities = [TimingsModel::class, DaysModel::class, LocationsModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accessDao(): RoomAccessDao
}
