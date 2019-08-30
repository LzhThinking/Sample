package com.lzh.sample.mpandroidchart;

import android.content.Context;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.Chart;

public class MyChart extends Chart {
    public MyChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void notifyDataSetChanged() {

    }

    @Override
    protected void calculateOffsets() {

    }

    @Override
    protected void calcMinMax() {

    }

    @Override
    public float getYChartMin() {
        return 0;
    }

    @Override
    public float getYChartMax() {
        return 0;
    }

    @Override
    public int getMaxVisibleCount() {
        return 0;
    }
}
