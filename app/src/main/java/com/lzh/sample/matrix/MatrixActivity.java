package com.lzh.sample.matrix;

import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.lzh.sample.R;
import com.lzh.sample.Utils.CommonPanelUtil;
import com.lzh.sample.Utils.CommonUtils;

import java.security.PrivilegedAction;

public class MatrixActivity extends AppCompatActivity {

    private ImageView mImageView;
    private MatrixView mMatrixView;
    private static final float SCALE_MAX = 3.0f;
    private static final float SCALE_MIN = 1.0f;

    private float mScale = SCALE_MIN;
    private float moveX, moveY;
    private PointF preP1, preP2;
    private float transX, transY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matrix);
        mImageView = findViewById(R.id.image_view);
        mMatrixView = findViewById(R.id.matrix_view);
        mImageView.setOnTouchListener(mImageViewTouchListener);
    }

    private View.OnTouchListener mImageViewTouchListener = (v, event) -> {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                moveX = event.getX();
                moveY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int pointCount = event.getPointerCount();
                if (pointCount == 1) {
                    float disX = event.getX() - moveX;
                    float disY = event.getY() - moveY;
                    if (disX > 10 || disY > 10) {
                        moveX = event.getX();
                        moveY = event.getY();
                        postTranslation(v, disX, disY);
                    }
                } else if (pointCount == 2) {
                    postGustureScale(v, event);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (CommonUtils.isDoubleClick(this)) {
                    postScale(v, mScale == SCALE_MIN ? SCALE_MAX : SCALE_MIN, mScale == SCALE_MIN ? SCALE_MAX : SCALE_MIN, event.getX(), event.getY() );
                } else {
                    mImageView.performClick();
                }

                resetData();
                break;
        }
        return true;
    };

    private void resetData() {
        moveX = 0;
        moveY = 0;
        preP1 = null;
        preP2 = null;
    }

    private void postGustureScale(View view, MotionEvent event) {
        PointF p1 = new PointF(event.getX(0), event.getY(0));
        PointF p2 = new PointF(event.getX(1), event.getY(1));
        if (preP1 == null || preP2 == null) {
            preP1 = p1;
            preP2 = p2;
            return;
        }
        float values[] = new float[9];
        Matrix matrix = view.getMatrix();
        matrix.getValues(values);
        float scaleXPre = values[Matrix.MSCALE_X];
        float scaleYPre = values[Matrix.MSCALE_Y];
//        double disPre = Math.sqrt(Math.pow(preP1.x - preP2.x, 2) + Math.pow(preP1.y - preP2.y, 2));
//        double disNow = Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
        float scaleX = ((p2.x - p1.x) - (preP2.x - preP1.x)) / (view.getWidth() * scaleXPre);
        float scaleY = ((p2.y - p1.y) - (preP2.y - preP2.y)) / (view.getHeight() * scaleYPre);
        float centerX = (p1.x + p2.x) / 2;
        float centerY = (p1.y + p2.y) / 2;
        postScale(view, scaleX, scaleY, centerX, centerY);
    }

    private void postTranslation(View view, float dX, float dY) {
        float values[] = new float[9];
        Matrix matrix = mImageView.getMatrix();
        matrix.getValues(values);
        matrix.postTranslate(dX, dY);
//        mImageView.setImageMatrix(matrix);
//        values[Matrix.MTRANS_X] = values[Matrix.MTRANS_X] + dX;
//        values[Matrix.MTRANS_Y] = values[Matrix.MTRANS_Y] + dY;
//        matrix.setValues(values);
        mImageView.setImageMatrix(matrix);
        mImageView.invalidate();
        log("dx = " + dX + ", dy = " + dY + ", matrix = " + matrix.toString());
    }

    private void postScale(View view, float scaleX, float scaleY, float pX, float pY) {
        view.setScaleX(scaleX);
        view.setScaleY(scaleY);
        mScale = scaleX;
//        view.setPivotX(pX);
//        view.setPivotX(pY);
//        Matrix matrix = view.getMatrix();
//        matrix.postScale(scaleX, scaleY, pX, pY);
//        log("scaleX = " + scaleX + ", scaleY = " + scaleY + ", px = " + pX + ", py = " + pY + ", matrix = " + matrix.toString());
    }

    private void log(String msg) {
        Log.d("zhiheng", msg);
    }
}
