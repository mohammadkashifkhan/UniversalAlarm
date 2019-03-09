package com.mdkashif.universalarm.persistence

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mdkashif.universalarm.alarm.misc.model.DaysModel
import com.mdkashif.universalarm.alarm.misc.model.LocationsModel
import com.mdkashif.universalarm.alarm.misc.model.TimingsModel

@Dao
interface RoomAccessDao {
    @Insert
    fun addNewTimeAlarm(timing: TimingsModel): Long

    @Insert (onConflict = OnConflictStrategy.REPLACE)
    fun addRepeatDays(day: DaysModel): Long

    @Insert
    fun addNewLocationAlarm(location: LocationsModel): Long

    // alarmtype isn't included here, because it shouldn't get updated
    @Query("Update Timings set hour=:hour, minute=:minute, note=:note, repeat=:repeat, status = CASE WHEN :autoUpdate THEN status ELSE :status END where id=:alarmId")
    fun updateAlarm(hour: String, minute: String, note: String, repeat: Boolean, status: Boolean= false, alarmId: Long, autoUpdate: Boolean)

    @Query("Delete from Timings where id=:alarmId")
    fun deleteAlarm(alarmId: Long)

    @Query("SELECT COUNT(*) from Timings")
    fun countAlarms(): LiveData<Int>

    @Query("SELECT * FROM Timings")
    fun getAllAlarms(): MutableList<TimingsModel>

    @Query("SELECT * FROM Days WHERE alarmId =:alarmId")
    fun getRepeatDays(alarmId: Long): MutableList<DaysModel>

    @Query("SELECT * FROM Timings where type=:type")
    fun getSpecificAlarms(type: String): MutableList<TimingsModel>

    @Query("SELECT * FROM Timings where type!='Time'")
    fun getPrayerAlarms(): MutableList<TimingsModel>

    @Query("SELECT * FROM Locations")
    fun getLocationAlarms(): MutableList<LocationsModel>
}