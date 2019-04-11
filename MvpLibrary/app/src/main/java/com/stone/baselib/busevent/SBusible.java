package com.stone.baselib.busevent;

/**
 * Stone
 * 2019/4/9
 **/
public interface SBusible {

    void register(Object object);

    void unregister(Object object);

    void post(SEvent event);

    void postSticky(SEvent event);

}
