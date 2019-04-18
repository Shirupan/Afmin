package com.stone.baselib.imageloader;

import android.graphics.drawable.Drawable;

/**
 * Stone
 * 2019/4/3
 **/

public abstract class SImgLoadCallback {
    void onLoadFailed() {}

    public abstract void onLoadReady(Drawable drawable);

    void onLoadCanceled() {}
}
