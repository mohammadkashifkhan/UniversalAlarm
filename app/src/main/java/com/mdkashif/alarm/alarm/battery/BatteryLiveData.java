package com.mdkashif.alarm.alarm.battery;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.mdkashif.alarm.alarm.battery.pojo.BatteryStats;

import androidx.lifecycle.LiveData;

public class BatteryLiveData extends LiveData<BatteryStats> {

    private Context context;
    int level;
    float temp;
    String status;

    public BatteryLiveData(Context context) {
        this.context = context;
    }

    @Override
    protected void onActive() {
        super.onActive();
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        context.registerReceiver(mBatInfoReceiver, filter);
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        context.unregisterReceiver(mBatInfoReceiver);
    }

    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context ctxt, Intent intent) {

            final int currLevel = intent.getIntExtra(
                    BatteryManager.EXTRA_LEVEL, -1);
            final int maxLevel = intent.getIntExtra(
                    BatteryManager.EXTRA_SCALE, -1);
            level = (int) Math.round((currLevel * 100.0) / maxLevel);

            if(isConnected(context))
                status="Charging";
            else
                status="Discharging";

            temp = ((float) intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0)) / 10;

            postValue(new BatteryStats(level,temp,status));

        }

    };

    public boolean isConnected(Context context) {
        Intent intent = context.registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        return plugged == BatteryManager.BATTERY_PLUGGED_AC || plugged == BatteryManager.BATTERY_PLUGGED_USB;
    }
}
