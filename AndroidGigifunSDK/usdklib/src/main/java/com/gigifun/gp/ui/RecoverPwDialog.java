package com.gigifun.gp.ui;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gigifun.gp.utils.UcallBack;
import com.gigifun.gp.utils.UgameUtil;
import com.gigifun.gp.utils.UhttpUtil;
import com.gigifun.gp.utils.LogUtil;
import com.gigifun.gp.utils.MResource;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by tim on 16/8/31.
 */
public class RecoverPwDialog implements View.OnClickListener{

    private Activity mActivity;
    private Dialog mDialog;


    private Button motifyBtn;
    private ImageView backBtn;
    private EditText motifyUserNameEt;
    private EditText regEmailEt;
    private EditText motifyPassWordConEt;
    private String GAME_ID;
    private String CLIENT_SECRET;
    private Message msg;
    private LinearLayout layouMotify;
    private ImageView eyeImg;
//    private Boolean isEye = false;
    private boolean isHidePwd = true;// 输入框密码是否是隐藏的，默认为true
    public static final int REGIST_RESULTCODE=2;
    private String sdkUid;


    private String userNameTxt;
    private String regEmailTxt;

  //  private Dialog loadingDialog;

    private OnRecoverPwListener onRecoverPwListener;

    private LoginDialog loginDialog;
    private ProgressWheel progressWheel;

    public interface OnRecoverPwListener {
//        public void onRegistSuccessful(String userName, String passWord, String sdkUid);
        public void notifyLoginDialogShow();

    }

    public RecoverPwDialog(Activity activity,OnRecoverPwListener onRecoverPwListener) {
        this.onRecoverPwListener = onRecoverPwListener;
        this.mActivity = activity;

        initUI();
        initData();
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
//                mDialog.setContentView(MResource.getIdByName(mActivity, "layout", "dialog_recoverpw"));
//                LogUtil.k("横屏----1");
//                break;
//            //竖屏
//            case Configuration.ORIENTATION_PORTRAIT:
//                mDialog.setContentView(MResource.getIdByName(mActivity, "layout", "dialog_recoverpw_ver"));
//                LogUtil.k("竖屏----");
//                break;
//            default:
//                mDialog.setContentView(MResource.getIdByName(mActivity, "layout", "dialog_recoverpw"));
//                LogUtil.k("横屏----");
//
//        }
        boolean flag=UgameUtil.getInstance().isVer(mActivity);
        if(flag){
            mDialog.setContentView(MResource.getIdByName(mActivity, "layout", "dialog_recoverpw"));
        }else{
            mDialog.setContentView(MResource.getIdByName(mActivity, "layout", "dialog_recoverpw_ver"));
        }
       // mDialog.setContentView(MResource.getIdByName(mActivity, "layout", "dialog_recoverpw"));
        mDialog.setCancelable(false);

      //  loadingDialog=LoadingDialog.createLoadingDialog(mActivity);
        progressWheel=(ProgressWheel)mDialog.findViewById(MResource.getIdByName(mActivity, "id", "progress_wheel"));

        motifyBtn = (Button) mDialog.findViewById(MResource.getIdByName(mActivity, "id", "btn_motify"));
        backBtn = (ImageView) mDialog.findViewById(MResource.getIdByName(mActivity, "id", "btn_back"));
        motifyUserNameEt = (EditText) mDialog.findViewById(MResource.getIdByName(mActivity, "id", "edt_motify_username"));
        regEmailEt = (EditText) mDialog.findViewById(MResource.getIdByName(mActivity, "id", "edt_regemail"));


        backBtn.setOnClickListener(this);
        motifyBtn.setOnClickListener(this);
        mDialog.show();

    }



