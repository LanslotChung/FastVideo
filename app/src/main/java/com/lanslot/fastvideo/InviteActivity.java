package com.lanslot.fastvideo;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.king.zxing.util.CodeUtils;
import com.lanslot.fastvideo.AOP.Authority.AuthUtils;
import com.lanslot.fastvideo.Adapter.UserListAdapter;
import com.lanslot.fastvideo.Bean.Config;
import com.lanslot.fastvideo.Bean.JSON.InviteGroupJSON;
import com.lanslot.fastvideo.Bean.MyContacts;
import com.lanslot.fastvideo.DB.UserInfoDao;
import com.lanslot.fastvideo.Http.HttpCommon;
import com.lanslot.fastvideo.Utils.ActionBarUtils;
import com.lanslot.fastvideo.Utils.BitmapUtils;
import com.lanslot.fastvideo.Utils.ContactUtils;
import com.lanslot.fastvideo.Utils.StatusBarUtil;
import com.scwang.smart.refresh.footer.BallPulseFooter;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;
import com.wangjie.rapidfloatingactionbutton.util.RFABTextUtil;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

@ContentView(R.layout.activity_invite)
public class InviteActivity extends AppCompatActivity implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {
    @ViewInject(R.id.invite_menu)
    private RapidFloatingActionButton menu;
    @ViewInject(R.id.invite_layout)
    private RapidFloatingActionLayout rfaLayout;
    @ViewInject(R.id.invite_list)
    private RecyclerView list;
    @ViewInject(R.id.nousers)
    private TextView nousers;
    @ViewInject(R.id.refreshLayout)
    private SmartRefreshLayout smartRefreshLayout;

    private RapidFloatingActionHelper rfabHelper;

    int currentPage = 1;
    boolean hasNextPage = true;

    ArrayList<MyContacts> allContacts;
    private UserListAdapter adapter;

    ClipboardManager cm;
    ClipData clipData;
    Bitmap qrCode;
    Bitmap poster;
    Canvas canvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        View actionBar = ActionBarUtils.getInstance().setCustomActionBar(this,
                getSupportActionBar(),
                R.layout.layout_actionbar);

        actionBar.findViewById(R.id.back).setOnClickListener(v -> finish());
        ((TextView)actionBar.findViewById(R.id.title)).setText("我的邀请");

