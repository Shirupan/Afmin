/*
 * Copyright (C) 2015 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.gigifun.gp.widget;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gigifun.gp.UgameSDK;
import com.gigifun.gp.service.FloatViewService;
import com.gigifun.gp.ui.FloatWebviewDialog;
import com.gigifun.gp.utils.LogUtil;
import com.gigifun.gp.utils.MResource;
import com.gigifun.gp.utils.UgameUtil;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Desction:悬浮窗
 */
public class FloatView extends FrameLayout implements OnTouchListener {

    private final int HANDLER_TYPE_HIDE_LOGO = 100;//隐藏LOGO
    private final int HANDLER_TYPE_CANCEL_ANIM = 101;//退出动画

    private WindowManager.LayoutParams mWmParams;
    private WindowManager mWindowManager;
    private Context mContext;
    private static Activity mActivity;

    //private View mRootFloatView;
    private ImageView mIvFloatLogo;
//    private ImageView mIvFloatLoader;
    private LinearLayout mLlFloatMenu;
    private FrameLayout mFmlayout;
    private TextView mTvAccount;

    private TextView mTvFloatGift;
    private TextView mTvFloatContact;
    private TextView mTvFloatBuy;
    private TextView mTvFacebook;
    private TextView mTvRelax;//娱乐一下
    private FrameLayout mFlFloatLogo;

    private boolean mIsRight;//logo是否在右边
    private boolean mCanHide;//是否允许隐藏
    private float mTouchStartX;
    private float mTouchStartY;
    private int mScreenWidth;
    private int mScreenHeight;
    private boolean mDraging;
    private boolean mShowLoader = true;

    private Timer mTimer;
    private TimerTask mTimerTask;

    public  static FloatViewService floatViewService;

    private String FLOAT_PERSONAL = "member";
    private String FLOAT_FACEBOOK = "news";
    private String FLOAT_GIFT = "gift";
    private String FLOAT_CONTACT = "cus";
    private String FLOAT_PAY = "pay";
    private String FLOAT_RELAX="relax";//娱乐一下

    private String fbflag;
    private String starflag;
    private String payflag;
    private String serverId;
    private String isRelax;//娱乐一下
    private String logoflag;//logo的状态
    private String giftflag;//礼包状态
    private String cusflag;//客服状态
    private String relaxRed;//娱乐一下红点
    private String payRedFlag;//支付的红点
    private String userRedFlag;//用户的红点


