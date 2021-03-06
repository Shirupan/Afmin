package com.example.example.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.example.bean.SerializableBean;
import com.example.example.present.MainPresent;
import com.example.example.R;
import com.example.example.app.Constants;
import com.example.example.base.BaseActivity;
import com.example.example.event.BaseEvent;
import com.example.example.event.TestIntEvent;
import com.example.example.event.TestStrEvent;
import com.stone.baselib.imageloader.SImgLoadFactory;
import com.stone.baselib.router.SRouterFactory;
import com.stone.baselib.utils.SLogUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.btn_main_sp)
    Button btnSp;

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    @OnClick({R.id.btn_main_sp})
    public void onClickSp(View view) {
        getP().getExitTime();
    }

    public void showExitTime(String time){
        btnSp.setText(time);
    }

    @OnClick({R.id.btn_main_http_post})
    public void onClickHttpPost(View view) {
        getP().httpPost();
    }

    @OnClick({R.id.btn_main_http_get})
    public void onClickHttpGet(View view) {
        getP().httpGet();
    }

    @OnClick({R.id.btn_main_viewpager})
    public void onClickViewPager(View view) {
        SRouterFactory.getARouter().setAction("/viewpager/activity")
                .setTransition(R.anim.s_right_in, R.anim.s_left_out)
                .startWithAnim(this);
    }

    @OnClick({R.id.btn_main_viewpager2})
    public void onClickViewPager2(View view) {
        toAct(ViewPager2Activity.class);
    }

    @OnClick(R.id.btn_main_pay)
    public void onClickArouter(View view) {
        SerializableBean sb = new SerializableBean();
        sb.setAge(18);
        sb.setName("sb");
        sb.setSex(1);
        List<SerializableBean> list = new ArrayList<>();
        list.add(sb);
//        ARouter.getInstance().build("/pay/activity")
//                .withObject("listMsg", list)
//                .withSerializable("serializableMsg", sb)
//                .withTransition(R.anim.s_left_in, R.anim.s_left_out)
//                .navigation(this);//withTransition一定要传context才生效

        SRouterFactory.getARouter().setAction("/pay/activity")
                .putInt("intMsg", 1)
                .putLong("longMsg", 2)
                .putFloat("floatMsg", 3)
                .putDouble("doubleMsg", 4)
                .putBoolean("booleanMsg", true)
                .putString("stringMsg", "string")
                .putSerializable("serializableMsg", sb)
                .putObject("listMsg", list)
                .setTransition(R.anim.s_right_in, R.anim.s_left_out)
                .startWithAnim(this);
    }

    private void toAct(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            activityManager.exitApp();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
