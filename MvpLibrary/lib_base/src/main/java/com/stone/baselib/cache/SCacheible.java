package com.stone.baselib.cache;

/**
 * Stone
 * 2019/4/3
 **/
public interface SCacheible {
    void put(String key, Object value);

    Object get(String key);

    void remove(String key);

    boolean contains(String key);

    void clear();

}
