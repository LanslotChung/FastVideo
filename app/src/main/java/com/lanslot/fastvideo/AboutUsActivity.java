package com.lanslot.fastvideo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.lanslot.fastvideo.AOP.Authority.AuthUtils;
import com.lanslot.fastvideo.Utils.ActionBarUtils;
import com.lanslot.fastvideo.Utils.StatusBarUtil;
import com.zzhoujay.richtext.ImageHolder;
import com.zzhoujay.richtext.RichText;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_about_us)
public class AboutUsActivity extends AppCompatActivity {

    @ViewInject(R.id.richtext)
    TextView richtext;

    private String html = "<div><span>我们的公众号</span><br /><img src=\"https://timgsa.baidu.com/timg?"+
            "image&quality=80&size=b9999_10000&sec=1596452682537&"+
            "di=b685e06305b1820647b6a5c463b011aa&imgtype=0&"+
            "src=http%3A%2F%2Fpic168.nipic.com%"+
            "2Ffile%2F20180611%2F27279308_183020193000_2.jpg\"></div>";


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
        ((TextView) actionBar.findViewById(R.id.title)).setText("关于我们");

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
