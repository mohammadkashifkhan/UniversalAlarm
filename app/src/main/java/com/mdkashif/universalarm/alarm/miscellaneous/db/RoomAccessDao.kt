package com.mdkashif.universalarm.alarm.miscellaneous.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RoomAccessDao {
    @Insert
    fun addNewAlarm(timings: TimingsModel) : Long

    @Insert
    fun addRepeatDays(days: DaysModel) : Long

    @Query("Update Timings set day= :day, month=:month, year=:year where id=:alarmId")
    fun updateAlarm(day: String, month: String, year: String, alarmId: Int)

    @Query("Update Timings set hour=:hour, minute=:minute where id=:alarmId")
    fun updateAlarm(hour: String, minute: String, alarmId: Int)

    @Query("Update Timings set repeat=:repeat where id=:alarmId")
    fun updateAlarm(repeat: Boolean, alarmId: Int)

    @Query("Delete from Timings where id=:alarmId")
    fun deleteAlarm(alarmId: Int)

    @Query("SELECT COUNT(*) from Timings")
    fun countAlarms(): Int

    @Query("SELECT * FROM Timings")
    fun getAllAlarms(): List<TimingsModel>

    @Query("SELECT * FROM Timings where type=:type")
    fun getAllSpecificAlarms(type: String): List<TimingsModel>

    @Query("SELECT * FROM Days WHERE alarmId =:alarmId")
    fun getRepeatDays(alarmId: Int): List<DaysModel>
}