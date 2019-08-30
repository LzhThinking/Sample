package com.lzh.sample;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lzh.sample.Utils.FormatUtils;
import com.lzh.sample.matrix.MatrixActivity;
import com.lzh.sample.mpandroidchart.ChartActivity;
import com.lzh.sample.mpandroidchart.custombarchart.StateBarChartActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    @BindView(R.id.list_view)
    public RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private static List<Class> mItems = new ArrayList<>();

    static {
        mItems.add(ViewTestActivity.class);
        mItems.add(TestActivity.class);
        mItems.add(ChartActivity.class);
//        mItems.add(MatrixActivity.class);
        mItems.add(StateBarChartActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mAdapter = new ClassAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        long endTime = 1565798399999L;
        long startTime = 1514736000000L;
        System.out.println("leizhiheng start time: " + FormatUtils.timeFormatWidthFormat(startTime, FormatUtils.YYYY_MM_DD_HH_MM));
        System.out.println("leizhiheng end time: " + FormatUtils.timeFormatWidthFormat(endTime, FormatUtils.YYYY_MM_DD_HH_MM));
        preloadBackground();
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
            holder.bind(mItems.get(position));
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

        public void bind(final Class clazz) {
            tv.setText(clazz.getSimpleName());
            tv.setOnClickListener(view -> MainActivity.this.startActivity(new Intent(MainActivity.this, clazz)));
        }
    }
}
