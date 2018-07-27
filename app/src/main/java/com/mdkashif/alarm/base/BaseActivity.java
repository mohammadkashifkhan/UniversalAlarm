package com.mdkashif.alarm.base;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.mdkashif.alarm.R;
import com.mdkashif.alarm.battery.BatteryReceiver;
import com.mdkashif.alarm.custom.CustomProgressDialog;
import com.mdkashif.alarm.prayer.geocoder.GetCurrentLocation;
import com.mdkashif.alarm.prayer.geocoder.GetLocationAddress;
import com.mdkashif.alarm.utils.AppDatabase;

public class BaseActivity extends AppCompatActivity {
    CustomProgressDialog progressDialog;
    View parentLayout;
    static public String city,country;
    BatteryReceiver batteryReceiver=BatteryReceiver.getInstance();
    public String tag = "universal_alarm_";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parentLayout = findViewById(android.R.id.content);
        Location location = GetCurrentLocation.getInstance().findLocation(getApplicationContext());
        progressDialog = new CustomProgressDialog(BaseActivity.this);
        if(location!=null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            GetLocationAddress.getAddressFromLocation(latitude, longitude,
                    getApplicationContext(), new GeocoderHandler());
        }

    }

    public void showLoader() {
        progressDialog.show();
    }

    public void hideLoader() {
        progressDialog.dismiss();
    }

    public void showToast(String message) {
        Toast.makeText(BaseActivity.this, message + "", Toast.LENGTH_SHORT).show();
    }

    public void showSnackbar(String message,Context context) {
        Snackbar.make(parentLayout.findViewById(android.R.id.content),
                message, Snackbar.LENGTH_LONG).show();
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting() && cm.getActiveNetworkInfo().isAvailable() && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        }
        return false;
    }

    public boolean isBlank(String strValue) {
        if (strValue == null || strValue.equals(""))
            return true;
        else
            return false;
    }

    public void replaceFragment(Fragment fragment, String tag, boolean isAddTobackStack) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.setCustomAnimations( R.animator.slide_up, 0, 0, R.animator.slide_down);
        ft.replace(R.id.fl_main_content, fragment, tag);
        if (isAddTobackStack) {
            ft.addToBackStack(tag);
        }
        ft.commit();
    }

    private static class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    city = bundle.getString("city");
                    country = bundle.getString("country");
                    break;
                default:
                    city = "";
                    country = "";
            }
        }
    }

    @Override
    protected void onDestroy() {
        AppDatabase.destroyInstance();
        super.onDestroy();
    }

}
