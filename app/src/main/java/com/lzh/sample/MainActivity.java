package com.lzh.sample;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lzh.sample.Utils.FormatUtils;
import com.lzh.sample.mpandroidchart.ChartActivity;
import com.lzh.sample.mpandroidchart.custombarchart.StateBarChartActivity;
import com.lzh.submodule.SubModuleMainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import me.yokeyword.fragmentation.SupportActivity;


public class MainActivity extends SupportActivity {

    public RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;
    private static List<Class> mItems = new ArrayList<>();

    static {
        mItems.add(ViewTestActivity.class);
        mItems.add(FragmentTestActivity.class);
        mItems.add(TestActivity.class);
        mItems.add(ChartActivity.class);
//        mItems.add(MatrixActivity.class);
        mItems.add(StateBarChartActivity.class);
        mItems.add(MockViewActivity.class);

    }

    private TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRecyclerView = findViewById(R.id.list_view);
        mAdapter = new ClassAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.scrollToPosition(20);


        long endTime = 1565798399999L;
        long startTime = 1514736000000L;
        preloadBackground();

        mTv = findViewById(R.id.tv_height_test);


        findViewById(R.id.text_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int textHeight = mTv.getHeight();
//                Paint.FontMetrics fontMetrics = mTv.getPaint().getFontMetrics();
//                int measureHeight = (int) (fontMetrics.bottom  - fontMetrics.top);
//                Log.d("zhiheng", "textHeight = " + textHeight + ", measuredheight = " + measureHeight + ", top = " + fontMetrics.top + ", bottom = " + fontMetrics.bottom);
//                ARouter.getInstance().build("/test/testActivity").navigation();
                ARouter.getInstance().build("/sub/MainActivity").navigation();
//                Intent intent = new Intent(MainActivity.this, SubModuleMainActivity.class);
//                startActivity(intent);
            }
        });

        Log.d("zhiheng", "num : " + Float.valueOf("10.01"));

        Log.d("zhiheng", "Flag FLAG_ACTIVITY_NEW_TASK: " + Integer.toBinaryString(Intent.FLAG_ACTIVITY_NEW_TASK));
        Log.d("zhiheng", "Flag FLAG_ACTIVITY_BROUGHT_TO_FRONT: " + Integer.toBinaryString(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT));
        Log.d("zhiheng", "Flag FILL_IN_ACTION: " + Integer.toBinaryString(Intent.FILL_IN_ACTION));
        Log.d("zhiheng", "Flag FLAG_ACTIVITY_CLEAR_TASK: " + Integer.toBinaryString(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        Log.d("zhiheng", "Flag FLAG_ACTIVITY_FORWARD_RESULT: " + Integer.toBinaryString(Intent.FLAG_ACTIVITY_FORWARD_RESULT));
        Log.d("zhiheng", "Flag FILL_IN_DATA: " + Integer.toBinaryString(Intent.FILL_IN_DATA));
        Log.d("zhiheng", "Flag FILL_IN_SELECTOR: " + Integer.toBinaryString(Intent.FILL_IN_SELECTOR));

        Log.d("zhiheng", FormatUtils.timeFormatWidthFormat(FormatUtils.getCurrentWeekZeroTime(), FormatUtils.YYYY_MM_DD_HH_MM_SSS, null));

        mTv.setBackgroundColor(-11125505);
        parseRGB(-11125505);

        List<String> datas = new ArrayList<>();

        rxJava();
    }

    private static void rxJava() {
        Observable.interval(2, TimeUnit.SECONDS).onTerminateDetach().observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(Long aLong) throws Exception {
                Log.d("zhiheng", "aLong = " + aLong);
            }
        });
    }


    private static void parseRGB(int colorInt) {
        int red = (colorInt & 0xff0000) >> 16;
        int green = (colorInt & 0x00ff00) >> 8;
        int blue = (colorInt & 0x0000ff);

        int color = (red << 16) | (green << 8) | blue;
        int color2 = 0xff000000 | (red << 16) | (green << 8) | blue;
        int color3 = Color.argb(255, red, green, blue);

        print("parseRGB:  = "  + ", red = " + red + ", green = " + green + ", blue = " + blue + ", color = " + color + ", color2 = " + color2 + ", color3 = " + color3);
    }

    public static void print(String msg) {
        Log.d("zhiheng", msg);
    }

    /**
     * 先预加载背景图片，避免跳转到MainActivity再去加载时出现闪白现象
     */
    private void preloadBackground() {
        String bgUrl = "http://img4q.duitang.com/uploads/item/201505/06/20150506202234_thzKj.jpeg";
        if (bgUrl.startsWith("http://")) {
            bgUrl = bgUrl.replace("http://", "https://");
        }
        Log.d("zhiheng", "preloadBackground url = " + bgUrl);
        Glide.with(getApplicationContext())
                .load(TextUtils.isEmpty(bgUrl) ? R.drawable.home_bg : bgUrl)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.RESOURCE))
                .preload();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    class ClassAdapter extends RecyclerView.Adapter<ClassViewHolder> {

        @NonNull
        @Override
        public ClassViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ClassViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ClassViewHolder holder, int position) {
            holder.bind(mItems.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mItems == null ? 0 : mItems.size();
        }
    }

    class ClassViewHolder extends RecyclerView.ViewHolder {

        private TextView tv;

        public ClassViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(android.R.id.text1);
        }

        public void bind(final Class clazz, int position) {
            tv.setText(clazz.getSimpleName());
            tv.setOnClickListener(view -> {

                MainActivity.this.startActivity(new Intent(MainActivity.this, clazz));

            });
        }
    }

    private void checkIntent(Context context, Intent intent) {
        Log.d("zhiheng", "flag before: " + intent.getFlags() + ", binary : " + Integer.toBinaryString(intent.getFlags()));
//        if (!(context instanceof Activity)) {
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            Log.d("zhiheng", "flag after: " + intent.getFlags() + ", binary : " + Integer.toBinaryString(intent.getFlags()));
//        }
        context.startActivity(intent);
    }
}
