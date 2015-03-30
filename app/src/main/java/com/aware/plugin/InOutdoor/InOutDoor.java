package com.aware.plugin.inoutdoor;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import com.aware.providers.Accelerometer_Provider;
import com.aware.providers.Barometer_Provider;
import com.aware.providers.Battery_Provider;
import com.aware.providers.Gravity_Provider;
import com.aware.providers.Light_Provider;
import com.aware.providers.Locations_Provider;
import com.aware.providers.Magnetometer_Provider;
import com.aware.providers.Screen_Provider;
import com.aware.providers.Temperature_Provider;
import com.aware.providers.WiFi_Provider;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Armand on 19/02/2015.
 */
public class InOutDoor {

    private double[] accelerometerB = new double[2];
    private double[] magnetometerB = new double[2];
    private double[] lightB = new double[2];
    private double[] wifiB = new double[2];
    private double[] temperatureB = new double[2];
    private double[] batteryB = new double[2];
    private double[] screenB = new double[2];

    private Context context;

    public InOutDoor(Context context) {
        this.context = context;
    }

    public double[] getAccelerometer() {
            accelerometerB[1] = 0.9;
            long last_timestamp = System.currentTimeMillis();
            long laps = last_timestamp - 30*1000;
            String[] values = {Long.toString(laps) ,Long.toString(last_timestamp)};

            Cursor last_time_accelerometer = context.getContentResolver().query(Accelerometer_Provider.Accelerometer_Data.CONTENT_URI, null, "TIMESTAMP BETWEEN ? AND ?", values, Accelerometer_Provider.Accelerometer_Data.TIMESTAMP + " DESC");
            if (last_time_accelerometer != null && last_time_accelerometer.moveToFirst()) {
                double accel = 0;
                double previous = 0;
                double evo = 0;
                double average_accel = 0;
                double average_evo = 0;
                int i = 0;
                while (!last_time_accelerometer.isLast()){
                    i++;
                    double x = last_time_accelerometer.getDouble(last_time_accelerometer.getColumnIndex(Accelerometer_Provider.Accelerometer_Data.VALUES_0));
                    double y = last_time_accelerometer.getDouble(last_time_accelerometer.getColumnIndex(Accelerometer_Provider.Accelerometer_Data.VALUES_1));
                    double z = last_time_accelerometer.getDouble(last_time_accelerometer.getColumnIndex(Accelerometer_Provider.Accelerometer_Data.VALUES_2));
                    accel = Math.sqrt(x * x + y * y + z * z);
                    average_accel += accel;
                    if (i > 0) {
                        evo = Math.sqrt(Math.pow(accel - previous, 2));
                        average_evo += evo;
                        previous = accel;
                    }
                    else
                        previous = accel;
                    last_time_accelerometer.moveToNext();
                }
                average_accel = average_accel / i;
                average_evo = average_evo / 99;
                if (average_accel != 0 && average_evo != 0 && (average_evo >= 1 || (average_accel >= 10.22 && average_accel <= 10.15))) {
                    accelerometerB[0] = 1;
                    accelerometerB[1] = 0.95;
                    Log.d("test", "ça bouge !");
                } else {
                    accelerometerB[0] = 0;
                    Log.d("test", "ça bouge pas !");
                }
            }

            if (last_time_accelerometer != null && !last_time_accelerometer.isClosed()) {
                last_time_accelerometer.close();
                Log.d("test", "accel cursor close");
            }
            Log.d("test", "accel end");
        return accelerometerB;
    }

    public double[] getLight() {
            lightB[1] = 0.95;
            Log.d("test", "light");
        long last_timestamp = System.currentTimeMillis();
        long laps = last_timestamp - 15*1000;
        String[] values = {Long.toString(laps) ,Long.toString(last_timestamp)};
            Cursor last_time_light = context.getContentResolver().query(Light_Provider.Light_Data.CONTENT_URI, null, "TIMESTAMP BETWEEN ? AND ?", values, Light_Provider.Light_Data.TIMESTAMP + " DESC");
            double average_light = 0;
            int k = 0;
            if (last_time_light != null && last_time_light.moveToFirst()) {
                while (!last_time_light.isLast()) {
                    k ++;
                    double lighti = last_time_light.getDouble(last_time_light.getColumnIndex(Light_Provider.Light_Data.LIGHT_LUX));
                    average_light += lighti;
                    last_time_light.moveToNext();
                }
            }
            average_light = average_light/k;
            Log.d("test", "Average light : "+ average_light);
            Boolean jour = false;
            Date date = new Date();   // given date
            Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
            calendar.setTime(date);   // assigns calendar to given date
            if (calendar.get(Calendar.HOUR_OF_DAY) >= 7 && calendar.get(Calendar.HOUR_OF_DAY) <= 19) {
                jour = true;
                Log.d("test", "jour ? : "+jour);
            }
            if ((average_light >= 2000 && jour) || (average_light <= 25 && jour == false)) {
                lightB[0] = 0;
                Log.d("test", "outdoor light "+ average_light);
            } else {
                lightB[0] = 1;
                Log.d("test", "indoor light "+ average_light);
            }

            if (last_time_light != null && !last_time_light.isClosed()) {
                last_time_light.close();
                Log.d("test", "light cursor close");
            }
            Log.d("test", "light end");
        return lightB;
    }

