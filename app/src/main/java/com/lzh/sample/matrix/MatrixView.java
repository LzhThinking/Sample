package com.lzh.sample.matrix;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.lzh.sample.R;

public class MatrixView extends View implements View.OnTouchListener {
    public MatrixView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setOnTouchListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Matrix matrix = getMatrix();
        log("matrix: " + matrix.toString());

        canvas.drawColor(getResources().getColor(R.color.color_5FA7FE));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    private void log(String msg) {
        Log.d("zhiheng-matrix", msg);
    }
}
