package com.example.example.activity;

import android.app.Activity;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.example.R;
import com.example.example.base.BaseActivity;
import com.example.example.bean.SerializableBean;
import com.stone.baselib.utils.SLogUtils;

import java.util.List;


@Route(path = "/pay/activity")
public class ArouterActivity extends BaseActivity {

    @Autowired int intMsg;
    @Autowired long longMsg;
    @Autowired double doubleMsg;
    @Autowired float floatMsg;
    @Autowired boolean booleanMsg;
    @Autowired String stringMsg;
    @Autowired List<SerializableBean> listMsg;
    @Autowired SerializableBean serializableMsg;

    private void log() {
        SLogUtils.d("int:"+intMsg);
        SLogUtils.d("long:"+longMsg);
        SLogUtils.d("double:"+doubleMsg);
        SLogUtils.d("float:"+floatMsg);
        SLogUtils.d("boolean:"+booleanMsg);
        SLogUtils.d("String:"+stringMsg);
        SLogUtils.d("object:"+listMsg.get(0).getName());
        SLogUtils.d("Serializable:"+serializableMsg.getAge()+","+serializableMsg.getName()+","+serializableMsg.getSex());
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_pay;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        log();
    }

    @Override
    public Object getPInstance() {
        return null;
    }
}
