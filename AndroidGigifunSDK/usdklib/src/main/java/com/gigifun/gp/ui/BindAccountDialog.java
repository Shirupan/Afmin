package com.gigifun.gp.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gigifun.gp.utils.AESEncode;
import com.gigifun.gp.utils.LogUtil;
import com.gigifun.gp.utils.MResource;
import com.gigifun.gp.utils.UcallBack;
import com.gigifun.gp.utils.UgameUtil;
import com.gigifun.gp.utils.UhttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by tim on 16/8/31.
 */
public class BindAccountDialog implements View.OnClickListener {

    private Activity mActivity;
    private Dialog mDialog;

    private Button motifyBtn;
    private ImageView backBtn;
    private EditText userNameEt;
    private EditText passWordEt;
    private EditText emailEt;
    private String GAME_ID;
    private String CLIENT_SECRET;
    private Message msg;
    private LinearLayout layouMotify;
    private boolean isHidePwd = false;// 输入框密码是否是隐藏的，默认为false,睁眼

    private String userNameTxt;
    private String passWrodTxt;
    private String emailTxt;

    //  private Dialog loadingDialog;

    private OnBindAccountListener mOnBindAccountListener;
    private LoginDialog loginDialog;
    private ProgressWheel progressWheel;
    private String deviceId;

    public interface OnBindAccountListener {

        public void notifyLoginDialogShow();

    }

    public BindAccountDialog(Activity activity,OnBindAccountListener onBindAccountListener) {
        this.mOnBindAccountListener = onBindAccountListener;
        this.mActivity = activity;

        initUI();
        initData();
    }
    //xyz
    public BindAccountDialog(Activity activity) {
        this.mActivity = activity;

        initUI();
        initData();
    }

    public void initData() {
        GAME_ID = UgameUtil.getInstance().GAME_ID;
        CLIENT_SECRET = UgameUtil.getInstance().CLIENT_SECRET;
        LogUtil.d("GAME_ID:" + GAME_ID + " CLIENT_SECRET:" + CLIENT_SECRET);
    }


    public void initUI() {
        mDialog = new Dialog(mActivity, MResource.getIdByName(mActivity, "style", "Dialog_Fullscreen"));
        mDialog.getWindow().getAttributes().windowAnimations = MResource.getIdByName(mActivity, "style", "dialogAnim");
        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

//        switch(mActivity.getResources().getConfiguration().orientation){
//            //横屏
//            case Configuration.ORIENTATION_LANDSCAPE:
//                mDialog.setContentView(MResource.getIdByName(mActivity, "layout", "dialog_bindaccount"));
//                LogUtil.k("横屏----1");
//                break;
//            //竖屏
//            case Configuration.ORIENTATION_PORTRAIT:
//                mDialog.setContentView(MResource.getIdByName(mActivity, "layout", "dialog_bindaccount_ver"));
//                LogUtil.k("竖屏----");
//                break;
//            default:
//                mDialog.setContentView(MResource.getIdByName(mActivity, "layout", "dialog_bindaccount"));
//                LogUtil.k("横屏----");
//
//        }
        boolean flag=UgameUtil.getInstance().isVer(mActivity);
        if(flag){
            mDialog.setContentView(MResource.getIdByName(mActivity, "layout", "dialog_bindaccount"));
        }else{
            mDialog.setContentView(MResource.getIdByName(mActivity, "layout", "dialog_bindaccount_ver"));
        }
        //mDialog.setContentView(MResource.getIdByName(mActivity, "layout", "dialog_bindaccount"));
        mDialog.setCancelable(false);

        //  loadingDialog=LoadingDialog.createLoadingDialog(mActivity);
        progressWheel = (ProgressWheel) mDialog.findViewById(MResource.getIdByName(mActivity, "id", "progress_wheel"));

        motifyBtn = (Button) mDialog.findViewById(MResource.getIdByName(mActivity, "id", "btn_bind"));
        backBtn = (ImageView) mDialog.findViewById(MResource.getIdByName(mActivity, "id", "btn_back"));
        userNameEt = (EditText) mDialog.findViewById(MResource.getIdByName(mActivity, "id", "edt_username"));
        passWordEt = (EditText) mDialog.findViewById(MResource.getIdByName(mActivity, "id", "edt_psw"));
        emailEt = (EditText) mDialog.findViewById(MResource.getIdByName(mActivity, "id", "edt_email"));

        layouMotify = (LinearLayout) mDialog.findViewById(MResource.getIdByName(mActivity, "id", "layout_motifyPw"));

        backBtn.setOnClickListener(this);
        motifyBtn.setOnClickListener(this);
        passWordEt.setOnClickListener(this);
        mDialog.show();

    }

