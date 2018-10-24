package com.gigifun.gp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.share.widget.AppInviteDialog;
import com.gigifun.gp.UgameSDK;
import com.gigifun.gp.adapter.InviteGiftNewAdapter;
import com.gigifun.gp.listener.IFbRequest;
import com.gigifun.gp.utils.UcallBack;
import com.gigifun.gp.utils.UgameUtil;
import com.gigifun.gp.utils.UhttpUtil;
import com.gigifun.gp.utils.LogUtil;
import com.gigifun.gp.utils.MResource;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.widget.GameRequestDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;

/**
 * Created by tim on 16/9/3.
 */
public class UIInviteLayout extends LinearLayout implements View.OnClickListener{

    private static Activity mActivity;
    private static InviteHandler handler;
    private LinearLayout layoutList;
    private ListView inviteListView;
    private InviteGiftNewAdapter adapter;
    private static String serverid;
    private static String roleid;
    private static String sdkUid;
    private ProgressBar inviteProgressBar;
    private TextView tvInviteFriends;
    private TextView imgDirection;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private static final int LOAD_INVITE_DATA=1;
    private static final int DIALOG_DISMISS=2;
    private static final int NO_ACTIVITY=3;
    public  static final int SAVE_INVITE_FRIENDS=111;
    private static final int SAVE_INVITE_SUCCESS=7;
    private static HashMap<String,String> inviteMap;
    private static boolean isInvite;
    private static boolean saveInvite;
    private int titleWidthParams;
    private static String direction;
    private GameRequestDialog requestDialog;
    private AppInviteDialog appInviteDialog;
    private List<String> valueId;
    private static IFbRequest mIfbshareReq;

    public static void setCallBace(IFbRequest iFbRequest){
        mIfbshareReq = iFbRequest;
    }

    public static void doCallBackMethods(){
        requestData(null);
    }
    public UIInviteLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public UIInviteLayout(Activity activity,String serverid,String roleid,String sdkuid,int titleWidthParams){
        super(activity);
        this.mActivity=activity;
        this.titleWidthParams=titleWidthParams;
        this.serverid=serverid;
        this.roleid=roleid;
        this.sdkUid=sdkuid;
        handler=new InviteHandler();
        initUI();
        initFBInvite();
        initImageLoader();
//        requestData(null);

    }


    public void initFBInvite(){
        requestDialog = new GameRequestDialog(mActivity);
        requestDialog.registerCallback(UgameSDK.callbackManager,facebookCallback);

        appInviteDialog = new AppInviteDialog(mActivity);
        appInviteDialog.registerCallback(UgameSDK.callbackManager,facebookCallback);
    }

    public void initUI(){
        View v = mActivity.getLayoutInflater().inflate(
                com.facebook.MResource.getIdByName(mActivity,"layout","layout_invite_gift"),null);
        addView(v, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));


        inviteListView=(ListView)findViewById(MResource.getIdByName(mActivity, "id", "invite_listview"));
        layoutList = (LinearLayout) findViewById(MResource.getIdByName(mActivity, "id", "layout_list"));
       // layoutList.setLayoutParams(new LinearLayout.LayoutParams(titleWidthParams, (int)(titleWidthParams/1.875)));
        layoutList.setBackgroundColor(Color.parseColor("#141e23"));

        inviteProgressBar = (ProgressBar) findViewById(MResource.getIdByName(mActivity, "id", "invite_progressbar"));
        tvInviteFriends = (TextView) findViewById(MResource.getIdByName(mActivity, "id", "tv_invite_friends"));
        imgDirection=(TextView)findViewById(MResource.getIdByName(mActivity, "id", "img_activite_direction"));


