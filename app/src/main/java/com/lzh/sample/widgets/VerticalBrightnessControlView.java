package com.lzh.sample.widgets;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

import com.lzh.sample.R;
import com.lzh.sample.Utils.BitmapUtils;


/**
 * @author leizhiheng
 * @date 2019/11/20 上午11:25
 * @description 恒流驱动器 静态模式亮度调节
 */
public class VerticalBrightnessControlView extends View {

    private static final int MIN_PROCESS = 0;
    private static final int MAX_PROCESS = 100;

    private static final float THRESHOLD = 20;
    private static final int PROGRESS_MIN = 0;
    private static final int PROGRESS_MAX = 100;
    public static final int TYPE_DRAW_SIMPLE = 1;
    public static final int TYPE_DRAW_BACK = 2;
    public static final int TYPE_DRAW_FORWARD = 3;
    private static final int TOTAL_ANIMATION_TIME = 1000;

    private int mWidth;
    private int mHeight;
    private float mInitialSpaceWidth;
    private int mDragBarWidth;
    private int mDragBarHeight;

    private float mProgress = 0; // progress of curtain
    private float mDragProgress; // progress of current
    private float mCurtainDistance = 0;
    private float mDragDistance = 0;
    private float mPreDragDistance = 0;
    private float mDownY;
    private boolean mSetFlag;
    private volatile int mDrawType = TYPE_DRAW_FORWARD;

    private int mColorDragArea; // drag area color
    private int mColorCurtainArea; // curtain area color
    private int mColorDragBar; // drag bar color
    private int mColorBackground;
    private Paint mPaint;
    private Paint mPaintDragBar;
    private ValueAnimator mCurveDragAnimator;

    private boolean mEnableControl = true;
    private int mTotalAnimationTime = TOTAL_ANIMATION_TIME;

    private OnSlideListener mOnSlideListener;

    private int mCoverColor;
    private int mBgColor;
    private float mShadowWidth;
    private float mControlAreaWidth;
    private float mControlAreaHeight;
    private int mCornerRadius;//圆角大小

    private int mMinProcess;
    private int mMaxProcess;

    Path mPath = new Path();

    float[] mRadiusArray;
    /**
     * 设置四个角的圆角半径
     */
    public void setRadius(float leftTop, float rightTop, float rightBottom, float leftBottom) {
        mRadiusArray = new float[8];
        mRadiusArray[0] = leftTop;
        mRadiusArray[1] = leftTop;
        mRadiusArray[2] = rightTop;
        mRadiusArray[3] = rightTop;
        mRadiusArray[4] = rightBottom;
        mRadiusArray[5] = rightBottom;
        mRadiusArray[6] = leftBottom;
        mRadiusArray[7] = leftBottom;
    }


    public VerticalBrightnessControlView(Context context) {
        this(context, null);
    }

