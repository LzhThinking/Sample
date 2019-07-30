package com.example.demo;

import android.app.Application;

public class MyApplication extends Application {
    public static Application sApp;

    @Override
    public void onCreate() {
        super.onCreate();
        sApp = this;
    }
}
