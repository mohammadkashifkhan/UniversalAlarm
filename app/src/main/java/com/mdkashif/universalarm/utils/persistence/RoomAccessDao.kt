package com.mdkashif.universalarm.utils.persistence

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mdkashif.universalarm.alarm.miscellaneous.model.DaysModel
import com.mdkashif.universalarm.alarm.miscellaneous.model.TimingsModel

@Dao
interface RoomAccessDao {
    @Insert
    fun addNewAlarm(timings: TimingsModel) : Long

    @Insert
    fun addRepeatDays(days: DaysModel) : Long

    @Query("Update Timings set day= :day, month=:month, year=:year where id=:alarmId")
    fun updateAlarm(day: String, month: String, year: String, alarmId: Long)

    @Query("Update Timings set hour=:hour, minute=:minute where id=:alarmId")
    fun updateAlarm(hour: String, minute: String, alarmId: Long)

    @Query("Update Timings set repeat=:repeat where id=:alarmId")
    fun updateAlarm(repeat: Boolean, alarmId: Long)

    @Query("Delete from Timings where id=:alarmId")
    fun deleteAlarm(alarmId: Long)

    @Query("SELECT COUNT(*) from Timings")
    fun countAlarms(): Int

    @Query("SELECT * FROM Timings")
    fun getAllAlarms(): List<TimingsModel>

    @Query("SELECT * FROM Timings where type=:type")
    fun getSpecificAlarms(type: String): List<TimingsModel>

    @Query("SELECT * FROM Days WHERE alarmId =:alarmId")
    fun getRepeatDays(alarmId: Long): List<DaysModel>
}