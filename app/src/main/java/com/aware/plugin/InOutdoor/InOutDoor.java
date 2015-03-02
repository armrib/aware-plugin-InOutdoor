package com.aware.plugin.InOutdoor;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.aware.providers.Accelerometer_Provider;
import com.aware.providers.Barometer_Provider;
import com.aware.providers.Battery_Provider;
import com.aware.providers.Gravity_Provider;
import com.aware.providers.Gyroscope_Provider;
import com.aware.providers.Linear_Accelerometer_Provider;
import com.aware.providers.Locations_Provider;
import com.aware.providers.Magnetometer_Provider;
import com.aware.providers.Temperature_Provider;
import com.aware.providers.WiFi_Provider;

/**
 * Created by Armand on 19/02/2015.
 */
public class InOutDoor {

    private Boolean accelerometerB;
    private Boolean magneticB;
    private Boolean gyroscopeB;
    private Boolean proximityB;
    private Boolean rotationB;
    private Boolean gravityB;
    private Boolean linearB;
    private Boolean barometerB;
    private Boolean lightB;
    private Boolean locationB;
    private Boolean temperatureB;

    private Boolean linear;
    private Boolean magnetic;
    private Boolean accelerometer;
    private Boolean gyroscope;
    private Boolean proximity;
    private Boolean rotation;
    private Boolean gravity;
    private Boolean barometer;
    private Boolean light;
    private Boolean location;
    private Boolean temperature;
    private Context context;
    private Singleton instance;

    public InOutDoor(Context context, Boolean magnetic, Boolean accelerometer, Boolean gyroscope,
                     Boolean proximity, Boolean rotation, Boolean gravity, Boolean linear,
                     Boolean barometer, Boolean light, Boolean location, Boolean temperature) {
        instance = Singleton.getInstance();
        this.linear = linear;
        this.magnetic = magnetic;
        this.accelerometer = accelerometer;
        this.gyroscope = gyroscope;
        this.proximity = proximity;
        this.rotation = rotation;
        this.gravity = gravity;
        this.barometer = barometer;
        this.light = light;
        this.location = location;
        this.temperature = temperature;
        this.context = context;
    }

    public boolean getLinear() {
        if (linear) {
            Cursor last_time_linear = context.getContentResolver().query(Linear_Accelerometer_Provider.Linear_Accelerometer_Data.CONTENT_URI, null, null, null, Linear_Accelerometer_Provider.Linear_Accelerometer_Data.TIMESTAMP + " DESC LIMIT 1");

            if (last_time_linear != null && last_time_linear.moveToFirst()) {
                double x = last_time_linear.getDouble(last_time_linear.getColumnIndex(Linear_Accelerometer_Provider.Linear_Accelerometer_Data.VALUES_0));
                double y = last_time_linear.getDouble(last_time_linear.getColumnIndex(Linear_Accelerometer_Provider.Linear_Accelerometer_Data.VALUES_1));
                double z = last_time_linear.getDouble(last_time_linear.getColumnIndex(Linear_Accelerometer_Provider.Linear_Accelerometer_Data.VALUES_2));
                instance.getLinear()[0] = x;
                instance.getLinear()[1] = y;
                instance.getLinear()[2] = z;
                Log.d("test", "linear x y z" + x + " " + y + " " + z);
                if (x > 1 || y > 1 || z > 1)
                    linearB = true;
                else
                    linearB = false;
                Log.d("test", "linear " + linearB);
            }
            if (last_time_linear != null && !last_time_linear.isClosed()) {
                last_time_linear.close();
                Log.d("test", "linear cursor close");
            }
            Log.d("test", "linear end");
        } else
            Log.d("test", "linear unactivated");
        return linearB;
    }

    public boolean getAccelerometer() {
        if (accelerometer) {
            Cursor last_time_accelerometer = context.getContentResolver().query(Accelerometer_Provider.Accelerometer_Data.CONTENT_URI, null, null, null, Accelerometer_Provider.Accelerometer_Data.TIMESTAMP + " DESC LIMIT 1");

            if (last_time_accelerometer != null && last_time_accelerometer.moveToFirst()) {
                double x = last_time_accelerometer.getDouble(last_time_accelerometer.getColumnIndex(Accelerometer_Provider.Accelerometer_Data.VALUES_0));
                double y = last_time_accelerometer.getDouble(last_time_accelerometer.getColumnIndex(Accelerometer_Provider.Accelerometer_Data.VALUES_1));
                double z = last_time_accelerometer.getDouble(last_time_accelerometer.getColumnIndex(Accelerometer_Provider.Accelerometer_Data.VALUES_2));
                instance.getAccelerometer()[0] = x;
                instance.getAccelerometer()[1] = y;
                instance.getAccelerometer()[2] = z;
                Log.d("test", "accel x y z" + x + " " + y + " " + z);
                if (x > 1 || y > 1 || z > 1)
                    accelerometerB = true;
                else
                    accelerometerB = false;
            }
            if (last_time_accelerometer != null && !last_time_accelerometer.isClosed()) {
                last_time_accelerometer.close();
                Log.d("test", "accel cursor close");
            }
            Log.d("test", "accel end");
        } else
            Log.d("test", "accel unactivated");
        return accelerometerB;
    }

