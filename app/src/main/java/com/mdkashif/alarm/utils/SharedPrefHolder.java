package com.mdkashif.alarm.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;

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

    public String returnUrl(String type){
        String url="";
        switch (type){
            case "faq": url= AppConstants.FAQ;
                break;
            case "tnc": url= AppConstants.TNC;
                break;
            case "pp": url= AppConstants.PP;
                break;
        }
        return url;
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        editor.putBoolean("isFirstTimeLaunch", isFirstTime);
        editor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean("isFirstTimeLaunch", true);
    }

    public void setringstatus(boolean status){
        editor.putBoolean("ring_status",status);
        editor.commit();
    }

    public boolean getringstatus(){
        return pref.getBoolean("ring_status",true);
    }

    public void seturi(Uri uri){
        editor.putString("uri",uri+"");
        editor.commit();
    }

    public String geturi(){
        return pref.getString("uri","");
    }

    public void setvibratestatus(boolean status){
        editor.putBoolean("vibrate_status",status);
        editor.commit();
    }

    public boolean getvibratestatus(){
        return pref.getBoolean("vibrate_status",true);
    }

    public void setprofileuri(Uri uri){
        editor.putString("profile_uri",uri+"");
        editor.commit();
    }

}
