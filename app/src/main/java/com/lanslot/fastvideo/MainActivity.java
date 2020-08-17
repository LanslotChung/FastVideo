package com.lanslot.fastvideo;

import android.animation.ArgbEvaluator;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.lanslot.fastvideo.Adapter.MyFragmentAdapter;
import com.lanslot.fastvideo.Bean.JSON.SettingJSON;
import com.lanslot.fastvideo.Bean.JSON.StringDataJSON;
import com.lanslot.fastvideo.Fragments.IndexFragment;
import com.lanslot.fastvideo.Fragments.SelfFragment;
import com.lanslot.fastvideo.Http.HttpCommon;
import com.lanslot.fastvideo.Utils.DownloadUtils;
import com.lanslot.fastvideo.Utils.PackageUtils;
import com.lanslot.fastvideo.Utils.StatusBarUtil;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.util.Date;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {
    @ViewInject(R.id.navbar)
    private BottomNavigationBar navBar;
    @ViewInject(R.id.view_container)
    private ViewPager container;

    private long exitTime = 0;
    private String pathStr="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        getSupportActionBar().hide();
        initBottomNavBar();
        initViewpager();
        if (!MyApplication.getApplication().isInit) {
            doCheckUpdate();
            requestSettings();
        }
    }


    private void requestSettings() {
        RequestParams params = new RequestParams(HttpCommon.CONFIG);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                SettingJSON jo = JSON.parseObject(result, SettingJSON.class);
                if (jo.getCode() == 0) {
                    MyApplication.setSettings(jo);
                    MyApplication.getApplication().isInit = true;
                } else {
                    Toast.makeText(MainActivity.this, jo.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(MainActivity.this, R.string.access_server_fail, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void initViewpager() {
        MyFragmentAdapter adapter = new MyFragmentAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        adapter.addFragment(new IndexFragment());
        adapter.addFragment(new SelfFragment());
        container.setAdapter(adapter);
        Intent intent = getIntent();
        if (intent.getBooleanExtra("self", false)) {
            container.setCurrentItem(1);
        } else {
            container.setCurrentItem(0);
        }
    }

    private void doCheckUpdate() {
        String versionCode = "";
        versionCode = PackageUtils.getVersion(this);
        RequestParams params = new RequestParams(HttpCommon.CHECK_VERSION);
        params.addQueryStringParameter("versionName", versionCode);
        params.addQueryStringParameter("type", "1");
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                StringDataJSON jo = JSON.parseObject(result, StringDataJSON.class);
                if (jo.getCode() == 0) {
//                    Toast.makeText(MainActivity.this, R.string.update_need_no, Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject jsonObj = new JSONObject(jo.getDatas());
                        String url=jsonObj.getString("apkUrl");
                        int forceUpdate=jsonObj.getInt("forceUpdate");
                        if(forceUpdate==0){
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this).setIcon(R.drawable.mainlogo).setTitle("版本更新")
                            .setMessage("是否更新").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Toast.makeText(MainActivity.this, R.string.start_update, Toast.LENGTH_LONG).show();
                                    String apkName = "fastvideo-" + new Date().getTime() + ".apk";
                                    //pathStr=MainActivity.this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)+"/"+apkName;
                                    new DownloadUtils(MainActivity.this, url, apkName).startDownload();
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    dialogInterface.dismiss();
                                }
                            });
                    alertDialog.show();
                        }else {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this).setIcon(R.drawable.mainlogo).setTitle("版本更新")
                                    .setMessage("是否更新").setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Toast.makeText(MainActivity.this, R.string.start_update, Toast.LENGTH_LONG).show();
                                            String apkName = "fastvideo-" + new Date().getTime() + ".apk";
                                            pathStr=MainActivity.this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)+"/"+apkName;
                                            new DownloadUtils(MainActivity.this, url, apkName).startDownload();
                                            AlertDialog alertDialog1 = new AlertDialog.Builder(MainActivity.this)
                                                    .setMessage("正在下载中...")//内容
                                                    .setIcon(R.drawable.mainlogo)//图标
                                                    .create();
                                            alertDialog1.setCancelable(false);
                                            alertDialog1.show();
                                        }
                                    });
                            alertDialog.setCancelable(false);
                            alertDialog.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void initBottomNavBar() {
        navBar.addItem(new BottomNavigationItem(R.drawable.index, "首页")
                .setInActiveColorResource(R.color.darkgray)
                .setActiveColorResource(R.color.colorPrimary))

                .addItem(new BottomNavigationItem(R.drawable.self, "我的")
                        .setInActiveColorResource(R.color.darkgray)
                        .setActiveColorResource(R.color.gold))
                .setFirstSelectedPosition(0)
                .initialise();
        navBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        navBar.setMode(BottomNavigationBar.MODE_FIXED);

        navBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:
                        StatusBarUtil.setStatusBarColor(MainActivity.this, R.color.colorPrimary);
                        break;
                    case 1:
                        StatusBarUtil.setStatusBarColor(MainActivity.this, R.color.gold);
                        break;
                }
                container.setCurrentItem(position);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });

        container.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (positionOffset == 0) return;
                StatusBarUtil.setStatusBarColorHex(MainActivity.this,
                        (int) new ArgbEvaluator()
                                .evaluate(positionOffset,
                                        getColor(R.color.colorPrimary), getColor(R.color.gold)));
            }

            @Override
            public void onPageSelected(int position) {
                navBar.selectTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == event.KEYCODE_BACK) {
            exit();
            return false;
        } else
            return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再按一次退出程序",
                    Toast.LENGTH_SHORT).show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        File f = new File(pathStr);
        if(f.exists())
        {
            new DownloadUtils(MainActivity.this,"","").installAPK(pathStr);
        }
    }

}
