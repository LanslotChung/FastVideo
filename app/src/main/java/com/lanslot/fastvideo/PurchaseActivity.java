package com.lanslot.fastvideo;

import android.os.Bundle;
import android.view.KeyEvent;
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

@ContentView(R.layout.activity_purchase)
public class PurchaseActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        View actionBar = ActionBarUtils.getInstance().setCustomActionBar(this,
                getSupportActionBar(),
                R.layout.layout_actionbar);

        actionBar.findViewById(R.id.actionbar_layout).setBackgroundResource(R.color.thistle);
        actionBar.findViewById(R.id.back).setOnClickListener(v -> {
            AuthUtils.getInstance().clear();
            finish();
        });
        ((TextView) actionBar.findViewById(R.id.title)).setText("订阅会员");

        StatusBarUtil.setStatusBarColor(this, R.color.thistle);

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