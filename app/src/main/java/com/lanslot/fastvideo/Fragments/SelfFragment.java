package com.lanslot.fastvideo.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alibaba.fastjson.JSON;
import com.lanslot.fastvideo.AOP.Authority.AuthUtils;
import com.lanslot.fastvideo.Bean.Config;
import com.lanslot.fastvideo.Bean.JSON.UserJSON;
import com.lanslot.fastvideo.Bean.User;
import com.lanslot.fastvideo.CommonQuestionActivity;
import com.lanslot.fastvideo.DB.UserInfo;
import com.lanslot.fastvideo.DB.UserInfoDao;
import com.lanslot.fastvideo.Http.HttpCommon;
import com.lanslot.fastvideo.InviteActivity;
import com.lanslot.fastvideo.LoginActivity;
import com.lanslot.fastvideo.MyApplication;
import com.lanslot.fastvideo.R;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

@ContentView(R.layout.fragment_self)
public class SelfFragment extends Fragment {
    static public final int LOGIN_REQUEST_CODE = 1000;
    @ViewInject(R.id.login)
    private Button login;
    @ViewInject(R.id.login_panel)
    RelativeLayout loginPanel;
    @ViewInject(R.id.userinfo_panel)
    LinearLayout userPanel;
    @ViewInject(R.id.phone_number)
    TextView tx_mobile;
    @ViewInject(R.id.iv_subscribe)
    ImageView statusImage;
    @ViewInject(R.id.subscribe_status)
    TextView statusText;
    @ViewInject(R.id.subscribe_date)
    TextView expireText;
    @ViewInject(R.id.logout)
    Button logout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = x.view().inject(this, inflater, container);
        if (UserInfoDao.getInstance().find() != null) {
            getUserInfo();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(MyApplication.getApplication().isLogin){
            setUserPanel();
        }
    }

    @Event(R.id.logout)
    private void onLogoutButtonClicked(View v) {
        if (MyApplication.getApplication().isLogin) {
            UserInfoDao.getInstance().delete();
            MyApplication.getApplication().isLogin = false;
            logout.setVisibility(View.GONE);
            loginPanel.setVisibility(View.VISIBLE);
            userPanel.setVisibility(View.GONE);
        }
    }

    @Event(R.id.common_question_button)
    private void onCommonQuestionButtonClicked(View v) {
        AuthUtils.getInstance().startActivity(getActivity(), CommonQuestionActivity.class, null);
    }

    @Event(R.id.login)
    private void onLoginButtonClicked(View v) {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivityForResult(intent, LOGIN_REQUEST_CODE);
    }

    @Event(R.id.invite_button)
    private void onInviteButtonClicked(View v) {
//        Intent intent = new Intent(getActivity(), InviteActivity.class);
//        startActivity(intent);
        AuthUtils.getInstance().startActivity(getActivity(),InviteActivity.class,null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST_CODE && resultCode == LoginActivity.RESPONSE_LOGIN_SUCC) {
            getUserInfo();
        }
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
                    setUserPanel();
                } else {
                    Toast.makeText(getActivity(), jo.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getActivity(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void setUserPanel(){
        loginPanel.setVisibility(View.GONE);
        userPanel.setVisibility(View.VISIBLE);
        logout.setVisibility(View.VISIBLE);
        User user = Config.getInstance().getUser();
        tx_mobile.setText(user.getMobile());

        DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
        expireText.setText(df.format(user.getExpireTime()));

        int imgId = R.drawable.unsubscriber;
        switch (user.getLevel()) {
            case 0:
                imgId = R.drawable.unsubscriber;
                statusText.setText(R.string.unsubscribe_status);
                expireText.setText(R.string.unsubscribe_status);
                break;
            case 1:
                imgId = R.drawable.subscriber;
                statusText.setText(R.string.subscribe_status);
                break;
            case 2:
                imgId = R.drawable.subscriber_forever;
                statusText.setText(R.string.forever_subscribe_status);
        }
        statusImage.setImageResource(imgId);
        MyApplication.getApplication().isLogin = true;
    }
}
