package com.stone.baselib.mvp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.painstone.mvplibrary.R;
import com.stone.baselib.busevent.SBusFactory;
import com.stone.baselib.utils.SShowMsgUtils;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import butterknife.ButterKnife;

/**
 * Stone
 * 2019/4/8
 * 继承的RxAppCompatActivity是FragmentActivity
 **/
public abstract class SAbstractActivity<P extends SPresentible> extends RxAppCompatActivity implements SViewible<P> {

    private P p;
    protected Activity context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
            initTopBar();
            butterKnifeBind(null);
        }
        initView(savedInstanceState);

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (useEventBus()) {
            SBusFactory.getBus().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (useEventBus()) {
            SBusFactory.getBus().unregister(this);
        }
        if (getP() != null) {
            getP().detachV();
        }
        p = null;
    }

    @Override
    public boolean useEventBus() {
        return false;
    }

    protected P getP() {
        if (p == null) {
            p = getPInstance();
            if (p != null) {
                p.attachV(this);
            }
        }
        return p;
    }

    @Override
    public void showToast(String msg) {
        SShowMsgUtils.showToast(context, msg);
    }

    @Override
    public void showSnackBar(String msg, int textColor, int btnText) {
        SShowMsgUtils.showSnack(context, msg, textColor, btnText);
    }

    @Override
    public void butterKnifeBind(View rootView) {
        ButterKnife.bind(this);
    }
}
