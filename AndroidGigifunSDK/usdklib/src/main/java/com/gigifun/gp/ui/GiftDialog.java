package com.gigifun.gp.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gigifun.gp.adapter.GiftPagerAdapter;
import com.gigifun.gp.listener.IFbRequest;
import com.gigifun.gp.service.FloatViewService;
import com.gigifun.gp.utils.LogUtil;
import com.gigifun.gp.utils.UcallBack;
import com.gigifun.gp.utils.UgameUtil;
import com.gigifun.gp.utils.UhttpUtil;
import com.gigifun.gp.utils.MResource;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;

/**
 * Created by tim on 16/9/2.
 */
public class GiftDialog implements OnClickListener,IFbRequest {

    private Activity mActivity;
    private Dialog mDialog;
    private GiftPagerAdapter giftPagerAdapter;


    private ViewPager mViewPager;
    private LinearLayout giftTitle;

    private RelativeLayout btnLike;
    private RelativeLayout btnShare;
    private RelativeLayout btnInvite;
    private TextView tvInvite;
    private TextView tvShare;
    private TextView tvLike;

    private Button btnCancel;
    private ImageView imgNewShare,imgNewInvite,imgNewLike;
    private boolean isRuning;

    private String sdkuid;
    private String serverid;
    private String roleid;
    private String direction;
    private String likeLink;
    private SharedPreferences preferences;
    List<HashMap<String,String>> likeList;
    private boolean SHAREDIALOGSTATE = true;
    private boolean INVITEDIALOGSTATE = true;

    FloatViewService mFloatViewService;

    public  GiftDialog(Activity activity,String serverid,FloatViewService floatViewService){
        this.mFloatViewService = floatViewService;
        this.mActivity=activity;
        this.serverid=serverid;
//        this.roleid=roleid;
//        this.sdkuid=sdkuid;
         preferences = activity.getSharedPreferences("LoginCount", Context.MODE_PRIVATE);
        sdkuid  =preferences.getString("paysdkUid","");
        roleid  =preferences.getString("payroleId","");
        initUI();
//        requestData();
    }

