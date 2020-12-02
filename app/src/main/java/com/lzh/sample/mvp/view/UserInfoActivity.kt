package com.lzh.sample.mvp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lzh.sample.R

class UserInfoActivity<IBasePresenter> : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
    }
}
