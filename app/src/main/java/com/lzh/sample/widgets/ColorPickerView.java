package com.lzh.sample.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.lzh.sample.R;
import com.lzh.sample.Utils.BitmapUtils;
import com.lzh.sample.Utils.ColorUtils;
import com.lzh.sample.Utils.CommonPanelUtil;

import java.io.InputStream;

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by cdy on 2018/3/8.
 */

public class ColorPickerView extends View {
    public final String TAG = getClass().getSimpleName();
    public static final int TYPE_COLOR_TEMP = 0;//色温模式：显示色温盘和亮度环
    public static final int TYPE_COLOR_VALUE = 1;//色值模式：显示色值盘和亮度环
    public static final int TYPE_LIGHT_RATE_ONLY = 2;//亮度模式：只显示亮度环
    public static final int TYPE_LIGHT_TEMP_ONLY = 3;//只显示色温盘
    public static final int TYPE_LIGHT_VALUE_ONLY = 4;//只显色色值盘

    public static final int COLOR_UI_ANGLE = 45;//ui色圆图红色开始的角度
    public static final int POINTER_UI_ANGLE = 90;//ui图指针要调整的角度

    //动画状态，后面如果不需要就去掉
    public static final int MSG_IDLE = 0;
    public static final int MSG_OPENING = 1;
    public static final int MSG_CLOSING = 2;

    private final int[] mCircleColors = new int[]{0xE0FF0000, 0xE0FF00FF,
            0xE00000FF, 0xE000FFFF, 0xE000FF00, 0xE0FFFF00, 0xE0FF0000};

    private static final int TEMP_MAX_COLOR = 0xFFFFAE54;//2700k
    private static final int TEMP_MIN_COLOR = 0xFFFFF9FD;//6500k

    private final int[] mTempColors = new int[]{TEMP_MAX_COLOR, 0xFFFFE2C0, 0xFFFFECE0, 0xFFFFF4ED, TEMP_MIN_COLOR};
    private final float[] mTempColorRates = new float[] {0.0f, 0.4f, 0.55f, 0.7f, 1.0f};

    private final int LED_MAX_COLOR = 0xFFF9B65C;
    private final int LED_MIN_COLOR = 0xFFFFFAFA;

    private final int[] mLEDColors = new int[]{LED_MAX_COLOR, 0xFFFFC178, 0xFFFFE8D8, 0xFFFFF2EF, 0xFFFFFAFE,
            LED_MIN_COLOR, 0xFFFFFAFE, 0xFFFFF2EF, 0xFFFFE8D8, 0xFFFFC178, LED_MAX_COLOR};

    private static final int grayColor = 0xFFEEEEEE;//灯关闭后为灰色
    private static final int darkColor = 0x000000;//亮度调小时的黑色蒙版

    private int mType = TYPE_COLOR_TEMP;
    /**
     * 色盘类型{@TYPE_COLOR_TEMP} or {@TYPE_LED}
     **/

    /**
     * 亮度比例,0-1
     */
    private float mBrightnessRate;//[0~1]
    /**
     * 色温比例，0-1
     */
    private float mColorTempRate;
    private int mSelectedColor;
    private int duration = 1000;
    private int colorAlpha = 0xff;
    private int rotateAngle;
    private int ringRotateAngle;
    /**亮度环可设置的最小值*/
    private int minRingRototeAngle = 0;
    /**亮度环可设置的最大值*/
    private int maxRingRotateAngle = 360;
    Shader temperatureShader;
    Bitmap temperatureBitmap;//色温的图片
    Matrix temperatureMatrix = new Matrix();
    Bitmap rgbBitmap;//色值的图片
    Matrix rgbMatrix = new Matrix();
    Bitmap indexBitmap;
    //整个渐变圆
    private Paint mColorfulPaint, mTemperaturePaint, mRgbPaint;// 渐变色环画笔
    private int mHeight;// View高
    private int mWidth;// View宽
    private float mRingRadius;//外圈圆环半径
    private float mRingAreaWidth;//外圈圆环区域宽度
    private float mRingRealWidht;//外圈圆环绘制宽度
    private float mPanelRadius;// 取色盘或者色温盘半径。panelRadius = view.getWidth - ringAreaWidth
    private Paint mRingIndexPaint;
    private Paint mRingPaint;
    Shader sweepGradient;
    //点击处圆环
    private int clickX = 0;//（调整后的）点击位置x坐标
    private int clickY = 0;//（调整后的）点击位置y坐标
    private Paint mClickPaint;// 触控点画笔
    private int clickPointRadius = 16;//点击处圆环半径
    private int clickPointX;//点击圆环的原点坐标
    private int clickPointY;
    //指针
    Paint mPointerPaint;//指针paint
    Matrix mPointerTempMatrix;//matrix矩阵变换
    int pointerSize;//指针的大小
    Bitmap pointBitmap;//指针bitmap
    Bitmap pointCloseBitmap;//关闭后的指针bitmap
    Paint centerPaint;//中心色圆paint
    int pointCenterRadius;//指针中心的表示当前颜色的圆形
    //关闭时的灰色和黑色蒙版
    Paint mGrayPaint;//灰色圆paint
    Paint blackPaint;//黑色圆paint

