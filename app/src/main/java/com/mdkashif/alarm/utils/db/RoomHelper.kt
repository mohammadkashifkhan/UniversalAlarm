package com.mdkashif.alarm.utils.db


import android.os.AsyncTask
import com.mdkashif.alarm.alarm.miscellaneous.TimingsModel


class RoomHelper {

    companion object {

        fun transactAsync(db: AppDatabase, type: String, timingsModel: TimingsModel) {
            val task = TransactDbAsync(db, timingsModel)
            task.execute(type)
        }

        private fun addAlarm(db: AppDatabase, timingsModel: TimingsModel) {
            db.userDao().addNewAlarm(timingsModel)
        }

        private fun viewAllAlarms(db: AppDatabase): List<TimingsModel> {
            return db.userDao().getAllAlarms()
        }

        private fun countAlarms(db: AppDatabase): Int{
            return db.userDao().countAlarms()
        }

        private fun deleteAlarm(db: AppDatabase, timingsModel: TimingsModel) {
            db.userDao().deleteAlarm(timingsModel)
        }

        private fun updateAlarm(db: AppDatabase, timingsModel: TimingsModel) {
            db.userDao().updateAlarm(timingsModel)
        }
    }

    private class TransactDbAsync internal constructor(internal var mDb: AppDatabase, internal var  timingsModel: TimingsModel) : AsyncTask<String, Void, Void>() {

        override fun doInBackground(vararg params: String): Void? {
            when (params[0]) {
                "add" -> {
                    addAlarm(mDb, timingsModel)
                }
                "view" -> {
                    viewAllAlarms(mDb)
                }
                "update" -> {
                    updateAlarm(mDb, timingsModel)
                }
                "count" -> {
                    countAlarms(mDb)
                }
                "delete" -> {
                    deleteAlarm(mDb, timingsModel)
                }
            }
            return null
        }
    }
}
