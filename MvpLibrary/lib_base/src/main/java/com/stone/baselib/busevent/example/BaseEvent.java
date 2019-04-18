package com.stone.baselib.busevent.example;

import com.stone.baselib.busevent.SEvent;

/**
 * Stone
 * 2019/4/9
 * 不同业务可编写不同Event实例类，也可用eventType来区分不同业务
 **/
public class BaseEvent implements SEvent {
    private int eventType;
    private Object date;
    private Object xx;

    @Override
    public int getEventType() {
        return 0;
    }

    @Override
    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

}
