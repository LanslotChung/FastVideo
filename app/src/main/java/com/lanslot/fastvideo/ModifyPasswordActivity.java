package com.lanslot.fastvideo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.lanslot.fastvideo.AOP.Authority.AuthUtils;
import com.lanslot.fastvideo.Bean.JSON.StringDataJSON;
import com.lanslot.fastvideo.DB.UserInfo;
import com.lanslot.fastvideo.DB.UserInfoDao;
import com.lanslot.fastvideo.Http.HttpCommon;
import com.lanslot.fastvideo.Utils.RegUtils;
import com.lanslot.fastvideo.Utils.StatusBarUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.modify_password)
public class ModifyPasswordActivity extends AppCompatActivity {
    static public final int RESPONSE_LOGIN_SUCC = 1001;
    @ViewInject(R.id.username)
    private EditText username;
    @ViewInject(R.id.password)
    private EditText password;
    Animation shakeAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        UserInfo userInfo = UserInfoDao.getInstance().find();
        username.setText(userInfo.getMobile());
        username.setEnabled(false);//去掉点击时编辑框下面横线:
        getSupportActionBar().hide();
        StatusBarUtil.setStatusBarColor(this,R.color.login_background);

    }
    @Event(R.id.back)
    private void onBackButtonClicked(View v){
        back();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        back();
        return false;
    }

    private void back(){
        AuthUtils.getInstance().clear();
        finish();
    }

    @Event(R.id.modify)
    private void onResetPassword(View v){
        String un = username.getText().toString();
        String pw = password.getText().toString();
        if(!RegUtils.isPhoneNumber(un)){
            username.startAnimation(shakeAnim);
            Toast.makeText(this,R.string.error_username,Toast.LENGTH_SHORT).show();
            return;
        }
        if(pw.length() < 6 || pw.length() >= 11){
            password.startAnimation(shakeAnim);
            Toast.makeText(this,R.string.error_password,Toast.LENGTH_SHORT).show();
            return;
        }
        modify(un,pw);
    }

    private void modify(String un ,String pw){
        RequestParams params = new RequestParams(HttpCommon.RESET_PASSWORD);
        params.addQueryStringParameter("mobile",un);
        params.addQueryStringParameter("password",pw);
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                StringDataJSON jo = JSON.parseObject(result, StringDataJSON.class);
                if(jo.getCode() == 0){
                    Toast.makeText(ModifyPasswordActivity.this,R.string.modify_succ,Toast.LENGTH_SHORT).show();
                    MyApplication.getApplication().isLogin = false;
                    login(un,pw);
                }else{
                    Toast.makeText(ModifyPasswordActivity.this,jo.getMsg(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(ModifyPasswordActivity.this,R.string.modify_fail,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

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
                    UserInfo userInfo = new UserInfo();
                    userInfo.setMobile(un);
                    userInfo.setPassword(pw);
                    userInfo.setToken(jo.getDatas());
                    UserInfoDao.getInstance().addOrUpdate(userInfo);

                    Intent intent = new Intent();
                    setResult(RESPONSE_LOGIN_SUCC, intent);

                    MyApplication.getApplication().isLogin = true;
                    AuthUtils.getInstance().pass(ModifyPasswordActivity.this);
                    finish();
                }else{
                    MyApplication.getApplication().isLogin = false;
                    Toast.makeText(ModifyPasswordActivity.this,jo.getMsg(),Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                MyApplication.getApplication().isLogin = false;
                Toast.makeText(ModifyPasswordActivity.this,R.string.login_fail,Toast.LENGTH_SHORT).show();
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