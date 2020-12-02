package com.lzh.sample.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.lzh.sample.Utils.CommonPanelUtil;

public class SweepGradientView extends View {
    public SweepGradientView(Context context) {
        super(context);
    }

    public SweepGradientView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(getWidth()/2, getHeight()/2);

        centerPoint.set(0, 0);

//        mPaint.setStyle(Paint.Style.FILL);
//        mPaint.setShader(gradient);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

        for (int i = 0; i <= 3600; i++) {
            hsv[0] = i/10f;
            hsv[1] = 1f;
            hsv[2] = 1f;
            int rgb = Color.HSVToColor(hsv);
            Log.d("zhiheng", "rgb = " + rgb + ", i = " + i);
            Log.d("zhiheng", "rgb str = " + CommonPanelUtil.toRGBHexStr(rgb));
            gradient = new LinearGradient(centerPoint.x, centerPoint.y, (float)(getWidth()/2f * Math.cos(i)), (float)(getHeight() /2f * Math.sin(i)), Color.WHITE, rgb, Shader.TileMode.CLAMP);
            mPaint.setShader(gradient);

            PointF end = new PointF();

//            rectF.set(-getWidth()/2, -getHeight()/2, getWidth()/2, getHeight()/2);
//            float startAngle = i * 0.1f;
//            canvas.drawArc(rectF, startAngle, 0.1f, true, mPaint);
        }
    }

    Path path = new Path();
    RectF rectF = new RectF();
    LinearGradient gradient;
    float hsv[] = new float[3];
    Paint mPaint = new Paint();
    PointF centerPoint = new PointF();
}
