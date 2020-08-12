package com.lanslot.fastvideo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.lanslot.fastvideo.Bean.Setting;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.BottomListPopupView;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebView;
import com.ycbjie.webviewlib.client.JsX5WebViewClient;
import com.ycbjie.webviewlib.view.X5WebView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_video)
public class VideoActivity extends AppCompatActivity {
    @ViewInject(R.id.webview)
    private X5WebView mWebView;
    @ViewInject(R.id.intro_panel)
    RelativeLayout introPanel;
    @ViewInject(R.id.custom_action_bar)
    View actionBar;

    String video;
    String currentRouter;
    String[] routers;
    BottomListPopupView menuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

//        actionBar = ActionBarUtils.getInstance().setCustomActionBar(this, getSupportActionBar(), R.layout.layout_video_actionbar);
        getSupportActionBar().hide();
        actionBar.findViewById(R.id.back).setOnClickListener(v -> {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                finish();
            }
        });
        actionBar.findViewById(R.id.change_router).setOnClickListener(v -> {
            menuView.show();
        });

        routers = MyApplication.getSettingByKey(Setting.Key.INTERFACE_MOVICE_URL).split("#");
        String[] menus = new String[routers.length];
        for (int i = 0; i < menus.length; i++)
            menus[i] = "线路" + i;
        menuView = new XPopup.Builder(this)
                .asBottomList("快看视频VIP线路", menus,
                        (position, string) -> {
                            currentRouter = routers[position];
                            mWebView.loadUrl(currentRouter + video);
                        });

        currentRouter = routers[0];

        video = getIntent().getBundleExtra("data").getString("video");
        mWebView.loadUrl(currentRouter + video);

        mWebView.setWebViewClient(new JsX5WebViewClient(mWebView, this) {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
                String url = webResourceRequest.getUrl().toString();
                for (String router : routers) {
                    if (url.startsWith(router)) {
                        return super.shouldOverrideUrlLoading(webView, webResourceRequest);
                    }
                }
                return true;
                //return super.shouldOverrideUrlLoading(webView, webResourceRequest);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        boolean dont_notice = sharedPreferences.getBoolean("dont_notice_video", false);
        if (dont_notice) {
            introPanel.setVisibility(View.GONE);
        }

    }

    @Event(value = R.id.dont_notice, type = CompoundButton.OnCheckedChangeListener.class)
    private void onDontNoticeCheckChanged(CompoundButton compoundButton, boolean isChecked) {
        SharedPreferences sharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("dont_notice_video", isChecked);
        editor.commit();
    }

    @Event(R.id.iknow)
    private void onIKnowButtonClicked(View v) {
        introPanel.setVisibility(View.GONE);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            if (mWebView.canGoBack()) {
                mWebView.goBack();
            } else {
                finish();
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
}