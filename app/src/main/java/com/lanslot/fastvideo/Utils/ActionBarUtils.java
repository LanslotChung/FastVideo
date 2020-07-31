package com.lanslot.fastvideo.Utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.ActionBar;

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
        ActionBar.LayoutParams lp =new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
        View mActionBarView = LayoutInflater.from(context).inflate(layoutId, null);
        actionBar.setCustomView(mActionBarView, lp);
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);
        return mActionBarView;
    }
}
