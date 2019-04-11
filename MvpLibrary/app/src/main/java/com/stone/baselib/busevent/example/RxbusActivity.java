package com.stone.baselib.busevent.example;

import android.app.Activity;
import android.os.Bundle;

import com.stone.baselib.busevent.SBusFactory;
import com.stone.baselib.busevent.SEvent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Stone
 * 2019/4/10
 **/
public class RxbusActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //发送BaseEvent类型事件
        SBusFactory.getBus().post(new BaseEvent());

    }

    //接收BaseEvent类型事件,方法名无规定
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(SEvent event) {
        switch (event.getEventType()){

        }
    }

    @Override
    protected void onStart() {
        //注册
        SBusFactory.getBus().register(this);
        super.onStart();
    }

    @Override
    protected void onStop() {
        //注销
        SBusFactory.getBus().unregister(this);
        super.onStop();
    }
}