    int mRingBgColor = getResources().getColor(R.color.color_f5f5f5);
    int mRingIndexColorClosed = getResources().getColor(R.color.color_dddddd);
    private Shader mProgressRingShader;//亮度环渐变shader

    public OnColorChangedListener mListener;

    private boolean downInRing = false;
    private boolean downInPanel = true;// 是否按在色盘上
    private boolean isShutdown = false;//是否关闭
    private boolean isDrawRing = true;//是否绘制色环
    private boolean isDrawRingIndex = true;
    private boolean isSupportChangeMode = false;//是否支持切换模式

    private boolean canClickOpen = false;//是否点击后立即打开灯
    private boolean isDrawLightRing = true;//自动化不需要绘制亮度环
    public void serIsDrawLightRing(boolean b){
        isDrawLightRing = b;
    }

    /**
     * 在View onMeasure()之前设置变量时，计算可能会依赖mPanalRadius,但这个时候mPanalRadius = 0.
     * 这样就会计算出错，为了避免这种情况，将计算放到onDraw()方法中。加上一个标志位避免不必要的计算。
     */
    private boolean isShouldCalculate = false;
    private boolean isFirst = true;

    private boolean controlEnable = true;
    private static final int MSG_INVALIDATE = 100;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == MSG_INVALIDATE) {
                setClickAngle();
                onSendChanging();
            }
            return true;
        }
    });

    public ColorPickerView(Context context) {
        super(context);
        init(context);
    }

    public ColorPickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ColorPickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        setFocusable(true);
        setFocusableInTouchMode(true);
        mRingAreaWidth = context.getResources().getDimensionPixelSize(R.dimen.px40);
        mRingRealWidht = context.getResources().getDimensionPixelSize(R.dimen.px30);
        pointerSize = context.getResources().getDimensionPixelSize(R.dimen.px85);
        sweepGradient = new SweepGradient(0, 0, mCircleColors, null);
        //准备色圆的画笔
        mColorfulPaint = new Paint(Paint.ANTI_ALIAS_FLAG); // 抗锯齿
        mColorfulPaint.setStyle(Paint.Style.FILL); // 设置实心
        mColorfulPaint.setShader(sweepGradient);
        clickPointRadius = context.getResources().getDimensionPixelSize(R.dimen.px12);
        //控制点的画笔
        mClickPaint = new Paint(Paint.ANTI_ALIAS_FLAG);// 抗锯齿
        mClickPaint.setStyle(Paint.Style.STROKE); // 设置空心
        mClickPaint.setStrokeWidth(context.getResources().getDimensionPixelSize(R.dimen.px2));
        mClickPaint.setColor(0xffffffff);
        //指针及中心