        imgDirection.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if(v.getId() == com.facebook.MResource.getIdByName(mActivity, "id", "img_activite_direction")){
                new DirectionDialog(mActivity,direction);
        }
    }



    public FacebookCallback facebookCallback=new FacebookCallback<GameRequestDialog.Result>() {
        public void onSuccess(GameRequestDialog.Result result) {
            String requestId = result.getRequestId();
            valueId=result.getRequestRecipients();
            LogUtil.k("邀请返回==="+result.toString());
            LogUtil.k("邀请返回requestId==="+requestId);
            LogUtil.k("邀请返回valueId==="+valueId);

            if (requestId != null) {

                if(null==valueId || valueId.size()<=0){
                    return;
                }
                StringBuffer buffer=new StringBuffer();
                for(String fbid:valueId){
                    buffer.append(fbid).append("|");
                }
                buffer.deleteCharAt(buffer.length()-1);
                Message msg=handler.obtainMessage();
                msg.what= SAVE_INVITE_FRIENDS;
                msg.obj=buffer.toString();
                handler.sendMessage(msg);
            }
        }
    public void onCancel() {
        Toast.makeText(mActivity,"cancel",Toast.LENGTH_SHORT).show();
    }
    public void onError(FacebookException error) {
        LogUtil.e("facebook Error ===" + error);
    }
};
    public FacebookCallback facebookCallback1=new FacebookCallback<AppInviteDialog.Result>() {
        public void onSuccess(AppInviteDialog.Result result) {
            String resoultData = result.getData().toString();

            LogUtil.e("resoultData==="+resoultData);
//            valueId=result.getRequestRecipients();
//            if (requestId != null) {
//
//                if(null==valueId || valueId.size()<=0){
//                    return;
//                }
//                StringBuffer buffer=new StringBuffer();
//                for(String fbid:valueId){
//                    buffer.append(fbid).append("|");
//                }
//                buffer.deleteCharAt(buffer.length()-1);
//                Message msg=handler.obtainMessage();
//                msg.what= SAVE_INVITE_FRIENDS;
//                msg.obj=buffer.toString();
//                handler.sendMessage(msg);
//            }
        }
        public void onCancel() {}
        public void onError(FacebookException error) {}
    };
    private static void requestData(String fbId) {

       // dialog.show();

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("Ugameid", UgameUtil.getInstance().GAME_ID);
        map.put("Ugamekey", UgameUtil.getInstance().CLIENT_SECRET);
        map.put("Uid", sdkUid);
        map.put("Serverid", serverid);
        map.put("Roleid", roleid);

//        if(isInvite && null!=inviteMap && !inviteMap.isEmpty() && null!=fbId){
//            map.put("saveinvite", "1");
//            map.put("Activeid", inviteMap.get("id"));
//            map.put("Invitefbid", fbId);
//        }

        UhttpUtil.post(UgameUtil.getInstance().FACEBOOK_INVITE, map, new UcallBack() {

            @Override
            public void onResponse(String response, int arg1) {
                // TODO Auto-generated method stub
                parseResult(response);
            }

            @Override
            public void onError(Call arg0, Exception arg1, int arg2) {
                Toast.makeText(mActivity, MResource.getIdByName(mActivity, "string", "network_error"), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void initImageLoader() {
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()

                .cacheInMemory(true).cacheOnDisc(true)
                .considerExifParams(true)
                //.displayer(new RoundedBitmapDisplayer(20))
                .build();
    }






    public class InviteHandler extends Handler {

        private String inviteFBId;

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DIALOG_DISMISS:
                    if(null!=msg.obj){
                        switchCode((String)msg.obj);
                    }
                    break;
                case NO_ACTIVITY:
                    if(isInvite && !saveInvite){

                      Toast.makeText(mActivity, MResource.getIdByName(mActivity, "string", "friend_has_been_invited"), Toast.LENGTH_SHORT).show();

                    }else{

                            Toast.makeText(mActivity, MResource.getIdByName(mActivity, "string", "has_no_invite_friend_event"), Toast.LENGTH_SHORT).show();

                    }
                    break;
                case LOAD_INVITE_DATA:
                    if(null!=inviteMap&&!inviteMap.isEmpty()){

                        tvInviteFriends.setText(mActivity.getString(
                            MResource.getIdByName(mActivity, "string", "invite_text"))+"("+inviteMap.get("invitenum")+"/"+inviteMap.get("maxinvitenum")+")"+
                                mActivity.getString(MResource.getIdByName(mActivity, "string", "invite_friend_text")));
                    }

                    inviteProgressBar.setMax(Integer.valueOf(inviteMap.get("maxinvitenum")));
                    inviteProgressBar.setProgress(Integer.valueOf(inviteMap.get("invitenum")));
                    List<HashMap<String,String>> list=(List<HashMap<String, String>>) msg.obj;
                    if(null==adapter){
                        adapter=new InviteGiftNewAdapter(mActivity, list, imageLoader, options,handler,requestDialog,appInviteDialog);
                        inviteListView.setAdapter(adapter);
                    }else{

                        adapter.setData(list);
                    }

                    break;
                //保存邀请用户
                case SAVE_INVITE_FRIENDS:
                    isInvite=true;
                    inviteFBId = (String) msg.obj;
                    LogUtil.d("邀请好友的id: "+ inviteFBId);
                    //requestData(inviteFBId);
                    requestGift(inviteFBId);
                    break;
                case SAVE_INVITE_SUCCESS:
                    saveInvite=true;

                    int maxNum=Integer.valueOf(msg.getData().getString("maxinvitenum"));
                    int num=Integer.valueOf(msg.getData().getString("invitenum"));

                    if(num>=maxNum){
                        SharedPreferences preferences=mActivity.getSharedPreferences("MyCount", Context.MODE_PRIVATE);
                        int quantity=preferences.getInt("quantity", -1);
                        quantity--;

                        SharedPreferences.Editor editor=preferences.edit();
                        editor.putInt("quantity", quantity);
                        editor.commit();
                    }

                    LogUtil.d("保存成功");
//                    requestData(null);
                    requestGift(inviteFBId);
                    break;
            }
        }

    }

    private void requestGift(String inviteFbId) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("Ugameid", UgameUtil.getInstance().GAME_ID);
        map.put("Ugamekey", UgameUtil.getInstance().CLIENT_SECRET);
        map.put("Uid", sdkUid);
        map.put("Serverid", serverid);
        map.put("Roleid", roleid);
        map.put("Invitefbid", inviteFbId);
        map.put("Activeid", inviteMap.get("id"));



        UhttpUtil.post(UgameUtil.getInstance().FACEBOOK_INVITE_GIFT, map, new UcallBack() {

            @Override
            public void onResponse(String response, int arg1) {
                // TODO Auto-generated method stub
              LogUtil.k("邀请礼包="+response);
                parseResult(response);
            }

            @Override
            public void onError(Call arg0, Exception arg1, int arg2) {
                Toast.makeText(mActivity, MResource.getIdByName(mActivity, "string", "network_error"), Toast.LENGTH_SHORT).show();
                    Log.d("邀请返回错误",arg1.toString());
            }
        });

    }

    private static void parseResult(String result){
        if(TextUtils.isEmpty(result)){
            return;
        }

        try {
            JSONObject json=new JSONObject(result);
            String status=json.optString("Status");
            String code=json.optString("Code");
            if("2".equals(status)){
                Message msg=handler.obtainMessage();
                msg.obj=code;
                msg.what=DIALOG_DISMISS;
                handler.sendMessage(msg);
            }else if("0".equals(status)){
                handler.sendEmptyMessage(NO_ACTIVITY);
            }else if("1".equals(status)){
                if(isInvite && !saveInvite){
                    String maxNum=json.optString("maxinvitenum");
                    String num=json.optString("invitenum");
                    Message msg=handler.obtainMessage();
                    msg.what=SAVE_INVITE_SUCCESS;
                    Bundle bundle=new Bundle();
                    bundle.putString("maxinvitenum", maxNum);
                    bundle.putString("invitenum", num);
                    msg.setData(bundle)	;
                    handler.sendMessage(msg);
                    return;
                }

                HashMap<String,String> map=new HashMap<String,String>();
                inviteMap=new HashMap<String,String>();
                inviteMap.put("id", json.optString("id"));
                inviteMap.put("explain", json.optString("explain"));
                direction=json.optString("explain");
                inviteMap.put("maxinvitenum", json.optString("maxinvitenum"));
                inviteMap.put("invitenum", json.optString("invitenum"));
                inviteMap.put("invitelang",json.optString("invitelang"));

                map.put("inviteUrl",json.optString("inviteLink"));
                map.put("invitePic",json.optString("inviteImage"));

                List<HashMap<String,String>> list=new ArrayList<HashMap<String,String>>();
                JSONArray jsonArray=json.optJSONArray("Data");
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jo=jsonArray.optJSONObject(i);
                    HashMap<String,String> mapList=new HashMap<String,String>();
                    mapList.put("targetnum", jo.optString("targetnum"));
                    mapList.put("packcomtent", jo.optString("packcomtent"));
                    mapList.put("targetlogo", jo.optString("targetlogo"));
                    mapList.put("complete", jo.optString("complete"));
                    mapList.put("invitelang", json.optString("invitelang"));

                    list.add(mapList);
                }
              //  LogUtil.w("share"+list);
                Message msg=handler.obtainMessage();
                msg.obj=list;
                msg.what=LOAD_INVITE_DATA;
                handler.sendMessage(msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void switchCode(String code){
        if(TextUtils.isEmpty(code)){
            return;
        }

//        Toast.makeText(mActivity, code+"", Toast.LENGTH_LONG).show();
       if ("128".equals(code)){
            Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "rewardsend"), Toast.LENGTH_SHORT).show();
        }
    	/*if("401".equals(code)){
    		Toast.makeText(this, "Client_id 没有传", Toast.LENGTH_LONG).show();
    	}else if("402".equals(code)){
    		Toast.makeText(this, "Client_secret 没有传", Toast.LENGTH_LONG).show();
    	}else if("403".equals(code)){
    		Toast.makeText(this, "Client_id与Client_secret不匹配", Toast.LENGTH_LONG).show();
    	}else if("404".equals(code)){
    		Toast.makeText(this, "用户的id没有传过来 (uid)", Toast.LENGTH_LONG).show();
    	}else if("405".equals(code)){
    		Toast.makeText(this, "缺少活动的id acitveid", Toast.LENGTH_LONG).show();
    	}else if("406".equals(code)){
    		Toast.makeText(this, "缺少邀请好友的id invitefbid", Toast.LENGTH_LONG).show();
    	}else if("407".equals(code)){
    		Toast.makeText(this, "saveinvite的值不为1", Toast.LENGTH_LONG).show();
    	}else if("408".equals(code)){
    		Toast.makeText(this, "serverid没有收到", Toast.LENGTH_LONG).show();
    	}else if("409".equals(code)){
    		Toast.makeText(this, "roleid没有收到", Toast.LENGTH_LONG).show();
    	}else if("410".equals(code)){
    		Toast.makeText(this, "系统错误", Toast.LENGTH_LONG).show();
    	}*/
    }

}
