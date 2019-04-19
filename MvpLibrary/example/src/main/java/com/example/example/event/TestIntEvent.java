package com.example.example.event;

import com.example.example.Activity.Constants;
import com.stone.baselib.busevent.SEvent;

/**
 * Stone
 * 2019/4/9
 * 不同业务可编写不同Event实例类，也可用eventType来区分不同业务
 **/
public class TestIntEvent implements SEvent {
    private int eventType = Constants.EVENT_TEST_INT;
    private int num;
    private Object xx;

    @Override
    public int getEventType() {
        return eventType;
    }

    @Override
    public TestIntEvent setEventType(int eventType) {
        this.eventType = eventType;
        return this;
    }

    public int getNum() {
        return num;
    }

    public TestIntEvent setNum(int num) {
        this.num = num;
        return this;
    }
}
