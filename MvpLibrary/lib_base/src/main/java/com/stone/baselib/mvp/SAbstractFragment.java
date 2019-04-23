package com.stone.baselib.mvp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.stone.baselib.busevent.SBusFactory;
import com.stone.baselib.utils.SShowMsgUtils;
import com.trello.rxlifecycle3.components.SRxFragment;

import androidx.annotation.Nullable;
import butterknife.ButterKnife;

/**
 * Stone
 * 2019/4/11
 **/
public abstract class SAbstractFragment<P extends SPresentible> extends SRxFragment implements SViewible<P> {

    private P p;
    private View mRootView;
    protected Activity context;
    protected LayoutInflater layoutInflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layoutInflater = inflater;
        if (mRootView == null && getLayoutId() > 0) {
            mRootView = inflater.inflate(getLayoutId(), null);
            butterKnifeBind(mRootView);
        } else {
            ViewGroup viewGroup = (ViewGroup) mRootView.getParent();
            if (viewGroup != null) {
                viewGroup.removeView(mRootView);
            }
        }

        return mRootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (useEventBus()) {
            SBusFactory.getBus().register(this);
        }
        initView(savedInstanceState);
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
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof Activity) {
            this.context = (Activity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (useEventBus()) {
            SBusFactory.getBus().unregister(this);
        }
        if (getP() != null) {
            getP().detachV();
        }

        p = null;
    }

    @Override
    public void showNoNetTips() {
        //TODO
    }

    @Override
    public boolean useEventBus() {
        return false;
    }

    @Override
    public void initTopBar() {
        //TODO
    }

    @Override
    public void butterKnifeBind(View rootView) {
        ButterKnife.bind(rootView);
    }

    @Override
    public void showToast(String msg) {
        SShowMsgUtils.showToast(context, msg);
    }

    @Override
    public void showSnackBar(String msg, int textColor, int btnText) {
        SShowMsgUtils.showSnack(context, msg, textColor, btnText);
    }


}
