package com.lzh.sample.mpandroidchart;

import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application {
    public static Context sAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = this;
    }
}
