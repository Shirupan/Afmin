package com.stone.baselib.net.example;

import android.Manifest;

import com.google.gson.Gson;
import com.stone.baselib.net.SHttpUtils;
import com.stone.baselib.net.SNetError;
import com.stone.baselib.net.subscriber.SNetSubscriber;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.functions.Consumer;
/**
 * Stone
 * 2019/4/4
 **/
//public class SPresentible extends BasePresent<SAbstractActivity> {
//TODO 写完activity解除注释
//    public void requestStone() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("stone", "stone");
//        SHttpFactory.getService().postStone(map)
//                .compose(SHttpUtils.<StoneModel>getApiTransformer())
//                .compose(SHttpUtils.<StoneModel>getScheduler())
//                .compose(getV().bindToLifecycle())
//                .subscribe(new SNetSubscriber<StoneModel>() {
//                    @Override
//                    public void onNext(StoneModel model) {
//                        getV().onStoneNext(model.getData());
//                    }
//
//                    @Override
//                    protected void onFail(SNetError error) {
//                        switch (error.getType()){
//                            case SNetError.NoConnectError:
//                                getV().noNetwork();
//                                break;
//                            default:
//                                getV().onStoneFail(error);
//                                break;
//                        }
//                    }
//
//                });
//
//    }

//}
