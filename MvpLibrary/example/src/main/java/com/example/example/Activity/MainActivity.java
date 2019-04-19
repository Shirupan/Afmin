package com.example.example.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.example.Present.MainPresent;
import com.example.example.R;
import com.example.example.base.BaseActivity;
import com.example.example.base.Constants;
import com.example.example.event.BaseEvent;
import com.example.example.event.TestIntEvent;
import com.example.example.event.TestStrEvent;
import com.stone.baselib.busevent.SBusFactory;
import com.stone.baselib.busevent.SEvent;
import com.stone.baselib.busevent.SEventBusImpl;
import com.stone.baselib.imageloader.SImgLoadFactory;
import com.stone.baselib.utils.SLogUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity<MainPresent> {
    public static final String TAG = "MainActivity";

    @BindView(R.id.btn_main_toast)
    Button btnToast;
    @BindView(R.id.btn_main_snack)
    Button btnSnack;
    @BindView(R.id.btn_main_event)
    Button btnEvent;
    @BindView(R.id.iv_main_bg)
    ImageView ivBg;
    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getP().getTestStr();

        //使用Glide加载本地图片，也可以从网络加载
        SImgLoadFactory.getGlideLoader().loadResource(ivBg, R.mipmap.jfla, null);
    }

    @Override
    public MainPresent getPInstance() {
        return new MainPresent();
    }

    @Override
    public boolean useEventBus() {
        return false;
    }

    public void showMsg(String msg) {
        //打印日志
        SLogUtils.d("log test:"+msg);
        SLogUtils.d(TAG, "log test:"+msg);
        //展示Snack提示
        showSnackBar(msg, R.color.colorMain, R.string.btnSnackText);
        //展示Toast提示
        showToast(msg);
    }

    @OnClick({R.id.btn_main_snack})
    public void onClickSnack(View view) {
        showSnackBar("Click snack", R.color.colorMain, R.string.btnSnackText);
    }

    @OnClick({R.id.btn_main_toast})
    public void onClickToast(View view) {
        showToast("Click toast");
    }

    @OnClick({R.id.btn_main_event})
    public void onClickEvent(View view) {
        SEventBusImpl bus = SBusFactory.getBus();
        bus.post(new TestStrEvent().setMsg("str event"));
        bus.post(new TestIntEvent().setNum(1));
        bus.post(new BaseEvent().setEventType(Constants.EVENT_BASE_INT).setData(1));
        bus.post(new BaseEvent().setEventType(Constants.EVENT_BASE_STR).setData("str"));
    }

    //按类型方式来接收event
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getStrEvent(TestStrEvent event){
        showToast(event.getMsg());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getIntEvent(TestIntEvent event){
        showSnackBar(String.valueOf(event.getNum()), R.color.colorMain, R.string.btnSnackText);
    }

    //按变量type方式来接收event
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getBaseEvent(BaseEvent event){
        if(event.getEventType()==Constants.EVENT_BASE_INT){
            showToast((String) event.getData());
        }else if(event.getEventType()==Constants.EVENT_BASE_STR){
            showSnackBar((String) event.getData(), R.color.colorMain, R.string.btnSnackText);
        }
    }
}
