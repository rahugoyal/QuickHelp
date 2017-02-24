package com.example.rahul.quickhelp.services;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MyService extends Service implements LocationListener {

    private Context mContext;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;
    private boolean canGetLocation = false;
    private Location location;
    double latitude;
    double longitude;
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
    protected LocationManager locationManager;
    private int countPowerOff = 0;
    private static BroadcastReceiver m_ScreenOffReceiver;
    private Calendar max = null;
    private long updateTime;

    public MyService() {
        max = Calendar.getInstance();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onCreate() {
        registerScreenOffReceiver();
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(m_ScreenOffReceiver);
        m_ScreenOffReceiver = null;
    }

    private void registerScreenOffReceiver() {
        m_ScreenOffReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mContext = context;

                if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF) || intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                    countPowerOff++;
                    if (countPowerOff == 1) {
                        max = Calendar.getInstance();
                        max.add(Calendar.SECOND, 20);

                        updateTime = max.getTimeInMillis();
                    }

                    if (updateTime >= Calendar.getInstance().getTimeInMillis()) {
                        if (countPowerOff == 5) {
                            countPowerOff = 0;
                            getLocation(context);
                            sendMessage(mContext);
                        }
                    } else countPowerOff = 0;

                    Log.e("count", countPowerOff + "");
                }

            }
        };
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(m_ScreenOffReceiver, filter);

    }

    private void sendMessage(Context context) {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("data", MODE_PRIVATE);
            String friend1 = sharedPreferences.getString("friend1", null);
            String friend2 = sharedPreferences.getString("friend2", null);
            String friend3 = sharedPreferences.getString("friend3", null);

            SmsManager smsManager = SmsManager.getDefault();
            String msg = "http://maps.google.com/?q=" + getLatitude() + "," + getLongitude();

            smsManager.sendTextMessage(friend1, null, "Hey buddy , I am in trouble please come at my location \n" + msg, null, null);
            smsManager.sendTextMessage(friend2, null, "Hey buddy , I am in trouble please come at my location \n" + msg, null, null);
            smsManager.sendTextMessage(friend3, null, "Hey buddy , I am in trouble please come at my location \n" + msg, null, null);


        } catch (Exception e) {
            Toast.makeText(context, "SMS faild, please try again later!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    public Location getLocation(Context mContext) {
        try {
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled && !isNetworkEnabled) {
            } else {
                this.canGetLocation = true;
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return null;
                    }
                    locationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if (locationManager != null) {
                        location = getLastKnownLocation(locationManager);
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }
                if (isGPSEnabled) {
                    if (location == null) {
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if (locationManager != null) {
                            location = getLastKnownLocation(locationManager);
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    private Location getLastKnownLocation(LocationManager mLocationManager) {
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            Location l = mLocationManager.getLastKnownLocation(provider);

            if (l == null) {
                continue;
            }
            if (bestLocation == null
                    || l.getAccuracy() < bestLocation.getAccuracy()) {
                bestLocation = l;
            }
        }
        if (bestLocation == null) {
            return null;
        }
        return bestLocation;
    }

    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }

        return latitude;
    }

    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }

        return longitude;
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}

