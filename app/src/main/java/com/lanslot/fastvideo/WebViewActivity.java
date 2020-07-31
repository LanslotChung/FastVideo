package com.lanslot.fastvideo;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.lanslot.fastvideo.Utils.ActionBarUtils;
import com.tencent.smtt.export.external.interfaces.WebResourceRequest;
import com.tencent.smtt.sdk.WebView;
import com.ycbjie.webviewlib.client.JsX5WebViewClient;
import com.ycbjie.webviewlib.view.X5WebView;
import com.ycbjie.webviewlib.widget.WebProgress;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_web_view)
public class WebViewActivity extends AppCompatActivity {
    @ViewInject(R.id.webview)
    private X5WebView mWebView;
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

            @Override
            public void onLoadResource(WebView webView, String s) {

                String js = "$(\".poster_mask_a\").css(\"background\",\"#fff\");\n" +
                        "$(\".player_viptips\").click = function(){alert(111)};";
                mWebView.evaluateJavascript(js);
                super.onLoadResource(webView, s);
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            Toast.makeText(this, R.string.noback, Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        if (mWebView != null)
            mWebView.destroy();
        super.onDestroy();
    }
}