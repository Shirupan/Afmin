package com.stone.baselib.mvp;

import java.lang.ref.WeakReference;

/**
 * Stone
 * 2019/4/10
 **/
public class SPresentImpl<V extends SViewible> implements SPresentible<V> {
    private WeakReference<V> vWeak;

    @Override
    public void attachV(V view) {
        vWeak = new WeakReference<>(view);
    }

    @Override
    public void detachV() {
        if (vWeak.get() != null) {
            vWeak.clear();
        }
        vWeak = null;
    }

    protected V getV() {
        if (vWeak == null || vWeak.get() == null) {
            throw new IllegalStateException("v is null");
        }
        return vWeak.get();
    }

    @Override
    public boolean hasV() {
        return vWeak != null && vWeak.get() != null;
    }
}