    private void parseModifyPwJosn(String josn) {
        if (TextUtils.isEmpty(josn)) {
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
                //成功了就把用户名密码，放到数据库
                SharedPreferences preferences = mActivity.getSharedPreferences("LoginCount", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = preferences.edit();
                edit.putString("zeName", userNameTxt);
                edit.putString("guestpassword", passWrodTxt);
                edit.putString("key", "3");
                edit.commit();
                mOnBindAccountListener.notifyLoginDialogShow();//kong


            } else if ("0".equals(status)) {
                if ("104".equals(code)) {
                    msg.what = 101;
                    handler.sendMessage(msg);
                } else if ("110".equals(code)) {
                    msg.what = 102;
                    handler.sendMessage(msg);
                } else if ("111".equals(code) || "148".equals(code)) {
                    msg.what = 103;
                    handler.sendMessage(msg);
                } else if ("151".equals(code)) {
                    msg.what = 104;
                    handler.sendMessage(msg);
                } else if ("153".equals(code)) {
                    msg.what = 105;
                    handler.sendMessage(msg);

                } else if ("166".equals(code)) {
                    msg.what = 105;
                    handler.sendMessage(msg);
                } else if ("155".equals(code)) {
                    msg.what = 106;
                    handler.sendMessage(msg);

                } else if ("156".equals(code) || "163".equals(code)) {
                    msg.what = 107;
                    handler.sendMessage(msg);
                } else if ("157".equals(code)) {
                    msg.what = 108;
                    handler.sendMessage(msg);
                } else if ("158".equals(code)) {
                    msg.what = 109;
                    handler.sendMessage(msg);
                } else if ("160".equals(code)) {
                    msg.what = 110;
                    handler.sendMessage(msg);
                } else if ("161".equals(code)) {
                    msg.what = 111;
                    handler.sendMessage(msg);
                } else if ("162".equals(code)) {
                    msg.what = 112;
                    handler.sendMessage(msg);
                } else {
                    msg.what = 999;
                    handler.sendMessage(msg);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            closeProgressWheel();
        }
    }
    Handler handler = new Handler() {

        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 100://nousages
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "login_success"), Toast.LENGTH_SHORT).show();
                    break;
                case 101:
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "exsituname"), Toast.LENGTH_SHORT).show();
                    break;
                case 102:
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "incorrectpw"), Toast.LENGTH_SHORT).show();
                    break;
                case 103:
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "notexsituname"), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "sendemailfail"), Toast.LENGTH_SHORT).show();
                    break;
                case 110:
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "invalidpw"), Toast.LENGTH_SHORT).show();
                    break;
                case 111:
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "invaliduname"), Toast.LENGTH_SHORT).show();
                    break;
                case 112:
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "unameinvalid"), Toast.LENGTH_SHORT).show();
                    break;
                case 400:
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "bindsucess"), Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "contactcustomservice"), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public void showHidePw() {

        final Drawable[] drawables = passWordEt.getCompoundDrawables();
        final Drawable drawableEyeClose = mActivity.getResources().getDrawable(com.gigifun.gp.utils.MResource.getIdByName(mActivity, "drawable", "eye"));
        final int eyeWidth = drawables[2].getBounds().width();// 眼睛图标的宽度
        final Drawable drawableEyeOpen = mActivity.getResources().getDrawable(com.gigifun.gp.utils.MResource.getIdByName(mActivity, "drawable", "zhenyan")
        );
        drawableEyeOpen.setBounds(drawables[2].getBounds());//这一步不能省略
        drawableEyeClose.setBounds(drawables[2].getBounds());//这一步不能省略
        passWordEt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    // getWidth,getHeight必须在这里处理
                    float et_pwdMinX = v.getWidth() - eyeWidth - passWordEt.getPaddingRight();
                    float et_pwdMaxX = v.getWidth();
                    float et_pwdMinY = 0;
                    float et_pwdMaxY = v.getHeight();
                    float x = event.getX();
                    float y = event.getY();
                    if (x < et_pwdMaxX && x > et_pwdMinX && y > et_pwdMinY && y < et_pwdMaxY) {
                        // 点击了眼睛图标的位置
                        isHidePwd = !isHidePwd;
                        LogUtil.k(isHidePwd + "");
                        if (isHidePwd) {
                            passWordEt.setCompoundDrawables(
                                    drawables[0],
                                    drawables[1],
                                    drawableEyeClose,
                                    drawables[3]);
                            passWordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        } else {
                            passWordEt.setCompoundDrawables(
                                    drawables[0],
                                    drawables[1],
                                    drawableEyeOpen,
                                    drawables[3]);
                            passWordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        }
                    }
                }
                return false;
            }
        });
    }

    public boolean emailValidation(String email) {
        String regex = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
        return email.matches(regex);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == MResource.getIdByName(mActivity, "id", "edt_psw")) {
            showHidePw();
        } else if (v.getId() == MResource.getIdByName(mActivity, "id", "btn_bind")) {
            userNameTxt = userNameEt.getText().toString().trim();
            passWrodTxt = passWordEt.getText().toString().trim();
            emailTxt = emailEt.getText().toString().trim();

            deviceId = getDeviceId(mActivity);

            if (!"".equals(emailTxt)&&!emailValidation(emailTxt)){
                Toast.makeText(mActivity, MResource.getIdByName(mActivity, "string", "invalidemail"), Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            if (UgameUtil.isNullOrEmpty(userNameTxt)
                    || UgameUtil.isNullOrEmpty(passWrodTxt)) {
                Toast.makeText(mActivity, MResource.getIdByName(mActivity, "string", "content_can_not_be_empty"), Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            if (userNameTxt.length() < 4 || userNameTxt.length() > 20) {
                Toast.makeText(mActivity, MResource.getIdByName(mActivity, "string", "unameinvalid"), Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            if (passWrodTxt.length() < 6 || passWrodTxt.length() > 12) {
                Toast.makeText(mActivity, MResource.getIdByName(mActivity, "string", "invalidpw"), Toast.LENGTH_SHORT)
                        .show();
                return;
            }
            showProgressWheel();
            final Map<String, String> map = new HashMap<String, String>();
            map.put("Ugameid", GAME_ID);
            map.put("Ugamekey", CLIENT_SECRET);
            map.put("Password", aes(passWrodTxt));
            map.put("Username", userNameTxt);
            map.put("Uuid", deviceId);
//            map.put("Uuid", "11115");//kong
            map.put("Email", emailTxt);


            UhttpUtil.post(UgameUtil.getInstance().BINDURL, map, new UcallBack() {

                @Override
                public void onResponse(String response, int arg1) {
                    Log.d("url", UgameUtil.getInstance().BINDURL + map.toString());
                    closeProgressWheel();
                    //解析response,根据返回吗，吐司相应错误
                    parseModifyPwJosn(response);
                }

                @Override
                public void onError(Call arg0, Exception arg1, int arg2) {

                    closeProgressWheel();
                    LogUtil.d("Reset error :" + arg1);
                    Toast.makeText(mActivity, MResource.getIdByName(mActivity, "string", "network_error"), Toast.LENGTH_SHORT)
                            .show();
                }
            });


        } else if (v.getId() == MResource.getIdByName(mActivity, "id", "btn_back")) {
            mDialog.dismiss();
            if (null != mOnBindAccountListener){
                mOnBindAccountListener.notifyLoginDialogShow();
            }

        }
    }

    public static String getDeviceId(Context context) {
        String id;
        //android.telephony.TelephonyManager
        TelephonyManager mTelephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (mTelephony.getDeviceId() != null) {
            id = mTelephony.getDeviceId();
        } else {
            //android.provider.Settings;
            id = Settings.Secure.getString(context.getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return id;
    }
    private String aes(String coorderid) {

        try {
            AESEncode aes = new AESEncode();
            return aes.encrypt(coorderid);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

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