    private SharedPreferences preferences;
    final Handler mTimerHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == HANDLER_TYPE_HIDE_LOGO) {
                // 比如隐藏悬浮框
                if (mCanHide) {
                    mCanHide = false;
                    if (mIsRight) {
                      mIvFloatLogo.setImageResource(MResource.getIdByName(mContext, "drawable", "iiu_float_right"));
//                        mIvFloatHide.setImageResource(MResource.getIdByName(mContext, "drawable","iiu_float_right"));

                    } else {
                        mIvFloatLogo.setImageResource(MResource.getIdByName(mContext, "drawable", "iiu_float_left"));

                    }

                    mWmParams.alpha = 0.7f;
                    mWindowManager.updateViewLayout(FloatView.this, mWmParams);
                    refreshFloatMenu(mIsRight);
                    mLlFloatMenu.setVisibility(View.GONE);
                }
            } else if (msg.what == HANDLER_TYPE_CANCEL_ANIM) {
//                mIvFloatLoader.clearAnimation();
//                mIvFloatLoader.setVisibility(View.GONE);
                mShowLoader = false;
            }
          //  setListImage(m);
        //    setListImage(mTvFloatContact,cusflag,"2");//1客服
       //     setListImage(mTvFloatGift,giftflag,"1");//礼包
            //setImgae(mIvFloatLogo,logoflag);
            //showFloat();
            super.handleMessage(msg);
        }
    };



    public FloatView(Context context) {
        super(context);
        init(context);
    }

    public FloatView(Activity activity, FloatViewService floatViewService) {
        super(activity);
        this.floatViewService = floatViewService;
        LogUtil.k("FloatView floatViewService ==" + this.floatViewService);
        this.mActivity = activity;
    }

    private void init(Context mContext) {
        this.mContext = mContext;

        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        // 更新浮动窗口位置参数 靠边
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        LogUtil.k("DisplayMetrics.toString="+dm.toString());
        this.mWmParams = new WindowManager.LayoutParams();
        // 设置window type
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWmParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        } else {
            mWmParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }

        // 设置图片格式，效果为背景透明
        mWmParams.format = PixelFormat.RGBA_8888;
        // 设置浮动窗口不可聚焦（实现操作除浮动窗口外的其他可见窗口的操作）
        mWmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 调整悬浮窗显示的停靠位置为左侧置?
        mWmParams.gravity = Gravity.LEFT | Gravity.TOP;

        mScreenHeight = mWindowManager.getDefaultDisplay().getHeight();

        // 以屏幕左上角为原点，设置x、y初始值，相对于gravity
        mWmParams.x = 0;
        mWmParams.y = mScreenHeight / 2;

        // 设置悬浮窗口长宽数据
        mWmParams.width = LayoutParams.WRAP_CONTENT;
        mWmParams.height = LayoutParams.WRAP_CONTENT;
        addView(createView(mContext));
        mWindowManager.addView(this, mWmParams);

        mTimer = new Timer();
        hide();
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // 更新浮动窗口位置参数 靠边
        DisplayMetrics dm = new DisplayMetrics();
        // 获取屏幕信息
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        mScreenWidth = dm.widthPixels;
        mScreenHeight = dm.heightPixels;
        int oldX = mWmParams.x;
        int oldY = mWmParams.y;
        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_LANDSCAPE://横屏
                if (mIsRight) {
                    mWmParams.x = mScreenWidth;
                    mWmParams.y = oldY;
                } else {
                    mWmParams.x = oldX;
                    mWmParams.y = oldY;
                }
                break;
            case Configuration.ORIENTATION_PORTRAIT://竖屏
                if (mIsRight) {
                    mWmParams.x = mScreenWidth;
                    mWmParams.y = oldY;
                } else {
                    mWmParams.x = oldX;
                    mWmParams.y = oldY;
                }
                break;
        }
        mWindowManager.updateViewLayout(this, mWmParams);
    }

    /**
     * 创建Float view
     *
     * @param context
     * @return
     */
    private View createView(final Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        UgameUtil.getInstance().changeLang(context);
        // 从布局文件获取浮动窗口视图
        View rootFloatView = inflater.inflate(MResource.getIdByName(mContext, "layout", "u_widget_float_view"), null);
        mFlFloatLogo = (FrameLayout) rootFloatView.findViewById(MResource.getIdByName(mContext, "id", "u_float_view"));
        mIvFloatLogo = (ImageView) rootFloatView.findViewById(MResource.getIdByName(mContext, "id", "u_float_view_icon_imageView"));
//        mIvFloatLoader = (ImageView) rootFloatView.findViewById(MResource.getIdByName(mContext, "id", "u_float_view_icon_notify"));
        mLlFloatMenu = (LinearLayout) rootFloatView.findViewById(MResource.getIdByName(mContext, "id", "ll_menu"));
        //mFmlayout = (FrameLayout) rootFloatView.findViewById(MResource.getIdByName(mContext, "id", "fm_lay"));

        mTvFloatGift = (TextView) rootFloatView.findViewById(MResource.getIdByName(mContext, "id", "tv_float_gift"));
        mTvFloatContact = (TextView) rootFloatView.findViewById(MResource.getIdByName(mContext, "id", "tv_float_context"));
        mTvFloatBuy = (TextView) rootFloatView.findViewById(MResource.getIdByName(mContext, "id", "tv_float_buy"));
        mTvAccount = (TextView) rootFloatView.findViewById(MResource.getIdByName(mContext, "id", "tv_account"));
        mTvFacebook = (TextView) rootFloatView.findViewById(MResource.getIdByName(mContext, "id", "tv_float_fb"));
        mTvRelax=(TextView) rootFloatView.findViewById(MResource.getIdByName(mContext, "id", "tv_float_play_game"));//娱乐一下

        preferences = mContext.getSharedPreferences("LoginCount", Context.MODE_PRIVATE);


        mTvAccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mLlFloatMenu.setVisibility(View.GONE);
                openUserCenter(FLOAT_PERSONAL);
            }
        });
        mTvFacebook.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mLlFloatMenu.setVisibility(View.GONE);
