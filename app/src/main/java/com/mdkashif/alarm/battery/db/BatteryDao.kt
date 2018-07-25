package com.mdkashif.alarm.battery.db

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import com.mdkashif.alarm.time.db.TimingsPojo

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