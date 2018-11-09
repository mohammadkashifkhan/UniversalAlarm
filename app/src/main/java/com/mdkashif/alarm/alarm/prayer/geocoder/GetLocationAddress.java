package com.mdkashif.alarm.alarm.prayer.geocoder;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Kashif on 07-Feb-18.
 */

public class GetLocationAddress {

    private static final String TAG = "LocationAddress";
    static String city;
    static String country;

    public static void getAddressFromLocation(final double latitude, final double longitude,
                                              final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                try {
                    List<Address> addressList = geocoder.getFromLocation(
                            latitude, longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        city=addressList.get(0).getLocality();
                        country=addressList.get(0).getCountryName();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Unable connect to Geocoder", e);
                } finally {
                    Message message = Message.obtain();
                    message.setTarget(handler);
                    if (!(city != null && city.equals(""))) {
                        message.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("city", city);
                        bundle.putString("country", country);
                        message.setData(bundle);
                    } else {
                        message.what = 0;
                    }
                    message.sendToTarget();
                }
            }
        };
        thread.start();
    }
}
