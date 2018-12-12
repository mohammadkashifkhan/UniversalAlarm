package com.mdkashif.alarm.alarm.miscellaneous.db

import androidx.room.*

@Dao
interface DaoAccess {
    @Insert
    fun addNewAlarm(vararg timings: TimingsModel)

    @Insert
    fun addRepeatDays(vararg days: DaysModel)

    @Update
    fun updateAlarm(timings: TimingsModel)

    @Delete
    fun deleteAlarm(timings: TimingsModel)

    @Query("SELECT COUNT(*) from Timings")
    fun countAlarms(): Int

    @Query("SELECT * FROM Timings")
    fun getAllAlarms(): List<TimingsModel>

    @Query("SELECT * FROM Days WHERE fkAlarmId =:alarmId")
    fun getRepeatDays(alarmId: Int): List<DaysModel>
}