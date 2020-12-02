package com.lzh.submodule

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter

@Route(path = "/sub/MainActivity")
class SubModuleMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub_module_main)

        findViewById<Button>(R.id.button).setOnClickListener { ARouter.getInstance().build("/test/testActivity").navigation() }
    }
}