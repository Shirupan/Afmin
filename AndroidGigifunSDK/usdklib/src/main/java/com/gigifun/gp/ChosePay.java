package com.gigifun.gp;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gigifun.gp.utils.MResource;
import com.gigifun.usdklib.R;

/**
 * Created by iuu1 on 2017/3/20.
 */

public class ChosePay {
    private Activity mActivity;
    private Dialog mDialog;
    private RadioButton checkRadioButton;
    RadioButton morepay;
    RadioButton alipay;
    RadioButton wxpay;
    private RadioGroup group_temo;
    private ChosePay(Activity activity){
        this.mActivity=activity;

        initUI();
    }

    private void initUI() {
        mDialog = new Dialog(mActivity, MResource.getIdByName(mActivity, "style", "custom_dialog"));
        mDialog.getWindow().getAttributes().windowAnimations = MResource.getIdByName(mActivity, "style", "dialogAnim");
        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(MResource.getIdByName(mActivity, "layout", "pay_choose"));
//        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
         group_temo  = (RadioGroup) mDialog.findViewById(R.id.radioGroup1);
        morepay = (RadioButton) mDialog.findViewById(R.id.radio0);
         alipay = (RadioButton) mDialog.findViewById(R.id.radio1);
         wxpay = (RadioButton) mDialog.findViewById(R.id.radio2);


//        group_temo.check(R.id.radio1);


        // 获取默认被被选中值

        checkRadioButton = (RadioButton) group_temo.findViewById(group_temo
                .getCheckedRadioButtonId());

        Toast.makeText(mActivity, "默认的选项的值是:" + checkRadioButton.getText(),Toast.LENGTH_LONG).show();

        // 注册事件
        group_temo
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        // 点击事件获取的选择对象
                        checkRadioButton = (RadioButton) group_temo
                                .findViewById(checkedId);

                        Toast.makeText(mActivity,
                                "获取的ID是" + checkRadioButton.getText(),
                                Toast.LENGTH_LONG).show();
                    }
                });


    }

    public static void show(Activity activity){
        new ChosePay(activity);
    }

}
