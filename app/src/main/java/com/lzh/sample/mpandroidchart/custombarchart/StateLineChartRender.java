package com.lzh.sample.mpandroidchart.custombarchart;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;

import com.github.mikephil.charting.animation.ChartAnimator;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.dataprovider.LineDataProvider;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineScatterCandleRadarDataSet;
import com.github.mikephil.charting.renderer.LineChartRenderer;
import com.github.mikephil.charting.utils.MPPointD;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * @Author leizhiheng
 * @CreateDate 2019/9/4
 * @Description
 */
public class StateLineChartRender extends LineChartRenderer {

    private Path mHighlightLinePath = new Path();
    private Path mStepLinePath = new Path();
    private float[] mLineBuffer = new float[4];

    public StateLineChartRender(LineDataProvider chart, ChartAnimator animator, ViewPortHandler viewPortHandler) {
        super(chart, animator, viewPortHandler);
        mRenderPaint.setStrokeJoin(Paint.Join.ROUND);
    }

    @Override
    public void drawHighlighted(Canvas c, Highlight[] indices) {

        LineData lineData = mChart.getLineData();

        for (Highlight high : indices) {

            ILineDataSet set = lineData.getDataSetByIndex(high.getDataSetIndex());

            if (set == null || !set.isHighlightEnabled())
                continue;

            Entry e = set.getEntryForXValue(high.getX(), high.getY());

            if (!isInBoundsX(e, set))
                continue;

            //MarkerView的位置时参照e.getY()来设置的，这里直接固定为数据的最大值，表示MarkerView固定显示在顶部
            MPPointD pix = mChart.getTransformer(set.getAxisDependency()).getPixelForValues(e.getX(), /*e.getY()*/ StateChartConstants.MAX_ENTRY_VALUE * mAnimator.getPhaseY());

            //在触摸点的x处绘制highlight
            high.setDraw(high.getXPx()/*pix.x*/, (float) pix.y);

            // draw the lines
            drawHighlightLines(c, high.getXPx()/*(float) pix.x*/, (float) pix.y, set);
        }
    }

    @Override
    public void drawHighlightLines(Canvas c, float x, float y, ILineScatterCandleRadarDataSet set) {
        // set color and stroke-width
        mHighlightPaint.setColor(set.getHighLightColor());
        mHighlightPaint.setStrokeWidth(set.getHighlightLineWidth());

        // draw highlighted lines (if enabled)
        mHighlightPaint.setPathEffect(set.getDashPathEffectHighlight());

        LineChart lineChart = (LineChart) mChart;
        // draw vertical highlight lines
        if (set.isVerticalHighlightIndicatorEnabled()) {

            // create vertical path
            mHighlightLinePath.reset();
            //设置highlight的顶部与Y轴最大数据值的位置一致
            float contentHeight = Math.abs(mViewPortHandler.getContentRect().top - mViewPortHandler.getContentRect().bottom);
            float yAxisHeight = lineChart.getAxisLeft().getAxisMaximum() - lineChart.getAxisLeft().getAxisMinimum();
            float top =  contentHeight * ((lineChart.getAxisLeft().getAxisMaximum() - StateChartConstants.MAX_ENTRY_VALUE) /yAxisHeight);
            float bottom = mViewPortHandler.contentBottom() - contentHeight * (StateChartConstants.MIN_ENTRY_VALUE - lineChart.getAxisLeft().getAxisMinimum())/yAxisHeight;
            mHighlightLinePath.moveTo(x, top);
            mHighlightLinePath.lineTo(x, bottom);

            c.drawPath(mHighlightLinePath, mHighlightPaint);
        }

        // draw horizontal highlight lines
        if (set.isHorizontalHighlightIndicatorEnabled()) {

            // create horizontal path
            mHighlightLinePath.reset();
            mHighlightLinePath.moveTo(mViewPortHandler.contentLeft(), y);
            mHighlightLinePath.lineTo(mViewPortHandler.contentRight(), y);

            c.drawPath(mHighlightLinePath, mHighlightPaint);
        }
    }

    @Override
    protected void drawFilledPath(Canvas c, Path filledPath, int fillColor, int fillAlpha) {

        int color = (fillAlpha << 24) | (fillColor & 0xffffff);

//        if (clipPathSupported()) {

            int save = c.save();

            c.clipPath(filledPath);

            mRenderPaint.setColor(color);
            mRenderPaint.setStyle(Paint.Style.FILL);
        mRenderPaint.setShader(new LinearGradient(0, 0,
                0, mChart.getHeight(), color, Color.TRANSPARENT, Shader.TileMode.CLAMP));

            c.drawPath(filledPath, mRenderPaint);
//            c.drawColor(color);
            c.restoreToCount(save);
//        } else {
//
//            // save
//            Paint.Style previous = mRenderPaint.getStyle();
//            int previousColor = mRenderPaint.getColor();
//
//            // set
//            mRenderPaint.setStyle(Paint.Style.FILL);
//            mRenderPaint.setColor(color);
//
//            c.drawPath(filledPath, mRenderPaint);
//
//            // restore
//            mRenderPaint.setColor(previousColor);
//            mRenderPaint.setStyle(previous);
//        }
    }

