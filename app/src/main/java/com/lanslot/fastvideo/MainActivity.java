package com.lanslot.fastvideo;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.lanslot.fastvideo.Adapter.MyFragmentAdapter;
import com.lanslot.fastvideo.Bean.JSON.SettingJSON;
import com.lanslot.fastvideo.Fragments.IndexFragment;
import com.lanslot.fastvideo.Fragments.SelfFragment;
import com.lanslot.fastvideo.Http.HttpCommon;
import com.lanslot.fastvideo.Utils.StatusBarUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    @ViewInject(R.id.navbar)
    private BottomNavigationBar navBar;
    @ViewInject(R.id.view_container)
    private ViewPager container;
    @ViewInject(R.id.main_view)
    LinearLayout mainView;

    private FragmentManager fragmentManager;
    private ArrayList<Fragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        getSupportActionBar().hide();
        initBottomNavBar();
        initViewpager();
        requestSettings();
    }

    private void requestSettings() {
        RequestParams params = new RequestParams(HttpCommon.CONFIG);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                SettingJSON jo = JSON.parseObject(result, SettingJSON.class);
                if (jo.getCode() == 0) {
                    MyApplication.setSettings(jo);
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
        if(intent.getBooleanExtra("self",false)){
            container.setCurrentItem(1);
        }else {
            container.setCurrentItem(0);
        }
    }

    private void initBottomNavBar() {
        navBar.addItem(new BottomNavigationItem(R.drawable.index,"首页")
                    .setInActiveColorResource(R.color.darkgray)
                    .setActiveColorResource(R.color.colorPrimary))

                .addItem(new BottomNavigationItem(R.drawable.self,"我的")
                        .setInActiveColorResource(R.color.darkgray)
                        .setActiveColorResource(R.color.gold))
                .setFirstSelectedPosition(0)
                .initialise();
        navBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        navBar.setMode(BottomNavigationBar.MODE_FIXED);

        navBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                switch (position){
                    case 0:
                        StatusBarUtil.setStatusBarColor(MainActivity.this,R.color.colorPrimary);
                        break;
                    case 1:
                        StatusBarUtil.setStatusBarColor(MainActivity.this,R.color.gold);
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
}
