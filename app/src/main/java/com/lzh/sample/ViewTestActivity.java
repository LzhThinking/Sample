package com.lzh.sample;

import android.animation.ObjectAnimator;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.lzh.sample.binder.IndicatorItemViewBinder;
import com.lzh.sample.entity.IndicatroItemBean;
import com.lzh.sample.viewtest.RemarkAdapter;
import com.lzh.sample.widgets.CommonGridDecoration;
import com.lzh.sample.widgets.LottieButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.MultiTypeAdapter;

public class ViewTestActivity extends AppCompatActivity {

    @BindView(R.id.iv_1)
    ImageView iv1;
    @BindView(R.id.iv_22)
    ImageView iv2;
    @BindView(R.id.root_view)
    public View mRootView;
    @BindView(R.id.iv_bg)
    public ImageView mIvBg;
    @BindView(R.id.list_view)
    public RecyclerView mRecyclerView;
    @BindView(R.id.lottie_button)
    LottieButton mLottieButton;
    @BindView(R.id.image)
    FrameLayout mImage;
    @BindView(R.id.image_view)
    ImageView mImageView;
    private List<Object> mItems;
    public MultiTypeAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_test);
        ButterKnife.bind(this);
        initView();

        getIndicatorBeans();
        mAdapter.notifyDataSetChanged();

        mLottieButton.setText("确定");
        mLottieButton.setLoadingText("Go!");
        mLottieButton.setEnable(true);

        glide();

//        gridView();
        viewShot();

        PhotoView photoView;
        text();
    }

    private void text() {
        TextView textView = findViewById(R.id.tv_click);
        ColorStateList csl = getResources().getColorStateList(R.color.black);
        textView.setTextColor(csl);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void viewShot() {
//        iv1.setRotationX(180);
//        ObjectAnimator animator = ObjectAnimator.ofFloat(iv1, "rotationX", 180);
//        animator.start();
        iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv1.setDrawingCacheEnabled(true);
                Bitmap bitmap = Bitmap.createBitmap(iv1.getDrawingCache());
                iv1.setDrawingCacheEnabled(false);

                Matrix matrix = new Matrix();
                //1, -1 垂直翻转
                //-1， 1 水平翻转
                //-1, -1 水平垂直翻转
                matrix.postScale(2, 2);
//                matrix.postRotate(90);
                Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, iv1.getWidth(), iv1.getHeight(), matrix, true);

                iv2.setImageBitmap(resizeBmp);
            }
        });

    }


    private void gridView() {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < 13; i++) {
            names.add("Name-" + i);
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        RemarkAdapter adapter = new RemarkAdapter(names);
        mRecyclerView.addItemDecoration(new CommonGridDecoration(40, 40, getResources().getColor(android.R.color.white), false));
        mRecyclerView.setAdapter(adapter);
    }

    private void initView() {
        mItems = new ArrayList<>();
        mAdapter = new MultiTypeAdapter(mItems);
        mAdapter.register(IndicatroItemBean.class, new IndicatorItemViewBinder());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void glide() {
        String bgUrl = "http://img4q.duitang.com/uploads/item/201505/06/20150506202234_thzKj.jpeg";
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.drawable.home_bg)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        if (bgUrl.startsWith("http://")) {
            bgUrl = bgUrl.replace("http://", "https://");
        }
        Log.d("zhiheng", "setHomeBackground url = " + bgUrl);
        Glide.with(mRootView.getContext()).load(TextUtils.isEmpty(bgUrl) ? R.drawable.home_bg : bgUrl)
                .apply(options)
                .into(new CustomViewTarget<View, Drawable>(mRootView) {
                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        mRootView.setBackground(errorDrawable);
                    }

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        mRootView.setBackground(resource);
                    }

                    @Override
                    protected void onResourceCleared(@Nullable Drawable placeholder) {
                        mRootView.setBackground(placeholder);
                    }
                });
    }

    private void getIndicatorBeans() {
        mItems.clear();
        for (int i = 1; i < 10; i++) {
            IndicatroItemBean bean = new IndicatroItemBean();
            bean.title = "Title-" + i;
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < i; j++) {
                builder.append("Content------").append(i).append("\n");
            }
            bean.content = builder.toString();
            mItems.add(bean);
        }
    }
}
