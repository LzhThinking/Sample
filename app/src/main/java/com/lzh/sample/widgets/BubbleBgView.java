package com.lzh.sample.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.lzh.sample.R;

public class BubbleBgView extends View {

    private Paint mPaint;
    private Path mPathRect;
    private Path mPathTriangle;

    private RectF mRectF;

    private int mSolidColor;
    private int mShadowColor;

    private float mShadowWidth;
    private float mArrowWidth;
    private float mArrowHeight;
    private float mRadius = getResources().getDimension(R.dimen.px8);
    private float mArrowDistanceRight = 0;//箭头距离右侧圆角的距离
    private float mArrowDistanceLeft = 0;//箭头距离左侧圆角的距离

    public BubbleBgView(Context context) {
        super(context);
    }

    public BubbleBgView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BubbleBgView);
        mSolidColor = typedArray.getColor(R.styleable.BubbleBgView_solidColor, getResources().getColor(R.color.white));
        mShadowColor = typedArray.getColor(R.styleable.BubbleBgView_shadowColor, getResources().getColor(R.color.color_23000000));
        mShadowWidth = typedArray.getDimension(R.styleable.BubbleBgView_shadowWidth, getResources().getDimension(R.dimen.px5));
        mArrowWidth = typedArray.getDimension(R.styleable.BubbleBgView_arrowWidth, getResources().getDimension(R.dimen.px15));
        mArrowHeight = typedArray.getDimension(R.styleable.BubbleBgView_arrowHeight, getResources().getDimension(R.dimen.px10));
        mRadius = typedArray.getDimension(R.styleable.BubbleBgView_radius, getResources().getDimension(R.dimen.px8));
        mArrowDistanceLeft = typedArray.getDimension(R.styleable.BubbleBgView_arrowDistanceLeft, 0);
        mArrowDistanceRight = typedArray.getDimension(R.styleable.BubbleBgView_arrowDistanceRight, getResources().getDimension(R.dimen.px2));

        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mSolidColor);
        mPaint.setShadowLayer(mShadowWidth, 0, 0, mShadowColor);
        mPathRect = new Path();
        mPathTriangle = new Path();

        mRectF = new RectF();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float width = getWidth();
        float height = getHeight();

        //绘制圆角矩形
        mPathRect.reset();
        mRectF.set(mShadowWidth, mShadowWidth + mArrowHeight, width - mShadowWidth, height - mShadowWidth);
        mPathRect.addRoundRect(mRectF, mRadius, mRadius, Path.Direction.CCW);

        //绘制箭头
        mPathTriangle.reset();
        float x, y;
        if (mArrowDistanceLeft > 0) {
            x = mShadowWidth + mRadius + mArrowDistanceLeft;
            y = mShadowWidth + mArrowHeight;
        } else {
            x = width - mShadowWidth - mRadius - mArrowDistanceRight - mArrowWidth;
            y = mShadowWidth + mArrowHeight;
            mPathTriangle.moveTo(x, y);
        }
        mPathTriangle.moveTo(x, y);
        mPathTriangle.lineTo(x + mArrowWidth/2, y - mArrowHeight);
        mPathTriangle.lineTo(x + mArrowWidth, y);
        mPathTriangle.moveTo(x, y);

        //拼接两个图形
        mPathRect.op(mPathTriangle, Path.Op.UNION);

        canvas.drawPath(mPathRect, mPaint);
    }
}
