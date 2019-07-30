package com.example.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class SubActivity extends AppCompatActivity {

    int fragmentCount;
    FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        manager = getSupportFragmentManager();
        addFragment();
    }

    public void addFragment() {
        if (fragmentCount == 0) {
            fragmentCount++;
            manager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_right)
                    .add(R.id.container, new MyFragment()).addToBackStack("fragment").commit();

        } else if (fragmentCount == 1) {
            fragmentCount++;
            manager.beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_right)
                    .add(R.id.container, new MyFragment2()).addToBackStack("fragment2").commit();
        } else {
            manager.popBackStack();
            fragmentCount--;
        }
    }
}
