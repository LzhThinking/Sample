package com.lzh.sample.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.lzh.sample.R;

/**
 * @Author leizhiheng
 * @CreateDate 2019/9/4
 * @Description
 */
public class TestCustomView extends TextView {

    private Paint mPaint;
    private Path mPath = new Path();
    private Path mDashPath = new Path();

    


    public TestCustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(3);
        mPaint.setTextSize(100);
        mPaint.setColor(getResources().getColor(R.color.red_ec2700));
        mPaint.setStrokeJoin(Paint.Join.BEVEL);



        DashPathEffect dashPathEffect = new DashPathEffect(new float[]{20, 20, 20, 20, 0}, 0);
//        mPaint.setPathEffect(dashPathEffect);
//        PathDashPathEffect pathDashPathEffect = new PathDashPathEffect();



    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawLine(50, 500, 200, 500, mPaint);

//        mPath.reset();
//        mPath.moveTo(0, 50);
//        mPath.lineTo(500, 50);
//        mPath.lineTo(500, 500);
//        mPath.lineTo(0, 500);
//        mPath.close();
//        mPaint.setShader(new LinearGradient(0, 0, 0, 500, getResources().getColor(R.color.red_ec2700), Color.TRANSPARENT, Shader.TileMode.CLAMP));
//        canvas.drawPath(mPath, mPaint);


        //获取FontMetrics对象，可以获得top,bottom,ascent,descent
        Paint.FontMetrics metrics = mPaint.getFontMetrics();
        float top = metrics.top;
        float ascent = metrics.ascent;
        float descent = metrics.descent;
        float bottom = metrics.bottom;

        int baseY = 300;

        canvas.drawText("日月", 0, baseY, mPaint);

        float y = baseY - ((bottom - top) - bottom * 2) / 2;
        canvas.drawLine(0, y, 200, y, mPaint);

    }
}
