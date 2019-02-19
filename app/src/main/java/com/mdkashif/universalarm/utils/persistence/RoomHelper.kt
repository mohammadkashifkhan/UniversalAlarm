package com.mdkashif.universalarm.utils.persistence


import android.os.AsyncTask
import android.util.Log
import com.mdkashif.universalarm.alarm.miscellaneous.AlarmOps
import com.mdkashif.universalarm.alarm.miscellaneous.db.TimingsModel


class RoomHelper {

    companion object {

        fun transactFetchAsync(db: AppDatabase): List<TimingsModel> {
            return getTimingsWithDays(db)
        }

        fun transactAmendAsync(db: AppDatabase, taskType: String, timingsModel: TimingsModel, id: Int = 0) { //id=0 means we are just inserting
            TransactDbAsync(db, timingsModel, id).execute(taskType)
        }

        fun transactCountAsync(db: AppDatabase): Int {
            return db.accessDao().countAlarms()
        }

        private fun addTimingsWithoutRepeatDays(db: AppDatabase, timingsModel: TimingsModel) {
            val alarmId = db.accessDao().addNewAlarm(timingsModel) as Any
            Log.d("check23132131321", ""+alarmId) // TODO: get alarmId to insert as foreignkey
        }

        private fun addTimingsWithRepeatDays(db: AppDatabase, timingsModel: TimingsModel) {
            db.accessDao().addNewAlarm(timingsModel)
            // TODO: insert foreignkey here
            for (i in timingsModel.repeatDays!!.indices) {
                db.accessDao().addRepeatDays(timingsModel.repeatDays!![i])
            }
        }

        private fun getTimingsWithDays(db: AppDatabase): List<TimingsModel> {
            val timings = db.accessDao().getAllAlarms()
            for (i in timings.indices) {
                if (db.accessDao().getRepeatDays(timings[i].id).isNotEmpty()) {
                    timings[i].repeatDays = db.accessDao().getRepeatDays(timings[i].id)
                }
            }
            return timings
        }

        private fun deleteAlarm(db: AppDatabase, id: Int) {
            db.accessDao().deleteAlarm(id)
        }

        private fun updateAlarm(db: AppDatabase, timingsModel: TimingsModel, id: Int) {
            when {
                timingsModel.day != "" -> db.accessDao().updateAlarm(timingsModel.day, timingsModel.month, timingsModel.year, id)
                timingsModel.hour != "" -> db.accessDao().updateAlarm(timingsModel.hour, timingsModel.minute, id)
                timingsModel.repeat -> {
                    db.accessDao().updateAlarm(timingsModel.repeat, id)
                    for (i in timingsModel.repeatDays!!.indices) {
                        db.accessDao().addRepeatDays(timingsModel.repeatDays!![i])
                    }
                }
            }
        }
    }

    private class TransactDbAsync(private val db: AppDatabase, private val timingsModel: TimingsModel, private val id: Int) : AsyncTask<String, Any, Any>() {

        override fun doInBackground(vararg params: String): Void? {
            when (params[0]) {
                AlarmOps.Add.toString() -> {
                    if (timingsModel.repeat)
                        addTimingsWithRepeatDays(db, timingsModel)
                    else
                        addTimingsWithoutRepeatDays(db, timingsModel)
                }
                AlarmOps.Update.toString() -> {
                    if (id != 0)
                        updateAlarm(db, timingsModel, id)
                }
                AlarmOps.Delete.toString() -> {
                    if (id != 0)
                        deleteAlarm(db, id)
                }
            }
            return null
        }
    }
}
