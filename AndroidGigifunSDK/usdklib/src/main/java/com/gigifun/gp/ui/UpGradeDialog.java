package com.gigifun.gp.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

import com.gigifun.gp.utils.ButtonUtil;
import com.gigifun.gp.utils.MResource;
import com.gigifun.gp.utils.UgameUtil;


public class UpGradeDialog extends Dialog {

	public UpGradeDialog(Context context) {
		super(context);
	}

	public UpGradeDialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private TextView btnPre;
		private TextView btnCancel;
		private UpGradeDialog dialog;
		private View layout;
		private OnClickListener positiveButtonClickListener;
		
		private OnClickListener cancelButtonClickListener;
		private TextView tvMessage;
		private String messageTxt;
		
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
		
		
		public Builder setCancelButton(int positiveButtonText,
				OnClickListener listener) {
			this.cancelButtonClickListener = listener;
			return this;
		}

		public Builder setCancelButton(
				OnClickListener listener) {
			this.cancelButtonClickListener = listener;
			return this;
		}

		public Builder setMessage(String message){
			this.messageTxt=message;
			return this;
		}
		
		
		private String getMessage(){
			return messageTxt;
		}
		
		public UpGradeDialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			// instantiate the dialog with the custom Theme
			
			// set the dialog title
			// set the confirm button

			UgameUtil.getInstance().changeLang(this.context);
			try {
				dialog = new UpGradeDialog(context,MResource.getIdByName(context, "style", "Dialog"));
				dialog.setCanceledOnTouchOutside(false);
				dialog.setCancelable(false);
				layout = inflater.inflate(MResource.getIdByName(context, "layout", "dialog_upgrade_layout"), null);
				dialog.addContentView(layout, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
				btnPre = ((TextView) layout.findViewById(MResource.getIdByName(context, "id", "positiveButton")));
				btnCancel = ((TextView) layout.findViewById(MResource.getIdByName(context, "id", "cancelButton")));
				tvMessage = ((TextView) layout.findViewById(MResource.getIdByName(context, "id", "message")));
				
				if(getMessage()!=null){
					tvMessage.setText(getMessage());
				}else{
					tvMessage.setText(MResource.getIdByName(context, "string", "upgrade_message"));
				}
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
									positiveButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
				
				if (cancelButtonClickListener != null) {
					btnCancel.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {

									if(ButtonUtil.isFastDoubleClick(v.getId())){
										return;
									}
									cancelButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_NEGATIVE);
								}
							});
				}else{
					btnCancel.setVisibility(View.GONE);
				}
			dialog.setContentView(layout);
			return dialog;
		}

	}
}