//                openUserCenter(FLOAT_FACEBOOK);
                UgameSDK.getInstance().startForGift(mActivity,serverId);
                hide();
            }
        });

        //娱乐一下
        mTvRelax.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mLlFloatMenu.setVisibility(View.GONE);
                openUserCenter(FLOAT_RELAX);
            }
        });
        mTvFloatGift.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mLlFloatMenu.setVisibility(View.GONE);
                openUserCenter(FLOAT_GIFT);
            }
        });
        mTvFloatContact.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mLlFloatMenu.setVisibility(View.GONE);
                openUserCenter(FLOAT_CONTACT);
            }
        });
        mTvFloatBuy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mLlFloatMenu.setVisibility(View.GONE);
                openUserCenter(FLOAT_PAY);
            }
        });

        rootFloatView.setOnTouchListener(this);
        rootFloatView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mDraging) {
                    if (mLlFloatMenu.getVisibility() == View.VISIBLE) {
                        mLlFloatMenu.setVisibility(View.GONE);
                    } else {
                        mLlFloatMenu.setVisibility(View.VISIBLE);
                    }
                }

//                Intent intent = new Intent(context, FloatActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                context.startActivity(intent);
//                hide();
            }


        });
        rootFloatView.measure(MeasureSpec.makeMeasureSpec(0,
                MeasureSpec.UNSPECIFIED), MeasureSpec
                .makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));


        return rootFloatView;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        removeTimerTask();
        // 获取相对屏幕的坐标，即以屏幕左上角为原点
      //  setListImage(mTvFloatContact,cusflag,"2");//1客服
      //  setListImage(mTvFloatGift,giftflag,"1");//礼包
        this.fbflag = preferences.getString("fbflag", "0").trim();
        this.starflag = preferences.getString("5starflag", "0").trim();
        this.payflag = preferences.getString("paymentflag", "0").trim();
        this.serverId = preferences.getString("serverId", "").trim();
        this.isRelax=preferences.getString("isRelax","0").trim();

       // setImgae(mIvFloatLogo,logoflag);//设置logo红点显示
        LogUtil.k("===payflag=="+payflag);
      //  showFloat();
        if ("0".equals(payflag)){
            mTvFloatBuy.setVisibility(GONE);
        }
        if ("0".equals(starflag)){
            mTvFloatGift.setVisibility(GONE);
        }
        if ("0".equals(fbflag)){
            mTvFacebook.setVisibility(GONE);
        }
        if("0".equals(isRelax)){
            mTvRelax.setVisibility(GONE);
        }
        int x = (int) event.getRawX();
        int y = (int) event.getRawY();
//        mIvFloatHide.setVisibility(GONE);
//        mIvFloatLogo.setVisibility(VISIBLE);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchStartX = event.getX();
                mTouchStartY = event.getY();
                mIvFloatLogo.setImageResource(MResource.getIdByName(mContext, "drawable", "iiu_float_logo"));

