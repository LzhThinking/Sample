package com.lzh.sample.widgets;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.lzh.sample.R;
import com.lzh.sample.Utils.DensityUtils;
import com.lzh.sample.Utils.FormatUtils;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * @Author leizhiheng
 * @CreateDate 2019/10/29
 * @Description
 */
public class CountDownView extends View {

    private int totolTime;//总的倒计时时间，单位秒
    private int currentTime;

    private Bitmap circleBitmap;
    private Paint mTimePaint, mTextPaint, mIndexPaint;
    Matrix rgbMatrix = new Matrix();

    private String decsText;

    private CompositeDisposable disposable = new CompositeDisposable();

    public CountDownView(Context context) {
        super(context);
        init();
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mTimePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTimePaint.setTextSize(DensityUtils.dp2px(getContext(), 35));
        mTimePaint.setColor(getResources().getColor(R.color.color_333333));
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "font/Helvetica.ttf");
        mTimePaint.setTypeface(typeface);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(DensityUtils.dp2px(getContext(), 13));
        mTextPaint.setColor(getResources().getColor(R.color.color_666666));

        mIndexPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mIndexPaint.setStyle(Paint.Style.FILL);
        mIndexPaint.setColor(getResources().getColor(R.color.color_5fa7fe));
        mIndexPaint.setStrokeWidth(DensityUtils.dp2px(getContext(), 4));

        circleBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.countdown_round);
    }

    /**
     * 设置总时间
     * @param totolTime 单位秒
     */
    public void setTotolTime(int totolTime) {
        this.totolTime = totolTime;
    }

    /**
     * 设置剩余时间
     * @param currentTime 单位秒
     */
    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public void setDecsText(String decsText) {
        this.decsText = decsText;
    }

    public void startCountDown() {
        Disposable task = Observable.interval(1000, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    if (currentTime > 0) {
                        currentTime--;
                    } else {
                        disposable.dispose();
                    }
                    postInvalidate();
                });
        disposable.add(task);
    }

    public void stopCountDown() {
        disposable.dispose();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.translate(getWidth() / 2, getHeight() / 2);

        drawCircle(canvas);
        drawTime(canvas);
        drawText(canvas);
        drawIndex(canvas);
    }

    private void drawCircle(Canvas canvas) {
        float viewHeight = getHeight();
        float bitmapHeight = circleBitmap.getHeight();
        float temperatureScale =viewHeight/bitmapHeight;
        rgbMatrix.reset();//色温图进行的矩阵调整
        rgbMatrix.setScale(temperatureScale, temperatureScale);
        rgbMatrix.postTranslate(-circleBitmap.getWidth() * temperatureScale / 2, -circleBitmap.getHeight() * temperatureScale / 2);
        canvas.drawBitmap(circleBitmap, rgbMatrix, mTextPaint);
    }

    private void drawTime(Canvas canvas) {
        String time = FormatUtils.time2HHlmmlss(getContext(), currentTime);
        Paint.FontMetrics fontMetrics = mTimePaint.getFontMetrics();
        int height = (int) (fontMetrics.bottom - fontMetrics.top);
        int width = (int) mTimePaint.measureText(time);
        canvas.drawText(time, -width / 2, 0 , mTimePaint);
    }

    private void drawText(Canvas canvas) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        int height = (int) (fontMetrics.bottom - fontMetrics.top);
        int width = (int) mTextPaint.measureText(decsText);
        canvas.drawText(decsText, -width / 2, height , mTextPaint);
    }

    private void drawIndex(Canvas canvas) {
        canvas.save();
        float circleWidth = (getHeight() * 0.1f);//圆环的高度是整个图片高度的1/10,这个由图片决定，无法更改。。
        float rotateAngle = (totolTime - currentTime) * 1.0f / totolTime * 1.0f * 360.0f;
        canvas.rotate(rotateAngle);
        float startY = (float) (-getHeight()/2.0 + circleWidth * 0.3);//index高度占圆环高度的0.7
        float stopY = (float) (-getHeight()/2.0 + circleWidth);
        canvas.drawLine(0, startY, 0, stopY, mIndexPaint);
        canvas.restore();
    }
}
