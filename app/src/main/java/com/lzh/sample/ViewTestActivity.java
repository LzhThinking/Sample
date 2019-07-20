package com.lzh.sample;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lzh.sample.binder.IndicatorItemViewBinder;
import com.lzh.sample.entity.IndicatroItemBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.MultiTypeAdapter;

public class ViewTestActivity extends AppCompatActivity {

    @BindView(R.id.list_view)
    public RecyclerView mRecyclerView;
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
    }

    private void initView() {
        mItems = new ArrayList<>();
        mAdapter = new MultiTypeAdapter(mItems);
        mAdapter.register(IndicatroItemBean.class, new IndicatorItemViewBinder());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
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
