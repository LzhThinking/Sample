package com.lzh.sample.mpandroidchart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

public class XAxisValueFormatter implements IAxisValueFormatter {
    private String[] xStrs = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};

    @Override

    public String getFormattedValue(float value, AxisBase axis) {
        int position = (int) value;
        return xStrs[position % xStrs.length];
    }
}
