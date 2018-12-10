package com.mdkashif.alarm.utils.db


import android.os.AsyncTask
import com.mdkashif.alarm.alarm.miscellaneous.DaysModel
import com.mdkashif.alarm.alarm.miscellaneous.TimingsModel


class RoomHelper {

    companion object {

        fun transactFetchAsync(db: AppDatabase): List<TimingsModel> {
            return db.userDao().getAllAlarms()
        }

        fun transactAmendAsync(db: AppDatabase, type: String, timingsModel: TimingsModel, days : List<String>?) {
            TransactDbAsync(db, timingsModel, days).execute(type)
        }

        fun transactCountAsync(db: AppDatabase): Int {
            return db.userDao().countAlarms()
        }

        private fun addAlarm(db: AppDatabase, timingsModel: TimingsModel, days : List<String>) {
            val alarmId = db.userDao().addNewAlarm(timingsModel)
            for(x in 0 until days.size) {
                val daysModel = DaysModel(0, alarmId, days[x])
                db.userDao().addRepeatDays(daysModel)
            }
        }

        private fun addAlarm(db: AppDatabase, timingsModel: TimingsModel) {
            db.userDao().addNewAlarm(timingsModel)
        }

        private fun deleteAlarm(db: AppDatabase, timingsModel: TimingsModel) {
            db.userDao().deleteAlarm(timingsModel)
        }

        private fun updateAlarm(db: AppDatabase, timingsModel: TimingsModel) {
            db.userDao().updateAlarm(timingsModel)
        }
    }

    private class TransactDbAsync internal constructor(internal var mDb: AppDatabase, internal var  timingsModel: TimingsModel, internal var  days: List<String>?) : AsyncTask<String, Any, Any>() {

        override fun doInBackground(vararg params: String): Void? {
            when (params[0]) {
                "add" -> {
                    if(timingsModel.repeat)
                        addAlarm(mDb, timingsModel, days!!)
                    else
                        addAlarm(mDb, timingsModel)
                }
                "update" -> {
                    updateAlarm(mDb, timingsModel)
                }
                "delete" -> {
                    deleteAlarm(mDb, timingsModel)
                }
            }
            return null
        }
    }
}
