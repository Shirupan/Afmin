package com.stone.baselib.mvp;

/**
 * Stone
 * 2019/4/10
 **/
public interface SPresentible<V> {
    void attachV(V view);

    void detachV();

    boolean hasV();
}
