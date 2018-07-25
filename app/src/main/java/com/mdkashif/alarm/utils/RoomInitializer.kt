package com.mdkashif.alarm.utils


import android.R
import android.os.AsyncTask
import android.util.Log

import com.mdkashif.alarm.time.db.TimingsPojo

class RoomInitializer {
//    val user  = ArrayList<TimingsPojo>()
    private val TAG = RoomInitializer::class.java.name

    companion object {

        fun transactAsync(db: AppDatabase, type: String, timingsPojo: TimingsPojo) {
            val task = TransactDbAsync(db,type)
            task.execute()
        }

        private fun addAlarm(db: AppDatabase, timingsPojo: TimingsPojo): TimingsPojo {
            db.userDao().insertNewAlarm(timingsPojo)
            return timingsPojo
        }

        private fun countAlarms(db: AppDatabase): Int{
            return db.userDao().countAlarms()
        }

        private fun deleteAlarm(db: AppDatabase, timingsPojo: TimingsPojo): TimingsPojo {
            db.userDao().insertNewAlarm(timingsPojo)
            return timingsPojo
        }

        private fun updateAlarm(db: AppDatabase, timingsPojo: TimingsPojo): TimingsPojo {
            db.userDao().insertNewAlarm(timingsPojo)
            return timingsPojo
        }

        private fun populateWithTestData(db: AppDatabase) {
//        user.add(TimingsPojo(0,"Kashif","K",25))
//        user.add(TimingsPojo(0,"rakhshi","K",21))
//        user.add(TimingsPojo(0,"saira","K",50))
            val user  = TimingsPojo(0,"Saira","K",true)
            //addUser(db, user)

            val userList = db.userDao().getAllAlarms()
            Log.d("check123134134134", "Rows Count: " + userList.size)
        }
    }



    private class TransactDbAsync internal constructor(mDb: AppDatabase, type: String) : AsyncTask<String, Void, Void>() {

        override fun doInBackground(vararg params: String): Void? {
            when (params[0]) {
                "add" -> {

                }
                "update" -> {

                }
                "count" -> {

                }
                "delete" -> {

                }
            }
            return null
        }

    }
}
