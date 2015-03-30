package com.aware.plugin.inoutdoor;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.util.Log;

import com.aware.Accelerometer;
import com.aware.Aware;
import com.aware.Aware_Preferences;
import com.aware.utils.Aware_Plugin;

import java.util.List;

public class Plugin extends Aware_Plugin {

    public static Boolean magnetometer = false;
    public static Boolean light = false;
    public static Boolean temperature = false;
    public static final String ACTION_AWARE_PLUGIN_INOUTDOOR = "ACTION_AWARE_PLUGIN_INOUTDOOR";
    private static ArbreBinaire arbre;
    public InOutAlarm alarm = new InOutAlarm();
    public static int temp_interval = 5;
    private static InOutDoor test;

    private static ContextProducer contextProducer;

    @Override
    public void onCreate() {
        super.onCreate();

        TAG = "InOutdoor";
        DEBUG = Aware.getSetting(this, Aware_Preferences.DEBUG_FLAG).equals("true");

        Intent aware = new Intent(this, Aware.class);
        startService(aware);

        Aware.setSetting(getApplicationContext(), Settings.STATUS_PLUGIN, true);

        if( Aware.getSetting(getApplicationContext(), Settings.FREQUENCY_PLUGIN).length() == 0 ) {
            Aware.setSetting(getApplicationContext(), Settings.FREQUENCY_PLUGIN, 5);
        }

        SensorManager mgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        List<Sensor> sensors = mgr.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : sensors) {
            Log.d("test", "" + sensor.getName());

            if (sensor.getName().contains("Magnetic")) {
                magnetometer = true;
            }
            if (sensor.getName().contains("Light")) {
                light = true;
            }
            if (sensor.getName().contains("Temperature")) {
                temperature = true;
            }
        }
        Log.d("test", "magnetometer : "+magnetometer+" Light : "+light+" temp : "+temperature);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_SCREEN, true);
        sendBroadcast(new Intent(Aware.ACTION_AWARE_REFRESH));

        test = new InOutDoor(this);

        arbre = ArbreBinaire.createGraph(magnetometer, light, temperature);

        int interval_min =  Integer.parseInt(Aware.getSetting(getApplicationContext(), Settings.FREQUENCY_PLUGIN));
        alarm.SetAlarm(Plugin.this, interval_min);
        temp_interval = interval_min;

        CONTEXT_PRODUCER = new ContextProducer() {
            @Override
            public void onContext() {
                String a = "";
                double k = 0;
                Cursor last_time = getApplicationContext().getContentResolver().query(Provider.IOMeter_Data.CONTENT_URI, null, null, null, Provider.IOMeter_Data.TIMESTAMP + " DESC LIMIT 1");
                if (last_time != null && last_time.moveToFirst()) {
                    a = last_time.getString(last_time.getColumnIndex(Provider.IOMeter_Data.IO_STATUS));
                    k = last_time.getDouble(last_time.getColumnIndex(Provider.IOMeter_Data.IO_CONFIDENCE));
                }
                if (last_time != null && !last_time.isClosed()) {
                    last_time.close();
                }
                Intent context_inout = new Intent();
                context_inout.setAction(ACTION_AWARE_PLUGIN_INOUTDOOR);
                context_inout.putExtra(Provider.IOMeter_Data.IO_STATUS, a);
                context_inout.putExtra(Provider.IOMeter_Data.IO_CONFIDENCE, k);
                sendBroadcast(context_inout);
            }
        };
        contextProducer = CONTEXT_PRODUCER;

        DATABASE_TABLES = Provider.DATABASE_TABLES;
        TABLES_FIELDS = Provider.TABLES_FIELDS;
        CONTEXT_URIS = new Uri[]{ Provider.IOMeter_Data.CONTENT_URI };

        if (DEBUG) Log.d(TAG, "Plugin running");
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("test", "onStartCommand");
        int interval_min =  Integer.parseInt(Aware.getSetting(getApplicationContext(), Settings.FREQUENCY_PLUGIN));

        if (interval_min != temp_interval) {
            Log.d("test", "differ");
            if(interval_min >= 1) {
                Log.d("test", "bigger");
                alarm.CancelAlarm(Plugin.this);
                alarm.SetAlarm(Plugin.this, interval_min);
                temp_interval = interval_min;
            } else {
                Log.d("test", "zero");
                temp_interval = interval_min;
                alarm.CancelAlarm(Plugin.this);

                Intent apply = new Intent(Aware.ACTION_AWARE_REFRESH);
                getApplicationContext().sendBroadcast(apply);
            }
        }

        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();

        alarm.CancelAlarm(Plugin.this);

        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_ACCELEROMETER, false);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_MAGNETOMETER, false);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_LIGHT, false);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_TEMPERATURE, false);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_WIFI, false);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_BATTERY, false);
        Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_SCREEN, false);

        sendBroadcast(new Intent(Aware.ACTION_AWARE_REFRESH));

        if (DEBUG) Log.d(TAG, "Plugin terminated");
    }

    protected static void getInout(Context context) {
        Log.d("test", "getInOut");
        try {
            Thread.sleep(10000);
        } catch(InterruptedException ex) {
            Thread.currentThread().interrupt();
        }

        Intent inout_Service = new Intent(context, Inout_Service.class);
        context.startService(inout_Service);
    }
    public static class Inout_Service extends IntentService {
        public Inout_Service() {
            super("AWARE INOUT");
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            Log.d("test", "onHandleIntent");
            Aware.setSetting(this, Aware_Preferences.STATUS_ACCELEROMETER, true);
            if (magnetometer) {
                Aware.setSetting(this, Aware_Preferences.STATUS_MAGNETOMETER, true);
            }
            if (light) {
                Aware.setSetting(this, Aware_Preferences.STATUS_LIGHT, true);
            }
            if (temperature) {
                Aware.setSetting(this, Aware_Preferences.STATUS_TEMPERATURE, true);
            }
            Aware.setSetting(this, Aware_Preferences.STATUS_WIFI, true);
            Aware.setSetting(this, Aware_Preferences.FREQUENCY_WIFI, 5);
            Aware.setSetting(this, Aware_Preferences.STATUS_BATTERY, true);

            Intent applySettings = new Intent(Aware.ACTION_AWARE_REFRESH);
            sendBroadcast(applySettings);

            try {
                Thread.sleep(15000);
            } catch(InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

            Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_ACCELEROMETER, false);
            Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_MAGNETOMETER, false);
            Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_LIGHT, false);
            Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_TEMPERATURE, false);
            Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_WIFI, false);
            Aware.setSetting(getApplicationContext(), Aware_Preferences.STATUS_BATTERY, false);
            Aware.setSetting(this, Aware_Preferences.STATUS_LOCATION_GPS,false);

            Intent apply = new Intent(Aware.ACTION_AWARE_REFRESH);
            getApplicationContext().sendBroadcast(apply);

            double[] in = ArbreBinaire.deapthSearch(test, arbre);
            String texte;
            if (in[0] == 1)
                texte = "indoor";
            else
                texte = "outdoor";

            if (DEBUG) Log.d("test", "in = " + texte+", confidence : "+in[1]);

            addData(in);
        }
        public void addData(double[] data)
        {
            String a;
            if (data[0] == 1)
                a = "indoor";
            else
                a = "outdoor";

            ContentValues context_data = new ContentValues();
            context_data.put(Provider.IOMeter_Data.TIMESTAMP, System.currentTimeMillis());
            context_data.put(Provider.IOMeter_Data.DEVICE_ID, Aware.getSetting(getApplicationContext(), Aware_Preferences.DEVICE_ID));
            context_data.put(Provider.IOMeter_Data.IO_STATUS, a);
            context_data.put(Provider.IOMeter_Data.IO_CONFIDENCE, data[1]);

            if( DEBUG ) Log.d(TAG, context_data.toString());

            //insert data to table
            getContentResolver().insert(Provider.IOMeter_Data.CONTENT_URI, context_data);
            contextProducer.onContext();
        }
    }

}