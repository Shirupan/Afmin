package com.gigifun.gp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gigifun.gp.UgameSDK;
import com.gigifun.gp.listener.IFbRequest;
import com.gigifun.gp.utils.ButtonUtil;
import com.gigifun.gp.utils.UcallBack;
import com.gigifun.gp.utils.UgameUtil;
import com.gigifun.gp.utils.UhttpUtil;
import com.gigifun.gp.utils.LogUtil;
import com.facebook.AccessToken;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.MResource;
import com.facebook.share.ShareApi;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import okhttp3.Call;

/**
 * @author hoangcaomobile
 * @since March 24, 2015
 */
public class UIShareLayout extends LinearLayout implements OnClickListener {

    private static Activity mActivity;
    private int titleWidthParams;
    private static final String PERMISSION = "publish_actions";
    private Button postStatusUpdateButton;
    private ImageView imgShareGift;
    private TextView imgDirection;
    private PendingAction pendingAction = PendingAction.NONE;
    static String serverid;
    static String roleid;
    static String sdkuid;
    static String direction;

    DisplayImageOptions options;
    ImageLoader imageLoader;
    //Dialog dialog;

    private static boolean hasShared;
    private boolean tag;
    private static ShareHandler handler;

    private static final int LOAD_LIKE_DATA = 1;
    private static final int DIALOG_DISMISS = 2;
    private static final int NO_ACTIVE = 3;// 当前没有活动
    private static final int NOT_SENDED = 6;
    private static final int HAS_SENDED = 7;
    private final int GIFT_SENDED = 8;
    private final int GIFT_NOT_SENDED = 9;
    static HashMap<String, String> map;
    private ShareDialog shareWebDialog;
    private boolean canPresentShareWebDialog;

    private static IFbRequest mIfbshareReq;
    public static void setCallBace(IFbRequest iFbRequest){
        mIfbshareReq = iFbRequest;
    }

    public static void doCallBackMethods(){
        requestData(null);
    }

    private enum PendingAction {
        NONE, POST_PHOTO, POST_STATUS_UPDATE
    }

    public UIShareLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UIShareLayout(Activity activity, String serverid, String roleid, String sdkuid, int titleWidthParams) {
        super(activity);
        mActivity = activity;
        this.serverid = serverid;
        this.roleid = roleid;
        this.sdkuid = sdkuid;
        this.titleWidthParams = titleWidthParams;
        initUI();
        handler = new ShareHandler();
        initImageLoader(mActivity);
//        requestData(null);
    }


    private void initUI() {

        View v = mActivity.getLayoutInflater().inflate(
                MResource.getIdByName(mActivity, "layout", "layout_share_gift"), null);
        addView(v, new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));

        imgShareGift = (ImageView) findViewById(MResource.getIdByName(mActivity,
                "id", "img_share_gift"));
        imgShareGift.setBackgroundColor(Color.parseColor("#141e23"));
        //imgShareGift.setLayoutParams(new LayoutParams(titleWidthParams, (int)(titleWidthParams/1.875)));
        postStatusUpdateButton = (Button) findViewById(MResource.getIdByName(mActivity, "id", "img_gift_has_sended"));

        imgDirection = (TextView) findViewById(MResource.getIdByName(mActivity, "id", "img_activite_direction"));
        imgDirection.setOnClickListener(this);
        postStatusUpdateButton.setOnClickListener(this);


