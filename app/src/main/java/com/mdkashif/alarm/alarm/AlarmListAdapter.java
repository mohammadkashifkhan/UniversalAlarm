package com.mdkashif.alarm.alarm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hendraanggrian.widget.ExpandableRecyclerView;
import com.mdkashif.alarm.R;
import com.mdkashif.alarm.utils.AppConstants;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Kashif on 20-Apr-18.
 */
public class AlarmListAdapter extends ExpandableRecyclerView.Adapter {
    private List<String> alarmType;

    public AlarmListAdapter(@NonNull LinearLayoutManager lm, List<String> alarmType) {
        super(lm);
        this.alarmType=alarmType;
    }

    class TimeViewHolder extends RecyclerView.ViewHolder {
        public TimeViewHolder(View itemView){
            super(itemView);

        }
    }

    class LocationViewHolder extends RecyclerView.ViewHolder {
        public LocationViewHolder(View itemView) {
            super(itemView);

        }
    }

    class BatteryViewHolder extends RecyclerView.ViewHolder {
        public BatteryViewHolder(View itemView) {
            super(itemView);

        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_time_list_item_row, parent, false);
                return new TimeViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_battery_list_item_row, parent, false);
                return new LocationViewHolder(view);
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_location_list_item_row, parent, false);
                return new BatteryViewHolder(view);
            case 3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_prayer_list_item_row, parent, false);
                return new BatteryViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
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
}
