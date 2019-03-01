package com.mdkashif.universalarm.utils.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mdkashif.universalarm.alarm.miscellaneous.model.DaysModel
import com.mdkashif.universalarm.alarm.miscellaneous.model.TimingsModel

@Dao
interface RoomAccessDao {
    @Insert
    fun addNewAlarm(timings: TimingsModel): Long

    @Insert
    fun addRepeatDays(days: DaysModel): Long

    // alarmtype isn't included here, because it shouldn't get updated
    @Query("Update Timings set hour=:hour, minute=:minute, note=:note, repeat=:repeat, status=:status where id=:alarmId")
    fun updateAlarm(hour: String, minute: String, note: String, repeat: Boolean, status: Boolean, alarmId: Long)

    @Query("Delete from Timings where id=:alarmId")
    fun deleteAlarm(alarmId: Long)

    @Query("SELECT COUNT(*) from Timings")
    fun countAlarms(): LiveData<Int>

    @Query("SELECT * FROM Timings")
    fun getAllAlarms(): MutableList<TimingsModel>

    @Query("SELECT * FROM Timings where type=:type")
    fun getSpecificAlarms(type: String): MutableList<TimingsModel>

    @Query("SELECT * FROM Timings where type!='Time'")
    fun getPrayerAlarms(): MutableList<TimingsModel>

    @Query("SELECT * FROM Days WHERE alarmId =:alarmId")
    fun getRepeatDays(alarmId: Long): MutableList<DaysModel>
}