    public boolean getGyroscope() {
        if (gyroscope) {
            Cursor last_time_gyroscope = context.getContentResolver().query(Gyroscope_Provider.Gyroscope_Data.CONTENT_URI, null, null, null, Gyroscope_Provider.Gyroscope_Data.TIMESTAMP + " DESC LIMIT 1");
            if (last_time_gyroscope != null && last_time_gyroscope.moveToFirst()) {
                double x = last_time_gyroscope.getDouble(last_time_gyroscope.getColumnIndex(Gyroscope_Provider.Gyroscope_Data.VALUES_0));
                double y = last_time_gyroscope.getDouble(last_time_gyroscope.getColumnIndex(Gyroscope_Provider.Gyroscope_Data.VALUES_1));
                double z = last_time_gyroscope.getDouble(last_time_gyroscope.getColumnIndex(Gyroscope_Provider.Gyroscope_Data.VALUES_2));
                instance.getAccelerometer()[0] = x;
                instance.getAccelerometer()[1] = y;
                instance.getAccelerometer()[2] = z;
                Log.d("test", "gyro x y z" + x + " " + y + " " + z);

                if (x > 1 || y > 1 || z > 1)
                    gyroscopeB = true;
                else
                    gyroscopeB = false;
            }
            if (last_time_gyroscope != null && !last_time_gyroscope.isClosed()) {
                last_time_gyroscope.close();
                Log.d("test", "gyro cursor close");
            }
            Log.d("test", "gyro end");
        } else
            Log.d("test", "gyro unactivated");
        return gyroscopeB;
    }

    public boolean getTemp() {
        if (temperature) {
            Cursor last_time_temperature = context.getContentResolver().query(Temperature_Provider.Temperature_Data.CONTENT_URI, null, null, null, Temperature_Provider.Temperature_Data.TIMESTAMP + " DESC LIMIT 1");
            if (last_time_temperature != null && last_time_temperature.moveToFirst()) {
                double x = last_time_temperature.getDouble(last_time_temperature.getColumnIndex(Temperature_Provider.Temperature_Data.TEMPERATURE_CELSIUS));
                //instance.getTemp() = x;
                Log.d("test", "temp x" + x);

                if (x > 1)
                    temperatureB = true;
                else
                    temperatureB = false;
            }
            if (last_time_temperature != null && !last_time_temperature.isClosed()) {
                last_time_temperature.close();
                Log.d("test", "temp cursor close");
            }


            Log.d("test", "temp end");
        } else
            Log.d("test", "temp unactivated");
        return temperatureB;
    }

    public boolean getMagnetic() {
        if (magnetic) {
            Cursor last_time_magnetometer = context.getContentResolver().query(Magnetometer_Provider.Magnetometer_Data.CONTENT_URI, null, null, null, Magnetometer_Provider.Magnetometer_Data.TIMESTAMP + " DESC LIMIT 1");
            if (last_time_magnetometer != null && last_time_magnetometer.moveToFirst()) {
                double x = last_time_magnetometer.getDouble(last_time_magnetometer.getColumnIndex(Magnetometer_Provider.Magnetometer_Data.VALUES_0));
                double y = last_time_magnetometer.getDouble(last_time_magnetometer.getColumnIndex(Magnetometer_Provider.Magnetometer_Data.VALUES_1));
                double z = last_time_magnetometer.getDouble(last_time_magnetometer.getColumnIndex(Magnetometer_Provider.Magnetometer_Data.VALUES_2));
                instance.getMagnetic()[0] = x;
                instance.getMagnetic()[1] = y;
                instance.getMagnetic()[2] = z;
                Log.d("test", "gyro x y z" + x + " " + y + " " + z);

                if (x > 1 || y > 1 || z > 1)
                    magneticB = true;
                else
                    magneticB = false;
            }
            if (last_time_magnetometer != null && !last_time_magnetometer.isClosed()) {
                last_time_magnetometer.close();
                Log.d("test", "mag cursor close");
            }
            Log.d("test", "mag end");
        } else
            Log.d("test", "temp unactivated");
        return magneticB;
    }

