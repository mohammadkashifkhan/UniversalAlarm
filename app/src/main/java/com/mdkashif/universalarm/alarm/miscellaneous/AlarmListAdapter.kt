package com.mdkashif.universalarm.alarm.miscellaneous

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.mdkashif.universalarm.R

/**
 * Created by Kashif on 20-Apr-18.
 */
class AlarmListAdapter(private val alarmType: MutableList<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val data: List<String>
        get() = alarmType

    internal inner class TimeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var clParentTimeListItem: ConstraintLayout = itemView.findViewById(R.id.clParentTimeListItem)
        var clDelete: ConstraintLayout = itemView.findViewById(R.id.clDelete)
    }

    internal inner class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var clParentLocationListItem: ConstraintLayout = itemView.findViewById(R.id.clParentLocationListItem)
        var clDelete: ConstraintLayout = itemView.findViewById(R.id.clDelete)
    }

    internal inner class BatteryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var clParentBatteryListItem: ConstraintLayout = itemView.findViewById(R.id.clParentBatteryListItem)
        var clDelete: ConstraintLayout = itemView.findViewById(R.id.clDelete)
    }

    internal inner class PrayerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var clParentPrayerListItem: ConstraintLayout = itemView.findViewById(R.id.clParentPrayerListItem)
        var clDelete: ConstraintLayout = itemView.findViewById(R.id.clDelete)
        var tvPrayerType: TextView = itemView.findViewById(R.id.tvPrayerType)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        lateinit var viewHolder: RecyclerView.ViewHolder
        when (viewType) {
            0 -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.item_alarm_time_list, parent, false)
                viewHolder = TimeViewHolder(view)
            }
            1 -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.item_alarm_battery_list, parent, false)
                viewHolder = BatteryViewHolder(view)
            }
            2 -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.item_alarm_location_list, parent, false)
                viewHolder = LocationViewHolder(view)
            }
            3 -> {
                view = LayoutInflater.from(parent.context).inflate(R.layout.item_alarm_prayer_list, parent, false)
                viewHolder = PrayerViewHolder(view)
            }
        }
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is TimeViewHolder -> {

            }
            is BatteryViewHolder -> {

            }
            is LocationViewHolder -> {

            }
            is PrayerViewHolder -> holder.tvPrayerType.text = alarmType[position]
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            AlarmTypes.Time.toString() == alarmType[position] -> 0
            AlarmTypes.Battery.toString() == alarmType[position] -> 1
            AlarmTypes.Location.toString() == alarmType[position] -> 2
            else -> 3
        }
    }

    override fun getItemCount(): Int {
        return alarmType.size
    }

    fun removeItem(position: Int) {
        alarmType.removeAt(position)
        notifyItemRemoved(position)
    }

    fun restoreItem(item: String, position: Int) {
        alarmType.add(position, item)
        notifyItemInserted(position)
    }
}
