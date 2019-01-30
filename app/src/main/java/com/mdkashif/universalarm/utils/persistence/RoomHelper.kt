package com.mdkashif.universalarm.utils.persistence


import android.os.AsyncTask
import com.mdkashif.universalarm.alarm.miscellaneous.db.TimingsModel


class RoomHelper {

    companion object {

        fun transactFetchAsync(db: AppDatabase): List<TimingsModel> {
            return getTimingsWithDays(db)
        }

        fun transactAmendAsync(db: AppDatabase, type: String, timingsModel: TimingsModel) {
            TransactDbAsync(db, timingsModel).execute(type)
        }

        fun transactCountAsync(db: AppDatabase): Int {
            return db.userDao().countAlarms()
        }

        private fun addTimingsWithoutRepeatDays(db: AppDatabase, timingsModel: TimingsModel) {
            db.userDao().addNewAlarm(timingsModel)
        }

        private fun addTimingsWithRepeatDays(db: AppDatabase, timings: TimingsModel) {
            for (i in timings.repeatDays!!.indices) {
                db.userDao().addRepeatDays(timings.repeatDays!![i])
            }
            db.userDao().addNewAlarm(timings)
        }

        private fun getTimingsWithDays(db: AppDatabase): List<TimingsModel> {
            val timings = db.userDao().getAllAlarms()
            for (i in timings.indices)
            {
                if(db.userDao().getRepeatDays(timings[i].id).isNotEmpty())
                {
                    timings[i].repeatDays=db.userDao().getRepeatDays(timings[i].id)
                }
            }
            return timings
        }

        private fun deleteAlarm(db: AppDatabase, timingsModel: TimingsModel) {
            db.userDao().deleteAlarm(timingsModel)
        }

        private fun updateAlarm(db: AppDatabase, timingsModel: TimingsModel) {
            db.userDao().updateAlarm(timingsModel)
        }
    }

    private class TransactDbAsync internal constructor(internal var mDb: AppDatabase, internal var  timingsModel: TimingsModel) : AsyncTask<String, Any, Any>() {

        override fun doInBackground(vararg params: String): Void? {
            when (params[0]) {
                "add" -> {
                    if(timingsModel.repeat)
                        addTimingsWithRepeatDays(mDb, timingsModel)
                    else
                        addTimingsWithoutRepeatDays(mDb, timingsModel)
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
