/**   
 * 文件名：LikeGiftAdapter.java</br>
 * 描述： </br>
 * 开发人员：杜逸平 </br>
 * 创建时间： 2014-12-3
 */

package com.gigifun.gp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gigifun.gp.utils.MResource;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;

/**
 * 类名: LikeGiftAdapter</br>  </br>
 * 开发人员： 谁抢了我的飞宇 </br>
 * QQ：460543600
 * 创建时间： 2014-12-3
 */

public class LikeGiftAdapter extends BaseAdapter {
	Context context;
	List<HashMap<String, String>> list;
	LayoutInflater inflater;
	DisplayImageOptions options;
	ImageLoader imageLoader;

	public LikeGiftAdapter(Context context, List<HashMap<String, String>> list,ImageLoader imageLoader,DisplayImageOptions options) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
		this.options=options;
		this.imageLoader=imageLoader;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getCount() 开发人员：杜逸平
	 */
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int) 开发人员：杜逸平
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItemId(int) 开发人员：杜逸平
	 */
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getView(int, android.view.View,
	 * android.view.ViewGroup) 开发人员：杜逸平
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			try {
				convertView = inflater.inflate(MResource.getIdByName(context,
						"layout", "like_list_item"), null);
				holder.tvLikeTitle = (TextView) convertView
						.findViewById(MResource.getIdByName(context, "id",
								"tv_like_title"));
				holder.tvLikeDirection = (TextView) convertView
						.findViewById(MResource.getIdByName(context, "id",
								"tv_like_direction"));
				holder.imgLike = (ImageView) convertView.findViewById(MResource
						.getIdByName(context, "id", "img_like"));
				holder.imgLikeReach = (Button) convertView
						.findViewById(MResource.getIdByName(context, "id",
								"img_like_reach"));
				convertView.setTag(holder);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		HashMap<String, String> map = list.get(position);
		try {
			//标题和解析
			holder.tvLikeDirection.setText(URLDecoder.decode(map.get("comtent"),"utf-8"));
			holder.tvLikeTitle.setText(URLDecoder.decode(map.get("title"),"utf-8"));
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		try {
			//按钮
			if (map.get("complete").equals("0")) {// 0未完成 1 已经完成
				holder.imgLikeReach.setBackgroundResource(MResource.getIdByName(
						context, "drawable", "hasreach"));
				holder.imgLikeReach.setText(MResource.getIdByName(
						context, "string", "like_incompleted"));
				holder.imgLikeReach.setTextColor(context.getResources().getColor(MResource.getIdByName(
						context, "color", "like_color")));
			} else if (map.get("complete").equals("1")) {
				holder.imgLikeReach.setBackgroundResource(MResource.getIdByName(
						context, "drawable", "hasunreach"));
				holder.imgLikeReach.setText(MResource.getIdByName(
						context, "string", "like_completed"));
				holder.imgLikeReach.setTextColor(context.getResources().getColor(MResource.getIdByName(
						context, "color", "un_like_color")));
			}
			//展示图片
			imageLoader.displayImage(map.get("logo"), holder.imgLike, options);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}

	class ViewHolder {
		ImageView imgLike;
		TextView tvLikeTitle;
		TextView tvLikeDirection;
		Button imgLikeReach;
	}



}
