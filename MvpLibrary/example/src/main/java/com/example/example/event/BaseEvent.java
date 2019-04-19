package com.example.example.event;

import com.stone.baselib.busevent.SEvent;

/**
 * Stone
 * 2019/4/9
 * 不同业务可编写不同Event实例类，也可用eventType来区分不同业务
 **/
public class BaseEvent implements SEvent {
    private int eventType;
    private Object data;

    @Override
    public int getEventType() {
        return eventType;
    }

    @Override
    public BaseEvent setEventType(int eventType) {
        this.eventType = eventType;
        return this;
    }

    public Object getData() {
        return data;
    }

    public BaseEvent setData(Object data) {
        this.data = data;
        return this;
    }
}