    public double[] getTemp() {
        temperatureB[1] = 0.8;
        long last_timestamp = System.currentTimeMillis();
        long laps = last_timestamp - 15*1000;
        String[] values = {Long.toString(laps) ,Long.toString(last_timestamp)};
        Cursor last_time_temperature = context.getContentResolver().query(Temperature_Provider.Temperature_Data.CONTENT_URI, null, "TIMESTAMP BETWEEN ? AND ?", values, Temperature_Provider.Temperature_Data.TIMESTAMP + " DESC LIMIT 2");
        if (last_time_temperature != null && last_time_temperature.moveToFirst()) {
            double average_temp = 0;
            double evo = 0;
            double previous = 0;
            int k = 0;
            if (last_time_temperature != null && last_time_temperature.moveToFirst()) {
                while (!last_time_temperature.isLast()) {
                    k ++;
                    double temp = last_time_temperature.getDouble(last_time_temperature.getColumnIndex(Temperature_Provider.Temperature_Data.TEMPERATURE_CELSIUS));
                    average_temp += temp;
                    if (k == 1)
                    {
                        previous = temp;
                    }
                    else {
                        double change = Math.sqrt(Math.pow(temp - previous, 2));
                        if(change > evo)
                            evo = change;
                    }
                    last_time_temperature.moveToNext();
                }
            }
            average_temp = average_temp/k;
            Log.d("test", "Average temp : "+average_temp+" ,diff de temp max : " + evo);

            if (average_temp >= 15 && average_temp >= 25 && evo < 2) {
                temperatureB[0] = 1;
                temperatureB[1] = 0.9;
            } else
                temperatureB[0] = 0;
        }
        if (last_time_temperature != null && !last_time_temperature.isClosed()) {
            last_time_temperature.close();
            Log.d("test", "temp cursor close");
        }
        Log.d("test", "temp end");

        return temperatureB;
    }

    public double[] getMagnetometer() {
            magnetometerB[1] = 0.8;
            long last_timestamp = System.currentTimeMillis();
            long laps = last_timestamp - 70*1000;
            String[] values = {Long.toString(laps) ,Long.toString(last_timestamp)};

            Cursor last_time_magneto = context.getContentResolver().query(Magnetometer_Provider.Magnetometer_Data.CONTENT_URI, null, "TIMESTAMP BETWEEN ? AND ?", values, Magnetometer_Provider.Magnetometer_Data.TIMESTAMP + " DESC");
            if (last_time_magneto != null && last_time_magneto.moveToFirst()) {
                boolean still = true;
                double average = 0;
                double previous = 0;
                int i = 0;

                while(still) {
                    i++;
                    double diff = 0;
                    double temp = last_time_magneto.getDouble(last_time_magneto.getColumnIndex(Gravity_Provider.Gravity_Data.TIMESTAMP));
                    if (i>1){
                        diff = Math.sqrt(Math.pow(temp - previous, 2));
                        previous = temp;
                    }
                    else
                        previous = temp;
                    if (diff < 1000) {
                        double x = last_time_magneto.getDouble(last_time_magneto.getColumnIndex(Gravity_Provider.Gravity_Data.VALUES_0));
                        double y = last_time_magneto.getDouble(last_time_magneto.getColumnIndex(Gravity_Provider.Gravity_Data.VALUES_1));
                        double z = last_time_magneto.getDouble(last_time_magneto.getColumnIndex(Gravity_Provider.Gravity_Data.VALUES_2));
                        double a = Math.sqrt(x * x + y * y + z * z);
                        average += a;
                        last_time_magneto.moveToNext();
                    }
                    else
                        still = false;
                }
                average = average/i;
                Log.d("test", "magneto average : "+average);
                if (average >= 48 && average < 54) {
                    magnetometerB[0] = 0;
                    magnetometerB[1] = 0.7;
                    Log.d("test", "magneto outdoor");
                }
                else {
                    magnetometerB[0] = 1;
                    magnetometerB[1] = 0.9;
                    Log.d("test", "magneto else indoor");
                }
            }
            if (last_time_magneto != null && !last_time_magneto.isClosed()) {
                last_time_magneto.close();
                Log.d("test", "magnetometer cursor close");
            }
            Log.d("test", "magnetometer end");
        return magnetometerB;
    }

