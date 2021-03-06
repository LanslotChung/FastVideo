package com.lanslot.fastvideo;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.fastjson.JSON;
import com.lanslot.fastvideo.AOP.Authority.AuthUtils;
import com.lanslot.fastvideo.Bean.Config;
import com.lanslot.fastvideo.Bean.JSON.StringDataJSON;
import com.lanslot.fastvideo.Bean.JSON.UserJSON;
import com.lanslot.fastvideo.DB.UserInfo;
import com.lanslot.fastvideo.DB.UserInfoDao;
import com.lanslot.fastvideo.Http.HttpCommon;
import com.lanslot.fastvideo.Utils.DeviceUtils;
import com.lanslot.fastvideo.Utils.RegUtils;
import com.lanslot.fastvideo.Utils.StatusBarUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {
    static public final int RESPONSE_LOGIN_SUCC = 1001;
    static public final int RESPONSE_LOGIN_FAIL = 1002;

    @ViewInject(R.id.username)
    EditText username;
    @ViewInject(R.id.password)
    EditText password;
    @ViewInject(R.id.regcode)
    EditText regcode;
    Animation shakeAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        getSupportActionBar().hide();
        StatusBarUtil.setStatusBarColor(this,R.color.login_background);
        shakeAnim = AnimationUtils.loadAnimation(this,R.anim.shake);
    }

    @Event(R.id.back)
    private void onBackButtonClicked(View v){
        back();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            back();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void back(){
        AuthUtils.getInstance().clear();
        finish();
    }

    @Event(R.id.login)
    private void onLoginButtonClicked(View v){
        String un = username.getText().toString();
        String pw = password.getText().toString();
        String rc = regcode.getText().toString();
        if(!RegUtils.isPhoneNumber(un)){
            username.startAnimation(shakeAnim);
            Toast.makeText(this, R.string.error_username, Toast.LENGTH_SHORT).show();
            return;
        }
        if (pw.length() < 6 || pw.length() >= 11) {
            password.startAnimation(shakeAnim);
            Toast.makeText(this, R.string.error_password, Toast.LENGTH_SHORT).show();
            return;
        }
        login(un, pw);
    }

    @Event(R.id.register)
    private void onRegisterButtonClicked(View v) {
        String un = username.getText().toString();
        String pw = password.getText().toString();
        String rc = regcode.getText().toString();
        if (!RegUtils.isPhoneNumber(un)) {
            username.startAnimation(shakeAnim);
            Toast.makeText(this, R.string.error_username, Toast.LENGTH_SHORT).show();
            return;
        }
        if(!RegUtils.isPassWord(pw)){
            password.startAnimation(shakeAnim);
            Toast.makeText(this,R.string.error_password,Toast.LENGTH_SHORT).show();
            return;
        }

        register(un,pw,rc);
    }

    private void login(String un,String pw){
        RequestParams params = new RequestParams(HttpCommon.LOGIN);
        params.addQueryStringParameter("mobile",un);
        params.addQueryStringParameter("password",pw);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                StringDataJSON jo = JSON.parseObject(result, StringDataJSON.class);
                if(jo.getCode() == 0){
                    Toast.makeText(LoginActivity.this,R.string.login_succ,Toast.LENGTH_SHORT).show();
                    UserInfo userInfo = new UserInfo();
                    userInfo.setMobile(un);
                    userInfo.setPassword(pw);
                    userInfo.setToken(jo.getDatas());
                    UserInfoDao.getInstance().addOrUpdate(userInfo);

                    getUserInfo();
                }else{
                    MyApplication.getApplication().isLogin = false;
                    Toast.makeText(LoginActivity.this,jo.getMsg(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MyApplication.getApplication().isLogin = false;
                Toast.makeText(LoginActivity.this,R.string.login_fail,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void register(String un,String pw,String rc){
        RequestParams params = new RequestParams(HttpCommon.REGISTER);
        params.addBodyParameter("mobile",un);
        params.addBodyParameter("password",pw);
        params.addBodyParameter("inviteCode",rc);
        params.addBodyParameter("deviceInfo", DeviceUtils.INSTANCE.getUniqueId(this));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                //finish();
                StringDataJSON jo = JSON.parseObject(result, StringDataJSON.class);
                if(jo.getCode() == 0){
                    Toast.makeText(LoginActivity.this,R.string.register_succ,Toast.LENGTH_SHORT).show();
                    login(un, pw);
                }else{
                    Toast.makeText(LoginActivity.this,jo.getMsg(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(LoginActivity.this, R.string.register_fail, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void getUserInfo() {
        UserInfo userInfo = UserInfoDao.getInstance().find();
        RequestParams params = new RequestParams(HttpCommon.USER_INFO);
        params.addHeader("uniquetoken", userInfo.getToken());
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                UserJSON jo = JSON.parseObject(result, UserJSON.class);
                if (jo.getCode() == 0) {
                    Config.getInstance().setUser(jo.getDatas());

                    Intent intent = new Intent();
                    setResult(RESPONSE_LOGIN_SUCC, intent);

                    MyApplication.getApplication().isLogin = true;
                    AuthUtils.getInstance().pass(LoginActivity.this);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, jo.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(LoginActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }
}
