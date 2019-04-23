package com.stone.baselib.utils;

import android.app.Activity;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.painstone.mvplibrary.R;

/**
 * Stone
 * 2019/4/11
 **/
public class SShowMsgUtils {

    public static void showToast(Activity activity, String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Show message
     *
     * @param activity Activity
     * @param msg message
     */
    public static void showSnack(final Activity activity, String msg, int textColor, int btnText) {
        //去掉虚拟按键
        activity.getWindow().getDecorView().setSystemUiVisibility(
                //隐藏虚拟按键栏 | 防止点击屏幕时,隐藏虚拟按键栏又弹了出来
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE
        );
        final Snackbar snackbar = Snackbar.make(activity.getWindow().getDecorView(), msg, Snackbar.LENGTH_SHORT);
        View view = snackbar.getView();
        ((TextView) view.findViewById(R.id.snackbar_text)).setTextColor(textColor);
        snackbar.setAction(activity.getString(btnText), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
                //隐藏SnackBar时记得恢复隐藏虚拟按键栏,不然屏幕底部会多出一块空白布局出来,很难看
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }).show();
        snackbar.addCallback(new BaseTransientBottomBar.BaseCallback<Snackbar>() {
            @Override
            public void onDismissed(Snackbar transientBottomBar, int event) {
                super.onDismissed(transientBottomBar, event);
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        });
    }
}
