package com.example.demo;

import android.content.Intent;
import android.view.MotionEvent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.airbnb.lottie.LottieAnimationView;

public class MainActivity extends AppCompatActivity {

    private LottieAnimationView lottieAnimationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.text_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent("action.test");
//                ComponentName componentName = new ComponentName("com.example.demo", "com.example.demo.MyReceiver");
//                intent.setComponent(componentName);
//                sendBroadcast(intent);
                Intent intent = new Intent(MainActivity.this, SubActivity.class);
                startActivity(intent);
            }
        });

        lottieAnimationView = findViewById(R.id.lottie);
        lottieAnimationView.setAnimation("json/button.json");
        lottieAnimationView.setRepeatCount(10);

        lottieAnimationView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                lottieAnimationView.playAnimation();
                return false;
            }
        });

        View rootView = findViewById(R.id.root_view);
        rootView.setAlpha(1.0f);
        rootView.postInvalidate();
    }
}
