package com.lzh.sample.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import com.lzh.sample.R;


public class HSBImageView extends AppCompatImageView {
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

    private float hueValue = 0f;
    private float saturationValue = 1f;
    private float brightnessValue = 1f;

    public HSBImageView(Context context) {
        super(context);
    }

    public HSBImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HSBImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.HSBImageView);
        hueValue = 180.0f;//array.getFloat(R.styleable.HSBImageView_hueValue, 0f);
        saturationValue = 0.5f;//array.getFloat(R.styleable.HSBImageView_saturationValue, 1f);
        brightnessValue = 0.5f;//array.getFloat(R.styleable.HSBImageView_brightnessValue, 1f);
        array.recycle();  //释放资源
        setHSB(hueValue, saturationValue, brightnessValue);
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        setHSB(hueValue, saturationValue, brightnessValue);
    }

    private void setHSB(float hueValue, float saturationValue, float brightnessValue) {
        hueValue = 180f;
        saturationValue = 1.0f;
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

        setColorFilter(new ColorMatrixColorFilter(colorMatrix));
    }
}
