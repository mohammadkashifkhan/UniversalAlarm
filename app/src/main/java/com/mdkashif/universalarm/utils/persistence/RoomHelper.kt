package com.mdkashif.universalarm.utils.persistence


import android.os.AsyncTask
import com.mdkashif.universalarm.alarm.miscellaneous.AlarmOps
import com.mdkashif.universalarm.alarm.miscellaneous.AlarmTypes
import com.mdkashif.universalarm.alarm.miscellaneous.model.TimingsModel


class RoomHelper {

    companion object {
        var alarmCount=0

        fun transactFetchAsync(db: AppDatabase): List<TimingsModel> {
            return getTimingsWithDays(db)
        }

        fun transactAmendAsync(db: AppDatabase, taskType: String, timingsModel: TimingsModel, id: Long = 0) { //id=0 means we are just inserting
            if (timingsModel.alarmType != AlarmTypes.Time.toString())
                TransactDbAsync(db, timingsModel, id).execute(AlarmOps.Get.toString()) // to check if we have any existing prayer alarm
            else
                TransactDbAsync(db, timingsModel, id).execute(taskType)
        }

        fun transactCountAsync(db: AppDatabase) {
            TransactDbAsync(db, null, 0).execute(AlarmOps.Count.toString())
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

        private fun getSpecificTimings(db: AppDatabase, timingsModel: TimingsModel): List<TimingsModel> {
            return db.accessDao().getSpecificAlarms(timingsModel.alarmType)
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

        private fun deleteAlarm(db: AppDatabase, id: Long) {
            db.accessDao().deleteAlarm(id)
        }

        private fun updateAlarm(db: AppDatabase, timingsModel: TimingsModel, id: Long) {
            db.accessDao().updateAlarm(timingsModel.day, timingsModel.month, timingsModel.year, id)
            db.accessDao().updateAlarm(timingsModel.hour, timingsModel.minute, id)
            if (timingsModel.repeat) {
                db.accessDao().updateAlarm(timingsModel.repeat, id)
                for (i in timingsModel.repeatDays!!.indices) {
                    timingsModel.repeatDays!![i].fkAlarmId = id
                    db.accessDao().addRepeatDays(timingsModel.repeatDays!![i])
                }
            }
        }

        private fun countAllAlarms(db: AppDatabase) : Int {
            return db.accessDao().countAlarms()
        }

        private class TransactDbAsync(private val db: AppDatabase, private val timingsModel: TimingsModel?, private val alarmId: Long) : AsyncTask<String, Any, Void>() {

            override fun doInBackground(vararg params: String): Void? {
                when (params[0]) {
                    AlarmOps.Get.toString() -> {
                        val prayerTimings = getSpecificTimings(db, timingsModel!!)

                        if (prayerTimings.isNotEmpty())
                            TransactDbAsync(db, timingsModel, prayerTimings[0].id).execute(AlarmOps.Update.toString()) // update an existing prayer alarm
                        else
                            TransactDbAsync(db, timingsModel, alarmId).execute(AlarmOps.Add.toString()) // add a prayer alarm
                    }
                    AlarmOps.Add.toString() -> {
                        if (timingsModel!!.repeat) {
                            addTimingsWithRepeatDays(db, timingsModel)
                        } else {
                            addTimingsWithoutRepeatDays(db, timingsModel)
                        }
                    }
                    AlarmOps.Update.toString() -> {
                        if (alarmId > 0)
                            updateAlarm(db, timingsModel!!, alarmId)
                    }
                    AlarmOps.Delete.toString() -> {
                        if (alarmId > 0)
                            deleteAlarm(db, alarmId)
                    }
                    AlarmOps.Count.toString() -> {
                        alarmCount = countAllAlarms(db)
                    }
                }
                return null
            }
        }
    }
}