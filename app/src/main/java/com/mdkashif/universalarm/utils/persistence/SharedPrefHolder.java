package com.mdkashif.universalarm.utils.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.mdkashif.universalarm.utils.AppConstants;

/**
 * Created by Kashif on 16-Apr-18.
 */
public class SharedPrefHolder {
    private final static String PREF_FILE = "PREF";

    private static SharedPrefHolder sharedPrefHolder;
    private SharedPreferences pref;
    SharedPreferences.Editor editor;

    public SharedPrefHolder(Context context) {
        pref = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public static SharedPrefHolder getInstance(Context context) {
        if (sharedPrefHolder == null) {
            sharedPrefHolder = new SharedPrefHolder(context);
        }
        return sharedPrefHolder;
    }

    public void clearData(){
        editor.clear().commit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean(AppConstants.isFirstTimeLaunch, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(AppConstants.isFirstTimeLaunch, true);
    }

    public void setRingtoneUri(Uri uri){
        editor.putString(AppConstants.ringtoneUri,uri+"");
        editor.commit();
    }

    public String getRingtoneUri(){
        return pref.getString(AppConstants.ringtoneUri,"");
    }

    public void setVibrateStatus(boolean status){
        editor.putBoolean(AppConstants.vibrateStatus,status);
        editor.commit();
    }

    public boolean getVibrateStatus(){
        return pref.getBoolean(AppConstants.vibrateStatus,true);
    }

    public void setCity(String city){
        editor.putString(AppConstants.city, city);
        editor.commit();
    }

    public String getCity(){
        return pref.getString(AppConstants.themeSelected, "");
    }

    public void setCountry(String country){
        editor.putString(AppConstants.country, country);
        editor.commit();
    }

    public String getCountry(){
        return pref.getString(AppConstants.themeSelected, "");
    }

    public void setHBL(float level){
        editor.putFloat(AppConstants.highBatteryLevel,level);
        editor.commit();
    }

    public Float getHBL(){
        return pref.getFloat(AppConstants.highBatteryLevel,0f);
    }

    public void setLBL(float level){
        editor.putFloat(AppConstants.lowBatteryLevel,level);
        editor.commit();
    }

    public Float getLBL(){
        return pref.getFloat(AppConstants.lowBatteryLevel,0f);
    }

    public void setTemp(float level){
        editor.putFloat(AppConstants.batteryTempLevel,level);
        editor.commit();
    }

    public Float getTemp(){
        return pref.getFloat(AppConstants.batteryTempLevel,0f);
    }

    public void setBatteryAlarmStatus(boolean status){
        editor.putBoolean(AppConstants.batteryAlarmStatus,status);
        editor.commit();
    }

    public Boolean getBatteryAlarmStatus(){
        return pref.getBoolean(AppConstants.batteryAlarmStatus,false);
    }

    public void setTemperatureAlarmStatus(boolean status){
        editor.putBoolean(AppConstants.temperatureAlarmStatus,status);
        editor.commit();
    }

    public Boolean getTemperatureAlarmStatus(){
        return pref.getBoolean(AppConstants.temperatureAlarmStatus,false);
    }

    public void setTheftAlarmStatus(boolean status){
        editor.putBoolean(AppConstants.theftAlarmStatus,status);
        editor.commit();
    }

    public Boolean getTheftAlarmStatus(){
        return pref.getBoolean(AppConstants.theftAlarmStatus,false);
    }

    public void setTheme(String theme){
        editor.putString(AppConstants.themeSelected,theme);
        editor.commit();
    }

    public String getTheme(){
        return pref.getString(AppConstants.themeSelected, AppConstants.themeLight);
    }

}
