package com.mdkashif.universalarm.persistence

import android.os.AsyncTask
import com.mdkashif.universalarm.alarm.misc.AlarmOps
import com.mdkashif.universalarm.alarm.misc.AlarmTypes
import com.mdkashif.universalarm.alarm.misc.model.LocationsModel
import com.mdkashif.universalarm.alarm.misc.model.TimingsModel

object RoomRepository {
    fun fetchDataAsync(db: AppDatabase, type: AlarmTypes = AlarmTypes.All, alarmStatus: Boolean = false, locationRequestId: Long = 0): Pair<MutableList<TimingsModel>?, MutableList<LocationsModel>?> {
        return when (type) {
            AlarmTypes.Prayer -> TransactRoomAsync(db, null, null, 0).execute(AlarmOps.Get.toString()).get() // passed 0 to get Prayer alarms
            AlarmTypes.Time -> when {
                alarmStatus -> TransactRoomAsync(db, null, null, 1).execute(AlarmOps.Get.toString()).get() // passed 1 to get only live alarms
                else -> TransactRoomAsync(db, null, null, 2).execute(AlarmOps.Get.toString()).get() // passed 2 to get all Time alarms
            }
            else -> {
                when {
                    alarmStatus -> TransactRoomAsync(db, null, null, 3).execute(AlarmOps.Get.toString()).get() // passed 3 to get all live alarms
                    else -> TransactRoomAsync(db, null, null, 4).execute(AlarmOps.Get.toString()).get() // passed 4 to get all alarms}
                }
            }
        }
    }

    fun amendTimingsAsync(db: AppDatabase, taskType: String, timingsModel: TimingsModel?, id: Long = 0, autoUpdate: Boolean = false) {//id=0 means we are just inserting
        when {
            timingsModel!!.alarmType != AlarmTypes.Time.toString() -> TransactRoomAsync(db, timingsModel, null, id, autoUpdate, deleteAlarmType = AlarmTypes.Time).execute(AlarmOps.Check.toString()) // to check if we have any existing prayer alarm
            timingsModel.alarmType == AlarmTypes.Time.toString() -> TransactRoomAsync(db, timingsModel, null, id, deleteAlarmType = AlarmTypes.Time).execute(taskType)
        }
    }

    fun amendLocationsAsync(db: AppDatabase, taskType: String, locationsModel: LocationsModel?, id: Long = 0) {
        TransactRoomAsync(db, null, locationsModel, id, deleteAlarmType = AlarmTypes.Location).execute(taskType)
    }

    private fun addTimingsWithoutRepeatDays(db: AppDatabase, timingsModel: TimingsModel): Long {
        return db.accessDao().addNewTimeAlarm(timingsModel)
    }

    private fun addTimingsWithRepeatDays(db: AppDatabase, timingsModel: TimingsModel) {
        val alarmId = db.accessDao().addNewTimeAlarm(timingsModel)
        for (i in timingsModel.repeatDays!!.indices) {
            timingsModel.repeatDays!![i].fkAlarmId = alarmId
            db.accessDao().addRepeatDays(timingsModel.repeatDays!![i])
        }
    }

    private fun addLocation(db: AppDatabase, locationsModel: LocationsModel): Long {
        return db.accessDao().addNewLocationAlarm(locationsModel)
    }

    private fun getSpecificTimings(db: AppDatabase, timingsModel: TimingsModel): MutableList<TimingsModel> {
        return db.accessDao().getSpecificTimeAlarms(timingsModel.alarmType)
    }

    private fun getPrayerTimings(db: AppDatabase): MutableList<TimingsModel> {
        return db.accessDao().getPrayerAlarms()
    }

    private fun getLocations(db: AppDatabase, status: Boolean = false): MutableList<LocationsModel> {
        return if (status)
            db.accessDao().getOnlyLiveLocationAlarms()
        else
            db.accessDao().getLocationAlarms()
    }

    private fun getTimingsWithRepeatDays(db: AppDatabase, status: Boolean = false): MutableList<TimingsModel> {
        val timings: MutableList<TimingsModel> = if (status)
            db.accessDao().getOnlyLiveTimeAlarms()
        else
            db.accessDao().getAllTimeAlarms()
        for (i in timings.indices) {
            if (db.accessDao().getRepeatDays(timings[i].id).isNotEmpty()) {
                timings[i].repeatDays = db.accessDao().getRepeatDays(timings[i].id)
            }
        }
        return timings
    }

