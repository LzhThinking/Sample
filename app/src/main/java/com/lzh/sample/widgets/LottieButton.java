package com.lzh.sample.widgets;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.lzh.sample.R;

public class LottieButton extends View implements View.OnClickListener {

    private int bgColor;
    private String text;
    private String loadingText;
    private int textColor;
    private boolean enable;
    private Status state;
    private int animWidth;
    private int loadingAngle;
    private int ringWidth = 5;
    private Paint mBgPaint;
    private Paint mTextPaint;
    private Paint mRingPaint;
    private Shader mRingShader;

    private ValueAnimator mTouchAnim;
    private ValueAnimator mLoadingAnim;
    private ValueAnimator mLoadedAnim;

    public enum Status {
        IDLE, //初始状态
        TOUCHED, //点击后
        LOADING, //下载中
        LOADED //下载完毕
    }
    public LottieButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        mBgPaint = new Paint();
        mBgPaint.setStyle(Paint.Style.FILL);
        mBgPaint.setAntiAlias(true);
        mBgPaint.setColor(context.getResources().getColor(R.color.colorAccent));

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setTextSize(50);
        mTextPaint.setStyle(Paint.Style.FILL);
        //该方法即为设置基线上那个点究竟是left,center,还是right  这里我设置为center
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        mRingPaint = new Paint();
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth(ringWidth);

        state = Status.IDLE;
        setOnClickListener(this);
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setState(Status state) {
        this.state = state;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public void setLoadingText(String loadingText) {
        this.loadingText = loadingText;
    }


    @Override
    public void onClick(View view) {
        if (!enable) {
            return;
        }
        if (state == Status.IDLE) {
            state = Status.TOUCHED;
            startTouchedAnim();
        } else if (state == Status.LOADING) {
            mLoadingAnim.cancel();
            startLoadedAnim();
            state = Status.LOADED;
        }
    }

    private void startTouchedAnim() {
        if (mTouchAnim != null && mTouchAnim.isRunning()) {
            mTouchAnim.cancel();
        }

        if (mTouchAnim == null) {
            mTouchAnim = ValueAnimator.ofInt(getWidth(), getHeight());
            mTouchAnim.addUpdateListener(valueAnimator -> {
                animWidth = (int) valueAnimator.getAnimatedValue();
                invalidate();
            });
            mTouchAnim.addListener(mAnimListener);
            mTouchAnim.setDuration(300);
        }
        mTouchAnim.start();
    }

    private void startLoadingAnim() {
        if (mLoadingAnim != null && mLoadingAnim.isRunning()) {
            mLoadingAnim.cancel();
        }

        if (mLoadingAnim == null) {
            mLoadingAnim = ValueAnimator.ofInt(0, 360);
            mLoadingAnim.addUpdateListener(valueAnimator -> {
                loadingAngle = (int) valueAnimator.getAnimatedValue();
                invalidate();
            });
            mLoadingAnim.addListener(mAnimListener);
            mLoadingAnim.setDuration(2000);
            mLoadingAnim.setRepeatCount(1000);
        }
        mLoadingAnim.start();
    }

    private void startLoadedAnim() {
        if (mLoadedAnim != null && mLoadedAnim.isRunning()) {
            mLoadedAnim.cancel();
        }

        if (mLoadedAnim == null) {
            mLoadedAnim = ValueAnimator.ofInt(getHeight(), getWidth());
            mLoadedAnim.addUpdateListener(valueAnimator -> {
                animWidth = (int) valueAnimator.getAnimatedValue();
                Log.d("zhiheng", "startLoadedAnim animWidth = " + animWidth);
                invalidate();
            });
            mLoadedAnim.addListener(mAnimListener);
            mLoadedAnim.setDuration(300);
        }

        mLoadedAnim.start();
    }

    private Animator.AnimatorListener mAnimListener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animator) {

        }

        @Override
        public void onAnimationEnd(Animator animator) {
            switch (state) {
                case TOUCHED:
                    state = Status.LOADING;
                    startLoadingAnim();
                    break;
                case LOADING:
                    state = Status.LOADED;
                    startLoadedAnim();
                    break;
                case LOADED:
                    state = Status.IDLE;
                    break;
            }
        }

        @Override
        public void onAnimationCancel(Animator animator) {
        }

        @Override
        public void onAnimationRepeat(Animator animator) {

        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (state == Status.IDLE) {
            animWidth = getWidth();
        }

        Log.d("zhiheng", "animWidth = " + animWidth + ", state == " + state);
        drawBg(canvas);

        if (state == Status.LOADING) {
            drawProgressRing(canvas);
        }

        drawText(canvas);
    }

    private void drawBg(Canvas canvas) {
        int height = getHeight();
        int width = getWidth();
        int btnWidth = animWidth;
        int btnHeight = height;
        if (state == Status.LOADING) {
            btnWidth = height - (ringWidth + ringWidth) * 2;
            btnHeight = btnWidth;
        }
        canvas.drawRoundRect((width - btnWidth)/2, (height - btnHeight)/2, width - (width - btnWidth)/2, height - (height - btnHeight)/2, btnHeight/2, btnHeight/2, mBgPaint);
    }

    private void drawProgressRing(Canvas canvas) {
        int width = getWidth();
        int height = getHeight();
        if (mRingShader == null) {
            mRingShader = new SweepGradient(width/2, height/2, getResources().getColor(android.R.color.holo_green_light), getResources().getColor(android.R.color.transparent));
            mRingPaint.setShader(mRingShader);
        }
        RectF rectF = new RectF((width - height)/2 + ringWidth , ringWidth, (width + height)/2 - ringWidth, height - ringWidth);
        Log.d("zhiheng", "loadingAngle = " + loadingAngle);
        canvas.drawArc(rectF, loadingAngle, 300, false, mRingPaint);
    }

    private void drawText(Canvas canvas) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom

        Rect rect  = new Rect();
        getFocusedRect(rect);
        int baseLineY = (int) (rect.centerY() - top/2 - bottom/2);//基线中间点的y轴计算公式

        canvas.drawText(text, rect.centerX(), baseLineY, mTextPaint);
    }
}