//        pointBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.half_light_picker);
//        pointCloseBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.half_light_picker);
        mPointerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointerTempMatrix = new Matrix();
        centerPaint = new Paint(Paint.ANTI_ALIAS_FLAG); // 抗锯齿
        centerPaint.setStyle(Paint.Style.FILL); // 设置实心
        pointCenterRadius = context.getResources().getDimensionPixelSize(R.dimen.px27);
        //色温
        temperatureBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.light_color_temp);
        mTemperaturePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //色值
        Drawable drawableRgb = getResources().getDrawable(R.mipmap.light_rgb);
        setHSB(drawableRgb, 0f, 0f, 0f);
        rgbBitmap = BitmapUtils.drawableToBitmap(drawableRgb);
        mRgbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //关闭时的灰色蒙版
        mGrayPaint = new Paint(Paint.ANTI_ALIAS_FLAG); // 抗锯齿
        mGrayPaint.setStyle(Paint.Style.FILL); // 设置实心
        mGrayPaint.setColor(grayColor);
        //调暗时的黑色蒙版
        blackPaint = new Paint(Paint.ANTI_ALIAS_FLAG); // 抗锯齿
        blackPaint.setStyle(Paint.Style.FILL); // 设置实心
        blackPaint.setColor(darkColor);

        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);// 抗锯齿
        mRingPaint.setStyle(Paint.Style.STROKE);//设置空心
        mRingPaint.setStrokeWidth(mRingRealWidht - 10);

        mRingIndexPaint = new Paint();
        mRingIndexPaint.setAntiAlias(true);
        mRingIndexPaint.setStyle(Paint.Style.STROKE);
        mRingIndexPaint.setStrokeWidth(mRingRealWidht + 10);

        Resources r = this.getContext().getResources();
        Drawable drawable = r.getDrawable(R.drawable.shape_ring_index, null);
        indexBitmap = BitmapUtils.drawableToBitmap(drawable);
    }

    public void setControlEnable(boolean controlEnable) {
        this.controlEnable = controlEnable;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isShouldCalculate) {
            isShouldCalculate = false;
            doCalculate();
        }
        //调整视觉颜色，看起来不会太暗
        //colorAlpha = (int) ((mBrightnessRate / 4 + 0.75f) * 0xff);
        // 移动中心
        canvas.translate(mWidth / 2, mHeight / 2);
        //画色圆(色温圆)， r是半径
        if (mType == TYPE_COLOR_VALUE || mType == TYPE_LIGHT_VALUE_ONLY) {
            drawRgbCircle(canvas);

        } else if (mType == TYPE_COLOR_TEMP || mType == TYPE_LIGHT_TEMP_ONLY) {
            drawTemperatureCircle(canvas);
        }
        //调暗灯光时的画黑色(灰色)蒙版
        drawDarkCircle(canvas);
        //画点击处的圆
        drawClickPoint(canvas);

        //绘制ring bg
        drawRing(canvas, true);

        //绘制ring 颜色
        drawRing(canvas, false);

        drawRingIndex(canvas);

