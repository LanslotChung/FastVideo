package com.lanslot.fastvideo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.lanslot.fastvideo.AOP.Authority.AuthUtils;
import com.lanslot.fastvideo.Utils.ActionBarUtils;
import com.lanslot.fastvideo.Utils.PackageUtils;
import com.lanslot.fastvideo.Utils.StatusBarUtil;
import com.zzhoujay.richtext.RichText;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

@ContentView(R.layout.activity_about_us)
public class AboutUsActivity extends AppCompatActivity {

 @ViewInject(R.id.versionCode)
     TextView versionCode;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);

        View actionBar = ActionBarUtils.getInstance().setCustomActionBar(this,
                getSupportActionBar(),
                R.layout.layout_actionbar);

        actionBar.findViewById(R.id.actionbar_layout).setBackgroundResource(R.color.sandybrown);
        actionBar.findViewById(R.id.back).setOnClickListener(v -> {
            AuthUtils.getInstance().clear();
            finish();
        });
        ((TextView) actionBar.findViewById(R.id.title)).setText("关于我们");

        StatusBarUtil.setStatusBarColor(this, R.color.sandybrown);
        versionCode.setText("v" + PackageUtils.getVersion(AboutUsActivity.this));

    }
    @Event(R.id.userAgreement)
    private void onServiceAgreementButtonClicked(View v) {
        AuthUtils.getInstance().startActivity(AboutUsActivity.this, UserAgreementActivity.class, null);
    }

    @Event(R.id.privacyPolicy)
    private void onPrivacyPolicyButtonClicked(View v) {
        AuthUtils.getInstance().startActivity(AboutUsActivity.this, PrivacyPolicyActivity.class, null);
    }

    @Event(R.id.disclaimer)
    private void onDisclaimerButtonClicked(View v){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AboutUsActivity.this).setIcon(R.drawable.mainlogo).setTitle("免责声明")
                .setMessage(getResources().getString(R.string.disclaimer_content)).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        alertDialog.show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        AuthUtils.getInstance().clear();
        finish();
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RichText.clear(this);
    }
}
