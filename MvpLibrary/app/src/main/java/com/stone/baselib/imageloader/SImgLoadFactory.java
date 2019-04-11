package com.stone.baselib.imageloader;

/**
 * Stone
 * 2019/4/3
 * 由此类获取加载实例
 **/
public class SImgLoadFactory {

    private static volatile SImgLoadible instance;

    private SImgLoadFactory() {

    }

    public static SImgLoadible getGlideLoader() {
        if (instance == null) {
            synchronized (SImgLoadFactory.class) {
                if (instance == null) {
                    instance = new SGlideImpl();
                }
            }
        }
        return instance;
    }


}
