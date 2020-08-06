package com.lanslot.fastvideo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.lanslot.fastvideo.AOP.Authority.AuthUtils;
import com.lanslot.fastvideo.Utils.ActionBarUtils;
import com.lanslot.fastvideo.Utils.PackageUtils;
import com.lanslot.fastvideo.Utils.StatusBarUtil;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_about_us)
public class AboutUsActivity extends AppCompatActivity {

 @ViewInject(R.id.versionCode)
     TextView versionCode;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        View actionBar = ActionBarUtils.getInstance().setCustomActionBar(this,
                getSupportActionBar(),
                R.layout.layout_actionbar);

        actionBar.findViewById(R.id.actionbar_layout).setBackgroundResource(R.color.paleturquoise);
        actionBar.findViewById(R.id.back).setOnClickListener(v -> {
            AuthUtils.getInstance().clear();
            finish();
        });
        ((TextView) actionBar.findViewById(R.id.title)).setText("关于我们");

        StatusBarUtil.setStatusBarColor(this, R.color.paleturquoise);
        versionCode.setText(PackageUtils.getVersion(AboutUsActivity.this));

    }
    @Event(R.id.userAgreement)
    private void onServiceAgreementButtonClicked(View v) {
        AuthUtils.getInstance().startActivity(AboutUsActivity.this, UserAgreementActivity.class, null);
    }

    @Event(R.id.privacyPolicy)
    private void onPrivacyPolicyButtonClicked(View v) {
        AuthUtils.getInstance().startActivity(AboutUsActivity.this, PrivacyPolicyActivity.class, null);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AuthUtils.getInstance().clear();
        finish();
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RichText.clear(this);
    }
}
