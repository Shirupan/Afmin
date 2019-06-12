package com.stone.baselib.mvp;

import android.app.Activity;
import android.os.Bundle;

import android.view.View;

import com.painstone.mvplibrary.R;
import com.stone.baselib.busevent.SBusFactory;
import com.stone.baselib.router.SRouterFactory;
import com.stone.baselib.utils.SShowMsgUtils;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import androidx.annotation.Nullable;
import butterknife.ButterKnife;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * Stone
 * 2019/4/8
 * 继承的RxAppCompatActivity是FragmentActivity
 **/
public abstract class SAbstractActivity<P extends SPresentible> extends RxAppCompatActivity implements SViewible<P>, BGASwipeBackHelper.Delegate {

    private P p;
    protected Activity context;
    protected BGASwipeBackHelper mSwipeBackHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;

        if (getLayoutId() > 0) {
            setContentView(getLayoutId());
            butterKnifeBind(null);
            initTopBar();
            initSwipeBackFinish();
            setStatusBarBg();
            SRouterFactory.init(this);
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

    /**
     * 初始化滑动返回。在 super.onCreate(savedInstanceState) 之前调用该方法
     */
    private void initSwipeBackFinish() {
        mSwipeBackHelper = new BGASwipeBackHelper(this, this);

        // 「必须在 Application 的 onCreate 方法中执行 BGASwipeBackHelper.init 来初始化滑动返回」
        // 下面几项可以不配置，这里只是为了讲述接口用法。

        // 设置滑动返回是否可用。默认值为 true
        mSwipeBackHelper.setSwipeBackEnable(true);
        // 设置是否仅仅跟踪左侧边缘的滑动返回。默认值为 true
        mSwipeBackHelper.setIsOnlyTrackingLeftEdge(true);
        // 设置是否是微信滑动返回样式。默认值为 true
        mSwipeBackHelper.setIsWeChatStyle(true);
        // 设置阴影资源 id。默认值为 R.drawable.bga_sbl_shadow
        mSwipeBackHelper.setShadowResId(R.drawable.bga_sbl_shadow);
        // 设置是否显示滑动返回的阴影效果。默认值为 true
        mSwipeBackHelper.setIsNeedShowShadow(true);
        // 设置阴影区域的透明度是否根据滑动的距离渐变。默认值为 true
        mSwipeBackHelper.setIsShadowAlphaGradient(true);
        // 设置触发释放后自动滑动返回的阈值，默认值为 0.3f
        mSwipeBackHelper.setSwipeBackThreshold(0.3f);
        // 设置底部导航条是否悬浮在内容上，默认值为 false
        mSwipeBackHelper.setIsNavigationBarOverlap(false);
    }

    /**
     * 是否支持滑动返回。这里在父类中默认返回 true 来支持滑动返回，如果某个界面不想支持滑动返回则重写该方法返回 false 即可
     *
     * @return
     */
    @Override
    public boolean isSupportSwipeBack() {
        return true;
    }

    /**
     * 正在滑动返回
     *
     * @param slideOffset 从 0 到 1
     */
    @Override
    public void onSwipeBackLayoutSlide(float slideOffset) {
    }

    /**
     * 没达到滑动返回的阈值，取消滑动返回动作，回到默认状态
     */
    @Override
    public void onSwipeBackLayoutCancel() {
    }

    /**
     * 滑动返回执行完毕，销毁当前 Activity
     */
    @Override
    public void onSwipeBackLayoutExecuted() {
        mSwipeBackHelper.swipeBackward();
    }

    @Override
    public void onBackPressed() {
        // 正在滑动返回的时候取消返回按钮事件
        if (mSwipeBackHelper.isSliding()) {
            return;
        }
        mSwipeBackHelper.backward();
    }
}
