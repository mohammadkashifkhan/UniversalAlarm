package com.mdkashif.universalarm.persistence

import com.mdkashif.universalarm.alarm.misc.enums.AlarmOps
import com.mdkashif.universalarm.alarm.misc.model.LocationsModel
import com.mdkashif.universalarm.alarm.misc.model.TimingsModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.KoinComponent
import org.koin.core.get

class RoomRepository : KoinComponent {
    private val dao: RoomAccessDao = get()

    suspend fun fetchPrayersAlarmsAsync(): MutableList<TimingsModel> {
        return withContext(Dispatchers.IO) { getPrayerTimings() }
    }

    suspend fun fetchTimingsAlarmsAsync(alarmStatus: Boolean = false): MutableList<TimingsModel> {
        return withContext(Dispatchers.IO) { getTimingsWithRepeatDays(alarmStatus) }
    }

    suspend fun fetchAllAlarmsAsync(alarmStatus: Boolean = false): Pair<MutableList<TimingsModel>, MutableList<LocationsModel>> {
        var timingslist = mutableListOf<TimingsModel>()
        var locationslist = mutableListOf<LocationsModel>()
        withContext(Dispatchers.IO) {
            timingslist = getTimingsWithRepeatDays(alarmStatus)
            locationslist = getLocations(alarmStatus)
        }
        return Pair(timingslist, locationslist)
    }

    suspend fun amendPrayerAlarmsAsync(timingsModel: List<TimingsModel>, autoUpdate: Boolean = false) { //autoUpdate=false for differentiating between manual & auto update
       //todo: not updating the existing ones
        withContext(Dispatchers.IO) {
            val timingsList = getPrayerTimings()

            for (timings in timingsModel) {
                when {
                    timingsList.isNotEmpty() -> updateTimeAlarm(timings, timings.id, autoUpdate)
                    else -> addTimingsWithoutRepeatDays(timings)
                }
            }
        }
    }

    suspend fun amendTimingsAlarmsAsync(taskType: String, timingsModel: TimingsModel?, id: Long = 0) { //id=0 means we are just inserting
        timingsModel!!.createdOn = System.currentTimeMillis()
        when (taskType) {
            AlarmOps.Add.toString() ->
                when {
                    timingsModel.repeat -> withContext(Dispatchers.IO) { addTimingsWithRepeatDays(timingsModel) }
                    else -> withContext(Dispatchers.IO) { addTimingsWithoutRepeatDays(timingsModel) }
                }

            AlarmOps.Update.toString() -> withContext(Dispatchers.IO) { updateTimeAlarm(timingsModel, id, false) }

            AlarmOps.Delete.toString() -> withContext(Dispatchers.IO) { deleteTimeAlarm(id) }
        }
    }

    suspend fun amendLocationsAlarmsAsync(taskType: String, locationsModel: LocationsModel?, id: Long = 0) {
        locationsModel!!.createdOn = System.currentTimeMillis()
        when (taskType) {
            AlarmOps.Add.toString() -> withContext(Dispatchers.IO) { addLocation(locationsModel) }

            AlarmOps.Update.toString() -> withContext(Dispatchers.IO) { updateLocationAlarm(locationsModel, id) }

            AlarmOps.Delete.toString() -> withContext(Dispatchers.IO) { deleteLocationAlarm(id) }
        }
    }

    private fun addTimingsWithoutRepeatDays(timingsModel: TimingsModel): Long {
        return dao.addNewTimeAlarm(timingsModel)
    }

    private fun addTimingsWithRepeatDays(timingsModel: TimingsModel) {
        val alarmId = dao.addNewTimeAlarm(timingsModel)
        for (i in timingsModel.repeatDays!!.indices) {
            timingsModel.repeatDays!![i].fkAlarmId = alarmId
            dao.addRepeatDays(timingsModel.repeatDays!![i])
        }
    }

    private fun addLocation(locationsModel: LocationsModel): Long {
        return dao.addNewLocationAlarm(locationsModel)
    }

    private fun getPrayerTimings(): MutableList<TimingsModel> {
        return dao.getPrayerAlarms()
    }

    private fun getLocations(status: Boolean = false): MutableList<LocationsModel> {
        return if (status)
            dao.getOnlyLiveLocationAlarms()
        else
            dao.getLocationAlarms()
    }

    private fun getTimingsWithRepeatDays(status: Boolean = false): MutableList<TimingsModel> {
        val timings: MutableList<TimingsModel> = when {
            status -> dao.getOnlyLiveTimeAlarms()
            else -> dao.getAllTimeAlarms()
        }
        for (i in timings.indices) {
            if (dao.getRepeatDays(timings[i].id).isNotEmpty()) {
                timings[i].repeatDays = dao.getRepeatDays(timings[i].id)
            }
        }
        return timings
    }

    private fun deleteTimeAlarm(id: Long) {
        dao.deleteTimeAlarm(id)
    }

    private fun deleteLocationAlarm(id: Long) {
        dao.deleteLocationAlarm(id)
    }

    private fun updateTimeAlarm(timingsModel: TimingsModel, id: Long, autoUpdate: Boolean) {
        dao.updateTimeAlarm(timingsModel.hour, timingsModel.minute, timingsModel.note, timingsModel.repeat, timingsModel.status, id, autoUpdate)
        if (timingsModel.repeat) {
            dao.deleteRepeatDays(timingsModel.id)
            for (i in timingsModel.repeatDays!!.indices) {
                timingsModel.repeatDays!![i].fkAlarmId = id
                dao.addRepeatDays(timingsModel.repeatDays!![i])
            }
        }
    }

    private fun updateLocationAlarm(locationsModel: LocationsModel, id: Long) {
        dao.updateLocationAlarm(locationsModel.address, locationsModel.city, locationsModel.latitude, locationsModel.longitude, locationsModel.status, id)
    }
}
