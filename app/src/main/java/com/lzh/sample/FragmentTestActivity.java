package com.lzh.sample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.lzh.sample.Utils.StatusBarCompat;

import me.yokeyword.fragmentation.SupportActivity;

public class FragmentTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题栏
        setContentView(R.layout.activity_fragment_test_actviity);
        StatusBarCompat.setStatusBarIconStyle(this, true);

//        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        addFragment();
        //setNavigationBar(this, View.GONE);//隐藏导航栏
//        loadRootFragment(R.id.container, TestFragment.newInstance());
//        loadMultipleRootFragment(R.id.container, 1, TestFragment.newInstance(), TestFragment2.newInstance());

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            showBottomNav(this);
        } else {
            hideBottomNav(this);
        }
    }

//    @Override
//    public void onBackPressedSupport() {
//        super.onBackPressedSupport();
//        Log.d("zhiheng", "activity onBackpressedSupport");
//    }

    /**
     * 隐藏导航栏，显示状态栏
     */
    public static void hideBottomNav(Activity activity) {
        if (activity == null) {
            return;
        }
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        decorView.setSystemUiVisibility(uiOptions);
    }

    /**
     * 显示导航栏和状态栏
     * @param activity
     */
    public static void showBottomNav(Activity activity) {
        if (activity == null) {
            return;
        }
        View decorView = activity.getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

    private void addFragment() {
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.container, TestFragment.newInstance()).commit();
    }


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.d("zhiheng", "activity onConfigurationChanged");
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setNavigationBar(this, View.GONE);
        }
    }

    public static void setNavigationBar(Activity activity, int visible){
        View decorView = activity.getWindow().getDecorView();
        //显示NavigationBar
        if (View.GONE == visible){
            int option = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            decorView.setSystemUiVisibility(option);
        }
    }
}
