package com.example.example.base;

import android.os.Bundle;
import com.example.example.R;
import com.jaeger.library.StatusBarUtil;

import android.view.View;

import com.stone.baselib.mvp.SAbstractActivity;
import com.stone.baselib.mvp.SPresentImpl;
import com.stone.baselib.wigeht.STitleBar;

import butterknife.BindView;

import androidx.appcompat.widget.Toolbar;
import cn.bingoogolapple.swipebacklayout.BGASwipeBackHelper;

/**
 * Stone
 * 2019/4/10
 * 处理一些定制化的title，StatusBar，加载提示
 **/
public abstract class BaseActivity<P extends SPresentImpl> extends SAbstractActivity<P> {
    STitleBar titleBar;
    protected BGASwipeBackHelper mSwipeBackHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initSwipeBackFinish();
    }

    private void setStatusBarBg() {
        //TODO
        StatusBarUtil.setTranslucent(this);
    }

    @Override
    public void initTopBar() {
        //要么在xml中统一设置，要么在此统一设置，单个activity修改。在此设置将覆盖xml设置。
        titleBar = findViewById(R.id.title_bar);
        if(titleBar == null){
            return;
        }
        titleBar.setTitle(R.string.title_title);//在子类activity中单独设置
        titleBar.setBg(R.color.bg_title);
        titleBar.setTextColor(R.color.text_title);
        titleBar.setLeftImg(R.mipmap.ic_back, close);
        titleBar.setLeftText(R.string.title_left, close);
        titleBar.setRightText(R.string.title_right, close);
        titleBar.setRightImg(R.mipmap.ic_close, close);
        titleBar.setLeftImgVisible(View.VISIBLE);
        titleBar.setLeftTextVisible(View.VISIBLE);
        titleBar.setTitleVisible(View.VISIBLE);
        titleBar.setRightTextVisible(View.VISIBLE);
        titleBar.setRightImgVisible(View.VISIBLE);
    }

    View.OnClickListener close=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    @Override
    public void showNoNetTips() {
        //TODO
    }

    public void showLoading(){
        //TODO
    }

    public void showError(){
        //TODO
    }

    public void showNoNet(){
        //TODO
    }

    public void showEmpty(){
        //TODO
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