    public VerticalBrightnessControlView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOverScrollMode(OVER_SCROLL_NEVER);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerticalBrightnessControlView);
        mCoverColor = typedArray.getColor(R.styleable.VerticalBrightnessControlView_cover_color, getResources().getColor(R.color.blue_5AC7EC));
        mShadowWidth = typedArray.getDimension(R.styleable.VerticalBrightnessControlView_shadow_width, 0);
        mCornerRadius = (int) typedArray.getDimension(R.styleable.VerticalBrightnessControlView_corner_radius, 0);
        mBgColor = typedArray.getColor(R.styleable.VerticalBrightnessControlView_background_color, getResources().getColor(R.color.white));
        mMaxProcess = typedArray.getInteger(R.styleable.VerticalBrightnessControlView_max_process, MAX_PROCESS);
        mMinProcess = typedArray.getInteger(R.styleable.VerticalBrightnessControlView_min_process, MIN_PROCESS);
        init();
    }

    /**
     * Set draw type
     *
     * @param drawType
     */
    public void setDrawType(int drawType) {
        this.mDrawType = drawType;
        invalidate();
    }

    private void init() {

        mDragBarWidth = getResources().getDimensionPixelOffset(R.dimen.px5);
        mDragBarHeight = getResources().getDimensionPixelOffset(R.dimen.px40);
        mColorDragArea = getResources().getColor(R.color.color_9ac8ff);
        mColorCurtainArea = getResources().getColor(R.color.color_5fa7fe);
        mColorDragBar = getResources().getColor(R.color.white);
        mColorBackground = getResources().getColor(R.color.color_f2f7fa);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setAntiAlias(true);
        mPaint.setColor(mCoverColor);
        mPaintDragBar = new Paint();
        mPaintDragBar.setAntiAlias(true);
        mPaintDragBar.setColor(getResources().getColor(R.color.white));

        //关闭硬件加速，否则绘制的阴影显示不了
        setLayerType(LAYER_TYPE_HARDWARE,null);

        setRadius(mCornerRadius, mCornerRadius, mCornerRadius, mCornerRadius);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mInitialSpaceWidth = mMinProcess * 1f / mMaxProcess * (getMeasuredHeight() - mShadowWidth * 2);
    }

    public void setEnableControl(boolean enableControl) {
        this.mEnableControl = enableControl;
    }

    /**
     * Set current progress of curtain
     *
     * @param curtainProgress
     */
    public void setCurtainProgress(float curtainProgress) {
        setCurtainProgress(curtainProgress, TOTAL_ANIMATION_TIME);
    }

    public void setCurtainProgress(float curtainProgress, int runTime) {
        setCurtainProgress(curtainProgress, runTime, false);
    }

    /**
     * Set current progress of curtain
     *
     * @param curtainProgress
     */
    public synchronized void setCurtainProgress(float curtainProgress, int totalTime, boolean isChangeTotalTime) {
        if (isTouch) {
            return;
        }
        if (mCurveDragAnimator != null && mCurveDragAnimator.isRunning()) {
            mCurveDragAnimator.cancel();
        }
        mSetFlag = true;
        if (isChangeTotalTime) {
            mTotalAnimationTime = totalTime;
        }
        if (curtainProgress < PROGRESS_MIN) {
            curtainProgress = PROGRESS_MIN;
        } else if (curtainProgress > PROGRESS_MAX) {
            curtainProgress = PROGRESS_MAX;
        }
        if(getWidth() - mInitialSpaceWidth != 0) {
            mDragDistance = curtainProgress * (getWidth() - mInitialSpaceWidth) / (PROGRESS_MAX - PROGRESS_MIN);
        }
        else{
            mDragDistance = 0;
        }
        if (mDrawType != TYPE_DRAW_SIMPLE) {
            if (mDragDistance > mCurtainDistance) {
                mDrawType = TYPE_DRAW_FORWARD;
            } else {
                mDrawType = TYPE_DRAW_BACK;
            }
        }
        startAnim(curtainProgress, totalTime);
    }

    private synchronized void startAnim(float dragProgress, int totalTime) {
        if (mCurveDragAnimator != null && mCurveDragAnimator.isRunning()) {
            mCurveDragAnimator.cancel();
        }
        if (totalTime <= 0) {
            mCurtainDistance = mDragDistance;
            mDragProgress = mProgress = dragProgress;
            invalidate();
            return;
        }
        mCurveDragAnimator = ObjectAnimator.ofFloat(mProgress, dragProgress);
        mCurveDragAnimator.addUpdateListener(animation -> {
            mProgress = (float) animation.getAnimatedValue();
            invalidate();
        });
        long duration = (long) ((Math.abs(mProgress - dragProgress) * totalTime) / 100);
        mCurveDragAnimator.setInterpolator(new LinearInterpolator());
        mCurveDragAnimator.setDuration(duration);
        mCurveDragAnimator.start();
    }

    public float getCurtainProgress() {
        return mProgress;
    }

    public float getDragCurtainProgress() {
        return calculateProgress(mDragDistance);
    }

    public void startRunCurtain() {
        startCurtainAnimation();
    }


    RectF mRectF = new RectF();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mWidth = getWidth();
        mHeight = getHeight();

        //绘制外圈阴影
        mPaintDragBar.setColor(getResources().getColor(R.color.white));
        mPaintDragBar.setShadowLayer(mShadowWidth / 2, 0, 0, getResources().getColor(R.color.gray_A4AAB3));
        mRectF.set(mShadowWidth, mShadowWidth, getWidth() - mShadowWidth, getHeight() - mShadowWidth);
        canvas.drawRoundRect(mRectF, mCornerRadius, mCornerRadius, mPaintDragBar);

        mPath.reset();
        canvas.save();
        //裁剪圆角矩形
        mRectF.set(mShadowWidth, mShadowWidth, mWidth - mShadowWidth, mHeight - mShadowWidth);
        mPath.addRoundRect(mRectF, mRadiusArray, Path.Direction.CCW);
        canvas.clipPath(mPath);

        //绘制白色背景
        mPaint.setColor(mBgColor);
        canvas.drawRect(mRectF, mPaint);
        //绘制process区域
        mPaint.setColor(mCoverColor);
        canvas.drawRect(mShadowWidth, (int) (mDragDistance), mWidth - mShadowWidth, mHeight - mShadowWidth, mPaint);
        canvas.restore();

    }

    boolean isTouch = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!mEnableControl) {
            return super.onTouchEvent(event);
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isTouch = true;
                mSetFlag = false;
                mDownY = event.getY();
                mPreDragDistance = mDownY - mInitialSpaceWidth < 0 ? 0 : mDownY - mInitialSpaceWidth;
            case MotionEvent.ACTION_MOVE:
                mDragDistance = (event.getY() - mDownY) + mPreDragDistance;
                if (mDragDistance < PROGRESS_MIN) {
                    mDragDistance = PROGRESS_MIN;
                } else if (mDragDistance > getHeight() - mInitialSpaceWidth) {
                    mDragDistance = getHeight() - mInitialSpaceWidth;
                }
                if (mOnSlideListener != null) {
                    mOnSlideListener.onSlide(calculateProgress(mDragDistance), false);
                }
                if (mDragDistance > mCurtainDistance) {
                    mDrawType = TYPE_DRAW_FORWARD;
                } else {
                    mDrawType = TYPE_DRAW_BACK;
                }
                invalidate();
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (Math.abs(event.getY() - mDownY) <= THRESHOLD) {
                    mDragDistance = event.getY() - mInitialSpaceWidth < 0 ? 0 : event.getY() - mInitialSpaceWidth;
//                    if (event.getX() > mPreDragDistance + mInitialSpaceWidth) {
                    if (mDragDistance > mCurtainDistance) {
                        mDrawType = TYPE_DRAW_FORWARD;
                    } else {
                        mDrawType = TYPE_DRAW_BACK;
                    }
                }
                mPreDragDistance = mDragDistance;
                invalidate();
                if (mOnSlideListener != null) {
                    mOnSlideListener.onSlide((int) calculateProgress(mDragDistance), true);
                }
                isTouch = false;
                break;
        }
        return true;
    }

    /**
     * Start curtain slide animation
     */
    private void startCurtainAnimation() {
        if (isTouch) {
            return;
        }
        if (mCurveDragAnimator != null && mCurveDragAnimator.isRunning()) {
            mCurveDragAnimator.cancel();
        }

        startAnim(calculateProgress(mDragDistance), mTotalAnimationTime);
    }

    private int calculateProgress(float mDragDistance) {
        int progress = 0;
        if(getWidth() - mInitialSpaceWidth != 0) {
            progress = (int) (mDragDistance / (getWidth() - mInitialSpaceWidth) * (PROGRESS_MAX - PROGRESS_MIN));
        }
        if (progress < PROGRESS_MIN) {
            progress = PROGRESS_MIN;
        } else if (progress > PROGRESS_MAX) {
            progress = PROGRESS_MAX;
        }

        return progress;
    }

    public void setColor(int dragArea, int curtainArea, int background) {
        mColorDragArea = dragArea;
        mColorCurtainArea = curtainArea;
        mColorBackground = background;
        invalidate();
    }

    public void setColorBackground(int colorBackground) {
        mColorBackground = colorBackground;
        invalidate();
    }

    public interface OnSlideListener {
        void onSlide(int progress, boolean pointerUp);
    }

    public void setOnSlideListener(OnSlideListener onSlideListener) {
        this.mOnSlideListener = onSlideListener;
    }

}
