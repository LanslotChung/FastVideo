package com.lanslot.fastvideo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lanslot.fastvideo.Utils.PackageUtils;
import com.lanslot.fastvideo.Views.CountDownView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;


@ContentView(R.layout.activity_splash)
public class SplashActivity extends AppCompatActivity {
    @ViewInject(R.id.version)
    private TextView version;
    @ViewInject(R.id.cd_view3)
    CountDownView countDownView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        version.setText("v" + PackageUtils.getVersion(this));
        countDownView.start();
        countDownView.setOnLoadingFinishListener(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            SplashActivity.this.finish();
        });
        countDownView.setOnClickListener((view) -> {

        });
    }
}
