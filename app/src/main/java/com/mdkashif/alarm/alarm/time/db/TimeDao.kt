package com.mdkashif.alarm.alarm.time.db

import androidx.room.*

@Dao
interface TimeDao {
    @Query("SELECT * FROM Timings")
    fun getAllAlarms(): List<TimingsPojo>

    @Query("SELECT COUNT(*) from Timings")
    fun countAlarms(): Int

    @Insert
    fun insertNewAlarm(vararg timings: TimingsPojo)

    @Update
    fun updateAlarm(timings: TimingsPojo)

    @Delete
    fun deleteAlarm(timings: TimingsPojo)
}