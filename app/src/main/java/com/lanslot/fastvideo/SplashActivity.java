package com.lanslot.fastvideo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lanslot.fastvideo.Utils.PackageUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Timer;
import java.util.TimerTask;

@ContentView(R.layout.activity_splash)
public class SplashActivity extends AppCompatActivity {
    @ViewInject(R.id.version)
    private TextView version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        version.setText(PackageUtils.getVersion(this));

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                SplashActivity.this.runOnUiThread(() -> {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                });
            }
        }, 2000);
    }
}
