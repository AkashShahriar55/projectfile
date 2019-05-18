package com.example.bimvendpro;

import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {

    private ImageView img;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        img = findViewById(R.id.splash_image);
        textView = findViewById(R.id.splash_copyright);

        textView.setAlpha((float) 0.0);

        // Making notification bar transparent
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this,IntroScreenActivity.class);
                startActivity(i);
                finish();
            }
        },5000);

        Animation animZoomIn = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.zoom_in);
        img.startAnimation(animZoomIn);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                textView.setAlpha((float) 1.0);
                Animation animfade = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.alpha);
                textView.startAnimation(animfade);
            }
        },1000);



    }
}
