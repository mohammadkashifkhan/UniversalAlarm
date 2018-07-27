package com.mdkashif.alarm.alarm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mdkashif.alarm.R;

import java.util.List;

/**
 * Created by Kashif on 20-Apr-18.
 */
public class AlarmListAdapter extends RecyclerView.Adapter {
    private List<String> alarmType;
    Context context;

    public AlarmListAdapter(Context context, List<String> alarmType) {
        this.context=context;
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
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_alarm_time_list_item, parent, false);
                return new TimeViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_alarm_battery_list_item, parent, false);
                return new LocationViewHolder(view);
            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_alarm_location_list_item, parent, false);
                return new BatteryViewHolder(view);
            case 3:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_alarm_prayer_list_item, parent, false);
                return new BatteryViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemViewType(int position) {
        switch (alarmType.get(position)) {
            case "time":
                return 0;
            case "battery":
                return 1;
            case "location":
                return 2;
            case "prayer":
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
