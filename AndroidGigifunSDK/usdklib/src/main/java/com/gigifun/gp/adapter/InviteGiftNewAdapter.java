/**   
 * 文件名：InviteGiftAdapter.java</br>
 * 描述： </br>
 * 开发人员：杜逸平 </br>
 * 创建时间： 2014-12-6
 */

package com.gigifun.gp.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.share.widget.AppInviteDialog;
import com.gigifun.gp.ui.UIInviteLayout;
import com.gigifun.gp.utils.ButtonUtil;
import com.gigifun.gp.utils.LogUtil;
import com.gigifun.gp.utils.MResource;
import com.facebook.share.model.GameRequestContent;
import com.facebook.share.widget.GameRequestDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;


import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;

/**
 * 类名: InviteGiftAdapter</br>  </br>
 * 开发人员： 谁抢了我的飞宇 </br>
 * QQ：460543600
 * 创建时间： 2014-12-6
 */

public class InviteGiftNewAdapter extends BaseAdapter {
	Context context;
	List<HashMap<String, String>> list;
	LayoutInflater inflater;
	DisplayImageOptions options;
	ImageLoader imageLoader;
	UIInviteLayout.InviteHandler handler;
	GameRequestDialog requestDialog;
	private SharedPreferences preferences;
	AppInviteDialog appInviteDialog;

	public InviteGiftNewAdapter(){

	}

	public InviteGiftNewAdapter(Context context,
							 List<HashMap<String, String>> list, ImageLoader imageLoader,
							 DisplayImageOptions options, UIInviteLayout.InviteHandler handler,
							 GameRequestDialog requestDialog,AppInviteDialog appInviteDialog) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
		this.options = options;
		this.imageLoader = imageLoader;
		this.handler=handler;
		this.requestDialog=requestDialog;
		this.appInviteDialog = appInviteDialog;
		preferences = context.getSharedPreferences("LoginCount", Context.MODE_PRIVATE);
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
		ViewHolder holder=null;
		if(null==convertView){
			holder=new ViewHolder();
			try {
				convertView=inflater.inflate(MResource.getIdByName(context, "layout", "invite_list_item"), null);
				holder.imgInviteItem=(ImageView)convertView.findViewById(MResource.getIdByName(context, "id", "img_invite_item"));
				holder.tvPeopleCount=(TextView)convertView.findViewById(MResource.getIdByName(context, "id", "tv_people_count"));
				holder.tvInviteComtent=(TextView)convertView.findViewById(MResource.getIdByName(context, "id", "tv_invite_comtent"));
				holder.imgInvite=(Button)convertView.findViewById(MResource.getIdByName(context, "id", "img_invite"));
				convertView.setTag(holder);

			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}else{
			holder=(ViewHolder) convertView.getTag();
		}

		try {
			final HashMap<String,String> map=list.get(position);
			holder.tvPeopleCount.setText(context.getString(MResource.getIdByName(context, "string", "invite_text")) + URLDecoder.decode(map.get("targetnum"),"utf-8")+context.getString(MResource.getIdByName(context, "string", "invite_people_text")));
			holder.tvInviteComtent.setText(URLDecoder.decode(map.get("packcomtent").trim(),"utf-8"));

			if(map.get("complete").equals("0")){
				holder.imgInvite.setBackgroundResource(MResource.getIdByName(context, "drawable", "img_invite"));
				holder.imgInvite.setText(MResource.getIdByName(context, "string", "invite_peo_send"));
				holder.imgInvite.setTextColor(Color.parseColor("#4d340f"));
			}else if(map.get("complete").equals("1")){
				holder.imgInvite.setBackgroundResource(MResource.getIdByName(context, "drawable", "img_invite_sended"));
				holder.imgInvite.setText(MResource.getIdByName(context, "string", "invite_peo_hadsend"));
				holder.imgInvite.setTextColor(Color.parseColor("#ffffff"));
			}
			imageLoader.displayImage(map.get("targetlogo"), holder.imgInviteItem, options);




			holder.imgInvite.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					if(ButtonUtil.isFastDoubleClick(v.getId())){
						return;
					}

					String inviteUrl = map.get("inviteUrl");
					GameRequestContent content = new GameRequestContent.Builder()
//							.setTitle("邀请你玩")
							.setMessage(URLDecoder.decode(map.get("invitelang")))
							.build();
					requestDialog.show(content);
					LogUtil.k("弹出fb邀请页面");

//					AppInviteContent invateContent = new AppInviteContent.Builder()
////							.setApplinkUrl(URLDecoder.decode(map.get("inviteUrl")))
//							.setApplinkUrl(inviteUrl)
//							.setPreviewImageUrl(URLDecoder.decode(map.get("invitePic")))
////							.setApplinkUrl("https://fb.me/1027866220677644")
////							.setPreviewImageUrl("https://iiusdkadmin.iiugame.com//data//upload//2017-06-27//5951c17244934.png")
////							.setApplinkUrl("https://d3uu10x6fsg06w.cloudfront.net/hosting-rps/applink.html")//facebook demo 的链接
////							.setPreviewImageUrl("https://d3uu10x6fsg06w.cloudfront.net/hosting-rps/rps-preview-image.jpg")//facebook demo 的图片
//							.build();
//
//					AppInviteDialog.show((Activity) context,invateContent);
				}
			});
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertView;
	}

	class ViewHolder{
		ImageView imgInviteItem;
		TextView tvPeopleCount;
		TextView tvInviteComtent;
		Button imgInvite;
	}

	public void setData(List<HashMap<String, String>> list){
		this.list=list;
		this.notifyDataSetChanged();
	}


}
