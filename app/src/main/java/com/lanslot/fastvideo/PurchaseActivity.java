package com.lanslot.fastvideo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.lanslot.fastvideo.AOP.Authority.AuthUtils;
import com.lanslot.fastvideo.Bean.Setting;
import com.lanslot.fastvideo.Utils.ActionBarUtils;
import com.lanslot.fastvideo.Utils.StatusBarUtil;
import com.zzhoujay.richtext.RichText;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;


@ContentView(R.layout.activity_purchase)
public class PurchaseActivity extends AppCompatActivity {
    @ViewInject(R.id.monthlyMembership)
    TextView monMem;
    @ViewInject(R.id.permanentMember)
    TextView perMem;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        View actionBar = ActionBarUtils.getInstance().setCustomActionBar(this,
                getSupportActionBar(),
                R.layout.layout_actionbar);

        actionBar.findViewById(R.id.actionbar_layout).setBackgroundResource(R.color.lightcoral);
        actionBar.findViewById(R.id.back).setOnClickListener(v -> {
            AuthUtils.getInstance().clear();
            finish();
        });
        ((TextView) actionBar.findViewById(R.id.title)).setText("订阅会员");

        StatusBarUtil.setStatusBarColor(this, R.color.lightcoral);
        monMem.setText("月度VIP会员："+MyApplication.getSettingByKey(Setting.Key.VIP_USER_MONTH)+"元/月");
        perMem.setText("永久VIP会员："+MyApplication.getSettingByKey(Setting.Key.VIP_USER_OVER)+"元");

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AuthUtils.getInstance().clear();
        finish();
        return false;
    }

    @Event(value = R.id.alipayImg,type = View.OnLongClickListener.class)
    private boolean onLongClickAlipayImageView(View v){
        Bitmap  bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        FileOutputStream outStream = null;
        String pngName = new Date().getTime() + ".png";
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"
                +Environment.DIRECTORY_DCIM,pngName);
        System.out.println("------------++:"+file.getPath());
        if (file.isDirectory()) {// 如果是目录不允许保存
            return true;
        }
        int permission = ActivityCompat.checkSelfPermission(PurchaseActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(PurchaseActivity.this, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
        try {
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            Toast.makeText(PurchaseActivity.this,file.getPath(),Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(PurchaseActivity.this,"保存失败",Toast.LENGTH_SHORT).show();
        }finally {
            try {
                bitmap.recycle();
                if(outStream!=null){
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                return true;
            }
        }

    }

    @Event(value = R.id.wechatImg,type = View.OnLongClickListener.class)
    private boolean onLongClickWeChatImageView(View v){
        Bitmap  bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        FileOutputStream outStream = null;
        String pngName = new Date().getTime() + ".png";
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"
                +Environment.DIRECTORY_DCIM,pngName);
        System.out.println("------------++:"+file.getPath());
        if (file.isDirectory()) {// 如果是目录不允许保存
            return true;
        }
        int permission = ActivityCompat.checkSelfPermission(PurchaseActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(PurchaseActivity.this, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
        try {
            outStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            Toast.makeText(PurchaseActivity.this,file.getPath(),Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(PurchaseActivity.this,"保存失败",Toast.LENGTH_SHORT).show();
        }finally {
            try {
                bitmap.recycle();
                if(outStream!=null){
                    outStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                return true;
            }
        }

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        RichText.clear(this);
    }
}