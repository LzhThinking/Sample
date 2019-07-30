package com.example.demo;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Dialog dialog = new Dialog(MyApplication.sApp);
        dialog.setTitle("测试广播");
        dialog.show();
    }
}
