package com.lanslot.fastvideo;

import android.os.Bundle;

import com.lanslot.fastvideo.AOP.Authority.AuthUtils;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.widget.ImageView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_splash)
public class SplashActivity extends AppCompatActivity {
    @ViewInject(R.id.splash)
    ImageView splashImg;

    private final int SPLASH_DISPLAY_LENGHT = 500;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        handler = new Handler();
        // 延迟SPLASH_DISPLAY_LENGHT时间然后跳转到MainActivity
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                AuthUtils.getInstance().startActivity(SplashActivity.this,MainActivity.class,null);
            }
        }, SPLASH_DISPLAY_LENGHT);
    }


}
