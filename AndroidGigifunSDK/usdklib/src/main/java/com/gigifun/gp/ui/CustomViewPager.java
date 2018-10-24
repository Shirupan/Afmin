package com.gigifun.gp.ui;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {

	private boolean enabled;

	public CustomViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		if (this.enabled) {
//			return super.onTouchEvent(event);
//		}

		return false;
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {


		return false;
	}

	public void setPagingEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}