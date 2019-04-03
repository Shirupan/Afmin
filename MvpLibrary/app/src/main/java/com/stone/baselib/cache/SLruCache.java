package com.stone.baselib.cache;

import android.support.v4.util.LruCache;
import android.text.TextUtils;

/**
 * Stone
 * 2019/4/3
 **/

public class SLruCache implements SCacheible {

    private LruCache<String, Object> cache;
    private static SLruCache instance;

    private SLruCache() {
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        cache = new LruCache<String, Object>(cacheSize);

    }

    public static SLruCache getInstance() {
        if (instance == null) {
            synchronized (SLruCache.class) {
                if (instance == null) {
                    instance = new SLruCache();
                }
            }
        }
        return instance;
    }


    @Override
    public synchronized void put(String key, Object value) {
        if (TextUtils.isEmpty(key)) return;

        if (cache.get(key) != null) {
            cache.remove(key);
        }
        cache.put(key, value);
    }

    @Override
    public Object get(String key) {
        return cache.get(key);
    }

    public synchronized <T> T get(String key, Class<T> clazz) {
        try {
            return (T) cache.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void remove(String key) {
        if (cache.get(key) != null) {
            cache.remove(key);
        }
    }

    @Override
    public boolean contains(String key) {
        return cache.get(key) != null;
    }

    @Override
    public void clear() {
        cache.evictAll();
    }
}
