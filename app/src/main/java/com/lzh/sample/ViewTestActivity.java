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
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.gif.GifBitmapProvider;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.github.chrisbanes.photoview.PhotoView;
import com.lzh.sample.binder.IndicatorItemViewBinder;
import com.lzh.sample.entity.IndicatroItemBean;
import com.lzh.sample.viewtest.RemarkAdapter;
import com.lzh.sample.widgets.CommonGridDecoration;
import com.lzh.sample.widgets.CountDownView;
import com.lzh.sample.widgets.DatePeriodRadio;
import com.lzh.sample.widgets.LottieButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import me.drakeet.multitype.MultiTypeAdapter;

public class ViewTestActivity extends AppCompatActivity {

    @BindView(R.id.button_1)
    Button mBtn;

    @BindView(R.id.seek_bar)
    SeekBar seekBar;
    @BindView(R.id.date_radio)
    DatePeriodRadio radio;
    @BindView(R.id.iv_1)
    ImageView iv1;
    @BindView(R.id.iv_22)
    ImageView iv2;
    @BindView(R.id.scroll_view)
    ScrollView mScrollView;
    @BindView(R.id.root_view)
    public LinearLayout mRootView;
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

    @BindView(R.id.tv_content_left)
    TextView tvContentLeft;
    @BindView(R.id.tv_title_left)
    TextView tvTitleLeft;
    @BindView(R.id.tv_unit_left)
    TextView tvUnitLeft;

    @BindView(R.id.tv_content_right)
    TextView tvContentRight;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.tv_unit_right)
    TextView tvUnitRight;

    @BindView(R.id.count_down_view)
    CountDownView countDownView;
    @BindView(R.id.tv_height_test)
    TextView textView;

    @BindView(R.id.iv_gif)
    ImageView mIvGif;

    private float scale = 0f;

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

        mRootView.setEnabled(false);
        mRootView.setNestedScrollingEnabled(false);

        dealTouch();


        mBtn.setOnClickListener(v -> {
//            scale += 1f;
//            mBtn.setText(mBtn.getWidth() * scale + "");
//
////            Toast.makeText(getApplicationContext(), "clickable", Toast.LENGTH_LONG).show();
//            int textHeight = textView.getHeight();
//            Paint.FontMetrics fontMetrics = textView.getPaint().getFontMetrics();
//            int measureHeight = (int) (fontMetrics.bottom = fontMetrics.top);
//            Log.d("zhiheng", "textHeight = " + textHeight + ", measuredheight = " + measureHeight + ", top = " + fontMetrics.top + ", bottom = " + fontMetrics.bottom);
            int width = seekBar.getWidth();
            int processDrawableWidth = seekBar.getProgressDrawable().getBounds().width();
            int processWidth = processDrawableWidth - seekBar.getThumb().getBounds().width();
            int leftPadding = seekBar.getPaddingLeft();
            int rightPadding = seekBar.getPaddingRight();
            Log.d("zhiheng", "seekBar width = " + width + ", processDrawableWidth = " + processDrawableWidth + ", processWidth = " + processWidth + ", leftPadding = " + leftPadding + ", rightPadding = " + rightPadding);
        });

        radio.setOnRadioSelectedListener(index -> Toast.makeText(ViewTestActivity.this, "index = " + index, Toast.LENGTH_LONG).show());

        setChartSummarize();

        getWindow().setNavigationBarColor(getResources().getColor(R.color.transparent));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);

        countDownView.setTotolTime(60);
        countDownView.setCurrentTime(20);
        countDownView.setDecsText("后关闭");
        countDownView.startCountDown();

        int color = Color.toArgb(-58048);
//        int red = (int) (color.red() * 255);
//        int green = (int) (color.green() * 255);
//        int blue = (int) (color.blue() * 255);
//        int r = Color.red(-58048);
//        int g = Color.green(-58048);
//        int b = Color.blue(-58048);

//        int bg = 0xff000000 | (r | g | b);

        mRootView.setBackgroundColor(color);

        bind();

    }

    public void RecommendedAutoItemViewHolder(View itemView) {


    }

    public void bind() {
//        String url = "http://cnbj2.fds.api.xiaomi.com/lumiaiot/ifttt/template-pic/recommend_auto_b%403x.png";
        String url = "http://henan.china.com.cn/pic/2020-01/02/81fcad0b-6c4c-4eb7-8351-324e8390ad77.jpg";
//        RoundedCornersTransformation mRoundedCornersTransformation = new RoundedCornersTransformation(60 , 0, RoundedCornersTransformation.CornerType.ALL);
//        Glide.with(getA).load(url).into(mIvGif);


        RequestOptions options = new RequestOptions()
                .centerCrop()
                .error(R.drawable.home_bg)
                .priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);
        Glide.with(mIvGif).load(url)
                .apply(options)
                .into(mIvGif);

    }


    private void setChartSummarize() {
        tvTitleLeft.setText("浸水");
        tvUnitLeft.setVisibility(View.GONE);
        tvContentLeft.setText("当前状态");

        tvTitleRight.setText("4");
        tvUnitRight.setText("次");
        tvContentRight.setText("今日事件触发");
    }

    private void dealTouch() {
        mRootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }

    private void text() {
        TextView textView = findViewById(R.id.tv_click);
        ColorStateList csl = getResources().getColorStateList(R.color.black);
        textView.setTextColor(csl);
        textView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return  true;
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
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE);

        Glide.with(mIvGif).load(R.mipmap.constant).into(mIvGif);

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
