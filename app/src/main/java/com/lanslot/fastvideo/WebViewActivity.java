package com.lanslot.fastvideo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lanslot.fastvideo.AOP.Authority.AuthUtils;
import com.lanslot.fastvideo.Utils.ActionBarUtils;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebView;
import com.ycbjie.webviewlib.client.JsX5WebViewClient;
import com.ycbjie.webviewlib.view.X5WebView;
import com.ycbjie.webviewlib.widget.WebProgress;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_web_view)
public class WebViewActivity extends AppCompatActivity {
    @ViewInject(R.id.webview)
    private X5WebView mWebView;
    @ViewInject(R.id.intro_panel)
    RelativeLayout introPanel;

    private WebProgress pb;
    private String lastUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        View actionBar = ActionBarUtils.getInstance().setCustomActionBar(this, getSupportActionBar(), R.layout.layout_webview_actionbar);

        actionBar.findViewById(R.id.back).setOnClickListener(v -> {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                Toast.makeText(this, R.string.noback, Toast.LENGTH_SHORT).show();
            }
        });

        actionBar.findViewById(R.id.close).setOnClickListener(v -> finish());
        actionBar.findViewById(R.id.refresh).setOnClickListener(v -> mWebView.reload());
        actionBar.findViewById(R.id.play).setOnClickListener(v -> {
            Bundle data = new Bundle();
            data.putString("video", mWebView.getUrl());
            AuthUtils.getInstance().startActivity(this, VideoActivity.class, data);
        });

        String url = getIntent().getBundleExtra("data").getString("url");
        mWebView.loadUrl(url);

        mWebView.setWebViewClient(new JsX5WebViewClient(mWebView, this) {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
                String scheme = webResourceRequest.getUrl().getScheme();
                if (scheme.equals("iqiyi")) {//爱奇艺
                    return true;
                } else if (scheme.equals("hntvmobile") || scheme.equals("imgotv")) {//芒果TV
                    return true;
                }
                return super.shouldOverrideUrlLoading(webView, webResourceRequest);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        boolean dont_notice = sharedPreferences.getBoolean("dont_notice", false);
        if (dont_notice) {
            introPanel.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                Toast.makeText(this, R.string.noback, Toast.LENGTH_SHORT).show();
            }
            return false;
        } else
            return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null)
            mWebView.destroy();
        super.onDestroy();
    }

    @Event(value = R.id.dont_notice, type = CompoundButton.OnCheckedChangeListener.class)
    private void onDontNoticeCheckChanged(CompoundButton compoundButton, boolean isChecked) {
        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("dont_notice", isChecked);
        editor.commit();
    }

    @Event(R.id.iknow)
    private void onIKnowButtonClicked(View v) {
        introPanel.setVisibility(View.GONE);
    }
}