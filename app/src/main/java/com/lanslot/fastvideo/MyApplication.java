package com.lanslot.fastvideo;

import android.app.Application;

import com.lanslot.fastvideo.AOP.Authority.AuthUtils;
import com.lanslot.fastvideo.AOP.Authority.LoginAuthority;
import com.ycbjie.webviewlib.utils.X5WebUtils;

import org.xutils.x;

public class MyApplication extends Application {
    public boolean isLogin;
    static private MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
        X5WebUtils.init(this);
        instance = this;
        AuthUtils.getInstance().addAuthority(LoginAuthority.class, InviteActivity.class, WebViewActivity.class);
    }

    public static MyApplication getApplication() {
        return instance;
    }
}
