package com.mdkashif.alarm.alarm.battery.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.mdkashif.alarm.alarm.time.db.TimingsPojo

@Dao
interface BatteryDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<TimingsPojo>

    @Query("SELECT * FROM user where first_name LIKE  :firstName AND last_name LIKE :lastName")
    fun findByName(firstName: String, lastName: String): TimingsPojo

    @Query("SELECT COUNT(*) from user")
    fun countUsers(): Int

    @Insert
    fun insertAll(vararg timingsPojos: TimingsPojo)

    @Delete
    fun delete(timingsPojo: TimingsPojo)
}