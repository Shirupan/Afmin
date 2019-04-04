package com.stone.baselib.net.progress;

/**
 * Stone
 * 2019/4/4
 **/
public interface SProgressListener {
    void onProgress(long soFarBytes, long totalBytes);

    void onError(Throwable throwable);
}
