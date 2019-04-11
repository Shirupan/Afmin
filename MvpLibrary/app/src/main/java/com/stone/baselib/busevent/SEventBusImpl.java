package com.stone.baselib.busevent;


import org.greenrobot.eventbus.EventBus;

/**
 * Stone
 * 2019/4/9
 **/
public class SEventBusImpl implements SBusible {
    private EventBus mbus;

    public SEventBusImpl() {
        mbus = EventBus.getDefault();
    }

    @Override
    public void register(Object object) {
        if (!mbus.isRegistered(object)) {
            mbus.register(object);
        }
    }

    @Override
    public void unregister(Object object) {
        if (mbus.isRegistered(object)) {
            mbus.unregister(object);
        }
    }

    @Override
    public void post(SEvent event) {
        mbus.post(event);
    }

    @Override
    public void postSticky(SEvent event) {
        mbus.postSticky(event);
    }

}
