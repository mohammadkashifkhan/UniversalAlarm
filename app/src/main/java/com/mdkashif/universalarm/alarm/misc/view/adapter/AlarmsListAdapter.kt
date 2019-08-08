package com.mdkashif.universalarm.alarm.misc.view.adapter

import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.hendraanggrian.recyclerview.widget.ExpandableRecyclerView
import com.mdkashif.universalarm.R
import com.mdkashif.universalarm.alarm.location.misc.LocationHelper
import com.mdkashif.universalarm.alarm.location.ui.SetLocationFragment
import com.mdkashif.universalarm.alarm.misc.AlarmHelper
import com.mdkashif.universalarm.alarm.misc.enums.AlarmOps
import com.mdkashif.universalarm.alarm.misc.enums.AlarmTypes
import com.mdkashif.universalarm.alarm.misc.model.LocationsModel
import com.mdkashif.universalarm.alarm.misc.model.TimingsModel
import com.mdkashif.universalarm.alarm.prayer.ui.PrayerPresenter
import com.mdkashif.universalarm.alarm.time.TimeHelper
import com.mdkashif.universalarm.alarm.time.ui.SetTimeFragment
import com.mdkashif.universalarm.misc.ui.ContainerActivity
import com.mdkashif.universalarm.persistence.AppPreferences
import com.mdkashif.universalarm.persistence.RoomRepository
import com.mdkashif.universalarm.utils.Utils
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

class AlarmsListAdapter(private val getTotalAlarmCountInterface: GetTotalAlarmCountInterface, private val alarmsList: MutableList<TimingsModel>, private val locationsList: MutableList<LocationsModel>, private val viewType: String, private val context: ContainerActivity, linearLayoutManager: LinearLayoutManager, private val disposable: CompositeDisposable) : ExpandableRecyclerView.Adapter<RecyclerView.ViewHolder>(linearLayoutManager), KoinComponent {

    private val roomRepository: RoomRepository by inject()

    private val appPreferences: AppPreferences by inject()

    private val locationHelper: LocationHelper by inject()

