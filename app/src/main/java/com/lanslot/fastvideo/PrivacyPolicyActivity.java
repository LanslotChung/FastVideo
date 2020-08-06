package com.lanslot.fastvideo;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lanslot.fastvideo.AOP.Authority.AuthUtils;
import com.lanslot.fastvideo.Utils.ActionBarUtils;
import com.lanslot.fastvideo.Utils.StatusBarUtil;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_privacy_policy)
public class PrivacyPolicyActivity extends AppCompatActivity {

    @ViewInject(R.id.privacyPolicyContent)
    TextView ppc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        View actionBar = ActionBarUtils.getInstance().setCustomActionBar(this,
                getSupportActionBar(),
                R.layout.layout_actionbar);

        actionBar.findViewById(R.id.actionbar_layout).setBackgroundResource(R.color.sandybrown);
        actionBar.findViewById(R.id.back).setOnClickListener(v -> {
            AuthUtils.getInstance().clear();
            finish();
        });
        ((TextView) actionBar.findViewById(R.id.title)).setText("隐私政策");

        StatusBarUtil.setStatusBarColor(this, R.color.sandybrown);
        RichText.initCacheDir(this);
        RichText.from(getResources().getString(R.string.privacy_policy_content))
                .bind(this)
                .showBorder(false)
                .size(ImageHolder.MATCH_PARENT, ImageHolder.WRAP_CONTENT)
                .into(ppc);

    }
}