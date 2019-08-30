package com.stone.baselib.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.TextView;

import com.painstone.mvplibrary.R;
import com.stone.baselib.utils.SLogUtils;

import androidx.constraintlayout.widget.ConstraintLayout;

/**
 * Stone
 * 2019/5/9
 * 建议在布局设置文字颜色，调用时设置文字和点击事件
 * 建议在xml中统一设置，或者在代码中统一设置。在代码中设置将覆盖xml设置。
 **/
public class STitleBar extends ConstraintLayout {
    public static final String TAG = "STitleBar";

    private ImageView imgLeft;
    private ImageView imgRight;
    private TextView tvLeft;
    private TextView tvCenter;
    private TextView tvRight;
    private int defTextColor = Color.BLACK;
    private int defTextSize = 54;//获取xml设置的值会翻三倍

    public STitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.view_stitlebar, this, true);

        bindId();
        setAttrs(context, attrs);
    }

    private void setAttrs(Context context, AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.STitleBar);
        if (attributes != null) {
            tvLeft.setText(attributes.getString(R.styleable.STitleBar_left_text));
            tvLeft.setTextColor(attributes.getColor(R.styleable.STitleBar_left_color, defTextColor));
            if(attributes.getBoolean(R.styleable.STitleBar_left_visible,true)){
                setLeftTextVisible(VISIBLE);
            }else {
                setLeftTextVisible(GONE);
            }

            imgLeft.setImageResource(attributes.getResourceId(R.styleable.STitleBar_img_left_src, 0));
            if(attributes.getBoolean(R.styleable.STitleBar_img_left_visible,true)){
                setLeftImgVisible(VISIBLE);
            }else {
                setLeftImgVisible(GONE);
            }

            tvCenter.setText(attributes.getString(R.styleable.STitleBar_title_text));
            tvCenter.setTextColor(attributes.getColor(R.styleable.STitleBar_title_color, defTextColor));
            if(attributes.getBoolean(R.styleable.STitleBar_title_visible,true)){
                setTitleVisible(VISIBLE);
            }else {
                setTitleVisible(GONE);
            }

            tvRight.setText(attributes.getString(R.styleable.STitleBar_right_text));
            tvRight.setTextColor(attributes.getColor(R.styleable.STitleBar_right_color, defTextColor));
            if(attributes.getBoolean(R.styleable.STitleBar_right_visible,true)){
                setRightTextVisible(VISIBLE);
            }else {
                setRightTextVisible(GONE);
            }

            imgRight.setImageResource(attributes.getResourceId(R.styleable.STitleBar_img_right_src, 0));
            if(attributes.getBoolean(R.styleable.STitleBar_img_right_visible,true)){
                setRightImgVisible(VISIBLE);
            }else {
                setRightImgVisible(GONE);
            }

            setTextSize(attributes.getDimension(R.styleable.STitleBar_text_size, defTextSize)/3);
        }
    }

    private void bindId(){
        imgLeft = findViewById(R.id.img_title_left);
        tvLeft = findViewById(R.id.tv_title_left);
        tvCenter = findViewById(R.id.tv_title_center);
        tvRight = findViewById(R.id.tv_title_right);
        imgRight = findViewById(R.id.img_title_right);
    }

    public void setBg(int colorId){
        setBackgroundColor(getContext().getResources().getColor(colorId));
    }

    public void setTextColor(int colorId){
        tvLeft.setTextColor(getContext().getResources().getColor(colorId));
        tvCenter.setTextColor(getContext().getResources().getColor(colorId));
        tvRight.setTextColor(getContext().getResources().getColor(colorId));
    }

    public void setTextSize(float size){
        tvLeft.setTextSize(size);
        tvCenter.setTextSize(size);
        tvRight.setTextSize(size);
    }

    public void setLeftImg(int id, OnClickListener onClickListener) {
        imgLeft.setImageResource(id);
        imgLeft.setOnClickListener(onClickListener);
    }

    public void setLeftImg(Drawable drawable, OnClickListener onClickListener) {
        imgLeft.setImageDrawable(drawable);
        imgLeft.setOnClickListener(onClickListener);
    }

    public void setLeftImgVisible(int visible) {
        imgLeft.setVisibility(visible);
    }

    public void setLeftText(int textId, OnClickListener onClickListener) {
        tvLeft.setText(textId);
        tvLeft.setOnClickListener(onClickListener);
    }

    public void setLeftText(String text, OnClickListener onClickListener) {
        tvLeft.setText(text);
        tvLeft.setOnClickListener(onClickListener);
    }

    public void setLeftTextVisible(int visible) {
        tvLeft.setVisibility(visible);
    }

    public void setTitle(int textId) {
        tvCenter.setText(textId);
    }

    public void setTitle(String text) {
        tvCenter.setText(text);
    }

    public void setTitleVisible(int visible) {
        tvCenter.setVisibility(visible);
    }

    public void setRightText(int textId, OnClickListener onClickListener) {
        tvRight.setText(textId);
        tvRight.setOnClickListener(onClickListener);
    }

    public void setRightText(String text, OnClickListener onClickListener) {
        tvRight.setText(text);
        tvRight.setOnClickListener(onClickListener);
    }

    public void setRightTextVisible(int visible) {
        tvRight.setVisibility(visible);
    }

    public void setRightImg(int id, OnClickListener onClickListener) {
        imgRight.setImageResource(id);
        imgRight.setOnClickListener(onClickListener);
    }

    public void setRightImg(Drawable drawable, OnClickListener onClickListener) {
        imgRight.setImageDrawable(drawable);
        imgRight.setOnClickListener(onClickListener);
    }

    public void setRightImgVisible(int visible) {
        imgRight.setVisibility(visible);
    }

}
