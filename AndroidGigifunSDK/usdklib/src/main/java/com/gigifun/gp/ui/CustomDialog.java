package com.gigifun.gp.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.gigifun.gp.utils.ButtonUtil;
import com.gigifun.gp.utils.MResource;


public class CustomDialog extends Dialog {

	public CustomDialog(Context context) {
		super(context);
	}

	public CustomDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private LinearLayout btnPre;
		private CustomDialog dialog;
		private View layout;
		private OnClickListener positiveButtonClickListener;

		public Builder(Context context) {
			this.context = context;
		}


	

	

		/**
		 * Set the positive button resource and it's listener
		 * 
		 * @param positiveButtonText
		 * @return
		 */
		public Builder setPositiveButton(int positiveButtonText,
				OnClickListener listener) {
			this.positiveButtonClickListener = listener;
			return this;
		}

		public Builder setPositiveButton(
				OnClickListener listener) {
			this.positiveButtonClickListener = listener;
			return this;
		}


		public CustomDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			
			// set the dialog title
			// set the confirm button
			
		
			try {
				dialog = new CustomDialog(context,MResource.getIdByName(context, "style", "Dialog"));
				layout = inflater.inflate(MResource.getIdByName(context, "layout", "dialog_normal_layout"), null);
				dialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				btnPre = ((LinearLayout) layout.findViewById(MResource.getIdByName(context, "id", "positiveButton")));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
				if (positiveButtonClickListener != null) {
					btnPre.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {

									if(ButtonUtil.isFastDoubleClick(v.getId())){
										return;
									}

									btnPre.setClickable(false);
									positiveButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			dialog.setContentView(layout);
			return dialog;
		}

	}
}
