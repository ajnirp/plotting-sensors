package com.example.rohan.plottingsensors;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sm;
    private Sensor accelerometer;
    private Sensor gyroscope;
    private List<Sensor> l;

    private float ax, ay, az; // Acceleration

    private int SENSOR_DURATION = 1000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        l = sm.getSensorList(Sensor.TYPE_ALL);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gyroscope = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        TextView v = (TextView) findViewById(R.id.accelerometer_info);
        if (accelerometer != null) {
            float res = accelerometer.getResolution();
            float range = accelerometer.getMaximumRange();
            int minDelay = accelerometer.getMinDelay();
            int maxDelay = accelerometer.getMaxDelay();
            String message = "Resolution: " + res + " Range: " + range + " Min delay: " + minDelay;
            v.setText(message.toCharArray(), 0, message.length());
            sm.registerListener(this, accelerometer, SENSOR_DURATION);
        } else {
            String message = "No accelerometer detected";
            v.setText(message.toCharArray(), 0, message.length());
        }

        v = (TextView) findViewById(R.id.gyroscope_info);
        if (gyroscope != null) {
            float res = gyroscope.getResolution();
            float range = gyroscope.getMaximumRange();
            int minDelay = gyroscope.getMinDelay();
            int maxDelay = gyroscope.getMaxDelay();
            String message = "Resolution: " + res + " Range: " + range + " Min delay: " + minDelay;
            v.setText(message.toCharArray(), 0, message.length());
            sm.registerListener(this, accelerometer, SENSOR_DURATION);
        } else {
            String message = "No gyroscope detected";
            v.setText(message.toCharArray(), 0, message.length());
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        ax = event.values[0];
        ay = event.values[1];
        az = event.values[2];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    @Override
    protected void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sm.registerListener(this, accelerometer, SENSOR_DURATION);
    }

    public void startAccelerometerActivity(View v) {
        Intent intent = new Intent(this, PlotAccelerometerActivity.class);
        startActivity(intent);
    }

    public void startLightActivity(View v) {
        Intent intent = new Intent(this, PlotGyroscopeActivity.class);
        startActivity(intent);
    }
}
