package com.example.example.anim;

import android.view.View;

import com.stone.baselib.animation.SPageTransformerable;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;

/**
 * Stone
 * 2019/4/29
 **/
public class SPageTransformerFadeIn extends SPageTransformerable {
    private float mMaxAlpha = 1.0f;

    public SPageTransformerFadeIn() {

    }

    public SPageTransformerFadeIn(float maxRotation) {
        setMaxAlpha(maxRotation);
    }

    @Override
    public void toLeft(View view, float position) {
        //设置旋转中心点
        view.setPivotX(view.getMeasuredWidth()* 0.5f);
        view.setPivotY(view.getMeasuredHeight() * 0.5f);
        if (Math.abs(position)<mMaxAlpha){
            view.setAlpha(1-Math.abs(position));
        }else{
            view.setAlpha(mMaxAlpha);
        }

    }

    @Override
    public void toRight(View view, float position) {
        //设置旋转中心点
        view.setPivotX(view.getMeasuredWidth()* 0.5f);
        view.setPivotY(view.getMeasuredHeight() * 0.5f);
        view.setAlpha(1-position);
    }

    @Override
    public void other(View view, float position) {

    }

    public void setMaxAlpha(float maxAlpha) {
        if (maxAlpha >= 0.0f && maxAlpha <= 1.0f) {
            mMaxAlpha = maxAlpha;
        }
    }

}