    private fun deleteTimeAlarm(db: AppDatabase, id: Long) {
        db.accessDao().deleteTimeAlarm(id)
    }

    private fun deleteLocationAlarm(db: AppDatabase, id: Long) {
        db.accessDao().deleteLocationAlarm(id)
    }

    private fun updateTimeAlarm(db: AppDatabase, timingsModel: TimingsModel, id: Long, autoUpdate: Boolean) {
        db.accessDao().updateTimeAlarm(timingsModel.hour, timingsModel.minute, timingsModel.note, timingsModel.repeat, timingsModel.status, id, autoUpdate)
        if (timingsModel.repeat) {
            db.accessDao().deleteRepeatDays(timingsModel.id)
            for (i in timingsModel.repeatDays!!.indices) {
                timingsModel.repeatDays!![i].fkAlarmId = id
                db.accessDao().addRepeatDays(timingsModel.repeatDays!![i])
            }
        }
    }

    private fun updateLocationAlarm(db: AppDatabase, locationsModel: LocationsModel, id: Long) {
        db.accessDao().updateLocationAlarm(locationsModel.address, locationsModel.city, locationsModel.latitude, locationsModel.longitude, locationsModel.status, id)
    }

    private class TransactRoomAsync(private val db: AppDatabase, private val timingsModel: TimingsModel?, private val locationsModel: LocationsModel?, private val alarmId: Long, private val autoUpdate: Boolean = false, private val deleteAlarmType: AlarmTypes = AlarmTypes.Time) : AsyncTask<String, Any, Pair<MutableList<TimingsModel>?, MutableList<LocationsModel>?>>() {
        var timingsList: MutableList<TimingsModel> = ArrayList()

        override fun onPreExecute() {
            super.onPreExecute()
            timingsList.clear()
        }

        override fun doInBackground(vararg params: String): Pair<MutableList<TimingsModel>?, MutableList<LocationsModel>?>? {
            when (params[0]) {
                AlarmOps.Get.toString() -> {
                    (return when (alarmId) {
                        0.toLong() -> Pair(getPrayerTimings(db), null)
                        1.toLong() -> Pair(getTimingsWithRepeatDays(db, true), null)
                        2.toLong() -> Pair(getTimingsWithRepeatDays(db), null)
                        3.toLong() -> Pair(getTimingsWithRepeatDays(db, true), getLocations(db, true))
                        else -> Pair(getTimingsWithRepeatDays(db), getLocations(db))
                    })
                }
                AlarmOps.Check.toString() -> {
                    timingsList = getSpecificTimings(db, timingsModel!!)
                    when {
                        timingsList.isNotEmpty() -> TransactRoomAsync(db, timingsModel, null, timingsList[0].id, autoUpdate).execute(AlarmOps.Update.toString()) // update an existing prayer alarm
                        else -> TransactRoomAsync(db, timingsModel, null, alarmId).execute(AlarmOps.Add.toString()) // add the prayer alarm
                    }
                }
                AlarmOps.Add.toString() -> {
                    when {
                        timingsModel != null -> when {
                            timingsModel.repeat -> addTimingsWithRepeatDays(db, timingsModel)
                            else -> addTimingsWithoutRepeatDays(db, timingsModel)
                        }
                        else -> addLocation(db, locationsModel!!)
                    }
                }
                AlarmOps.Update.toString() -> {
                    when {
                        alarmId > 0 -> when {
                            timingsModel != null -> updateTimeAlarm(db, timingsModel, alarmId, autoUpdate)
                            else -> updateLocationAlarm(db, locationsModel!!, alarmId)
                        }
                    }
                }
                AlarmOps.Delete.toString() -> {
                    when {
                        alarmId > 0 -> when (deleteAlarmType) {
                            AlarmTypes.Time -> deleteTimeAlarm(db, alarmId)
                            else -> deleteLocationAlarm(db, alarmId)
                        }
                    }
                }
            }
            return null
        }
    }

}