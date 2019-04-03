package com.stone.baselib.imageloader;

import android.content.Context;
import android.widget.ImageView;

import com.stone.baselib.SConfig;

import java.io.File;

/**
 * Stone
 * 2019/4/3
 * 图片加载接口
 **/
public interface SImgLoadible {

    void init(Context context);

    void loadNet(ImageView target, String url, Options options);

    void loadNet(Context context, String url, Options options, SImgLoadCallback callback);

    void loadResource(ImageView target, int resId, Options options);

    void loadAssets(ImageView target, String assetName, Options options);

    void loadFile(ImageView target, File file, Options options);

    void clearMemoryCache(Context context);

    void clearDiskCache(Context context);

    void resume(Context context);

    void pause(Context context);

    void loadCircle(String url, ImageView target, Options options);

    void loadCorner(String url, ImageView target, int radius, Options options);

    class Options {

        public int loadingResId;        //加载中的资源id
        public int loadErrorResId;      //加载失败的资源id

        public ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER_CROP;

        public static Options defaultOptions() {
            return new Options(SConfig.IMAGE_LOADING_RES, SConfig.IMAGE_ERROR_RES);
        }

        public Options(int loadingResId, int loadErrorResId) {
            this.loadingResId = loadingResId;
            this.loadErrorResId = loadErrorResId;
        }

        public Options scaleType(ImageView.ScaleType scaleType) {
            this.scaleType = scaleType;
            return this;
        }
    }

}
