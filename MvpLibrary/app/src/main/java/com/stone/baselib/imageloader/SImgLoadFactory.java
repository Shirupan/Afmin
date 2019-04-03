package com.stone.baselib.imageloader;

/**
 * Stone
 * 2019/4/3
 * 由此类获取加载实例
 **/
public class SImgLoadFactory {

    private static SImgLoadible loader;

    private SImgLoadFactory() {

    }

    public static SImgLoadible getGlideLoader() {
        if (loader == null) {
            synchronized (SImgLoadFactory.class) {
                if (loader == null) {
                    loader = new SGlideLoader();
                }
            }
        }
        return loader;
    }


}
