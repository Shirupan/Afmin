package com.example.pay;

import android.app.Activity;
import android.os.Bundle;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.stone.baselib.utils.SLogUtils;

import java.io.Serializable;


@Route(path = "/pay/activity")
public class PayActivity extends Activity {

    @Autowired int intMsg;
    @Autowired long longMsg;
    @Autowired double doubleMsg;
    @Autowired float floatMsg;
    @Autowired boolean booleanMsg;
    @Autowired String stringMsg;
    @Autowired SerializableBean serializableMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        ARouter.getInstance().inject(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        log();
    }
     ;
    private void log() {
        SLogUtils.d("int:"+intMsg);
        SLogUtils.d("long:"+longMsg);
        SLogUtils.d("double:"+doubleMsg);
        SLogUtils.d("float:"+floatMsg);
        SLogUtils.d("boolean:"+booleanMsg);
        SLogUtils.d("String:"+stringMsg);
        SLogUtils.d("Serializable:"+serializableMsg.getAge()+","+serializableMsg.getName()+","+serializableMsg.getSex());
    }
}