//        //画指针罗盘
//        drawPointerBitmap(canvas);
//        //画罗盘中心的色圆
//        drawPointerCenter(canvas);
        super.onDraw(canvas);
    }


    private static ColorMatrix colorMatrix = new ColorMatrix();
    /**
     * 色调，改变颜色
     */
    private static ColorMatrix hueMatrix = new ColorMatrix();
    /**
     * 饱和度，改变颜色的纯度
     */
    private static ColorMatrix saturationMatrix = new ColorMatrix();
    /**
     * 亮度，控制明暗
     */
    private static ColorMatrix brightnessMatrix = new ColorMatrix();
    private void setHSB(Drawable drawable, float hueValue, float saturationValue, float brightnessValue) {
        hueValue = 360f;
        saturationValue = 1.1f;
        brightnessValue = 1.0f;
        //设置色相，为0°和360的时候相当于原图
        hueMatrix.reset();
        hueMatrix.setRotate(0, hueValue);
        hueMatrix.setRotate(1, hueValue);
        hueMatrix.setRotate(2, hueValue);

        //设置饱和度，为1的时候相当于原图
        saturationMatrix.reset();
        saturationMatrix.setSaturation(saturationValue);

        //亮度，为1的时候相当于原图
        brightnessMatrix.reset();
        brightnessMatrix.setScale(brightnessValue, brightnessValue, brightnessValue, 1);

        //将上面三种效果和选中的模式混合在一起
        colorMatrix.reset();
        colorMatrix.postConcat(hueMatrix);
        colorMatrix.postConcat(saturationMatrix);
        colorMatrix.postConcat(brightnessMatrix);

        drawable.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
    }

    private void doCalculate() {
        if (mType == TYPE_LIGHT_TEMP_ONLY || mType == TYPE_COLOR_TEMP) {
            int radius = (int) (mPanelRadius - clickPointRadius);
            clickY = (int) ((radius * 2 * mColorTempRate) - radius);
            //clickX = clickX == 0 ? 0 : clickX;

            if (Math.pow(clickX, 2) + Math.pow(clickY, 2) > Math.pow(radius, 2)) {
                clickPointX = (int) (Math.cos(rotateAngle * Math.PI / 180) * radius);
                clickPointY = (int) (Math.sin(rotateAngle * Math.PI / 180) * radius);
            } else {
                clickPointX = clickX;
                clickPointY = clickY;
            }
            mSelectedColor = convetXy2Color();
        } else if (mType == TYPE_COLOR_VALUE || mType == TYPE_LIGHT_VALUE_ONLY) {
            calculatePositionByColor(mSelectedColor);
        }
    }

    @SuppressLint("CheckResult")
    private void calculatePositionByColor(final int targetColor) {
        Single.create((SingleOnSubscribe<Point>) e -> {
            int pixelDistance = 3;//每隔3个像素点比较一次
            int bitmapWidth = rgbBitmap.getWidth();
            int bitmapHeight = rgbBitmap.getHeight();
            double colorDistance;
            double colorDistanceMin = Double.MAX_VALUE;
            Point point = new Point(0, 0);
            for (int i = 0; i < rgbBitmap.getWidth();) {
                for (int j = 0; j < rgbBitmap.getHeight();) {
                    int x = j;
                    int y = i;
                    if (x >= bitmapWidth) {
                        x = bitmapWidth - 1;
                    }
                    if (y >= bitmapHeight) {
                        y = bitmapHeight - 1;
                    }
                    if ((Math.pow((x - bitmapWidth/2), 2) + Math.pow((y - bitmapHeight/2), 2)) > Math.pow(bitmapHeight/2, 2)) {
                        j = j + pixelDistance;
                        continue;
                    }
                    int tempColor = rgbBitmap.getPixel(x, y);
                    tempColor = tempColor & 0x00ffffff;
                    colorDistance = CommonPanelUtil.calculateColorDifference(targetColor, tempColor);
                    if (colorDistance < colorDistanceMin) {
                        colorDistanceMin = colorDistance;
                        point.x = x;
                        point.y = y;
                    }
                    j = j + pixelDistance;
                }
                i += pixelDistance;
            }
            e.onSuccess(point);
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(point -> {
                    float scale = rgbBitmap.getWidth() / (mPanelRadius * 2);
                    clickX = (int) ((point.x / scale) - mPanelRadius);
                    clickY = (int) ((point.y / scale) - mPanelRadius);
                    rotateAngle = (int) (Math.atan2(clickY, clickX) * 180 / Math.PI);
                    calculateXY();
                    invalidate();
                });
    }

    private void drawTemperatureCircle(Canvas canvas) {
//        mTemperaturePaint.setAlpha(colorAlpha);
        float temperatureScale = (mPanelRadius * 2) / Math.min(temperatureBitmap.getHeight(), temperatureBitmap.getWidth());//!float
        temperatureMatrix.reset();//色温图进行的矩阵调整
        temperatureMatrix.setScale(temperatureScale, temperatureScale);
        temperatureMatrix.postTranslate(-temperatureBitmap.getWidth() * temperatureScale / 2, -temperatureBitmap.getHeight() * temperatureScale / 2);
//        //（*注）色温图不用额外加45度。但为了统一touchevent中的处理(加45)，故canvas仍然-45，这里加45矫正过来。下面的同理
//        //temperatureMatrix.postRotate(COLOR_UI_ANGLE);
//        canvas.drawBitmap(temperatureBitmap, temperatureMatrix, mTemperaturePaint);

        if (temperatureShader == null) {
            temperatureShader = new LinearGradient(-mPanelRadius,-mPanelRadius, -mPanelRadius, mPanelRadius, mTempColors, mTempColorRates, LinearGradient.TileMode.CLAMP);
        }
        mTemperaturePaint.setShader(temperatureShader);
        canvas.drawCircle(0, 0, mPanelRadius, mTemperaturePaint);
    }

    private void drawRgbCircle(Canvas canvas) {
        mRgbPaint.setAlpha(colorAlpha);
        float temperatureScale = (mPanelRadius * 2) / Math.min(rgbBitmap.getHeight(), rgbBitmap.getWidth());//!float
        rgbMatrix.reset();//色温图进行的矩阵调整
        rgbMatrix.setScale(temperatureScale, temperatureScale);
        rgbMatrix.postTranslate(-rgbBitmap.getWidth() * temperatureScale / 2, -rgbBitmap.getHeight() * temperatureScale / 2);
        canvas.drawBitmap(rgbBitmap, rgbMatrix, mRgbPaint);
    }

    private void drawClickPoint(Canvas canvas) {
        if (!isShutdown) {
            canvas.drawCircle(clickPointX, clickPointY, clickPointRadius - 3, mClickPaint);
        }
    }

    private void drawRing(Canvas canvas, boolean isDrawRingBg) {
        float ringRadius = mRingRadius - mRingAreaWidth / 2;

        float startAngle = -90;
        float sweepAngle = 360;
        if (isDrawRingBg || !isDrawRing) {
            mRingPaint.setColor(mRingBgColor);
        } else {
            sweepAngle = ringRotateAngle;
//            int[] mRingColors  = new int[]{mSelectedColor, Color.parseColor("#FFFFFF")};
//            mProgressRingShader = new SweepGradient(0, 0, mRingColors, null);
//            mRingPaint.setShader(mProgressRingShader);
            //mRingPaint.setColor(mSelectedColor);
            mRingPaint.setColor(getContext().getResources().getColor(R.color.e3e3e3));
        }

//        ALog.d("sweepAngle = " + sweepAngle);
        if(isDrawLightRing){
            canvas.drawArc(new RectF(-ringRadius, -ringRadius, ringRadius, ringRadius),
                    startAngle, sweepAngle, false, mRingPaint);
        }
    }

    private void drawRingIndex(Canvas canvas) {
        if (isDrawRingIndex) {
            float ringRadius = mRingRadius - mRingAreaWidth / 2;
            float startAngle = -90;
            if (!isDrawRing) {
                mRingIndexPaint.setColor(mRingIndexColorClosed);
            } else {
                startAngle = (float) ringRotateAngle - 89;
                //mRingIndexPaint.setColor(mSelectedColor);
                mRingIndexPaint.setColor(getContext().getResources().getColor(R.color.e3e3e3));
            }
            if (isDrawLightRing){
                canvas.save();
//                canvas.drawArc(new RectF(-ringRadius, -ringRadius, ringRadius, ringRadius),
//                        startAngle, 2, false, mRingIndexPaint);
                int halfIndexWidth = (int) getContext().getResources().getDimension(R.dimen.px3);
                canvas.rotate(startAngle + 90);
                int left = -halfIndexWidth;
                int top = (int) (-(mRingRadius - (mRingAreaWidth - mRingRealWidht)/2) - halfIndexWidth);
                int right = halfIndexWidth;
                int bottom = (int) (-(mRingRadius - mRingAreaWidth + (mRingAreaWidth - mRingRealWidht)/2) + halfIndexWidth);
                mRingIndexPaint.setStyle(Paint.Style.FILL);
                mRingIndexPaint.setStrokeWidth(1);
                //BitmapDrawable drawable = BitmapDrawable.
                //Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.light_color_temp, null);
                Rect rectF = new Rect(0, 0, indexBitmap.getWidth(), indexBitmap.getHeight());
                Rect rectD = new Rect(left, top, right, bottom);
                canvas.drawBitmap(indexBitmap, rectF, rectD, mRingIndexPaint);
                //canvas.drawRoundRect(left, top, right, bottom, halfIndexWidth, halfIndexWidth, mRingIndexPaint);
                canvas.restore();
            }

        }
    }

    private void drawGrayCircle(Canvas canvas) {
        canvas.drawOval(new RectF(-mPanelRadius, -mPanelRadius, mPanelRadius, mPanelRadius), mGrayPaint);
    }

    private void drawDarkCircle(Canvas canvas) {
        if (!isShutdown) {
            blackPaint.setAlpha(0xff - colorAlpha);
            canvas.drawOval(-mPanelRadius, -mPanelRadius, mPanelRadius, mPanelRadius, blackPaint);
        } else {
            drawGrayCircle(canvas);
        }
    }

    private void drawColorfulCircle(Canvas canvas) {
        mColorfulPaint.setAlpha(colorAlpha);
        canvas.drawCircle(0, 0, mPanelRadius, mColorfulPaint);
    }

    private void drawPointerCenter(Canvas canvas) {
        int color = mSelectedColor & 0xffffff | colorAlpha << 24;//color
        centerPaint.setColor(color);
        canvas.drawCircle(0, 0, pointCenterRadius, centerPaint);
        if (isShutdown) {
            centerPaint.setColor(grayColor);
            canvas.drawCircle(0, 0, pointCenterRadius, centerPaint);
        } else if (colorAlpha != 0xff) {
            int darkAlphaColor = darkColor | 0xff - colorAlpha << 24;//dark
            centerPaint.setColor(darkAlphaColor);
            canvas.drawCircle(0, 0, pointCenterRadius, centerPaint);
        }
    }

    private void drawPointerBitmap(Canvas canvas) {
        float pointerScale = (float) pointerSize / Math.min(pointBitmap.getHeight(), pointBitmap.getWidth());//!float
        mPointerTempMatrix.reset();//矩阵实现旋转+缩放调整
        mPointerTempMatrix.setScale(pointerScale, pointerScale);
        mPointerTempMatrix.postTranslate(-pointBitmap.getWidth() * pointerScale / 2, -pointBitmap.getHeight() * pointerScale / 2);
        int offsetAngle = mType == TYPE_COLOR_TEMP ? POINTER_UI_ANGLE - COLOR_UI_ANGLE : POINTER_UI_ANGLE;//106(切图的问题指针需要调整角度) - 45（详情见*注)
        mPointerTempMatrix.postRotate((float) rotateAngle + offsetAngle, 0, 0);
        if(isShutdown){
            canvas.drawBitmap(pointCloseBitmap, mPointerTempMatrix, mPointerPaint);
        }
        else {
            canvas.drawBitmap(pointBitmap, mPointerTempMatrix, mPointerPaint);
        }
    }

    //触摸事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!controlEnable) {
            return super.onTouchEvent(event);
        }

        float x = event.getX() - mWidth / 2;
        float y = event.getY() - mHeight / 2;

        int tempClickX;
        int tempClickY;

        boolean inCircle = inColorCircle(x, y, mPanelRadius, 0);
        boolean inCenter = inColorCircle(x, y, pointCenterRadius, 0);
        boolean inRingArea = inColorCircle(x, y, mRingRadius, mPanelRadius);
        //判断事件
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downInPanel = inCircle;
                downInRing = inRingArea;
            case MotionEvent.ACTION_MOVE:
                tempClickX = (int) x;
                tempClickY = (int) y;
                clickX = (int) x;
                clickY = (int) y;
                //rotateAngle是坐标系的角度，水平向右为0度，逆时针180为-180，顺时针180为180
                rotateAngle = (int) (Math.atan2(tempClickY, tempClickX) * 180 / Math.PI);
                if (!isShutdown && downInPanel) {
                    //防止色环刷新太快出现闪烁情况
                    if (!mHandler.hasMessages(MSG_INVALIDATE)) {
                        if (mHandler.hasMessages(MSG_INVALIDATE)) {
                            mHandler.removeMessages(MSG_INVALIDATE);
                        }
                        mHandler.sendEmptyMessage(MSG_INVALIDATE);
                    }
                }  else if (downInRing) {//isShutdown = true时也可以调节亮度
                    convertRingRotateAngle();
                    mBrightnessRate = (float) (ringRotateAngle / 360.0) > 1 ? 1 : (float) (ringRotateAngle / 360.0);
                    if (mListener != null) {
                        mListener.onBrightnessChanging(mBrightnessRate);
                    }
                    //如果触摸色环后,且亮度大于或等于1%即可绘制色环，即使isShutdown = true;
                    if (mBrightnessRate >= 0.01f) {
                        isDrawRing = true;
                    } else {
                        isDrawRing = false;
                    }
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                mHandler.removeMessages(MSG_INVALIDATE);
                onSendChanged();
                invalidate();
                break;
        }
        return true;
    }

    /**
     * 通过rotateAngle得到圆环的绘制弧度ringRotateAngle。
     * ringRotateAngle为圆环绘制弧度，正上方为0度，逆时针旋转一圈为360度
     */
    private void convertRingRotateAngle() {
        int tempAngle = ringRotateAngle;
        if (rotateAngle >= -90) {
            ringRotateAngle = rotateAngle + 90;
        } else {
            ringRotateAngle = rotateAngle + 450;
        }
        ringRotateAngle = ringRotateAngle > 360 ? 360 : ringRotateAngle;

        // TODO: 2019/6/6 只能划一圈的处理，后续优化
        if (tempAngle >= 345 && ringRotateAngle < 180){
            ringRotateAngle = maxRingRotateAngle;
        }
        if (tempAngle <= 15 && ringRotateAngle > 180){
            ringRotateAngle = minRingRototeAngle;
        }
//        ALog.d("ringRotateAngle = " + ringRotateAngle);
    }

    private void onSendChanged() {
        if (mListener == null) return;

        if (!isShutdown && downInPanel) {
            if (mType == TYPE_COLOR_VALUE || mType == TYPE_LIGHT_VALUE_ONLY) {
                mListener.onColorChanged();
            } else if (mType == TYPE_COLOR_TEMP || mType == TYPE_LIGHT_TEMP_ONLY) {
                mListener.onTemperatureChanged();
            }
        }

        if (downInRing) {
            mListener.onBrightnessChanged();
        }
    }

    private void onSendChanging() {
        if (mType == TYPE_COLOR_VALUE || mType == TYPE_LIGHT_VALUE_ONLY) {
            if (mListener != null) {
                mListener.onColorChanging(CommonPanelUtil.getRGB(CommonPanelUtil.getARGB(String.valueOf(mSelectedColor))));
            }
        } else if (mType == TYPE_COLOR_TEMP || mType == TYPE_LIGHT_TEMP_ONLY){
            if (mListener != null) {
                mListener.onTemperatureChanging(mColorTempRate, mSelectedColor);
            }
        }
    }

    //测量view的尺寸
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mHeight = getMeasuredHeight();
        mWidth = getMeasuredWidth();
        mRingRadius = mWidth / 2;
        mPanelRadius = mRingRadius - mRingAreaWidth - 10;
    }

    //将触摸点的坐标转换成颜色值
    private int convetXy2Color() {
        if (mType == TYPE_COLOR_VALUE) {
            float scale = rgbBitmap.getWidth() / (mPanelRadius * 2);
            int pixelX = (int) (clickX > 0 ? (clickX + mPanelRadius)*scale : Math.abs(mPanelRadius + clickX)*scale);
            int pixelY = (int) ((mPanelRadius + clickY)*scale);
            if (pixelX >= rgbBitmap.getWidth()) {
                pixelX = rgbBitmap.getWidth() -1;
            }
            if (pixelX < 0) {
                pixelX = 0;
            }
            if (pixelY >= rgbBitmap.getHeight()) {
                pixelY = rgbBitmap.getHeight() - 1;
            }
            if (pixelY < 0) {
                pixelY = 0;
            }
            return rgbBitmap.getPixel(pixelX, pixelY);
        } else {
            //色温盘最上面表示色温比例1，最下面表示色温比例0。
            //比如欧普灯的色温值范围是2000-5700，那么：色温 = 2000 + （5700 - 2000）* mColorTempRate
            mColorTempRate = (mPanelRadius + clickY) / (mPanelRadius * 2);
            //四舍五入保留两位小数
            mColorTempRate = CommonPanelUtil.get2DecimalNum(mColorTempRate);
            return getTempColor(mColorTempRate);
        }
    }

    private static int getTempColor(float radio) {
        int redStart = Color.red(TEMP_MAX_COLOR);
        int blueStart = Color.blue(TEMP_MAX_COLOR);
        int greenStart = Color.green(TEMP_MAX_COLOR);
        int redEnd = Color.red(TEMP_MIN_COLOR);
        int blueEnd = Color.blue(TEMP_MIN_COLOR);
        int greenEnd = Color.green(TEMP_MIN_COLOR);

        int red = (int) (redStart + ((redEnd - redStart) * radio + 0.5));
        int greed = (int) (greenStart + ((greenEnd - greenStart) * radio + 0.5));
        int blue = (int) (blueStart + ((blueEnd - blueStart) * radio + 0.5));
        int color = Color.argb(255, red, greed, blue);
        return color;
    }


    private boolean inColorCircle(float x, float y, float outRadius, float innerRadius) {
        //double outCircle = Math.PI * outRadius * outRadius;
        // double inCircle = Math.PI * inRadius * inRadius;
        //double fingerCircle = Math.PI * (x * x + y * y);
        float fingerCircle = (float) Math.sqrt(x * x + y * y);
        // && fingerCircle > inCircle
        if (fingerCircle > innerRadius && fingerCircle < outRadius) {
            return true;
        } else {
            return false;
        }
    }

    private void setClickAngle() {
        calculateXY();
        mSelectedColor = convetXy2Color();
        invalidate();
    }

    private void calculateXY () {
        double length = mPanelRadius;
        //转换成最边上的位置
        if (Math.pow(clickX, 2) + Math.pow(clickY, 2) > Math.pow(length, 2)) {
            clickX = (int) (Math.cos(rotateAngle * Math.PI / 180) * length);
            clickY = (int) (Math.sin(rotateAngle * Math.PI / 180) * length);
        }

        length = mPanelRadius - clickPointRadius;
        if (Math.pow(clickX, 2) + Math.pow(clickY, 2) > Math.pow(length, 2)) {
            clickPointX = (int) (Math.cos(rotateAngle * Math.PI / 180) * length);
            clickPointY = (int) (Math.sin(rotateAngle * Math.PI / 180) * length);
        } else {
            clickPointX = clickX;
            clickPointY = clickY;
        }
    }

    /**
     * 通过色温比例获取色温值
     * @param colorTempRate
     * @return
     */
    public static int getTempColorByRate(float colorTempRate) {
        return getTempColor(colorTempRate);
    }

    /**
     * 关闭灯
     */
    public void shutdown() {
        isShutdown = true;
        isDrawRing = false;
        invalidate();
    }

    /**
     * 设置灯的亮度(幅度[0..1])
     *
     * @param brightnessRate
     */
    public void setBrightnessRate(float brightnessRate) {
        ringRotateAngle = (int) (360 * brightnessRate);
        invalidate();
    }

    public void setMinRingRototeAngle(int minRingRototeAngle) {
        this.minRingRototeAngle = minRingRototeAngle;
    }

    public void setMaxRingRotateAngle(int maxRingRotateAngle) {
        this.maxRingRotateAngle = maxRingRotateAngle;
    }

    public void setColorTempRate(float tempRate) {
        isShouldCalculate = true;
        mColorTempRate = tempRate;
        invalidate();
    }

    public void setSelectedColor(int color) {
        mSelectedColor = CommonPanelUtil.parseColor(color);
        isShouldCalculate = true;
        onSendChanging();
        invalidate();
    }

    public int getSelectedColor() {
        return mSelectedColor;
    }

    public void setIsDrawRingIndex(boolean isDrawRingIndex) {
        this.isDrawRingIndex = isDrawRingIndex;
    }

    /**
     * 打开灯
     */
    public void open() {
        isShutdown = false;
        isDrawRing = true;
        invalidate();
    }

    /**
     * 当前是否是色温
     *
     * @return
     */
    public boolean isTemperature() {
        return mType == TYPE_COLOR_TEMP || mType == TYPE_LIGHT_TEMP_ONLY;
    }

    /**
     * 是否允许点击切换到其他模式（色温->颜色 或 颜色->色温）
     *
     * @param b
     */
    public void setIsSupportChangeMode(boolean b) {
        isSupportChangeMode = b;
    }

    //设置颜色变化listener
    public void setColorChangedListener(OnColorChangedListener mListener) {
        this.mListener = mListener;
    }

    public void setType(int type) {
        mType = type;
        isDrawRingIndex = true;
        if (mType == TYPE_LIGHT_TEMP_ONLY || mType == TYPE_LIGHT_VALUE_ONLY) {
            isDrawRingIndex = false;
        }
        if (type == TYPE_COLOR_VALUE || type == TYPE_LIGHT_VALUE_ONLY) {
            if (temperatureBitmap == null) {
                temperatureBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.light_rgb);
            }
        } else {
            //色温
            //temperatureBitmap = BitmapFactory.decodeResource(getContext().getResources(), R.mipmap.rgbxxhdpi);
            //色值一个默认的色温比例
            //setColorTempRate(0.5f);
        }
        invalidate();
    }

    public int getType() {
        return mType;
    }

    //回调接口, 调用的地方需要实现这个接口，响应颜色变化消息
    public interface OnColorChangedListener {
        /**
         * 色值变化
         * @param colorValue
         */
        void onColorChanging(int colorValue);

        void onColorChanged();

        /**
         * 色温变化
         * @param rate 色盘上每个y值表示一个色温百分比
         * @param tempColor
         */
        void onTemperatureChanging(float rate, int tempColor);

        /**
         * 调节色温后，手指抬起时调用
         */
        void onTemperatureChanged();

        /**
         * 亮度变化
         * @param openRate 亮度的百分比，取值是0-1， light = Math.abs(ringSwipeAngle)/360
         */
        void onBrightnessChanging(float openRate);

        /**
         * 调节亮度后，手指抬起时执行
         */
        void onBrightnessChanged();
    }
}
