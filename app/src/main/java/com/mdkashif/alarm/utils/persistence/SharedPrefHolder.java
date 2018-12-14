package com.mdkashif.alarm.utils.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

import com.mdkashif.alarm.utils.AppConstants;

import static com.mdkashif.alarm.utils.AppConstants.batteryAlarmStatus;
import static com.mdkashif.alarm.utils.AppConstants.batteryTempLevel;
import static com.mdkashif.alarm.utils.AppConstants.highBatteryLevel;
import static com.mdkashif.alarm.utils.AppConstants.isFirstTimeLaunch;
import static com.mdkashif.alarm.utils.AppConstants.lowBatteryLevel;
import static com.mdkashif.alarm.utils.AppConstants.ringStatus;
import static com.mdkashif.alarm.utils.AppConstants.ringtoneUri;
import static com.mdkashif.alarm.utils.AppConstants.temperatureAlarmStatus;
import static com.mdkashif.alarm.utils.AppConstants.theftAlarmStatus;
import static com.mdkashif.alarm.utils.AppConstants.themeSelected;
import static com.mdkashif.alarm.utils.AppConstants.vibrateStatus;

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
        editor.putBoolean(isFirstTimeLaunch, isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(isFirstTimeLaunch, true);
    }

    public void setRingStatus(boolean status){
        editor.putBoolean(ringStatus,status);
        editor.commit();
    }

    public boolean getRingStatus(){
        return pref.getBoolean(ringStatus,true);
    }

    public void setRingtoneUri(Uri uri){
        editor.putString(ringtoneUri,uri+"");
        editor.commit();
    }

    public String getRingtoneUri(){
        return pref.getString(ringtoneUri,"");
    }

    public void setVibrateStatus(boolean status){
        editor.putBoolean(vibrateStatus,status);
        editor.commit();
    }

    public boolean getVibrateStatus(){
        return pref.getBoolean(vibrateStatus,true);
    }

    public void setCity(String city){
        editor.putString(AppConstants.city, city);
        editor.commit();
    }

    public String getCity(){
        return pref.getString(themeSelected, "");
    }

    public void setCountry(String country){
        editor.putString(AppConstants.country, country);
        editor.commit();
    }

    public String getCountry(){
        return pref.getString(themeSelected, "");
    }

    public void setHBL(float level){
        editor.putFloat(highBatteryLevel,level);
        editor.commit();
    }

    public Float getHBL(){
        return pref.getFloat(highBatteryLevel,0f);
    }

    public void setLBL(float level){
        editor.putFloat(lowBatteryLevel,level);
        editor.commit();
    }

    public Float getLBL(){
        return pref.getFloat(lowBatteryLevel,0f);
    }

    public void setTemp(float level){
        editor.putFloat(batteryTempLevel,level);
        editor.commit();
    }

    public Float getTemp(){
        return pref.getFloat(batteryTempLevel,0f);
    }

    public void setBatteryAlarmStatus(boolean status){
        editor.putBoolean(batteryAlarmStatus,status);
        editor.commit();
    }

    public Boolean getBatteryAlarmStatus(){
        return pref.getBoolean(batteryAlarmStatus,false);
    }

    public void setTemperatureAlarmStatus(boolean status){
        editor.putBoolean(temperatureAlarmStatus,status);
        editor.commit();
    }

    public Boolean getTemperatureAlarmStatus(){
        return pref.getBoolean(temperatureAlarmStatus,false);
    }

    public void setTheftAlarmStatus(boolean status){
        editor.putBoolean(theftAlarmStatus,status);
        editor.commit();
    }

    public Boolean getTheftAlarmStatus(){
        return pref.getBoolean(theftAlarmStatus,false);
    }

    public void setTheme(String theme){
        editor.putString(themeSelected,theme);
        editor.commit();
    }

    public String getTheme(){
        return pref.getString(themeSelected, AppConstants.themeLight);
    }

}
