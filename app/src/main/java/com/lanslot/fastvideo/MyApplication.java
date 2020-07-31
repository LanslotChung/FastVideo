package com.lanslot.fastvideo;

import android.app.Application;

import com.lanslot.fastvideo.AOP.Authority.AuthUtils;
import com.lanslot.fastvideo.AOP.Authority.LoginAuthority;

import org.xutils.x;

public class MyApplication extends Application {
    public boolean isLogin;
    static private MyApplication instance;
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
        instance = this;
        AuthUtils.getInstance().addAuthority(LoginAuthority.class,InviteActivity.class);
    }

    public static MyApplication getApplication() {
        return instance;
    }
}
