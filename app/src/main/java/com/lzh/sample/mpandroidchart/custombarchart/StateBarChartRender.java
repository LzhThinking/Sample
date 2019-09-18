package com.lzh.sample.mpandroidchart.custombarchart;

import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.Log;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.buffer.BarBuffer;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.highlight.Range;
import com.github.mikephil.charting.interfaces.dataprovider.BarDataProvider;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.renderer.BarChartRenderer;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;

public class StateBarChartRender extends BarChartRenderer {
    public StateBarChartRender(BarDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
    }

    /**
     * 重写这个方法，设置每根柱子的x值和宽度
     */
    @Override
    public void initBuffers() {
        BarData barData = mChart.getBarData();
        mBarBuffers = new BarBuffer[barData.getDataSetCount()];

        for (int i = 0; i < mBarBuffers.length; i++) {
            IBarDataSet set = barData.getDataSetByIndex(i);
            mBarBuffers[i] = new StateBarBuffer(set.getEntryCount() * 4 * (set.isStacked() ? set.getStackSize() : 1),
                    barData.getDataSetCount(), set.isStacked());
        }
    }

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {
        BarData barData = mChart.getBarData();

        for (Highlight highlight : indices) {
            IBarDataSet set = barData.getDataSetByIndex(highlight.getDataSetIndex());

            if (set == null || !set.isHighlightEnabled())
                continue;

            BarEntry e = set.getEntryForXValue(highlight.getX(), highlight.getY());

            if (!isInBoundsX(e, set))
                continue;

            Transformer trans = mChart.getTransformer(set.getAxisDependency());

            mHighlightPaint.setColor(set.getHighLightColor());
            mHighlightPaint.setAlpha(set.getHighLightAlpha());

            boolean isStack = (highlight.getStackIndex() >= 0  && e.isStacked()) ? true : false;

            final float y1;
            final float y2;

            if (isStack) {

                if(mChart.isHighlightFullBarEnabled()) {

                    y1 = e.getPositiveSum();
                    y2 = -e.getNegativeSum();

                } else {

                    Range range = e.getRanges()[highlight.getStackIndex()];

                    y1 = range.from;
                    y2 = range.to;
                }

            } else {
                y1 = e.getY();
                y2 = 0.f;
            }

            float viewWidth = mChart.getWidth();
            //动态修改barWidth，否则放大时barWidth也会跟着变大
            float barWidth = (Utils.convertDpToPixel(0.5f) / viewWidth) * (barData.getXMax() - barData.getXMin())/ mViewPortHandler.getScaleX();
            prepareBarHighlight(highlight.getX(), y1, y2, barWidth, trans);
            RectF contentRect = mChart.getContentRect();
            Log.d("zhiheng", "barWidth = " + barWidth + ", left = " + contentRect.left + ", right = " + contentRect.right + ", width = " + viewWidth + ", scale = " + mViewPortHandler.getScaleX());

            setHighlightDrawPos(highlight, mBarRect);

            c.drawRect(mBarRect, mHighlightPaint);
        }
    }

    protected void prepareBarHighlight(float x, float y1, float y2, float barWidthHalf, Transformer trans) {

        float left = x - barWidthHalf;
        float right = x + barWidthHalf;
        float top = y1;
        float bottom = y2;

        mBarRect.set(left, top, right, bottom);

        trans.rectToPixelPhase(mBarRect, mAnimator.getPhaseY());
    }
}
