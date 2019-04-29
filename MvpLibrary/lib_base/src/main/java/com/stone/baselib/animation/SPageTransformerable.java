package com.stone.baselib.animation;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

/**
 * Stone
 * 2019/4/29
 * ViewPager切换动画
 **/
public abstract class SPageTransformerable implements ViewPager.PageTransformer {
    @Override
    public void transformPage(@NonNull View page, float position) {
        if (position < -1 || position >= 1) {
            other(page, position);
        } else if (position < 1) {
            if (position > -1 && position < 0.0f) {
                toLeft(page, position);
            } else if (position >= 0.0f && position < 1.0f) {
                toRight(page, position);
            }
        }
    }

    protected abstract void other(View page, float position);

    protected abstract void toLeft(View page, float position);

    protected abstract void toRight(View page, float position);
}