    internal inner class TimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTime: TextView = itemView.findViewById(R.id.tvTime)
        val tvTimeType: TextView = itemView.findViewById(R.id.tvTimeType)
        val tvRepeatDays: TextView = itemView.findViewById(R.id.tvRepeatDays)
        val tvETA: TextView = itemView.findViewById(R.id.tvETA)
        val swTime: SwitchCompat = itemView.findViewById(R.id.swTime)
        val ibDelete: ImageButton = itemView.findViewById(R.id.ibDelete)
        val ibEdit: ImageButton = itemView.findViewById(R.id.ibEdit)
    }

    internal inner class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvAddress: TextView = itemView.findViewById(R.id.tvAddress)
        val tvCity: TextView = itemView.findViewById(R.id.tvCity)
        val tvDistance: TextView = itemView.findViewById(R.id.tvDistance)
        val swLocation: SwitchCompat = itemView.findViewById(R.id.swLocation)
        val ibDelete: ImageButton = itemView.findViewById(R.id.ibDelete)
        val ibEdit: ImageButton = itemView.findViewById(R.id.ibEdit)
    }

    internal inner class BatteryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHbl: TextView = itemView.findViewById(R.id.tvHbl)
        val tvLbl: TextView = itemView.findViewById(R.id.tvLbl)
        val tvTemp: TextView = itemView.findViewById(R.id.tvTemp)
        val tvNote: TextView = itemView.findViewById(R.id.tvNote)
        val swBattery: SwitchCompat = itemView.findViewById(R.id.swBattery)
        val swTemperature: SwitchCompat = itemView.findViewById(R.id.swTemperature)
    }

    internal inner class PrayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvPrayerType: TextView = itemView.findViewById(R.id.tvPrayerType)
        val tvPrayerTime: TextView = itemView.findViewById(R.id.tvPrayerTime)
        val tvPrayerTimeType: TextView = itemView.findViewById(R.id.tvPrayerTimeType)
        val tvNote: TextView = itemView.findViewById(R.id.tvNote)
        val tvETA: TextView = itemView.findViewById(R.id.tvETA)
        val swPrayer: SwitchCompat = itemView.findViewById(R.id.swPrayer)
    }

    internal inner class EmptyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivPlaceHolder: ImageView = itemView.findViewById(R.id.ivPlaceHolder)
        val tvNote: TextView = itemView.findViewById(R.id.tvNote)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        lateinit var viewHolder: RecyclerView.ViewHolder
        when (viewType) {
            0 -> {
                view = LayoutInflater.from(context).inflate(R.layout.item_alarm_expandable_time, parent, false)
                viewHolder = TimeViewHolder(view)
            }
            1 -> {
                view = LayoutInflater.from(context).inflate(R.layout.item_alarm_expandable_battery, parent, false)
                viewHolder = BatteryViewHolder(view)
            }
            2 -> {
                view = LayoutInflater.from(context).inflate(R.layout.item_alarm_expandable_location, parent, false)
                viewHolder = LocationViewHolder(view)
            }
            3 -> {
                view = LayoutInflater.from(context).inflate(R.layout.item_alarm_expandable_prayer, parent, false)
                viewHolder = PrayerViewHolder(view)
            }
            -1 -> {
                view = LayoutInflater.from(context).inflate(R.layout.item_alarm_expandable_empty, parent, false)
                viewHolder = EmptyViewHolder(view)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        when (holder) {
            is TimeViewHolder -> {
                val builder = StringBuilder()
                when {
                    alarmsList[position].hour.toInt() >= 12 -> {
                        val hour = alarmsList[position].hour.toInt()
                        when {
                            hour == 12 -> holder.tvTime.text = "$hour:${alarmsList[position].minute}"
                            hour - 12 >= 10 -> holder.tvTime.text = "${hour - 12}:${alarmsList[position].minute}"
                            else -> holder.tvTime.text = "0${hour - 12}:${alarmsList[position].minute}"
                        }
                        holder.tvTimeType.text = "PM"
                    }
                    else -> {
                        val hour = alarmsList[position].hour.toInt()
                        if (hour >= 10)
                            holder.tvTime.text = "$hour:${alarmsList[position].minute}"
                        else
                            holder.tvTime.text = "0$hour:${alarmsList[position].minute}"
                        holder.tvTimeType.text = "AM"
                    }
                }
                if (alarmsList[position].repeat) {
                    for (i in alarmsList[position].repeatDays!!.indices) {
                        builder.append(alarmsList[position].repeatDays!![i].repeatDay.substring(0, 1)).append(" ")
                    }
                }
                holder.tvRepeatDays.text = builder
                disposable.add(TimeHelper.getTimeFromNow(alarmsList[position].hour.toInt(), alarmsList[position].minute.toInt()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(object : DisposableObserver<String>() {
                    override fun onError(e: Throwable) {
                        // do nothing
                    }

                    override fun onComplete() {
                        // do nothing
                    }

                    override fun onNext(t: String) {
                        holder.tvETA.text = "$t from now"
                    }
                }))
                holder.swTime.isChecked = alarmsList[position].status
                holder.swTime.setOnCheckedChangeListener { p0, p1 ->
                    alarmsList[position].status = p1
                    GlobalScope.launch {
                        roomRepository.amendTimingsAlarmsAsync(AlarmOps.Update.toString(), alarmsList[position], alarmsList[position].id)
                    }
                    if (p1) {
                        if (alarmsList[position].repeat)
                            AlarmHelper.setAlarm(alarmsList[position].hour.toInt(), alarmsList[position].minute.toInt(), alarmsList[position].pIntentRequestCode.toInt(), context, AlarmTypes.Time, alarmsList[position].note, repeat = true, repeatDays = alarmsList[position].repeatDays)
                        else
                            AlarmHelper.setAlarm(alarmsList[position].hour.toInt(), alarmsList[position].minute.toInt(), alarmsList[position].pIntentRequestCode.toInt(), context, AlarmTypes.Time, alarmsList[position].note, repeatDays = null)
                    } else
                        AlarmHelper.stopAlarm(alarmsList[position].pIntentRequestCode.toInt(), context)
                }
                holder.ibEdit.setOnClickListener {
                    context.replaceFragment(SetTimeFragment(), SetTimeFragment::class.java.simpleName, false, timingDao = alarmsList[position])
                }
                holder.ibDelete.setOnClickListener {
                    GlobalScope.launch {
                        roomRepository.amendTimingsAlarmsAsync(AlarmOps.Delete.toString(), null, alarmsList[position].id)
                        alarmsList.removeAt(position)
                    }
                    getTotalAlarmCountInterface.fetchTotalAlarmCount()
                    notifyItemRemoved(position)
                }
            }
            is BatteryViewHolder -> {
                holder.tvHbl.text = appPreferences.hbl.toString()
                holder.tvLbl.text = appPreferences.lbl.toString()
                holder.tvTemp.text = appPreferences.temp.toString()
                holder.tvNote.text = context.getText(R.string.tvItemBatteryExpandableRv)
                holder.swBattery.isChecked = appPreferences.batteryAlarmStatus!!
                holder.swTemperature.isChecked = appPreferences.temperatureAlarmStatus!!
                holder.swBattery.setOnCheckedChangeListener { p0, p1 ->
                    appPreferences.batteryAlarmStatus = p1
                }
                holder.swTemperature.setOnCheckedChangeListener { p0, p1 ->
                    appPreferences.temperatureAlarmStatus = p1
                }
            }
            is LocationViewHolder -> {
                val index = if (alarmsList.size == 0)
                    position
                else
                    position - alarmsList.size
                holder.tvAddress.text = locationsList[index].address
                holder.tvCity.text = locationsList[index].city

                disposable.add(PrayerPresenter.getLocationContinuously().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(object : DisposableObserver<Location>() {
                    override fun onNext(t: Location) {
                        holder.tvDistance.text = locationHelper.getDistance(LatLng(locationsList[index].latitude, locationsList[index].longitude), LatLng(t.latitude, t.longitude))
                    }

                    override fun onError(e: Throwable) {
                        // do nothing
                    }

                    override fun onComplete() {
                        // do nothing
                    }
                }))

                holder.swLocation.isChecked = locationsList[index].status
                holder.swLocation.setOnCheckedChangeListener { p0, p1 ->
                    locationsList[position].status = p1
                    GlobalScope.launch {
                        roomRepository.amendLocationsAlarmsAsync(AlarmOps.Update.toString(), locationsList[index], locationsList[index].id.toLong())
                    }
                }
                holder.ibEdit.setOnClickListener {
                    context.replaceFragment(SetLocationFragment(), SetLocationFragment::class.java.simpleName, false, locationDao = locationsList[index])
                }
                holder.ibDelete.setOnClickListener {
                    locationHelper.removeAlarm(locationsList[index].pIntentRequestCode.toString(), success = {
                        GlobalScope.launch {
                            roomRepository.amendLocationsAlarmsAsync(AlarmOps.Delete.toString(), locationsList[index], locationsList[index].id.toLong())
                        }
                        locationsList.removeAt(index)
                        getTotalAlarmCountInterface.fetchTotalAlarmCount()
                        notifyItemRemoved(position)
                    }, failure = {
                        Utils.showToast(it, context)
                    }, context = context)
                }
            }
            is PrayerViewHolder -> {
                holder.tvPrayerType.text = alarmsList[position].alarmType
                holder.tvNote.text = context.getText(R.string.tvItemPrayerExpandableRv)
                when {
                    alarmsList[position].hour.toInt() >= 12 -> {
                        val hour = alarmsList[position].hour.toInt()
                        when {
                            hour == 12 -> holder.tvPrayerTime.text = "$hour:${alarmsList[position].minute}"
                            hour - 12 >= 10 -> holder.tvPrayerTime.text = "${hour - 12}:${alarmsList[position].minute}"
                            else -> holder.tvPrayerTime.text = "0${hour - 12}:${alarmsList[position].minute}"
                        }
                        holder.tvPrayerTimeType.text = "PM"
                    }
                    else -> {
                        holder.tvPrayerTime.text = """${alarmsList[position].hour}:${alarmsList[position].minute}"""
                        holder.tvPrayerTimeType.text = "AM"
                    }
                }
                disposable.add(TimeHelper.getTimeFromNow(alarmsList[position].hour.toInt(), alarmsList[position].minute.toInt()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribeWith(object : DisposableObserver<String>() {
                    override fun onError(e: Throwable) {
                        // do nothing
                    }

                    override fun onComplete() {
                        // do nothing
                    }

                    override fun onNext(t: String) {
                        holder.tvETA.text = "$t from now"
                    }
                }))
                holder.swPrayer.isChecked = alarmsList[position].status
                holder.swPrayer.setOnCheckedChangeListener { p0, p1 ->
                    alarmsList[position].status = p1
                    GlobalScope.launch {
                        roomRepository.amendPrayerAlarmsAsync(alarmsList[position])
                    } // sending add instead of update because its handled in the repository
                    if (p1)
                        AlarmHelper.setAlarm(alarmsList[position].hour.toInt(), alarmsList[position].minute.toInt(), alarmsList[position].pIntentRequestCode.toInt(), context, AlarmTypes.valueOf(alarmsList[position].alarmType), repeatDays = null)
                    else
                        AlarmHelper.stopAlarm(alarmsList[position].pIntentRequestCode.toInt(), context)
                }
            }
            is EmptyViewHolder -> {
                holder.tvNote.text = context.getText(R.string.tvItemEmptyExpandableRv)
                Glide.with(context)
                        .load(R.raw.no_alarms_placeholder)
                        .into(holder.ivPlaceHolder)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position < alarmsList.size -> when {
                AlarmTypes.Time.toString() == alarmsList[position].alarmType -> 0
                else -> 3
            }
            (position - alarmsList.size) < locationsList.size -> 2
            (position - (alarmsList.size + locationsList.size) < 1) && (appPreferences.hbl != 0f) -> 1
            else -> -1
        }
    }

    override fun getItemCount(): Int {
        when (viewType) {
            "ShowAll" -> return when {
                appPreferences.hbl != 0f -> alarmsList.size + locationsList.size + 1
                else -> alarmsList.size + locationsList.size
            }
            "Home" -> when {
                (alarmsList.size + locationsList.size) > 4 -> return 4
                (alarmsList.size + locationsList.size) in 1..4 -> return when {
                    appPreferences.hbl != 0f -> alarmsList.size + locationsList.size + 1
                    else -> alarmsList.size + locationsList.size
                }
                appPreferences.hbl != 0f -> return 1
            }
        }
        return 1
    }

    interface GetTotalAlarmCountInterface {
        fun fetchTotalAlarmCount()
    }
}