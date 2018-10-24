/**   
 * 文件名：LoadingDialog.java</br>
 * 描述： </br>
 * 开发人员：杜逸平 </br>
 * 创建时间： 2014-11-28
 */

package com.gigifun.gp.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gigifun.gp.utils.LogUtil;
import com.gigifun.gp.utils.MResource;

/**
 * 开发人员： 谁抢了我的飞宇 </br>
 * QQ：460543600</br>
 * 创建时间： 2014-11-28
 */

public class LoadingDialog {
	public static Dialog createLoadingDialog(Context context, String msg) {

		try {
			LayoutInflater inflater = LayoutInflater.from(context);
			View v = inflater.inflate(MResource.getIdByName(context, "layout", "loading_dialog"), null);// 得到加载view
			LinearLayout layout = (LinearLayout) v.findViewById(MResource.getIdByName(context, "id", "dialog_view"));// 加载布局
			// main.xml中的ImageView
			ImageView spaceshipImage = (ImageView) v.findViewById(MResource.getIdByName(context, "id", "img"));
			TextView tipTextView = (TextView) v.findViewById(MResource.getIdByName(context, "id", "tipTextView"));// 提示文字
			//TextView tipTextView = MResource.getIdByName(context, "id", "tipTextView");
			tipTextView.setVisibility(View.INVISIBLE);
			
			// 加载动画
			Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(
					context, MResource.getIdByName(context, "anim", "load_animation"));
			
			// 使用ImageView显示动画
			spaceshipImage.startAnimation(hyperspaceJumpAnimation);
			tipTextView.setText(msg);// 设置加载信息

			Dialog loadingDialog = new Dialog(context, MResource.getIdByName(context, "style", "loading_dialog"));// 创建自定义样式dialog
			loadingDialog.setCancelable(false);// 不可以用“返回键”取消
			loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.MATCH_PARENT));// 设置布局
			return loadingDialog;
		} catch (NotFoundException e) {
			LogUtil.e("NotFoundException: "+e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			LogUtil.e("Exception: "+e.getMessage());
			e.printStackTrace();
		}

		return new Dialog(context);
	}
	
	
	public static Dialog createLoadingDialog(Context context){
		try {
			ProgressBar bar=new ProgressBar(context);
			Dialog dialog=new Dialog(context,MResource.getIdByName(context, "style", "loading_dialog"));
			dialog.setCanceledOnTouchOutside(false);
			//dialog.setCancelable(false);
			dialog.setContentView(bar);
			return dialog;
		} catch (Exception e) {
			LogUtil.e("创建Dialog异常： "+e.getMessage());
			e.printStackTrace();
		}
		return new Dialog(context);
	}
}