//

                mWmParams.alpha = 1f;
                mWindowManager.updateViewLayout(this, mWmParams);
                mDraging = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float mMoveStartX = event.getX();
                float mMoveStartY = event.getY();
                // 如果移动量大于3才移动
                if (Math.abs(mTouchStartX - mMoveStartX) > 3
                        && Math.abs(mTouchStartY - mMoveStartY) > 3) {
                    mDraging = true;
                    // 更新浮动窗口位置参数
                    mWmParams.x = (int) (x - mTouchStartX);
                    mWmParams.y = (int) (y - mTouchStartY);
                    mWindowManager.updateViewLayout(this, mWmParams);
                    mLlFloatMenu.setVisibility(View.GONE);
                    return false;
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:

                if (mWmParams.x >= mScreenWidth / 2) {
                    mWmParams.x = mScreenWidth;
                    mIsRight = true;
                } else if (mWmParams.x < mScreenWidth / 2) {
                    mIsRight = false;
                    mWmParams.x = 0;
                }
//                mIvFloatLogo.setImageResource(ResourceUtils.getDrawableId(mContext, "pj_image_float_logo"));
              //  mIvFloatLogo.setImageResource(MResource.getIdByName(mContext, "drawable", "iiu_float_logo"));

                refreshFloatMenu(mIsRight);
                timerForHide();
                mWindowManager.updateViewLayout(this, mWmParams);
                // 初始化
                mTouchStartX = mTouchStartY = 0;
                break;
        }
        return false;
    }

    private void removeTimerTask() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    private void removeFloatView() {
        try {
            mWindowManager.removeView(this);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 隐藏悬浮窗
     */
    public void hide() {
        LogUtil.k("FloatView hide");
        setVisibility(View.GONE);
        Message message = mTimerHandler.obtainMessage();
        message.what = HANDLER_TYPE_HIDE_LOGO;
        mTimerHandler.sendMessage(message);
        removeTimerTask();
    }

    /**
     * 显示悬浮窗
     */
    public void show() {
        if (getVisibility() != View.VISIBLE) {
            setVisibility(View.VISIBLE);
            if (mShowLoader) {
                LogUtil.k("FloatView Show");
                int[] location  = new int[2];
                this.getLocationOnScreen(location);
                LogUtil.k("x:" + location[0] + "y:" + location[1]);
//                mIvFloatLogo.setImageResource(ResourceUtils.getDrawableId(mContext, "pj_image_float_logo"));
                mIvFloatLogo.setImageResource(MResource.getIdByName(mContext, "drawable", "iiu_float_logo"));
                mWmParams.alpha = 1f;
                mWindowManager.updateViewLayout(this, mWmParams);

                timerForHide();
              //  init(mContext);

                mShowLoader = false;
            }
        }

    }



    /**
     * 刷新float view menu
     *
     * @param right
     */
    private void refreshFloatMenu(boolean right) {
        if (right) {

            LayoutParams paramsFloatImage = (LayoutParams) mIvFloatLogo.getLayoutParams();
            paramsFloatImage.gravity = Gravity.RIGHT;
            mIvFloatLogo.setLayoutParams(paramsFloatImage);
            mIvFloatLogo.setScaleType(ImageView.ScaleType.FIT_END);

            LayoutParams paramsFlFloat = (LayoutParams) mFlFloatLogo.getLayoutParams();
            paramsFlFloat.gravity = Gravity.RIGHT;
            mFlFloatLogo.setLayoutParams(paramsFlFloat);

            int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, mContext.getResources().getDisplayMetrics());
            int padding10 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, mContext.getResources().getDisplayMetrics());
            int padding52 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 52, mContext.getResources().getDisplayMetrics());

            LinearLayout.LayoutParams paramsMenuAccount = (LinearLayout.LayoutParams) mTvAccount.getLayoutParams();
            paramsMenuAccount.rightMargin = padding;
            paramsMenuAccount.leftMargin = padding10;
            mTvAccount.setLayoutParams(paramsMenuAccount);

            LinearLayout.LayoutParams paramsMenuGift = (LinearLayout.LayoutParams) mTvFloatGift.getLayoutParams();
            paramsMenuGift.rightMargin = padding;
            paramsMenuGift.leftMargin = padding;
            mTvFloatGift.setLayoutParams(paramsMenuGift);

            LinearLayout.LayoutParams paramsMenuContact = (LinearLayout.LayoutParams) mTvFloatContact.getLayoutParams();
            if ("0".equals(payflag)){
                paramsMenuContact.rightMargin = padding52;
            }else{
                paramsMenuContact.rightMargin = padding;
            }
            paramsMenuContact.leftMargin = padding;
            mTvFloatContact.setLayoutParams(paramsMenuContact);

            LinearLayout.LayoutParams paramsMenuBuy = (LinearLayout.LayoutParams) mTvFloatBuy.getLayoutParams();
            paramsMenuBuy.rightMargin = padding52;
            paramsMenuBuy.leftMargin = padding;
            mTvFloatBuy.setLayoutParams(paramsMenuBuy);
        } else {
            LayoutParams params = (LayoutParams) mIvFloatLogo.getLayoutParams();
            params.setMargins(0, 0, 0, 0);
            params.gravity = Gravity.LEFT;
            mIvFloatLogo.setLayoutParams(params);
            mIvFloatLogo.setScaleType(ImageView.ScaleType.FIT_START);

            LayoutParams paramsFlFloat = (LayoutParams) mFlFloatLogo.getLayoutParams();
            paramsFlFloat.gravity = Gravity.LEFT;
            mFlFloatLogo.setLayoutParams(paramsFlFloat);

            int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, mContext.getResources().getDisplayMetrics());
            int padding10 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, mContext.getResources().getDisplayMetrics());
            int padding52 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 52, mContext.getResources().getDisplayMetrics());

            LinearLayout.LayoutParams paramsMenuAccount = (LinearLayout.LayoutParams) mTvAccount.getLayoutParams();
            paramsMenuAccount.rightMargin = padding;
            paramsMenuAccount.leftMargin = padding52;
            mTvAccount.setLayoutParams(paramsMenuAccount);


            LinearLayout.LayoutParams paramsMenuGift = (LinearLayout.LayoutParams) mTvFloatGift.getLayoutParams();
            paramsMenuGift.rightMargin = padding;
            paramsMenuGift.leftMargin = padding;
            mTvFloatGift.setLayoutParams(paramsMenuGift);

            LinearLayout.LayoutParams paramsMenuContact = (LinearLayout.LayoutParams) mTvFloatContact.getLayoutParams();
            paramsMenuContact.rightMargin = padding;
            paramsMenuContact.leftMargin = padding;
            mTvFloatContact.setLayoutParams(paramsMenuContact);

            LinearLayout.LayoutParams paramsMenuBuy = (LinearLayout.LayoutParams) mTvFloatBuy.getLayoutParams();
            paramsMenuBuy.rightMargin = padding10;
            paramsMenuBuy.leftMargin = padding;
            mTvFloatBuy.setLayoutParams(paramsMenuBuy);
        }
    }

    /**
     * 定时隐藏float view
     */
    private void timerForHide() {
        mCanHide = true;
        //结束任务
        if (mTimerTask != null) {
            try {
                mTimerTask.cancel();
                mTimerTask = null;
            } catch (Exception e) {
            }

        }
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Message message = mTimerHandler.obtainMessage();
                message.what = HANDLER_TYPE_HIDE_LOGO;
                mTimerHandler.sendMessage(message);
            }
        };
        if (mCanHide) {
            //设置定时器
            mTimer.schedule(mTimerTask, 8000, 3000);
        }
    }

    /**
     * 打开用户中心
     */
    private void openUserCenter(String select) {
        LogUtil.k("Activity==" + mActivity);
      //  mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT);
        mActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);

        boolean isVer=true;//默认是横屏
        switch(mActivity.getResources().getConfiguration().orientation){

            case Configuration.ORIENTATION_LANDSCAPE:
                //横屏
                isVer=true;
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                //竖屏
                isVer=false;
                break;
            default:
                //默认是横屏
                isVer=true;
        }
        new FloatWebviewDialog(mActivity, serverId, floatViewService, select,isVer, null);
    }

    /**
     * 是否Float view
     */
    public void destroy() {
        hide();
        removeFloatView();
        removeTimerTask();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        try {
            mTimerHandler.removeMessages(1);
        } catch (Exception e) {
        }
    }
}
