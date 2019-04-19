package com.example.example.Present;

import com.example.example.Activity.Constants;
import com.example.example.Activity.MainActivity;
import com.example.example.event.BaseEvent;
import com.example.example.event.TestIntEvent;
import com.example.example.event.TestStrEvent;
import com.stone.baselib.busevent.SBusFactory;
import com.stone.baselib.busevent.SEventBusImpl;
import com.stone.baselib.cache.SSpUtil;
import com.stone.baselib.mvp.SPresentImpl;
import com.stone.baselib.utils.SDateUtils;

/**
 * Stone
 * 2019/4/11
 **/
public class MainPresent extends SPresentImpl<MainActivity> {

    public void getTestStr() {
        getV().showMsg("test");
    }

    public void postBus() {
        SEventBusImpl bus = SBusFactory.getBus();
        bus.post(new TestStrEvent().setMsg("str event"));
        bus.post(new TestIntEvent().setNum(1));
        bus.post(new BaseEvent().setEventType(Constants.EVENT_BASE_INT).setData(1));
        bus.post(new BaseEvent().setEventType(Constants.EVENT_BASE_STR).setData("str"));
    }

    public void getExitTime() {
        long time = SSpUtil.getInstance(getV()).getLong(Constants.SP_EXIT_TIME,0);
        getV().showExitTime(SDateUtils.getYmdhms(time));
    }

    public void saveTime() {
        SSpUtil.getInstance(getV()).putLong(Constants.SP_EXIT_TIME, System.currentTimeMillis());
    }
}
