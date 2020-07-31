package com.lanslot.fastvideo.AOP.Authority;

import com.lanslot.fastvideo.AOP.Annotation.AOPOrder;
import com.lanslot.fastvideo.LoginActivity;
import com.lanslot.fastvideo.MyApplication;

@AOPOrder(0)
public class LoginAuthority extends BaseAuthority {
    LoginAuthority(){
        failActivity = LoginActivity.class;
    }
    @Override
    public boolean check() {
        return MyApplication.getApplication().isLogin;
    }
}
