package com.lanslot.fastvideo.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.lanslot.fastvideo.AboutUsActivity;
import com.lanslot.fastvideo.Bean.Config;
import com.lanslot.fastvideo.Bean.JSON.StringDataJSON;
import com.lanslot.fastvideo.Bean.JSON.UserJSON;
import com.lanslot.fastvideo.Bean.User;
import com.lanslot.fastvideo.CommonQuestionActivity;
import com.lanslot.fastvideo.DB.UserInfo;
import com.lanslot.fastvideo.DB.UserInfoDao;
import com.lanslot.fastvideo.Http.HttpCommon;
import com.lanslot.fastvideo.InviteActivity;
import com.lanslot.fastvideo.LoginActivity;
import com.lanslot.fastvideo.ModifyPasswordActivity;
import com.lanslot.fastvideo.MyApplication;
import com.lanslot.fastvideo.PurchaseActivity;
import com.lanslot.fastvideo.R;
import com.lanslot.fastvideo.Utils.DownloadUtils;
import com.lanslot.fastvideo.Utils.PackageUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


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
            if (Config.getInstance().getUser() == null) {
                getUserInfo();
            }
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (MyApplication.getApplication().isLogin) {
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

    @Event(R.id.purchase_button)
    private void onPuchaseButtonClicked(View v) {
        AuthUtils.getInstance().startActivity(getActivity(), PurchaseActivity.class, null);
    }

    @Event(R.id.common_question_button)
    private void onCommonQuestionButtonClicked(View v) {
        AuthUtils.getInstance().startActivity(getActivity(), CommonQuestionActivity.class, null);
    }

    @Event(R.id.check_update_button)
    private void onCheckUpdateButtonClicked(View v) {
        String versionCode = "";
        versionCode = PackageUtils.getVersion(getActivity());
        RequestParams params = new RequestParams(HttpCommon.CHECK_VERSION);
        params.addQueryStringParameter("versionName", versionCode);
        params.addQueryStringParameter("type", "1");
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                StringDataJSON jo = JSON.parseObject(result, StringDataJSON.class);
                if (jo.getCode() == 0) {
                    Toast.makeText(getActivity(), R.string.update_need_no, Toast.LENGTH_SHORT).show();
                } else {
                    String url = "https://ucan.25pp.com/Wandoujia_web_seo_baidu_homepage.apk";//jo.getDatas();
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity()).setIcon(R.drawable.mainlogo).setTitle("版本更新")
                            .setMessage("是否更新").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(getActivity(), R.string.start_update, Toast.LENGTH_LONG).show();
                                    String apkName = "fastvideo-" + new Date().getTime() + ".apk";
                                    new DownloadUtils(getActivity(), url, apkName).startDownload();
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(getActivity(), "检查更新失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(getActivity(), "检查更新失败", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFinished() {
            }
        });
    }


    @Event(R.id.about_us_button)
    private void onAboutUsButtonClicked(View v) {
        AuthUtils.getInstance().startActivity(getActivity(), AboutUsActivity.class, null);
    }


    @Event(R.id.login)
    private void onLoginButtonClicked(View v) {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivityForResult(intent, LOGIN_REQUEST_CODE);
    }

    @Event(R.id.invite_button)
    private void onInviteButtonClicked(View v) {
        AuthUtils.getInstance().startActivity(getActivity(), InviteActivity.class, null);
//        AuthUtils.getInstance().startActivity(getActivity(), TestActivity.class, null);
    }

    @Event(R.id.modify_password)
    private void onModifyPasswordButtonClicked(View v) {
        AuthUtils.getInstance().startActivity(getActivity(), ModifyPasswordActivity.class, null);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LOGIN_REQUEST_CODE && resultCode == LoginActivity.RESPONSE_LOGIN_SUCC) {
            setUserPanel();
        }
    }

    private void setUserPanel() {
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

}