    public double[] getWifi() {
        wifiB[1] = 0.7;
        long last_timestamp = System.currentTimeMillis();
        long laps = last_timestamp - 45*1000;
        String[] values = {Long.toString(laps) ,Long.toString(last_timestamp)};

        Cursor last_time_wifi = context.getContentResolver().query(WiFi_Provider.WiFi_Sensor.CONTENT_URI, null, "TIMESTAMP BETWEEN ? AND ?", values, WiFi_Provider.WiFi_Sensor.TIMESTAMP + " DESC LIMIT 1");
        if (last_time_wifi != null && last_time_wifi.moveToFirst()) {
            String x = last_time_wifi.getString(last_time_wifi.getColumnIndex(WiFi_Provider.WiFi_Sensor.SSID));
            Log.d("test", "wifi : " + x);
            if (x.contentEquals("0x"))
                wifiB[0] = 0;
            else {
                wifiB[0] = 1;
                wifiB[1] = 0.9;
            }
        }
        if (last_time_wifi != null && !last_time_wifi.isClosed()) {
            last_time_wifi.close();
            Log.d("test", "wifi cursor close");
        }
        Log.d("test", "wifi end");
        return wifiB;
    }
    public double[] getWifiP() {
        wifiB[1] = 0.95;
        long last_timestamp = System.currentTimeMillis();
        long laps = last_timestamp - 45*1000;
        String[] values = {Long.toString(laps) ,Long.toString(last_timestamp)};

        Cursor last_time_wifi = context.getContentResolver().query(WiFi_Provider.WiFi_Sensor.CONTENT_URI, null, "TIMESTAMP BETWEEN ? AND ?", values, WiFi_Provider.WiFi_Sensor.TIMESTAMP + " DESC LIMIT 1");
        String connect = "0x";
        if (last_time_wifi != null && last_time_wifi.moveToFirst()) {
            connect = last_time_wifi.getString(last_time_wifi.getColumnIndex(WiFi_Provider.WiFi_Sensor.SSID));
            Log.d("test", "wifi : "+connect);
        }
        String[] values2 = {Long.toString(laps) ,Long.toString(last_timestamp),connect};
        Cursor last_time_wifiP = context.getContentResolver().query(WiFi_Provider.WiFi_Data.CONTENT_URI, null, "TIMESTAMP BETWEEN ? AND ? AND ssid = ?", values2, WiFi_Provider.WiFi_Data.TIMESTAMP + " DESC LIMIT 1");
        String security = " ";
        if (last_time_wifiP != null && last_time_wifiP.moveToFirst()) {
            security = last_time_wifiP.getString(last_time_wifiP.getColumnIndex(WiFi_Provider.WiFi_Data.SSID));
            Log.d("test", "Security : "+security);
        }
        if (security.length() > 5)
        {
            wifiB[0] = 1;
            Log.d("test", "Private wifi");
        }
        else {
            wifiB[0] = 0;
            wifiB[1] = 0.6;
            Log.d("test", "Public wifi");
        }
        if (last_time_wifi != null && !last_time_wifi.isClosed()) {
            last_time_wifi.close();
            Log.d("test", "wifi cursor close");
        }
        if (last_time_wifiP != null && !last_time_wifiP.isClosed()) {
            last_time_wifiP.close();
            Log.d("test", "wifi cursor close");
        }
        Log.d("test", "wifi end");
        return wifiB;
    }

    public double[] getBattery() {
        batteryB[1] = 0.99;
        Cursor last_time_battery = context.getContentResolver().query(Battery_Provider.Battery_Data.CONTENT_URI, null, null, null, Battery_Provider.Battery_Data.TIMESTAMP + " DESC LIMIT 1");
        if (last_time_battery != null && last_time_battery.moveToFirst()) {
            int x = last_time_battery.getInt(last_time_battery.getColumnIndex(Battery_Provider.Battery_Data.PLUG_ADAPTOR));
            Log.d("test", "battery : " + x);
            if (x == 1 || x == 2 || x == 4)
                batteryB[0] = 1;
            else
                batteryB[0] = 0;
        }
        if (last_time_battery != null && !last_time_battery.isClosed()) {
            last_time_battery.close();
            Log.d("test", "bat cursor close");
        }
        Log.d("test", "bat end");
        return batteryB;
    }

    public double[] getScreen() {
        screenB[1] = 1;
        Cursor last_time_screen = context.getContentResolver().query(Screen_Provider.Screen_Data.CONTENT_URI, null, null, null, Screen_Provider.Screen_Data.TIMESTAMP + " DESC LIMIT 1");
        if (last_time_screen != null && last_time_screen.moveToFirst()) {
            double x = last_time_screen.getDouble(last_time_screen.getColumnIndex(Screen_Provider.Screen_Data.SCREEN_STATUS));
            Log.d("test", "screen : "+x);
            if(x == 3)
                screenB[0] = 1;
            else {
                screenB[0] = 0;
                screenB[1] = 0.9;
            }
        }
        if (last_time_screen != null && !last_time_screen.isClosed()) {
            last_time_screen.close();
            Log.d("test", "screen cursor close");
        }
        return screenB;
    }
}