    @Override
    protected void drawLinear(Canvas c, ILineDataSet dataSet) {

        int entryCount = dataSet.getEntryCount();

        final boolean isDrawSteppedEnabled = dataSet.isDrawSteppedEnabled();
        final int pointsPerEntryPair = isDrawSteppedEnabled ? 4 : 2;

        Transformer trans = mChart.getTransformer(dataSet.getAxisDependency());

        float phaseY = mAnimator.getPhaseY();

        mRenderPaint.setStyle(Paint.Style.STROKE);

        Canvas canvas = null;

        // if the data-set is dashed, draw on bitmap-canvas
        if (dataSet.isDashedLineEnabled()) {
            canvas = mBitmapCanvas;
        } else {
            canvas = c;
        }

        mXBounds.set(mChart, dataSet);

        // if drawing filled is enabled
        if (dataSet.isDrawFilledEnabled() && entryCount > 0) {
            drawLinearFill(c, dataSet, trans, mXBounds);
        }

        // more than 1 color
        if (dataSet.getColors().size() > 1) {

            if (mLineBuffer.length <= pointsPerEntryPair * 2)
                mLineBuffer = new float[pointsPerEntryPair * 4];

            for (int j = mXBounds.min; j <= mXBounds.range + mXBounds.min; j++) {

                Entry e = dataSet.getEntryForIndex(j);
                if (e == null) continue;

                mLineBuffer[0] = e.getX();
                mLineBuffer[1] = e.getY() * phaseY;

                if (j < mXBounds.max) {

                    e = dataSet.getEntryForIndex(j + 1);

                    if (e == null) break;

                    if (isDrawSteppedEnabled) {
                        mLineBuffer[2] = e.getX();
                        mLineBuffer[3] = mLineBuffer[1];
                        mLineBuffer[4] = mLineBuffer[2];
                        mLineBuffer[5] = mLineBuffer[3];
                        mLineBuffer[6] = e.getX();
                        mLineBuffer[7] = e.getY() * phaseY;
                    } else {
                        mLineBuffer[2] = e.getX();
                        mLineBuffer[3] = e.getY() * phaseY;
                    }

                } else {
                    mLineBuffer[2] = mLineBuffer[0];
                    mLineBuffer[3] = mLineBuffer[1];
                }

                trans.pointValuesToPixel(mLineBuffer);

                if (!mViewPortHandler.isInBoundsRight(mLineBuffer[0]))
                    break;

                // make sure the lines don't do shitty things outside
                // bounds
                if (!mViewPortHandler.isInBoundsLeft(mLineBuffer[2])
                        || (!mViewPortHandler.isInBoundsTop(mLineBuffer[1]) && !mViewPortHandler
                        .isInBoundsBottom(mLineBuffer[3])))
                    continue;

                // get the color that is set for this line-segment
                mRenderPaint.setColor(dataSet.getColor(j));

                canvas.drawLines(mLineBuffer, 0, pointsPerEntryPair * 2, mRenderPaint);
            }

        } else { // only one color per dataset

            if (mLineBuffer.length < Math.max((entryCount) * pointsPerEntryPair, pointsPerEntryPair) * 2)
                mLineBuffer = new float[Math.max((entryCount) * pointsPerEntryPair, pointsPerEntryPair) * 4];

            Entry e1, e2;

            e1 = dataSet.getEntryForIndex(mXBounds.min);

            if (e1 != null) {

                int j = 0;
                for (int x = mXBounds.min; x <= mXBounds.range + mXBounds.min; x++) {

                    e1 = dataSet.getEntryForIndex(x == 0 ? 0 : (x - 1));
                    e2 = dataSet.getEntryForIndex(x);

                    if (e1 == null || e2 == null) continue;

                    mLineBuffer[j++] = e1.getX();
                    mLineBuffer[j++] = e1.getY() * phaseY;

                    if (isDrawSteppedEnabled) {
                        mLineBuffer[j++] = e2.getX();
                        mLineBuffer[j++] = e1.getY() * phaseY;
                        mLineBuffer[j++] = e2.getX();
                        mLineBuffer[j++] = e1.getY() * phaseY;
                    }

                    mLineBuffer[j++] = e2.getX();
                    mLineBuffer[j++] = e2.getY() * phaseY;
                }

                if (j > 0) {
                    trans.pointValuesToPixel(mLineBuffer);

                    //数组中实际参与绘制的数据个数
                    final int size = Math.max((mXBounds.range + 1) * pointsPerEntryPair, pointsPerEntryPair) * 2;
                    mRenderPaint.setColor(dataSet.getColor());

                    mStepLinePath.reset();
                    mStepLinePath.moveTo(mLineBuffer[0], mLineBuffer[1]);
                    for (int i = 2; i < size; ) {
                        if (mLineBuffer[i] < mLineBuffer[i - 2]) {
                            break;
                        }
                        mStepLinePath.lineTo(mLineBuffer[i++], mLineBuffer[i++]);
                    }
                    canvas.drawPath(mStepLinePath, mRenderPaint);
                }
            }
        }
        mRenderPaint.setPathEffect(null);
    }
}
