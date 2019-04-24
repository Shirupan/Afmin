package com.example.example.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.example.Present.HomePresent;
import com.example.example.Present.SetPwdPresent;
import com.example.example.R;
import com.example.example.activity.Constants;
import com.example.example.base.BaseFragment;
import com.example.example.event.BaseEvent;
import com.example.example.event.TestIntEvent;
import com.example.example.event.TestStrEvent;
import com.stone.baselib.imageloader.SImgLoadFactory;
import com.stone.baselib.utils.SLogUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Stone
 * 2019/4/11
 **/
public class HomeFragment extends BaseFragment<HomePresent> {
    public static final String TAG = "HomeFragment";

    @BindView(R.id.btn_home_toast)
    Button btnToast;
    @BindView(R.id.btn_home_snack)
    Button btnSnack;
    @BindView(R.id.btn_home_event)
    Button btnEvent;
    @BindView(R.id.btn_home_sp)
    Button btnSp;

    @BindView(R.id.iv_home_bg)
    ImageView ivBg;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        getP().getTestStr();

        //使用Glide加载本地图片，也可以从网络加载
        SImgLoadFactory.getGlideLoader().loadResource(ivBg, R.mipmap.jfla, null);
    }

    @Override
    public HomePresent getPInstance() {
        return new HomePresent();
    }

    @Override
    public boolean useEventBus() {
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getP().saveTime();
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

    @OnClick({R.id.btn_home_snack})
    public void onClickSnack(View view) {
        showSnackBar("Click snack", R.color.colorMain, R.string.btnSnackText);
    }

    @OnClick({R.id.btn_home_toast})
    public void onClickToast(View view) {
        showToast("Click toast");
    }

    @OnClick({R.id.btn_home_event})
    public void onClickEvent(View view) {
        getP().postBus();
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

    @OnClick({R.id.btn_home_sp})
    public void onClickSp(View view) {
        getP().getExitTime();
    }

    public void showExitTime(String time){
        btnSp.setText(time);
    }

    @OnClick({R.id.btn_home_http_post})
    public void onClickHttpPost(View view) {
        getP().httpPost();
    }

    @OnClick({R.id.btn_home_http_get})
    public void onClickHttpGet(View view) {
        getP().httpGet();
    }
}
