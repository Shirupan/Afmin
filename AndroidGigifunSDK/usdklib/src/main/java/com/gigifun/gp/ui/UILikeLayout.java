package com.gigifun.gp.ui;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gigifun.gp.adapter.LikeGiftAdapter;
import com.gigifun.gp.utils.LogUtil;
import com.gigifun.gp.utils.UcallBack;
import com.gigifun.gp.utils.UgameUtil;
import com.gigifun.gp.utils.UhttpUtil;
import com.facebook.MResource;
import com.facebook.share.widget.LikeView;
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
 * @author hoangcaomobile
 * @since March 24, 2015
 */
public class UILikeLayout extends LinearLayout implements OnClickListener {

    private static Activity mActivity;

    private ListView likeListView;
    private RelativeLayout layoutList;
    private TextView imgDirection;
    private String serverid;
    private String roleid;
    private String sdkuid;
    private LikeHandler handler;
    private final int LOAD_LIKE_DATA = 1;
    private final int DIALOG_DISMISS = 2;
    private final int SET_LIKE_LINK = 3;
    private DisplayImageOptions options;
    private ImageLoader imageLoader;
    private String direction;
    private String likeLink;
    private LikeView likeView;
    private int titleWidthParams;
    private ProgressWheel progressWheel;


    public UILikeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public UILikeLayout(Activity activity, String serverid, String roleid, String sdkuid, int titleWidthParams) {
        super(activity);
        this.mActivity = activity;
        this.serverid = serverid;
        this.roleid = roleid;
        this.sdkuid = sdkuid;
        this.titleWidthParams = titleWidthParams;
        initUI();
        initImageLoader();
        requestData();
    }


    private void initUI() {
        handler = new LikeHandler();
        View v = mActivity.getLayoutInflater().inflate(
                MResource.getIdByName(mActivity, "layout", "layout_like_gift"), null);
        addView(v, new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT));

        progressWheel = (ProgressWheel) findViewById(MResource.getIdByName(mActivity, "id", "progress_wheel"));
        layoutList = (RelativeLayout) findViewById(MResource.getIdByName(mActivity, "id", "layout_list"));
        likeListView = (ListView) findViewById(MResource.getIdByName(mActivity, "id", "like_listview"));
        imgDirection = (TextView) findViewById(MResource.getIdByName(mActivity, "id", "img_activite_direction"));
        layoutList.setBackgroundColor(Color.parseColor("#141e23"));
        likeView = (LikeView) findViewById(MResource.getIdByName(mActivity, "id", "like_view"));
//		progressWheel.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				showProgressWheel();
//			}
//		},50);
        showProgressWheel();

        imgDirection.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == MResource.getIdByName(mActivity, "id", "img_activite_direction")) {
            new DirectionDialog(mActivity, direction);
        }

    }

    private void initImageLoader() {
        imageLoader = ImageLoader.getInstance();
        options = new DisplayImageOptions.Builder()

                .cacheInMemory(true)
                .cacheOnDisc(true)
                .considerExifParams(true)
                .build();
    }

    private void requestData() {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("Ugameid", UgameUtil.getInstance().GAME_ID);
        map.put("Ugamekey", UgameUtil.getInstance().CLIENT_SECRET);
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
                Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "network_error"), Toast.LENGTH_SHORT).show();
                closeProgressWheel();

            }
        });


    }

    class LikeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case LOAD_LIKE_DATA:
                    @SuppressWarnings("unchecked")
                    List<HashMap<String, String>> list = (List<HashMap<String, String>>) msg.obj;
                    LogUtil.k("点赞返回DATA"+list);
                    LikeGiftAdapter adapter = new LikeGiftAdapter(mActivity, list, imageLoader, options);
                    likeListView.setAdapter(adapter);
                    closeProgressWheel();
                    break;
                case DIALOG_DISMISS:
                    if (null != msg.obj) {
                        switchCode((String) msg.obj);
                    }
                    closeProgressWheel();
                    break;
                case SET_LIKE_LINK:
                    setLikeLink();
                    closeProgressWheel();
                    break;
//				case 200:
//					showProgressWheel();
//					break;

            }
        }

    }

    private void parseResult(String result) {
        if (TextUtils.isEmpty(result)) {
            closeProgressWheel();
            return;
        }
        try {
            JSONObject json = new JSONObject(result);
            String status = json.optString("Status");
            String code = json.optString("Code");
            String invite = json.optString("invite");
            String share = json.optString("share");
            direction = json.optString("explain");
            likeLink = json.optString("likelink");
            handler.sendEmptyMessage(SET_LIKE_LINK);
            if ("0".equals(status)) {
                Message msg = handler.obtainMessage();
                msg.obj = code;
                msg.what = DIALOG_DISMISS;
                handler.sendMessage(msg);
            } else if ("1".equals(status)) {
                List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                JSONArray array = json.optJSONArray("Data");
                for (int i = 0; i < array.length(); i++) {
                    HashMap<String, String> map = new HashMap<String, String>();
                    JSONObject jo = array.optJSONObject(i);
                    map.put("id", jo.optString("id"));
                    map.put("title", jo.optString("title"));
                    map.put("logo", jo.optString("logo"));
                    map.put("comtent", jo.optString("comtent"));
                    map.put("complete", jo.optString("complete"));
                    map.put("likenumber", jo.optString("likenumber"));
                    list.add(map);
                }
                Message msg = handler.obtainMessage();
                msg.obj = list;
                msg.what = LOAD_LIKE_DATA;
                handler.sendMessage(msg);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void switchCode(String code) {
        if (TextUtils.isEmpty(code)) {
            closeProgressWheel();
            return;
        }
//		Toast.makeText(mActivity, code+"", Toast.LENGTH_LONG).show();
        if ("128".equals(code)) {
            Toast.makeText(mActivity, com.gigifun.gp.utils.MResource.getIdByName(mActivity, "string", "rewardsend"), Toast.LENGTH_SHORT).show();
        }
    }

    private void setLikeLink() {
        if (TextUtils.isEmpty(likeLink)) {
            likeView.setObjectIdAndType("https://www.facebook.com/FacebookDevelopers", LikeView.ObjectType.PAGE);
        } else {
            likeView.setObjectIdAndType(likeLink, LikeView.ObjectType.PAGE);
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
