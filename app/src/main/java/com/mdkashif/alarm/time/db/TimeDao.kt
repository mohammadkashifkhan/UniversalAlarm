package com.mdkashif.alarm.time.db

import android.arch.persistence.room.*

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