package com.lzh.sample.mpandroidchart;

import android.graphics.Canvas;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.renderer.DataRenderer;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class MyBarChartRenderer extends BarChartRenderer {
    protected int renderColor;
    public MyBarChartRenderer(BarDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
    }

    public int getRenderColor() {
        return renderColor;
    }

    public void setRenderColor(int renderColor) {
        this.renderColor = renderColor;
    }

    @Override
    public void drawData(Canvas c) {
        mRenderPaint.setColor(renderColor);
//        c.translate(300, 100);
//        c.rotate(30);
        super.drawData(c);
    }
}
