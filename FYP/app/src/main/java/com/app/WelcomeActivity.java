package com.example.app;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class WelcomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        ImageView imageView = findViewById(R.id.imageView1);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.left_to_right);
        imageView.setAnimation(animation);

        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(WelcomeActivity.this, SliderActivity.class));
                    finish();
                }
            }, 3000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}