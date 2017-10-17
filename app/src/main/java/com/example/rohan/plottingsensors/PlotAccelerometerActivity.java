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

public class PlotAccelerometerActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sm;
    private Sensor accelerometer;
    private AccelerometerPlotView pv;

    private int SENSOR_DURATION = 100000;

    private int currentAnimation = 1;

    private boolean animationLocked = false;

    private ImageView i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot_accelerometer);

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sm.registerListener(this, accelerometer, SENSOR_DURATION);

        pv = (AccelerometerPlotView) findViewById(R.id.accelerometer_canvas);
        pv.setRange(accelerometer.getMaximumRange());

        i = (ImageView) findViewById(R.id.accelerometer_animation);
        i.setBackgroundResource(R.drawable.car1);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float ax = event.values[0];
        float ay = event.values[1];
        float az = event.values[2];
        float a = (float) Math.sqrt(ax*ax + ay*ay + az*az);
        pv.addPoint(a);
        updateAnimation(a);
        pv.invalidate();
    }

//    private void updateAnimation(float f) {
//        if (animationLocked) {
//            return;
//        }
//
//        float ratio = f / pv.getRange();
//
//        int newAnimation;
//        if (ratio < 0.2) {
//            newAnimation = 1;
//        } else if (ratio >= 0.2 && ratio < 0.5) {
//            newAnimation = 2;
//        } else {
//            newAnimation = 3;
//        }
//
//        if (newAnimation != currentAnimation) {
//            ImageView i = (ImageView) findViewById(R.id.bird_view);
//            if (newAnimation == 1) {
//                i.setBackgroundResource(R.drawable.bird_slow);
//            } else if (newAnimation == 2) {
//                i.setBackgroundResource(R.drawable.bird_medium);
//            } else {
//                i.setBackgroundResource(R.drawable.bird_fast);
//            }
//            currentAnimation = newAnimation;
//            ((AnimationDrawable) i.getBackground()).start();
//        }
//    }

    private void updateAnimation(float f) {
        float ratio = f / pv.getRange();
        if (ratio < 0.2) {
            i.setBackgroundResource(R.drawable.car1);
        } else if (ratio >= 0.2 && ratio < 0.4) {
            i.setBackgroundResource(R.drawable.car2);
        } else if (ratio >= 0.4 && ratio < 0.6) {
            i.setBackgroundResource(R.drawable.car3);
        } else if (ratio >= 0.6 && ratio < 0.8) {
            i.setBackgroundResource(R.drawable.car4);
        } else if (ratio >= 0.8) {
            i.setBackgroundResource(R.drawable.car5);
        }
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

    public void goBack(View v) {
        super.onBackPressed();
    }
}