    //设置字体繁简
    private static void changeLang(Context context) {

        String languageToLoad = context.getResources().getString(MResource.getIdByName(context, "string", "lang"));
        Locale locale = null;
        if ("EN".equals(languageToLoad)) {
            locale = new Locale("en");
            LogUtil.k("字体改为英文");
        }else if("TH".equals(languageToLoad)) {
            locale = new Locale("th");
            LogUtil.k("字体改为泰文");
        } else {
            locale = new Locale("zh", languageToLoad);
            LogUtil.k("字体改为" + languageToLoad);
        }
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, null);
    }

    public void initUI(){

       // changeLang(mActivity);
        UgameUtil.getInstance().changeLang(mActivity);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("floatStatus", "1");
        edit.commit();

        mDialog = new Dialog(mActivity, MResource.getIdByName(mActivity, "style", "custom_dialog"));
        mDialog.getWindow().getAttributes().windowAnimations = MResource.getIdByName(mActivity, "style", "dialogAnim");
        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(MResource.getIdByName(mActivity, "layout", "dialog_gift"));
        mDialog.setCancelable(true);

        imgNewLike=(ImageView)mDialog.findViewById(MResource.getIdByName(mActivity, "id", "img_new_like"));
        imgNewShare=(ImageView)mDialog.findViewById(MResource.getIdByName(mActivity, "id", "img_new_share"));
        imgNewInvite=(ImageView)mDialog.findViewById(MResource.getIdByName(mActivity, "id", "img_new_invite"));

        imgNewShare.setVisibility(View.VISIBLE);
        imgNewInvite.setVisibility(View.VISIBLE);

        giftTitle=(LinearLayout)mDialog.findViewById(MResource.getIdByName(mActivity, "id", "gift_title"));
        mViewPager=(ViewPager) mDialog.findViewById(MResource.getIdByName(mActivity, "id", "viewpager_gift"));
        btnLike=(RelativeLayout)mDialog.findViewById(MResource.getIdByName(mActivity, "id", "btn_like"));
        btnShare=(RelativeLayout)mDialog.findViewById(MResource.getIdByName(mActivity, "id", "btn_share"));
        btnInvite=(RelativeLayout)mDialog.findViewById(MResource.getIdByName(mActivity, "id", "btn_invite"));
        tvInvite=(TextView)mDialog.findViewById(MResource.getIdByName(mActivity, "id", "select_invate"));
        tvShare=(TextView)mDialog.findViewById(MResource.getIdByName(mActivity, "id", "select_share"));
        tvLike=(TextView)mDialog.findViewById(MResource.getIdByName(mActivity, "id", "select_like"));

        btnCancel=(Button)mDialog.findViewById(MResource.getIdByName(mActivity, "id", "btn_cancel"));

        giftPagerAdapter=new GiftPagerAdapter(mActivity);

        btnLike.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        btnInvite.setOnClickListener(this);
        //设置文字变色
        tvInvite.setFocusable(true);
        tvInvite.setFocusableInTouchMode(true);
        tvInvite.requestFocus();
        tvInvite.requestFocusFromTouch();

        tvShare.setFocusable(true);
        tvShare.setFocusableInTouchMode(true);
        tvShare.requestFocus();
        tvShare.requestFocusFromTouch();

        tvLike.setFocusable(true);
        tvLike.setFocusableInTouchMode(true);
        tvLike.requestFocus();
        tvLike.requestFocusFromTouch();

        btnCancel.setOnClickListener(this);

        giftTitle.post(new Runnable() {
            @Override
            public void run() {
                giftPagerAdapter.add(new UILikeLayout(mActivity,serverid,roleid,sdkuid,giftTitle.getWidth()));
                giftPagerAdapter.add(new UIShareLayout(mActivity,serverid,roleid,sdkuid,giftTitle.getWidth()));
                giftPagerAdapter.add(new UIInviteLayout(mActivity,serverid,roleid,sdkuid,giftTitle.getWidth()));

                mViewPager.setAdapter(giftPagerAdapter);
            }
        });
        mDialog.show();
    }

    @Override
    public void onClick(View v) {

        if (v.getId()==MResource.getIdByName(mActivity, "id", "btn_like")){

            mViewPager.setCurrentItem(0);

            btnLike.setBackgroundResource(MResource.getIdByName(mActivity, "drawable", "gift_click"));
            btnShare.setBackgroundResource(MResource.getIdByName(mActivity, "drawable", "gift_normal"));
            btnInvite.setBackgroundResource(MResource.getIdByName(mActivity, "drawable", "gift_normal"));
            tvLike.setTextColor(Color.parseColor("#fac600"));
            tvShare.setTextColor(Color.parseColor("#9f9f9f"));
            tvInvite.setTextColor(Color.parseColor("#9f9f9f"));

        }else if (v.getId()==MResource.getIdByName(mActivity, "id", "btn_share")){
            if (SHAREDIALOGSTATE){
                UIShareLayout.setCallBace(this);
                UIShareLayout.doCallBackMethods();
                SHAREDIALOGSTATE = false;
            }
            mViewPager.setCurrentItem(1);
            btnLike.setBackgroundResource(MResource.getIdByName(mActivity, "drawable", "gift_normal"));
            btnShare.setBackgroundResource(MResource.getIdByName(mActivity, "drawable", "gift_click"));
            btnInvite.setBackgroundResource(MResource.getIdByName(mActivity, "drawable", "gift_normal"));
            tvLike.setTextColor(Color.parseColor("#9f9f9f"));
            tvShare.setTextColor(Color.parseColor("#fac600"));
            tvInvite.setTextColor(Color.parseColor("#9f9f9f"));
            imgNewShare.setVisibility(View.INVISIBLE);

        }else if (v.getId()==MResource.getIdByName(mActivity, "id", "btn_invite")){
            if (INVITEDIALOGSTATE){
                UIInviteLayout.setCallBace(this);
                UIInviteLayout.doCallBackMethods();
                INVITEDIALOGSTATE = false;
            }
            mViewPager.setCurrentItem(2);
            btnLike.setBackgroundResource(MResource.getIdByName(mActivity, "drawable", "gift_normal"));
            btnShare.setBackgroundResource(MResource.getIdByName(mActivity, "drawable", "gift_normal"));
            btnInvite.setBackgroundResource(MResource.getIdByName(mActivity, "drawable", "gift_click"));
            tvLike.setTextColor(Color.parseColor("#9f9f9f"));
            tvShare.setTextColor(Color.parseColor("#9f9f9f"));
            tvInvite.setTextColor(Color.parseColor("#fac600"));
            imgNewInvite.setVisibility(View.INVISIBLE);

        }else if(v.getId()==MResource.getIdByName(mActivity, "id", "btn_cancel")){

            mDialog.dismiss();
            SHAREDIALOGSTATE = true;
            INVITEDIALOGSTATE = true;
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString("floatStatus", "0");
            edit.commit();
            mFloatViewService.showFloat();

        }
    }

    private void requestData() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("Ugameid", UgameUtil.getInstance().GAME_ID);
        map.put("Ugamekey ", UgameUtil.getInstance().CLIENT_SECRET);
        map.put("Uid", sdkuid);
        map.put("Serverid", serverid);
        map.put("Roleid", roleid);

        UhttpUtil.post(UgameUtil.getInstance().FACEBOOK_LIKE, map, new UcallBack() {

            @Override
            public void onResponse(String response, int arg1) {
                parseResult(response);

            }

            @Override
            public void onError(Call arg0, Exception arg1, int arg2) {


            }
        });

    }

    private void parseResult(String result){
        if(TextUtils.isEmpty(result)){

            return;
        }
        try {
            JSONObject json=new JSONObject(result);
            String status=json.optString("Status");
            String code=json.optString("Code");
            String invite=json.optString("Invite");
            String share=json.optString("Share");
            direction=json.optString("Explain");
            likeLink=json.optString("Likelink");
            if("0".equals(status)){

            }else if("1".equals(status)){
                if(!TextUtils.isEmpty(invite)){
                    if(invite.equals("2") || invite.equals("3")){
                        imgNewInvite.setVisibility(View.VISIBLE);
                    }
                }
                if(!TextUtils.isEmpty(share)){
                    if(share.equals("2")){
                        imgNewShare.setVisibility(View.VISIBLE);
                    }
                }
                if(!preferences.getBoolean("isLiked", false)){
                    imgNewLike.setVisibility(View.VISIBLE);
                }

            }else if("2".equals(status)){
                if(!TextUtils.isEmpty(invite)){
                    if(invite.equals("2") || invite.equals("3")){
                        imgNewInvite.setVisibility(View.VISIBLE);
                    }
                }
                if(!TextUtils.isEmpty(share)){
                    if(share.equals("2")){
                        imgNewShare.setVisibility(View.VISIBLE);
                    }
                }
                if(!preferences.getBoolean("isLiked", false)){
                    imgNewLike.setVisibility(View.VISIBLE);
                }

            }
        } catch (JSONException e) {

            e.printStackTrace();
        }
    }

    @Override
    public void request() {

    }
}





