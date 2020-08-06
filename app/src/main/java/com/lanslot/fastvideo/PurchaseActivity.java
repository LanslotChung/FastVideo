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
    @ViewInject(R.id.richtext)
    TextView richtext;

    private String html = "<div><span>请添加支付宝/微信订阅会员</span><br /><img src=\"http://timg01.bdimg.com/timg" +
            "?pacompress&imgtype=0&sec=1439619614&" +
            "di=8a54bb99aef8b192debf0b251be173be" +
            "&quality=90&size=b870_10000&src=http%3A%2F%2Fbos.nj.bpc.baidu.com" +
            "%2Fv1%2Fmediaspot" +
            "%2Fb767998c68ac1c9aec747491b0f5e1c5.jpeg\"></div>";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        View actionBar = ActionBarUtils.getInstance().setCustomActionBar(this,
                getSupportActionBar(),
                R.layout.layout_actionbar);

        actionBar.findViewById(R.id.actionbar_layout).setBackgroundResource(R.color.dodgerblue);
        actionBar.findViewById(R.id.back).setOnClickListener(v -> {
            AuthUtils.getInstance().clear();
            finish();
        });
        ((TextView) actionBar.findViewById(R.id.title)).setText("订阅会员");

        StatusBarUtil.setStatusBarColor(this, R.color.dodgerblue);

        RichText.initCacheDir(this);
        RichText.from(html)
                .bind(this)
                .showBorder(false)
                .size(ImageHolder.MATCH_PARENT, ImageHolder.WRAP_CONTENT)
                .into(richtext);
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