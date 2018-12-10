package com.mdkashif.alarm.alarm.miscellaneous

import androidx.room.*

@Dao
interface DaoAccess {
    @Query("SELECT * FROM Timings")
    fun getAllAlarms(): List<TimingsModel>

    @Query("SELECT COUNT(*) from Timings")
    fun countAlarms(): Int

    @Insert
    fun addNewAlarm(vararg timings: TimingsModel) : Int

    @Insert
    fun addRepeatDays(vararg days: DaysModel)

    @Update
    fun updateAlarm(timings: TimingsModel)

    @Delete
    fun deleteAlarm(timings: TimingsModel)
}