package com.lanslot.fastvideo.Utils;

import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;

public class ActionBarUtils {
    private static volatile ActionBarUtils mInstance;

    private ActionBarUtils() {
    }

    public static ActionBarUtils getInstance() {
        if (mInstance == null) {
            synchronized (ActionBarUtils.class) {
                if (mInstance == null) {
                    mInstance = new ActionBarUtils();
                }
            }
        }
        return mInstance;
    }

    public View setCustomActionBar(Context context, ActionBar actionBar, int layoutId) {
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        View mActionBarView = LayoutInflater.from(context).inflate(layoutId, null, false);
        actionBar.setCustomView(mActionBarView, lp);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Toolbar parent = (Toolbar) mActionBarView.getParent();
            parent.setContentInsetsAbsolute(0, 0);
        }
        return mActionBarView;
    }
}