        StatusBarUtil.setStatusBarColor(this,R.color.colorPrimary);

        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(this);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);

        ArrayList<RFACLabelItem> itmes = new ArrayList<>();
        itmes.add(new RFACLabelItem<Integer>()
                .setResId(R.drawable.nice)
                .setIconNormalColor(getColor(R.color.white))
                .setIconPressedColor(getColor(R.color.whitesmoke))
                .setLabel("邀请码：" + Config.getInstance().getUser().getInviteCode())
                .setWrapper(0)
        );
        itmes.add(new RFACLabelItem<Integer>()
                .setResId(R.drawable.postcard)
                .setIconNormalColor(getColor(R.color.white))
                .setIconPressedColor(getColor(R.color.whitesmoke))
                .setLabel("分享海报")
                .setWrapper(1)
        );
        itmes.add(new RFACLabelItem<Integer>()
                .setResId(R.drawable.text)
                .setIconNormalColor(getColor(R.color.white))
                .setIconPressedColor(getColor(R.color.whitesmoke))
                .setLabel("分享链接")
                .setWrapper(2)
        );
        rfaContent.setItems(itmes)
                .setIconShadowRadius(RFABTextUtil.dip2px(this, 2))
                .setIconShadowColor(getColor(R.color.lightgray))
                .setIconShadowDx(RFABTextUtil.dip2px(this, 1))
                .setIconShadowDy(RFABTextUtil.dip2px(this, 1));
        rfabHelper = new RapidFloatingActionHelper(
                this,
                rfaLayout,
                menu,
                rfaContent
        ).build();

        addPermissByPermissionList(this, new String[]{"android.permission.READ_CONTACTS", "android.permission.WRITE_EXTERNAL_STORAGE"}, 0);
    }

    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        int id = (Integer) item.getWrapper();
        switch (id) {
            case 0://邀请码
                if (cm == null)
                    cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipData = ClipData.newPlainText("邀请码", "我的邀请码" + Config.getInstance().getUser().getInviteCode());
                cm.setPrimaryClip(clipData);
                Toast.makeText(this, "邀请码已复制，你可以分享给您的好友了", Toast.LENGTH_SHORT).show();
                break;
            case 1://postcard
                if (poster == null) {
                    qrCode = CodeUtils.createQRCode(
                            Config.getInstance().getUser().getInviteUrl(),
                            1500,
                            BitmapUtils.drawableToBitmap(getDrawable(R.drawable.mainlogo)));
                    poster = BitmapUtils.drawableToBitmap(getDrawable(R.drawable.poster));
                    canvas = new Canvas(poster);
                    canvas.drawBitmap(qrCode, (canvas.getWidth() - qrCode.getWidth()) / 2, canvas.getHeight() - qrCode.getHeight() - 300, null);
                }
                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), poster, null, null));
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);//设置分享行为
                intent.setType("image/*");//设置分享内容的类型
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent = Intent.createChooser(intent, "分享");
                startActivity(intent);
                break;
            case 2://url
                if (cm == null)
                    cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                clipData = ClipData.newPlainText("邀请链接",
                        "看视频，免广告！尽在快看视频！这是我的邀请链接:"
                                + Config.getInstance().getUser().getInviteUrl()
                                + ",现在下载，马上体验全网免广告观看视频");
                cm.setPrimaryClip(clipData);
                Toast.makeText(this, "邀请链接已生成，你可以分享给您的好友了", Toast.LENGTH_SHORT).show();
                break;
        }
        rfabHelper.toggleContent();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AuthUtils.getInstance().clear();
        finish();
        return false;
    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        onRFACItemLabelClick(position,item);
        rfabHelper.toggleContent();
    }

    /**
     * 动态权限
     */
    public void addPermissByPermissionList(Activity activity, String[] permissions, int request) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {   //Android 6.0开始的动态权限，这里进行版本判断
            ArrayList<String> mPermissionList = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(activity, permissions[i])
                        != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(permissions[i]);
                }
            }
            if (mPermissionList.isEmpty()) {  //非初次进入App且已授权
                showContacts();
                requestUserGroups();
            } else {
                //请求权限方法
                String[] permissionsNew = mPermissionList.toArray(new String[mPermissionList.size()]);//将List转为数组
                ActivityCompat.requestPermissions(activity, permissionsNew, request); //这个触发下面onRequestPermissionsResult这个回调
            }
        }
    }

    private void showContacts() {
        allContacts = ContactUtils.getAllContacts(this);
    }

    /**
     * requestPermissions的回调
     * 一个或多个权限请求结果回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasAllGranted = true;
        //判断是否拒绝  拒绝后要怎么处理 以及取消再次提示的处理
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                hasAllGranted = false;
                break;
            }
        }
        if (hasAllGranted) { //同意权限做的处理,开启服务提交通讯录
            showContacts();
        }else{
            Toast.makeText(this, R.string.get_contact_error, Toast.LENGTH_SHORT).show();
        }

        requestUserGroups();
    }

    private void requestUserGroups(){
        RequestParams params = new RequestParams(HttpCommon.USER_GROUP);
        params.addHeader("uniquetoken", UserInfoDao.getInstance().find().getToken());
        params.addBodyParameter("pageNum", currentPage);
        x.http().get(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                InviteGroupJSON jo = JSON.parseObject(result, InviteGroupJSON.class);
                if (jo.getCode() == 0 && jo.getDatas() != null && jo.getDatas().getList() != null && jo.getDatas().getList().size() > 0) {
                    nousers.setVisibility(View.GONE);
                    smartRefreshLayout.setVisibility(View.VISIBLE);
                    smartRefreshLayout.setRefreshHeader(new MaterialHeader(InviteActivity.this).setShowBezierWave(true));
                    smartRefreshLayout.setRefreshFooter(new BallPulseFooter(InviteActivity.this).setSpinnerStyle(SpinnerStyle.Translate));

                    adapter = new UserListAdapter(InviteActivity.this, jo.getDatas().getList(), allContacts);
                    list.setLayoutManager(new LinearLayoutManager(InviteActivity.this));
                    list.setAdapter(adapter);
                    smartRefreshLayout.setEnableRefresh(false);
                    smartRefreshLayout.setEnableLoadMore(jo.getDatas().getHasNextPage());
                    smartRefreshLayout.setOnLoadMoreListener(srl -> {
                        currentPage++;
                        RequestParams params = new RequestParams(HttpCommon.USER_GROUP);
                        params.addHeader("uniquetoken", UserInfoDao.getInstance().find().getToken());
                        params.addBodyParameter("pageNum", currentPage);
                        x.http().get(params, new Callback.CommonCallback<String>() {

                            @Override
                            public void onSuccess(String result) {
                                InviteGroupJSON jo = JSON.parseObject(result, InviteGroupJSON.class);
                                if (jo.getCode() == 0 && jo.getDatas() != null && jo.getDatas().getList() != null && jo.getDatas().getList().size() > 0) {
                                    srl.finishLoadMore(true);
                                    smartRefreshLayout.setEnableLoadMore(jo.getDatas().getHasNextPage());
                                    adapter.loadMore(jo.getDatas().getList());
                                } else {
                                    srl.finishLoadMore(false);
                                }
                            }

                            @Override
                            public void onError(Throwable ex, boolean isOnCallback) {
                                srl.finishLoadMore(false);
                            }

                            @Override
                            public void onCancelled(CancelledException cex) {
                                srl.finishLoadMore(false);
                            }

                            @Override
                            public void onFinished() {

                            }
                        });
                    });
                }else{
                    nousers.setVisibility(View.VISIBLE);
                    smartRefreshLayout.setVisibility(View.GONE);
                    Toast.makeText(InviteActivity.this, jo.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(InviteActivity.this,"获取受邀列表失败",Toast.LENGTH_SHORT).show();
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