    public boolean getLocation() {
        if (location) {
            Cursor last_time_locationGPS = context.getContentResolver().query(Locations_Provider.Locations_Data.CONTENT_URI, null, null, null, Locations_Provider.Locations_Data.TIMESTAMP + " DESC LIMIT 1");
            if (last_time_locationGPS != null && last_time_locationGPS.moveToFirst()) {
                double x = last_time_locationGPS.getDouble(last_time_locationGPS.getColumnIndex(Locations_Provider.Locations_Data.LATITUDE));
                double y = last_time_locationGPS.getDouble(last_time_locationGPS.getColumnIndex(Locations_Provider.Locations_Data.LONGITUDE));
                double z = last_time_locationGPS.getDouble(last_time_locationGPS.getColumnIndex(Locations_Provider.Locations_Data.ALTITUDE));
                // ajouter les deux autres !
                instance.getLocation()[0] = x;
                instance.getLocation()[1] = y;
                instance.getLocation()[2] = z;
                Log.d("test", "loca x y z" + x + " " + y + " " + z);

                if (x > 1 || y > 1 || z > 1)
                    locationB = true;
                else
                    locationB = false;
            }
            if (last_time_locationGPS != null && !last_time_locationGPS.isClosed()) {
                last_time_locationGPS.close();
                Log.d("test", "loca cursor close");
            }
            Log.d("test", "loca end");
        } else
            Log.d("test", "temp unactivated");
        return locationB;
    }

    public boolean getBarometer() {
        if (barometer) {
            Cursor last_time_barometer = context.getContentResolver().query(Barometer_Provider.Barometer_Data.CONTENT_URI, null, null, null, Barometer_Provider.Barometer_Data.TIMESTAMP + " DESC LIMIT 1");
            if (last_time_barometer != null && last_time_barometer.moveToFirst()) {
                double x = last_time_barometer.getDouble(last_time_barometer.getColumnIndex(Barometer_Provider.Barometer_Data.AMBIENT_PRESSURE));
                //instance.getBaro() = x;
                Log.d("test", "baro x" + x);

                if (x > 1)
                    barometerB = true;
                else
                    barometerB = false;
            }
            if (last_time_barometer != null && !last_time_barometer.isClosed()) {
                last_time_barometer.close();
                Log.d("test", "baro cursor close");
            }
            Log.d("test", "baro end");
        } else
            Log.d("test", "baro unactivated");
        return barometerB;
    }

    public boolean getGravity() {
        if (gravity) {
            Cursor last_time_gravity = context.getContentResolver().query(Gravity_Provider.Gravity_Data.CONTENT_URI, null, null, null, Gravity_Provider.Gravity_Data.TIMESTAMP + " DESC LIMIT 1");
            if (last_time_gravity != null && last_time_gravity.moveToFirst()) {
                double[] data = new double[3];
                data[0] = last_time_gravity.getDouble(last_time_gravity.getColumnIndex(Gravity_Provider.Gravity_Data.VALUES_0));
                data[1] = last_time_gravity.getDouble(last_time_gravity.getColumnIndex(Gravity_Provider.Gravity_Data.VALUES_1));
                data[2] = last_time_gravity.getDouble(last_time_gravity.getColumnIndex(Gravity_Provider.Gravity_Data.VALUES_2));
                instance.setGravity(data);
                Log.d("test", "gravity x y z" + data);

                if (data[0] > 1 || data[1] > 1 || data[2] > 1)
                    gravityB = true;
                else
                    gravityB = false;
            }
            if (last_time_gravity != null && !last_time_gravity.isClosed()) {
                last_time_gravity.close();
                Log.d("test", "gravity cursor close");
            }
            Log.d("test", "gravity end");
        } else
            Log.d("test", "gravity unactivated");
        return gravityB;
    }

    public boolean getWifi() {
        Cursor last_time_wifi = context.getContentResolver().query(WiFi_Provider.WiFi_Data.CONTENT_URI, null, null, null, WiFi_Provider.WiFi_Data.TIMESTAMP + " DESC LIMIT 1");
        if (last_time_wifi != null && last_time_wifi.moveToFirst()) {
            String x = last_time_wifi.getString(last_time_wifi.getColumnIndex(WiFi_Provider.WiFi_Data.SSID));
            instance.setWifi(x);
            Log.d("test", "wifi x" + x);
        }
        if (last_time_wifi != null && !last_time_wifi.isClosed()) {
            last_time_wifi.close();
            Log.d("test", "wifi cursor close");
        }
        Log.d("test", "wifi end");
        return true;
    }

    public boolean getBattery() {
        Cursor last_time_battery = context.getContentResolver().query(Battery_Provider.Battery_Data.CONTENT_URI, null, null, null, Battery_Provider.Battery_Data.STATUS + " DESC LIMIT 1");
        if (last_time_battery != null && last_time_battery.moveToFirst()) {
            String x = last_time_battery.getString(last_time_battery.getColumnIndex(Battery_Provider.Battery_Data.STATUS));
            instance.setBattery(x);
            Log.d("test", "bat = " + x);
        }
        if (last_time_battery != null && !last_time_battery.isClosed()) {
            last_time_battery.close();
            Log.d("test", "bat cursor close");
        }
        Log.d("test", "bat end");
        return true;
    }
}
