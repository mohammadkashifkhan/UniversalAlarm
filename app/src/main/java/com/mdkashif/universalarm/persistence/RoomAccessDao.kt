package com.mdkashif.universalarm.persistence

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRepeatDays(day: DaysModel): Long

    @Insert
    fun addNewLocationAlarm(location: LocationsModel): Long

    // alarmtype isn't included here, because it shouldn't get updated
    @Query("Update Timings set hour=:hour, minute=:minute, note=:note, repeat=:repeat, status = CASE WHEN :autoUpdate THEN status ELSE :status END where id=:alarmId")
    fun updateTimeAlarm(hour: String, minute: String, note: String, repeat: Boolean, status: Boolean = false, alarmId: Long, autoUpdate: Boolean)

    @Query("Update Locations set address=:address, city=:city, latitude=:latitude, longitude=:longitude, status=:status where id=:alarmId")
    fun updateLocationAlarm(address: String, city: String, latitude: Double, longitude: Double, status: Boolean, alarmId: Long)

    @Query("Delete from Timings where id=:alarmId")
    fun deleteTimeAlarm(alarmId: Long)

    @Query("Delete from Locations where id=:alarmId")
    fun deleteLocationAlarm(alarmId: Long)

    @Query("SELECT * FROM Timings order by hour asc")
    fun getAllTimeAlarms(): MutableList<TimingsModel>

    @Query("SELECT * FROM Timings where status='1' order by hour asc")
    fun getOnlyLiveTimeAlarms(): MutableList<TimingsModel>

    @Query("SELECT * FROM Days WHERE alarmId =:alarmId")
    fun getRepeatDays(alarmId: Long): MutableList<DaysModel>

    @Query("Delete FROM Days WHERE alarmId =:alarmId")
    fun deleteRepeatDays(alarmId: Long)

    @Query("SELECT * FROM Timings where type=:type")
    fun getSpecificTimeAlarms(type: String): MutableList<TimingsModel>

    @Query("SELECT * FROM Timings where type!='Time'")
    fun getPrayerAlarms(): MutableList<TimingsModel>

    @Query("SELECT * FROM Locations")
    fun getLocationAlarms(): MutableList<LocationsModel>

    @Query("SELECT * FROM Locations where status='1'")
    fun getOnlyLiveLocationAlarms(): MutableList<LocationsModel>
}