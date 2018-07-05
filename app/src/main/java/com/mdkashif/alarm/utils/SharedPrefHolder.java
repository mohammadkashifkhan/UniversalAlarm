package com.mdkashif.alarm.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Kashif on 16-Apr-18.
 */
public class SharedPrefHolder {
    private final static String PREF_FILE = "PREF";

    private static SharedPrefHolder sharedPrefHolder;
    private SharedPreferences pref;
    SharedPreferences.Editor editor;

    public SharedPrefHolder(Context context) {
        pref = context.getSharedPreferences("Prefs", Context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public static SharedPrefHolder getInstance(Context context) {
        if (sharedPrefHolder == null) {
            sharedPrefHolder = new SharedPrefHolder(context);
        }
        return sharedPrefHolder;
    }

    public void clear(){
        editor.clear().commit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean("isFirstTimeLaunch", isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean("isFirstTimeLaunch", true);
    }

}