    private void parseModifyPwJosn(String josn) {
        if(TextUtils.isEmpty(josn)){
            closeProgressWheel();
            handler.sendEmptyMessage(203);
            return;
        }
        msg = new Message();
        JSONObject obj;
        try {
            obj = new JSONObject(josn);
            String status = obj.getString("Status");
            String code = obj.getString("Code");
            if ("1".equals(status) && "100".equals(code)) {// 可以修改
                msg.what = 400;
                handler.sendMessage(msg);
                mDialog.dismiss();

            } else if ("0".equals(status)) {
                if ("104".equals(code)){
                    msg.what = 101;
                    handler.sendMessage(msg);
                }else if ("110".equals(code)){
                    msg.what = 102;
                    handler.sendMessage(msg);
                }else if ("111".equals(code)||"148".equals(code)){
                    msg.what = 103;
                    handler.sendMessage(msg);
                }else if ("151".equals(code)){
                    msg.what = 104;
                    handler.sendMessage(msg);
                }else if ("153".equals(code)){
                    msg.what = 105;
                    handler.sendMessage(msg);

                } else if ("155".equals(code)){
                    msg.what = 106;
                    handler.sendMessage(msg);

                }else if ("156".equals(code)||"163".equals(code)){
                    msg.what = 107;
                    handler.sendMessage(msg);
                }else if ("157".equals(code)){
                    msg.what = 108;
                    handler.sendMessage(msg);
                }else if ("158".equals(code)){
                    msg.what = 109;
                    handler.sendMessage(msg);
                }else if ("161".equals(code)){
                    msg.what = 111;
                    handler.sendMessage(msg);
                }else{
                    msg.what = 999;
                    handler.sendMessage(msg);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }finally{
           closeProgressWheel();

        }


    }

    Handler handler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 101:
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "exsituname"), Toast.LENGTH_SHORT).show();
                    break;
                case 102:
                    Toast.makeText(mActivity,com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "incorrectpw"), Toast.LENGTH_SHORT).show();
                    break;
                case 103:
                    Toast.makeText(mActivity,com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "notexsituname"), Toast.LENGTH_SHORT).show();
                    break;
                case 104:
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "vistornotexsit"), Toast.LENGTH_SHORT).show();
                    break;
                case 105:
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "vistorhadbind"), Toast.LENGTH_SHORT).show();
                    break;
                case 106:
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "emailhadbind"), Toast.LENGTH_SHORT).show();
                    break;
                case 107:
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "acchadbindemail"), Toast.LENGTH_SHORT).show();
                    break;
                case 108:
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "emailnotbind"), Toast.LENGTH_SHORT).show();
                    break;
                case 109:
                    Toast.makeText(mActivity,com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "sendemailfail"), Toast.LENGTH_SHORT).show();
                    break;

                case 111:
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "invaliduname"), Toast.LENGTH_SHORT).show();
                    break;


                case 400:
                    Toast.makeText(mActivity,com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "send_to_email"), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "contactcustomservice"), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @Override
    public void onClick(View v) {
         if (v.getId()==MResource.getIdByName(mActivity, "id", "btn_motify")){
            userNameTxt = motifyUserNameEt.getText().toString().trim();
             regEmailTxt = regEmailEt.getText().toString().trim();

            if (UgameUtil.isNullOrEmpty(userNameTxt)
                    || UgameUtil.isNullOrEmpty(regEmailTxt)
                    ) {
                Toast.makeText(mActivity, MResource.getIdByName(mActivity, "string", "content_can_not_be_empty"), Toast.LENGTH_SHORT)
                        .show();
                return;
            }
             if (userNameTxt.length()<4||userNameTxt.length()>20) {
                 Toast.makeText(mActivity, MResource.getIdByName(mActivity, "string", "unameinvalid"), Toast.LENGTH_SHORT)
                         .show();
                 return;
             }
             if (!"".equals(regEmailTxt)&&!emailValidation(regEmailTxt)){
                 Toast.makeText(mActivity, MResource.getIdByName(mActivity, "string", "invalidemail"), Toast.LENGTH_SHORT)
                         .show();
                 return;
             }


            showProgressWheel();
            final Map<String, String> map = new HashMap<String, String>();
            map.put("Ugameid", GAME_ID);
            map.put("Ugamekey", CLIENT_SECRET);
            map.put("Email", regEmailTxt);
            map.put("Username", userNameTxt);

            UhttpUtil.post(UgameUtil.getInstance().RECOVERPWURL,map, new UcallBack() {

                @Override
                public void onResponse(String response, int arg1) {
                    LogUtil.k("url="+ UgameUtil.getInstance().RECOVERPWURL+map.toString());
                    //Toast.makeText(mActivity,"修改成功",Toast.LENGTH_SHORT).show();
                    closeProgressWheel();

                    //解析response,根据返回吗，吐司相应错误
                    parseModifyPwJosn(response);
                }

                @Override
                public void onError(Call arg0, Exception arg1, int arg2) {

                    closeProgressWheel();
                    LogUtil.d("Reset error :"+arg1);
                    Toast.makeText(mActivity, MResource.getIdByName(mActivity, "string", "network_error"), Toast.LENGTH_SHORT)
                            .show();
                }
            });


        }else if (v.getId()==MResource.getIdByName(mActivity, "id", "btn_back")){
             mDialog.dismiss();
             onRecoverPwListener.notifyLoginDialogShow();
        }
    }

    public boolean emailValidation(String email) {
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        return email.matches(regex);
    }
    private void showProgressWheel() {
        if (progressWheel != null) {
            progressWheel.setVisibility(View.VISIBLE);
            progressWheel.spin();
        }
    }

    private void closeProgressWheel() {
        if (progressWheel != null) {
            progressWheel.stopSpinning();
            progressWheel.setVisibility(View.GONE);
        }

    }


}
