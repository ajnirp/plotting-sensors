package com.example.rohan.plottingsensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class PlotGyroscopeActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sm;
    private Sensor gyroscopeSensor;
    private AccelerometerPlotView pv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot_gyroscope);

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroscopeSensor = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        pv = (AccelerometerPlotView) findViewById(R.id.gyroscope_canvas);
        pv.setRange(gyroscopeSensor.getMaximumRange());
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float ax = event.values[0];
        float ay = event.values[1];
        float az = event.values[2];
        float a = (float) Math.sqrt(ax*ax + ay*ay + az*az);
        pv.addPoint(a);
        pv.invalidate();
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
        sm.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void goBack(View v) {
        super.onBackPressed();
    }
}
