package com.mdkashif.alarm.alarm.prayer.geocoder;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import java.util.List;

import androidx.core.app.ActivityCompat;



public class GetCurrentLocation {

    private static GetCurrentLocation getCurrentLocation = new GetCurrentLocation( );

    private GetCurrentLocation() { }

    public static GetCurrentLocation getInstance( ) {
        return getCurrentLocation;
    }

    private static Location location;
    private LocationManager locationManager;

    public Location findLocation(Context con) {

        String location_context = Context.LOCATION_SERVICE;
        locationManager = (LocationManager) con.getSystemService(location_context);
        List<String> providers = locationManager.getProviders(true);
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(con, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(con, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            }
            locationManager.requestLocationUpdates(provider, 1000, 0,
                    new LocationListener() {

                        public void onLocationChanged(Location location) {}

                        public void onProviderDisabled(String provider) {}

                        public void onProviderEnabled(String provider) {}

                        public void onStatusChanged(String provider, int status,
                                                    Bundle extras) {}
                    });
           location = locationManager.getLastKnownLocation(provider);

        }
        return location;
    }
}
