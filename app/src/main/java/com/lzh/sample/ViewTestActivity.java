package com.lzh.sample;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

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
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
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

//        getIndicatorBeans();
//        mAdapter.notifyDataSetChanged();

        mLottieButton.setText("确定");
        mLottieButton.setLoadingText("Go!");
        mLottieButton.setEnable(true);

        glide();

        gridView();
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
        String bgUrl = "http://cdn.cnbj2.fds.api.mi-img.com/lumiaiot/service/icon/background/homepage_background_a.png";
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.home_bg)
                .error(R.drawable.home_bg)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

        Glide.with(mImageView.getContext()).load(TextUtils.isEmpty(bgUrl) ? R.drawable.home_bg : bgUrl)
                .apply(options)
//                .into(new CustomViewTarget<View, Drawable>(mImage) {
//                    @Override
//                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
//                        Log.d("zhiheng", "onLoadFailed = " + errorDrawable);
//                        mImage.setBackground(errorDrawable);
//                    }
//
//                    @Override
//                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
//                        Log.d("zhiheng", "onResourceReady = " + resource);
//                        mImage.setBackground(resource);
//                    }
//
//                    @Override
//                    protected void onResourceCleared(@Nullable Drawable placeholder) {
//                        Log.d("zhiheng", "onResourceCleared = " + placeholder);
//                        mImage.setBackground(placeholder);
//                    }
//                })
                .into(new SimpleTarget<Drawable>() {

                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        Log.d("zhiheng", "onResourceReady = " + resource);
                        mImageView.setBackground(resource);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        Log.d("zhiheng", "onLoadFailed = " + errorDrawable);
                        mImageView.setBackground(errorDrawable);
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
