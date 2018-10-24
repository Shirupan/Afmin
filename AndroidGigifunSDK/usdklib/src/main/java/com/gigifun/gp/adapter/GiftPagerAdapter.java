package com.gigifun.gp.adapter;

import android.app.Activity;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * tim
 */
public class GiftPagerAdapter extends PagerAdapter {

	private ArrayList<LinearLayout> views;

	public GiftPagerAdapter(Activity activity) {
		views = new ArrayList<LinearLayout>();
	;
	}

	public void add(LinearLayout ll) {
		views.add(ll);
	}

	@Override
	public void destroyItem(View view, int arg1, Object object) {
		((ViewPager) view).removeView((LinearLayout) object);
	}

	@Override
	public void finishUpdate(View arg0) {

	}

	@Override
	public int getCount() {
		return views.size();
	}

	@Override
	public Object instantiateItem(View view, int position) {
		View myView = views.get(position);
		((ViewPager) view).addView(myView);
		return myView;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {

	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {

	}

}