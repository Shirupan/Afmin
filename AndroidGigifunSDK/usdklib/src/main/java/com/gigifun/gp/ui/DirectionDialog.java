package com.gigifun.gp.ui;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.gigifun.gp.utils.MResource;

/**
 * Created by tim on 16/9/2.
 */
public class DirectionDialog {

    private Activity mActivity;
    private Dialog mDialog;
    private TextView tvDirection;
    private TextView tv1;
    private ScrollView scrollView;
    private Button btnCancel;
    private RelativeLayout directionLayout;
    private String direction;

    public DirectionDialog(){


    }



    public DirectionDialog(Activity activity,String direction){
        this.mActivity=activity;
        this.direction=direction;
        initUI();

    }


    public void initUI() {
        mDialog = new Dialog(mActivity, MResource.getIdByName(mActivity, "style", "custom_dialog"));
        mDialog.getWindow().getAttributes().windowAnimations = MResource.getIdByName(mActivity, "style", "dialogAnim");
        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(MResource.getIdByName(mActivity, "layout", "dialog_gift_direction"));
        mDialog.setCancelable(false);


        tvDirection = (TextView)mDialog.findViewById(MResource.getIdByName(mActivity, "id", "tv_direction"));
        directionLayout = (RelativeLayout)mDialog.findViewById(MResource.getIdByName(mActivity, "id", "direction_layout"));
        btnCancel = (Button)mDialog.findViewById(MResource.getIdByName(mActivity, "id", "btn_cancel"));
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mDialog.dismiss();
            }
        });

        tvDirection.setText(direction);

        mDialog.show();
    }

    }

