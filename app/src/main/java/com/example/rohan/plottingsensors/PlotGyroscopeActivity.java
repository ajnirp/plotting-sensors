package com.example.rohan.plottingsensors;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class PlotGyroscopeActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sm;
    private Sensor gyroscopeSensor;
    private AccelerometerPlotView pv;

    private ImageView i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot_gyroscope);

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        gyroscopeSensor = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        pv = (AccelerometerPlotView) findViewById(R.id.gyroscope_canvas);
        pv.setRange(gyroscopeSensor.getMaximumRange());

        i = (ImageView) findViewById(R.id.gyroscope_animation);
        i.setBackgroundResource(R.drawable.car4);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float ax = event.values[0];
        float ay = event.values[1];
        float az = event.values[2];
        float a = (float) Math.sqrt(ax*ax + ay*ay + az*az);
        pv.addPoint(a);
        pv.invalidate();
        updateAnimation(a);
    }

    private void updateAnimation(float f) {
        float ratio = f / pv.getRange();
        float dr = 10;
        if (ratio < 0.2) {
            dr = 10;
        } else if (ratio >= 0.2 && ratio < 0.4) {
            dr = 40;
        } else if (ratio >= 0.4 && ratio < 0.6) {
            dr = 70;
        } else if (ratio >= 0.6 && ratio < 0.8) {
            dr = 100;
        } else if (ratio >= 0.8) {
            dr = 130;
        }
        i.setRotation(i.getRotation() + dr);
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
