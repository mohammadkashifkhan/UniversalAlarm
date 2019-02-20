package com.mdkashif.universalarm.utils.persistence


import android.os.AsyncTask
import com.mdkashif.universalarm.alarm.miscellaneous.AlarmOps
import com.mdkashif.universalarm.alarm.miscellaneous.AlarmTypes
import com.mdkashif.universalarm.alarm.miscellaneous.db.TimingsModel


class RoomHelper {

    companion object {

        fun transactFetchAsync(db: AppDatabase): List<TimingsModel> {
            return getTimingsWithDays(db)
        }

        fun transactAmendAsync(db: AppDatabase, taskType: String, timingsModel: TimingsModel, id: Int = 0) { //id=0 means we are just inserting
            for (index in 3 until 7) {
                if (timingsModel.alarmType == AlarmTypes.values()[index].toString()) {
                    val prayerTimings = db.accessDao().getAllSpecificAlarms(timingsModel.alarmType)

                    if (prayerTimings.isNotEmpty()) {
                        TransactDbAsync(db, timingsModel, prayerTimings[0].id).execute(AlarmOps.Update.toString())
                        break
                    } else {
                        TransactDbAsync(db, timingsModel, id).execute(taskType)
                        break
                    }
                } else if (timingsModel.alarmType == AlarmTypes.Time.toString()) {
                    TransactDbAsync(db, timingsModel, id).execute(taskType)
                    break
                }
            }
        }

        fun transactCountAsync(db: AppDatabase): Int {
            return db.accessDao().countAlarms()
        }

        private fun addTimingsWithoutRepeatDays(db: AppDatabase, timingsModel: TimingsModel): Long {
            return db.accessDao().addNewAlarm(timingsModel)
        }

        private fun addTimingsWithRepeatDays(db: AppDatabase, timingsModel: TimingsModel) {
            val alarmId = db.accessDao().addNewAlarm(timingsModel)
            for (i in timingsModel.repeatDays!!.indices) {
                timingsModel.repeatDays!![i].fkAlarmId = alarmId
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

    private class TransactDbAsync(private val db: AppDatabase, private val timingsModel: TimingsModel, private val alarmId: Int) : AsyncTask<String, Any, Any>() {

        override fun doInBackground(vararg params: String): Void? {
            when (params[0]) {
                AlarmOps.Add.toString() -> {
                    if (timingsModel.repeat) {
                        addTimingsWithRepeatDays(db, timingsModel)
                    } else {
                        addTimingsWithoutRepeatDays(db, timingsModel)
                    }

                }
                AlarmOps.Update.toString() -> {
                    if (alarmId != 0)
                        updateAlarm(db, timingsModel, alarmId)
                }
                AlarmOps.Delete.toString() -> {
                    if (alarmId != 0)
                        deleteAlarm(db, alarmId)
                }
            }
            return null
        }
    }
}
