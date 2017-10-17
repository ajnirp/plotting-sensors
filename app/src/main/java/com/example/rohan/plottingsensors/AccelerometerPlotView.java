package com.example.rohan.plottingsensors;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

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
    private int POINT_RADIUS = 20;

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
        result.f = width * 0.05f + (x+1) * width * 0.9f / DATA_SIZE;
        result.s = - (0.05f * height) + (range - y) * height * 0.93f / range;
        return result;
    }

    private void drawArray(Canvas canvas, ArrayList<Float> array, int width, int height, int color) {
        for (int i = 0; i < array.size(); i++) {
            Pair coords = getCanvasCoords(i, array.get(i), width, height);
            paint.setColor(color);
            canvas.drawCircle(coords.f, coords.s, POINT_RADIUS, paint);
            if (i < array.size()-1) {
                Pair coordsNext = getCanvasCoords(i+1, array.get(i+1), width, height);
                canvas.drawLine(coords.f, coords.s, coordsNext.f, coordsNext.s, paint);
            }
        }
    }

    public float getRange() {
        return range;
    }

    private void drawXYLabels(Canvas canvas, int width, int height) {
        paint.setColor(Color.BLACK);
        paint.setTextSize(30f);

        // x label
        String xLabel = "Time (x100 msec)";
        canvas.drawText(xLabel, 0, xLabel.length(), 0.4f*width, 0.99f*height, paint);

        // y label
        String yLabel = "Data (% of max range)";
        canvas.save();
        canvas.rotate(-90f);
        canvas.drawText(yLabel, 0, yLabel.length(), height*-0.65f, width*0.05f, paint);
        canvas.restore();
    }

    private void drawAxes(Canvas canvas, int width, int height) {
        // vertical
        for (int i = 0; i < DATA_SIZE; i++) {
            float cx = width * 0.05f + (i+1) * width * 0.9f / DATA_SIZE;
            paint.setColor(0xffafafaf);
            canvas.drawLine(cx, 0, cx, height * 0.85f, paint);
            String label = Integer.toString(i);
            paint.setColor(Color.BLACK);
            canvas.drawText(label, 0, label.length(), cx, height*0.95f, paint);
        }

        // horizontal
        int num_horiz_lines = 10;
        for (int i = 0; i < num_horiz_lines; i++) {
            float cx = width * 0.14f;
            paint.setColor(Color.BLACK);
            String label = Integer.toString(i * 100 / num_horiz_lines);
            float cy = - (0.05f * height) + (num_horiz_lines - i) * height * 0.93f / num_horiz_lines;
            canvas.drawLine(cx, cy, width, cy, paint);
            canvas.drawText(label, 0, label.length(), width * 0.075f, cy, paint);
        }
    }

    private void drawData(Canvas canvas, int width, int height) {
        drawArray(canvas, data, width, height, Color.RED);
        drawArray(canvas, means, width, height, Color.BLUE);
        drawArray(canvas, stddevs, width, height, Color.GREEN);
    }

    private void drawLegend(Canvas canvas, int width, int height) {
        paint.setColor(Color.GREEN);
        canvas.drawCircle(width*0.3f, height*0.06f, POINT_RADIUS, paint);
        paint.setColor(Color.BLUE);
        canvas.drawCircle(width*0.55f, height*0.06f, POINT_RADIUS, paint);
        paint.setColor(Color.RED);
        canvas.drawCircle(width*0.8f, height*0.06f, POINT_RADIUS, paint);

        String label;
        paint.setColor(Color.BLACK);
        label = "Value";
        canvas.drawText(label, 0, label.length(), width * 0.26f, height * 0.03f, paint);
        label = "Mean";
        canvas.drawText(label, 0, label.length(), width * 0.51f, height * 0.03f, paint);
        label = "StdDev";
        canvas.drawText(label, 0, label.length(), width * 0.76f, height * 0.03f, paint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = canvas.getWidth();
        int height = canvas.getHeight();
        paint.setStyle(Paint.Style.FILL);
        drawData(canvas, width, height);
        drawXYLabels(canvas, width, height);
        drawAxes(canvas, width, height);
        drawLegend(canvas, width, height);
    }
}