        //fb init
        shareWebDialog = new ShareDialog(mActivity);
        shareWebDialog.registerCallback(UgameSDK.callbackManager, shareCallback);
        canPresentShareWebDialog = ShareDialog.canShow(ShareLinkContent.class);

    }


    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {
            Toast.makeText(mActivity.getApplicationContext(), "Publish cancelled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(FacebookException error) {
            Toast.makeText(mActivity.getApplicationContext(), "Error posting story", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSuccess(Sharer.Result result) {
            if (result != null) {
                updateUI();
                LogUtil.e("分享成功！！,result.getPostId()=" + result.getPostId());
            }
        }
    };


    @Override
    public void onClick(View v) {

        if (v.getId() == MResource.getIdByName(mActivity, "id", "img_activite_direction")) {
            new DirectionDialog(mActivity, direction);
        } else if (v.getId() == MResource.getIdByName(mActivity, "id", "img_gift_has_sended")) {
            if (ButtonUtil.isFastDoubleClick(v.getId())) {
                return;
            }
            onClickPostStatusUpdate();
        }

    }


    public void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs()
                .build();
        ImageLoader.getInstance().init(config);
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder().cacheInMemory(true)
                .cacheOnDisc(true).considerExifParams(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
                .build();
    }


    private static void requestData(String sharedTag) {

        HashMap<String, String> map = new HashMap<String, String>();
        map.put("Ugameid", UgameUtil.getInstance().GAME_ID);
        map.put("Ugamekey", UgameUtil.getInstance().CLIENT_SECRET);
        if (null != sharedTag && hasShared) {
            map.put("sendpack", sharedTag);
            map.put("shareid", map.get("id"));
        }
        map.put("Uid", sdkuid);
        map.put("Serverid", serverid);
        map.put("Roleid", roleid);

        UhttpUtil.post(UgameUtil.getInstance().FACEBOOK_SHARE, map, new UcallBack() {

            @Override
            public void onResponse(String response, int arg1) {
                parseResult(response);
            }

            @Override
            public void onError(Call arg0, Exception arg1, int arg2) {
                Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "network_error"), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }


    class ShareHandler extends Handler {

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DIALOG_DISMISS:
                    switchCode((String) msg.obj);
                    break;
                case NOT_SENDED:
                    // 未分享
                    postStatusUpdateButton.setBackgroundResource(com.gigifun.gp.utils.MResource.getIdByName(
                            mActivity, "drawable", "gift_immediate_share"));
                    postStatusUpdateButton.setText(com.gigifun.gp.utils.MResource.getIdByName(
                            mActivity, "string", "share_gift_send"));

                    break;
                case HAS_SENDED:
                    tag = true;
                    //已分享
                    postStatusUpdateButton.setBackgroundResource(com.gigifun.gp.utils.MResource.getIdByName(
                            mActivity, "drawable",
                            "gift_has_sended"));
                    postStatusUpdateButton.setText(com.gigifun.gp.utils.MResource.getIdByName(
                            mActivity, "string",
                            "share_gift_hadsend"));

                    break;
                case LOAD_LIKE_DATA:
                    HashMap<String, String> map = (HashMap<String, String>) msg.obj;
                    imageLoader.displayImage(map.get("activeImage"), imgShareGift,
                            options);
                    break;
                case NO_ACTIVE:// 当前无活动
                    disMissDialog();
                    if (!hasShared) {
                        Toast.makeText(mActivity, MResource.getIdByName(mActivity, "string", "has_no_share_event"),
                                Toast.LENGTH_LONG).show();
                    } else {
                        //已分享
                        postStatusUpdateButton.setBackgroundResource(MResource.getIdByName(
                                mActivity, "drawable", "gift_has_sended"));
                        postStatusUpdateButton.setText(MResource.getIdByName(
                                mActivity, "string", "share_gift_hadsend"));
                    }
                    break;
                case GIFT_SENDED:
                    disMissDialog();
                    //已分享
                    postStatusUpdateButton.setBackgroundResource(MResource.getIdByName(
                            mActivity, "drawable", "gift_has_sended"));
                    postStatusUpdateButton.setText(MResource.getIdByName(
                            mActivity, "string", "share_gift_hadsend"));
                    break;

            }
        }

    }

    private void parseGiftResult(String response) {
        try {
            JSONObject json = new JSONObject(response);
            String status = json.optString("Status");
            String code = json.optString("Code");
            handler.sendEmptyMessage(GIFT_SENDED);

            if ("0".equals(status)) {

                if ("128".equals(code)) {
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "has_received_reward"), Toast.LENGTH_SHORT).show();
                }
                if ("129".equals(code)) {
                    Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "send_reward_failed"), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private static void parseResult(String result) {
        try {
            JSONObject json = new JSONObject(result);
            String status = json.optString("Status");
            String code = json.optString("Code");
            if ("2".equals(status)) {
                Message msg = handler.obtainMessage();
                msg.obj = code;
                msg.what = DIALOG_DISMISS;
                handler.sendMessage(msg);
            } else if ("0".equals(status)) {
                handler.sendEmptyMessage(NO_ACTIVE);
            } else if ("1".equals(status)) {
                map = new HashMap<String, String>();
                map.put("title", json.optString("title"));
                map.put("Subtitle", json.optString("Subtitle"));
                map.put("id", json.optString("id"));
                map.put("Comtent", json.optString("Comtent"));
                map.put("shareLink", json.optString("shareLink"));
                map.put("shareImage", json.optString("shareImage"));
                map.put("activeImage", json.optString("activeImage"));
                map.put("isend", json.optString("isend"));

                direction = json.optString("shareExplain");

                if (!TextUtils.isEmpty(json.optString("isend"))) {
                    if ("0".equals(json.optString("isend"))) {// 未发送
                        handler.sendEmptyMessage(NOT_SENDED);
                    } else if ("1".equals(json.optString("isend"))) {// 已发送
                        handler.sendEmptyMessage(HAS_SENDED);
                    }
                }

                Message msg = handler.obtainMessage();
                msg.obj = map;
                msg.what = LOAD_LIKE_DATA;
                handler.sendMessage(msg);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void switchCode(String code) {
        if (TextUtils.isEmpty(code)) {
            return;
        }

//		Toast.makeText(mActivity, code, Toast.LENGTH_LONG).show();
        if ("128".equals(code)) {
            Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "rewardsend"), Toast.LENGTH_SHORT).show();
        }
    }


    private void updateUI() {

        if (!tag) {
            // 通知游戏把数量-1

            SharedPreferences preferences = mActivity.getSharedPreferences("MyCount",
                    Context.MODE_PRIVATE);
            int quantity = preferences.getInt("quantity", -1);
            quantity--;

            //USDKCaller.setQuantity(String.valueOf(quantity), "");

            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("quantity", quantity);
            editor.commit();
        }

        hasShared = true;
        //requestData("1");
        sendPresent("1");
    }

    //发送礼品
    private void sendPresent(String sharedTag) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("Ugameid", UgameUtil.getInstance().GAME_ID);
        map.put("Ugamekey", UgameUtil.getInstance().CLIENT_SECRET);
        if (null != sharedTag && hasShared) {
            //map.put("sendpack", sharedTag);//
            map.put("Shareid", this.map.get("id"));
        }
//		map.put("Shareid", this.map.get("id"));
        map.put("Uid", sdkuid);
        map.put("Serverid", serverid);
        map.put("Roleid", roleid);

        UhttpUtil.post(UgameUtil.getInstance().FACEBOOK_SEND_GIFT, map, new UcallBack() {

            @Override
            public void onResponse(String response, int arg1) {
                // TODO Auto-generated method stub
                parseGiftResult(response);
                LogUtil.k("sendgift=respone=" + response);
            }

            @Override
            public void onError(Call arg0, Exception arg1, int arg2) {
                Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "network_error"), Toast.LENGTH_SHORT)
                        .show();


            }
        });

    }


    private void disMissDialog() {
    }

    private void handlePendingAction() {
        PendingAction previouslyPendingAction = pendingAction;

        pendingAction = PendingAction.NONE;

        LogUtil.d("previouslyPendingAction" + previouslyPendingAction);

        switch (previouslyPendingAction) {
            case POST_STATUS_UPDATE:
                postStatusUpdate();
                break;
        }
    }

    private void onClickPostStatusUpdate() {
        performPublish(PendingAction.POST_STATUS_UPDATE, canPresentShareWebDialog);
    }

    private void performPublish(PendingAction action, boolean allowNoToken) {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null) {
            pendingAction = action;

            handlePendingAction();
            return;
        }

        if (allowNoToken) {
            pendingAction = action;
            handlePendingAction();
        }
    }


    private boolean hasPublishPermission() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && accessToken.getPermissions().contains("publish_actions");
    }


    private void postStatusUpdate() {

//		ShareLinkContent linkContent = new ShareLinkContent.Builder()
//				.setContentTitle(map.get("title"))
//				.setContentDescription(map.get("Comtent"))
//				.setContentUrl(Uri.parse(map.get("shareLink")))
//				.setImageUrl(Uri.parse(map.get("shareImage")))
//				.build();
        ShareLinkContent linkContent = new ShareLinkContent.Builder()
                .setContentTitle(map.get("title"))
                .setContentUrl(Uri.parse(map.get("shareLink")))
                .build();
        if (canPresentShareWebDialog) {
            Log.d("UIShare-postUpdate", "shareWebDialog");
            shareWebDialog.show(linkContent);
        } else {
            Log.d("UIShare-postUpdate", "shareApi.share");
            ShareApi.share(linkContent, shareCallback);
        }
    }

}
