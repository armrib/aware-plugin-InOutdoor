package com.aware.plugin.InOutdoor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.util.Log;

import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.Screen;
import com.aware.utils.Aware_Plugin;

import java.util.List;

public class Plugin extends Aware_Plugin {

    public Boolean magnetic = false;
    public Boolean accelerometer = false;
    public Boolean gyroscope = false;
    public Boolean proximity = false;
    public Boolean rotation = false;
    public Boolean gravity = false;
    public Boolean linear = false;
    public Boolean barometer = false;
    public Boolean light = false;
    public Boolean location = false;
    public Boolean temperature = false;
    private ScreenListener screen_listener = new ScreenListener();

    @Override
    public void onCreate() {
        super.onCreate();

        TAG = "InOutdoor";
        DEBUG = Aware.getSetting(this, Aware_Preferences.DEBUG_FLAG).equals("true");

        SensorManager mgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors = mgr.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : sensors) {
            Log.d("sensors", "" + sensor.getName());

            if (sensor.getName().contains("Magnetic")) {
                magnetic = true;
            }
            if (sensor.getName().contains("Accelerometer")) {
                accelerometer = true;
            }
            if (sensor.getName().contains("Gyroscope")) {
                gyroscope = true;
            }
            if (sensor.getName().contains("Proximity")) {
                proximity = true;
            }
            if (sensor.getName().contains("Rotation")) {
                rotation = true;
            }
            if (sensor.getName().contains("Gravity")) {
                gravity = true;
            }
            if (sensor.getName().contains("Linear")) {
                linear = true;
            }
        }

        if (accelerometer) {
            Aware.setSetting(this, Aware_Preferences.STATUS_ACCELEROMETER, true);
        }
        if (barometer) {
            Aware.setSetting(this, Aware_Preferences.STATUS_BAROMETER, true);
        }
        if (gravity) {
            Aware.setSetting(this, Aware_Preferences.STATUS_GRAVITY, true);
        }
        if (gyroscope) {
            Aware.setSetting(this, Aware_Preferences.STATUS_GYROSCOPE, true);
        }
        if (light) {
            Aware.setSetting(this, Aware_Preferences.STATUS_LIGHT, true);
        }
        if (linear) {
            Aware.setSetting(this, Aware_Preferences.STATUS_LINEAR_ACCELEROMETER, true);
        }
        if (location) {
            Aware.setSetting(this, Aware_Preferences.STATUS_LOCATION_GPS, true);
            Aware.setSetting(this, Aware_Preferences.STATUS_LOCATION_NETWORK, true);
        }
        if (magnetic) {
            Aware.setSetting(this, Aware_Preferences.STATUS_MAGNETOMETER, true);
        }
        if (temperature) {
            Aware.setSetting(this, Aware_Preferences.STATUS_TEMPERATURE, true);
        }
        if (proximity) {
            Aware.setSetting(this, Aware_Preferences.STATUS_PROXIMITY, true);
        }
        Aware.setSetting(this, Aware_Preferences.STATUS_WIFI, true);
        Aware.setSetting(this, Aware_Preferences.STATUS_BATTERY, true);
        Aware.setSetting(this, Aware_Preferences.STATUS_SCREEN, true);
        Aware.setSetting(this, Aware_Preferences.STATUS_ESM, true);

        Intent applySettings = new Intent(Aware.ACTION_AWARE_REFRESH);
        sendBroadcast(applySettings);

        IntentFilter broadcastFilter = new IntentFilter();
        broadcastFilter.addAction(Screen.ACTION_AWARE_SCREEN_UNLOCKED);
        registerReceiver(screen_listener, broadcastFilter);

        ArbreBinaire.createGraph();

        if (DEBUG) Log.d(TAG, "Plugin running");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_ACCELEROMETER, false);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_BAROMETER, false);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_GRAVITY, false);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_GYROSCOPE, false);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_LIGHT, false);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_LINEAR_ACCELEROMETER, false);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_LOCATION_GPS, false);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_LOCATION_NETWORK, false);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_MAGNETOMETER, false);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_TEMPERATURE, false);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_WIFI, false);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_PROXIMITY, false);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_BATTERY, false);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_SCREEN, false);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_ESM, false);

        unregisterReceiver(screen_listener);
        sendBroadcast(new Intent(Aware.ACTION_AWARE_REFRESH));

        if (DEBUG) Log.d(TAG, "Plugin terminated");
    }

    public class ScreenListener extends BroadcastReceiver {
        public void onReceive(Context context, Intent intent) {
            if (DEBUG) Log.d(TAG, "Phone unlock !");
            if (DEBUG) Log.d(TAG, "magnetic : " + magnetic
                    + ", accelerometer : " + accelerometer
                    + ", gyroscope : " + gyroscope
                    + ", proximity : " + proximity
                    + ", rotation : " + rotation
                    + ", gravity : " + gravity
                    + ", linear : " + linear
                    + ", barometer : " + barometer
                    + ", light : " + light
                    + ", location : " + location
                    + ", temperature : " + temperature);
            InOutDoor test = new InOutDoor(context, magnetic, accelerometer, gyroscope, proximity,
                    rotation, gravity, linear, barometer, light, location, temperature);
            Singleton.getInstance().setInOut(test);
            ArbreBinaire.deapthSearch(Singleton.getInstance().getTree());
        }
    }
}
