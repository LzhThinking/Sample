package com.lzh.sample;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.lzh.sample.widgets.DatePeriodRadio;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * @Author leizhiheng
 * @CreateDate 2019/9/11
 * @Description
 */
public class TestFragment extends Fragment {

    @BindView(R.id.root_view)
    RelativeLayout rootView;

    @BindView(R.id.title_view)
    View titleView;

    @BindView(R.id.radio_container)
    LinearLayout container;

    @BindView(R.id.date_radio)
    DatePeriodRadio datePeriodRadio;

    @BindView(R.id.constraint)
    ConstraintLayout constraintLayout;

    @BindView(R.id.text)
    TextView textView;

    public static TestFragment newInstance() {
        TestFragment fragment = new TestFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("zhiheng", "fragment onCreateView orientation = " + getResources().getConfiguration().orientation);
        return inflater.inflate(R.layout.fragment_test, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("zhiheng", "fragment onViewCreated orientation = " + getResources().getConfiguration().orientation);
//        view.findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                    //setNavigationBar(getActivity(), View.GONE);
//                    hideBottomNav(getActivity());
//                }
//            }
//        });

        ButterKnife.bind(this, view);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("zhiheng", "fragment onConfigurationChanged orientation = " + getResources().getConfiguration().orientation);
        updateViewPosition();
    }

    private void updateViewPosition() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            RelativeLayout.LayoutParams containerParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            containerParam.addRule(RelativeLayout.CENTER_IN_PARENT, R.id.root_view);
            containerParam.addRule(RelativeLayout.BELOW, R.id.title_view);
            containerParam.setMargins(0, 0, 0, 0);
            container.setLayoutParams(containerParam);


            ConstraintLayout.LayoutParams textParam = (ConstraintLayout.LayoutParams) textView.getLayoutParams();
            textParam.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
            textParam.leftToLeft = ConstraintLayout.LayoutParams.PARENT_ID;
            textParam.rightMargin = 0;
            textView.setLayoutParams(textParam);
        } else {
            RelativeLayout.LayoutParams containeParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            containeParam.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, R.id.root_view);
            containeParam.setMargins(0, 0, 0, 0);
            container.setLayoutParams(containeParam);

//            RelativeLayout.LayoutParams constraintLayoutParam = (RelativeLayout.LayoutParams) constraintLayout.getLayoutParams();
//            constraintLayoutParam.addRule(RelativeLayout.BELOW, R.id.container);

            ConstraintLayout.LayoutParams textParam = (ConstraintLayout.LayoutParams) textView.getLayoutParams();
            textParam.rightToRight = ConstraintLayout.LayoutParams.PARENT_ID;
            textParam.leftToLeft = -1;
            textParam.rightMargin = 0;
            textView.setLayoutParams(textParam);
        }
    }

    public static void hideBottomNav(Activity activity) {
        if (activity == null) {
            return;
        }
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(0);
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    /**
     * 重新显示导航栏和状态栏
     */
    public static void showBottomNav(Activity activity) {
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(0);
    }

    public static void setNavigationBar(Activity activity, int visible){
        View decorView = activity.getWindow().getDecorView();
        //显示NavigationBar
        if (View.GONE == visible){
            int option = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            decorView.setSystemUiVisibility(option);
        }
    }

//    @Override
//    public boolean onBackPressedSupport() {
//        Log.d("zhiheng", "fragment onBackpressedSupport");
//        return super.onBackPressedSupport();
//    }
}
