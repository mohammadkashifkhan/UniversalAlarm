package com.mdkashif.universalarm.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mdkashif.universalarm.alarm.misc.model.DaysModel
import com.mdkashif.universalarm.alarm.misc.model.LocationsModel
import com.mdkashif.universalarm.alarm.misc.model.TimingsModel

@Database(entities = [TimingsModel::class, DaysModel::class, LocationsModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun accessDao(): RoomAccessDao

    companion object {
        private var mINSTANCE: AppDatabase? = null

        fun getAppDatabase(context: Context): AppDatabase {
            if (mINSTANCE == null) {
                mINSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "universal-alarm-db")
                        .build()
            }
            return mINSTANCE!!
        }
    }
}