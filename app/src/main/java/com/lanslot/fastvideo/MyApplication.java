package com.lanslot.fastvideo;

import android.app.Application;

import com.lanslot.fastvideo.AOP.Authority.AuthUtils;
import com.lanslot.fastvideo.AOP.Authority.LoginAuthority;
import com.lanslot.fastvideo.Bean.JSON.SettingJSON;
import com.lanslot.fastvideo.Bean.Setting;
import com.lanslot.fastvideo.Utils.PackageUtils;

import org.xutils.x;

public class MyApplication extends Application {
    public boolean isLogin;
    static private MyApplication instance;
    static private SettingJSON settings;


    public static String getApkName() {
        return apkName;
    }

    public static void setApkName(String apkName) {
        MyApplication.apkName = apkName;
    }

    static private String apkName;
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
        instance = this;
        AuthUtils.getInstance().addAuthority(LoginAuthority.class,InviteActivity.class);
        AuthUtils.getInstance().addAuthority(LoginAuthority.class, ModifyPasswordActivity.class);
    }

    public static MyApplication getApplication() {
        return instance;
    }

    public static void setSettings(SettingJSON settings) {
        MyApplication.settings = settings;
    }

    public static String getSettingByKey(Setting.Key key) {
        if (settings != null) {
            for (Setting setting : settings.getDatas()) {
                if (setting.getKey().equals(key.toString())) {
                    return setting.getValue();
                }
            }
        }
        return null;
    }
}
