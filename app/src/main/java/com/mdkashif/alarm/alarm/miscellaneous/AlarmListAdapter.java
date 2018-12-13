package com.mdkashif.alarm.alarm.miscellaneous;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mdkashif.alarm.R;
import com.mdkashif.alarm.utils.AppConstants;

import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Kashif on 20-Apr-18.
 */
public class AlarmListAdapter extends RecyclerView.Adapter {
    private List<String> alarmType;

    public AlarmListAdapter(List<String> alarmType) {
        this.alarmType=alarmType;
    }

    class TimeViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout clParentTimeListItem, clDelete;
        public TimeViewHolder(View itemView){
            super(itemView);
            clParentTimeListItem = itemView.findViewById(R.id.clParentTimeListItem);
            clDelete = itemView.findViewById(R.id.clDelete);
        }
    }

    class LocationViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout clParentLocationListItem, clDelete;
        public LocationViewHolder(View itemView) {
            super(itemView);
            clParentLocationListItem = itemView.findViewById(R.id.clParentLocationListItem);
            clDelete = itemView.findViewById(R.id.clDelete);
        }
    }

    class BatteryViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout clParentBatteryListItem, clDelete;
        public BatteryViewHolder(View itemView) {
            super(itemView);
            clParentBatteryListItem = itemView.findViewById(R.id.clParentBatteryListItem);
            clDelete = itemView.findViewById(R.id.clDelete);
        }
    }

    class PrayerViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout clParentPrayerListItem, clDelete;
        public PrayerViewHolder(View itemView) {
            super(itemView);
            clParentPrayerListItem = itemView.findViewById(R.id.clParentPrayerListItem);
            clDelete = itemView.findViewById(R.id.clDelete);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm_time_list, parent, false);
                return new TimeViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm_battery_list, parent, false);
                return new BatteryViewHolder(view);
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm_location_list, parent, false);
                return new LocationViewHolder(view);
            case 3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alarm_prayer_list, parent, false);
                return new PrayerViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof TimeViewHolder) {

        }
        else if (holder instanceof BatteryViewHolder) {

        }
        else if (holder instanceof LocationViewHolder) {

        }
        else if (holder instanceof PrayerViewHolder) {

        }
    }

    @Override
    public int getItemViewType(int position) {
        switch (alarmType.get(position)) {
            case AppConstants.alarmTypeTime:
                return 0;
            case AppConstants.alarmTypeBattery:
                return 1;
            case AppConstants.alarmTypeLocation:
                return 2;
            case AppConstants.alarmTypePrayer:
                return 3;
            default:
                return -1;
        }
    }

    @Override
    public int getItemCount() {
        return alarmType.size();
    }

    public void removeItem(int position) {
        alarmType.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(String item, int position) {
        alarmType.add(position, item);
        notifyItemInserted(position);
    }

    public List<String> getData() {
        return alarmType;
    }
}
