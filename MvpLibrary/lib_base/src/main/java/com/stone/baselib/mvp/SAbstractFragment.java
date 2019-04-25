package com.stone.baselib.mvp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.stone.baselib.busevent.SBusFactory;
import com.stone.baselib.utils.SLogUtils;
import com.stone.baselib.utils.SShowMsgUtils;
import com.trello.rxlifecycle3.components.SRxFragment;

import androidx.annotation.Nullable;
import butterknife.ButterKnife;

/**
 * Stone
 * 2019/4/11
 **/
public abstract class SAbstractFragment<P extends SPresentible> extends SRxFragment implements SViewible<P> {
    public static final String TAG = "SAbstractFragment";
    private P p;
    private View mRootView;
    protected Activity context;
    protected LayoutInflater layoutInflater;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        SLogUtils.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SLogUtils.d(TAG, "onCreateView");
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
        SLogUtils.d(TAG, "onActivityCreated");

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
    public void onStart() {
        SLogUtils.d(TAG, "onStart");
        super.onStart();
    }

    @Override
    public void onResume() {
        SLogUtils.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        SLogUtils.d(TAG, "onPause");
        super.onPause();
    }

    @Override
    public void onStop() {
        SLogUtils.d(TAG, "onStop");
        super.onStop();
    }

    @Override
    public void onAttach(Context context) {
        SLogUtils.d(TAG, "onAttach");

        super.onAttach(context);
        if (context instanceof Activity) {
            this.context = (Activity) context;
        }
    }

    @Override
    public void onDestroyView() {
        SLogUtils.d(TAG, "onDestroyView");

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
    public void onDestroy() {
        SLogUtils.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        SLogUtils.d(TAG, "onDetach");

        super.onDetach();
        context = null;
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
        ButterKnife.bind(this, rootView);
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
