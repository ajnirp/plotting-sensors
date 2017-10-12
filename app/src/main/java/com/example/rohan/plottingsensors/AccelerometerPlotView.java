package com.example.rohan.plottingsensors;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by rohan on 10/8/17.
 */

public class AccelerometerPlotView extends View {
    private ArrayList<Float> data;
    private ArrayList<Float> means;
    private ArrayList<Float> stddevs;

    public void clearList() { data.clear(); }
    private int DATA_SIZE = 10;

    private float range;

    private Paint paint;

    public void setRange(float f) {
        range = f;
    }

    public void addPoint(float f) {
        data.add(f);

        if (data.size() > 3) {
            Pair meanAndStdDev = computeRecentMeanAndStdDev();
            means.add(meanAndStdDev.f);
            stddevs.add(meanAndStdDev.s);
        } else {
            means.add(f);
            stddevs.add(f);
        }

        checkLength(data);
        checkLength(means);
        checkLength(stddevs);
    }

    private void checkLength(ArrayList<Float> array) {
        if (array.size() > DATA_SIZE) {
            array.remove(0);
        }
    }

    // Compute the mean of the last 3 elements in 'data'
    private Pair computeRecentMeanAndStdDev() {
        Pair result = new Pair();
        int length = data.size();
        float a = data.get(length-1);
        float b = data.get(length-2);
        float c = data.get(length-3);
        result.f = (a+b+c)/3;
        float da = a - result.f;
        float db = b - result.f;
        float dc = c - result.f;
        result.s = (float) Math.sqrt((da*da + db*db + dc*dc)/2);
        return result;
    }

    public AccelerometerPlotView(Context context) {
        super(context);
        paint = new Paint();
        data = new ArrayList<Float>();
        means = new ArrayList<Float>();
        stddevs = new ArrayList<Float>();
    }

    public AccelerometerPlotView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        paint = new Paint();
        data = new ArrayList<Float>();
        means = new ArrayList<Float>();
        stddevs = new ArrayList<Float>();
    }

    public AccelerometerPlotView(Context context, AttributeSet attributeSet, int defStyleAttr) {
        this(context, attributeSet);
    }

    public AccelerometerPlotView(Context context, AttributeSet attributeSet, int defStyleAttr, int defStyleRes) {
        this(context, attributeSet);
    }

    Pair getCanvasCoords(int x, float y, int width, int height) {
        Pair result = new Pair();
        result.f = (x+1) * width * 0.9f / DATA_SIZE;
        result.s = - (0.05f * height) + (range - y) * height * 0.9f / range;
        return result;
    }

    private void drawArray(Canvas canvas, ArrayList<Float> array, int width, int height, int color) {
        for (int i = 0; i < array.size(); i++) {
            Pair coords = getCanvasCoords(i, array.get(i), width, height);
            paint.setColor(color);
            canvas.drawCircle(coords.f, coords.s, 20, paint);
            if (i < array.size()-1) {
                Pair coordsNext = getCanvasCoords(i+1, array.get(i+1), width, height);
                paint.setColor(Color.BLACK);
                canvas.drawLine(coords.f, coords.s, coordsNext.f, coordsNext.s, paint);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        paint.setStyle(Paint.Style.FILL);
        drawArray(canvas, data, width, height, Color.RED);
        drawArray(canvas, means, width, height, Color.BLUE);
        drawArray(canvas, stddevs, width, height, Color.GREEN);
    }
}
