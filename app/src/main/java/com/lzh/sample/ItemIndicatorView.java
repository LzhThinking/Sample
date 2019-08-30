package com.lzh.sample;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;


public class ItemIndicatorView extends View {
    private float aboveDis;
    private int iconResId;
    private float iconSize;
    private float indicatorMargin;
    private int lineColor;
    private float lineWidth;
    private boolean isDrawTopLine;
    private boolean isShowBottomLine;
    private Bitmap iconBitmap;
    private Paint mPaint;
    //由于图标周围有空白部分，所以这里需要设置一个偏移量，保证线条和圈圈是连接着的
    float bottomOffset;
    float topOffset;
    public ItemIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ItemIndicatorView);
        aboveDis = typedArray.getDimension(R.styleable.ItemIndicatorView_indicator_above_dis, 0);
        iconResId = typedArray.getResourceId(R.styleable.ItemIndicatorView_indicator_icon_id, R.mipmap.family_manage_message_center_group);
        iconSize = typedArray.getDimension(R.styleable.ItemIndicatorView_indicator_icon_size, 30);
        indicatorMargin = typedArray.getDimension(R.styleable.ItemIndicatorView_indicator_margin, 0);
        lineColor = typedArray.getColor(R.styleable.ItemIndicatorView_indicator_line_color, context.getResources().getColor(android.R.color.holo_blue_bright));
        lineWidth = typedArray.getDimension(R.styleable.ItemIndicatorView_indicator_line_width, 1);
        iconBitmap = BitmapFactory.decodeResource(getResources(), iconResId);
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(lineWidth);
        mPaint.setAntiAlias(true);
        mPaint.setColor(lineColor);
        isDrawTopLine = true;
        bottomOffset = context.getResources().getDimension(R.dimen.px2);
        topOffset = context.getResources().getDimension(R.dimen.px6);
    }

    public void isDrawTopLine(boolean isDrawTopLine) {
        this.isDrawTopLine = isDrawTopLine;
    }

    public void isDrawBottomLine(boolean isShowBottomLine) {
        this.isShowBottomLine = isShowBottomLine;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();

        int destTop = (int) (aboveDis + indicatorMargin);
        int dextLeft = (int) ((width - iconSize) / 2);
        if (iconBitmap != null) {
            canvas.drawBitmap(iconBitmap, new Rect(0, 0, iconBitmap.getWidth(), iconBitmap.getHeight()), new Rect(dextLeft, destTop, (int)(dextLeft + iconSize), (int)(destTop + iconSize)), mPaint);
        }

        if (isDrawTopLine) {

            canvas.drawLine(width/2, 0, width/2, aboveDis + bottomOffset, mPaint);
        }

        if (isShowBottomLine) {
            canvas.drawLine(width/2,destTop + iconSize + indicatorMargin - topOffset, width/2, height, mPaint);
        }
    }
}
