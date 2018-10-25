package com.gigifun.gp.ui;

import android.app.Activity;
import android.app.Dialog;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gigifun.gp.utils.LogUtil;
import com.gigifun.gp.utils.MResource;
import com.gigifun.gp.utils.UgameUtil;

/**
 * Created by tim on 16/8/31.
 */
public class SettingDialog implements View.OnClickListener{

    private Activity mActivity;
    private Dialog mDialog;



    private TextView motifyPasswordTv;
    private TextView bindEmailTv;
    private TextView bindGuestTv;
    private TextView bindFacebookTv;


    private ImageView backBtn;
    private String GAME_ID;
    private String CLIENT_SECRET;
    private LinearLayout layouSetting;
    private OnSettingListener onSettingListener;
    private ProgressWheel progressWheel;



    public SettingDialog(Activity activity,OnSettingListener onSettingListener){
        this.onSettingListener = onSettingListener;
        this.mActivity = activity;
        Log.d("SettingDialog==","进入账号管理");
        initUI();
        initData();
    }
    public interface OnSettingListener {
        public void notifyLoginDialogShow();
    }
    public void initData(){
        GAME_ID = UgameUtil.getInstance().GAME_ID;
        CLIENT_SECRET =  UgameUtil.getInstance().CLIENT_SECRET;
        LogUtil.d("GAME_ID:"+GAME_ID+" CLIENT_SECRET:"+CLIENT_SECRET);
    }



    public void initUI(){
        mDialog = new Dialog(mActivity, MResource.getIdByName(mActivity, "style", "Dialog_Fullscreen"));
        mDialog.getWindow().getAttributes().windowAnimations = MResource.getIdByName(mActivity, "style", "dialogAnim");
        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        switch(mActivity.getResources().getConfiguration().orientation){
//            //横屏
//            case Configuration.ORIENTATION_LANDSCAPE:
//                mDialog.setContentView(MResource.getIdByName(mActivity, "layout", "dialog_setting"));
//                LogUtil.k("横屏----1");
//                break;
//            //竖屏
//            case Configuration.ORIENTATION_PORTRAIT:
//                mDialog.setContentView(MResource.getIdByName(mActivity, "layout", "dialog_setting_ver"));
//                LogUtil.k("竖屏----");
//                break;
//            default:
//                mDialog.setContentView(MResource.getIdByName(mActivity, "layout", "dialog_setting"));
//                LogUtil.k("横屏----");
//
//        }
        boolean flag=UgameUtil.getInstance().isVer(mActivity);
        if(flag){
            mDialog.setContentView(MResource.getIdByName(mActivity, "layout", "dialog_setting"));
        }else{
            mDialog.setContentView(MResource.getIdByName(mActivity, "layout", "dialog_setting_ver"));
        }

        mDialog.setCancelable(false);

      //  loadingDialog=LoadingDialog.createLoadingDialog(mActivity);
        //查找控件
        progressWheel=(ProgressWheel)mDialog.findViewById(MResource.getIdByName(mActivity, "id", "progress_wheel"));
        motifyPasswordTv = (TextView) mDialog.findViewById(MResource.getIdByName(mActivity, "id", "tv_motifyPassword"));
        bindEmailTv = (TextView) mDialog.findViewById(MResource.getIdByName(mActivity, "id", "tv_bindEmail"));
        bindGuestTv = (TextView) mDialog.findViewById(MResource.getIdByName(mActivity, "id", "tv_bindGuest"));
        bindFacebookTv = (TextView) mDialog.findViewById(MResource.getIdByName(mActivity, "id", "tv_bindFacebook"));
        backBtn = (ImageView) mDialog.findViewById(MResource.getIdByName(mActivity, "id", "btn_back"));


        layouSetting = (LinearLayout)mDialog.findViewById(MResource.getIdByName(mActivity, "id", "dialog_setting_ver"));

        backBtn.setOnClickListener(this);
        motifyPasswordTv.setOnClickListener(this);
        bindEmailTv.setOnClickListener(this);
        bindGuestTv.setOnClickListener(this);
        bindFacebookTv.setOnClickListener(this);
        mDialog.show();

    }
    @Override
    public void onClick(View v) {
        if (v.getId()==MResource.getIdByName(mActivity, "id", "tv_motifyPassword")){
            LogUtil.k("SettingDialog-click"+"修改密码");
            new MotifyPwDialog(mActivity);
        }else  if (v.getId()==MResource.getIdByName(mActivity, "id", "tv_bindEmail")){
            LogUtil.k("绑定邮箱");
            new BindEmailDialog(mActivity);
        }else  if (v.getId()==MResource.getIdByName(mActivity, "id", "tv_bindGuest")){
            LogUtil.k("绑定游客账号");
            //xyz
            new BindAccountDialog(mActivity);

        }else if (v.getId()==MResource.getIdByName(mActivity, "id", "tv_bindFacebook")){
            LogUtil.k("绑定FACEBOOK");
//            new BindFbDialog(mActivity);
        }else if (v.getId()==MResource.getIdByName(mActivity, "id", "btn_back")){
            mDialog.dismiss();
            onSettingListener.notifyLoginDialogShow();
        }
    }

}
