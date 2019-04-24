package com.example.example.Present;

import com.example.example.activity.Constants;
import com.example.example.activity.MainActivity;
import com.example.example.event.BaseEvent;
import com.example.example.event.TestIntEvent;
import com.example.example.event.TestStrEvent;
import com.example.example.fragment.HomeFragment;
import com.example.example.http.SHttpFactory;
import com.example.example.http.subscriber.SNetSubscriber;
import com.example.example.model.NearMerChantsModel;
import com.example.example.model.StoneModel;
import com.stone.baselib.busevent.SBusFactory;
import com.stone.baselib.busevent.SEventBusImpl;
import com.stone.baselib.cache.SSpUtil;
import com.stone.baselib.mvp.SPresentImpl;
import com.stone.baselib.net.SHttpUtils;
import com.stone.baselib.net.SNetError;
import com.stone.baselib.utils.SDateUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Stone
 * 2019/4/11
 **/
public class HomePresent extends SPresentImpl<HomeFragment> {

    public void getTestStr() {
        getV().showMsg("test");
    }

    public void postBus() {
        SEventBusImpl bus = SBusFactory.getBus();
        bus.post(new TestStrEvent().setMsg("str event"));
        bus.post(new TestIntEvent().setNum(1));
        bus.post(new BaseEvent().setEventType(Constants.EVENT_BASE_INT).setData(1));
        bus.post(new BaseEvent().setEventType(Constants.EVENT_BASE_STR).setData("str"));
    }

    public void getExitTime() {
        long time = SSpUtil.getInstance(getV().getContext()).getLong(Constants.SP_EXIT_TIME, 0);
        getV().showExitTime(SDateUtils.getYmdhms(time));
    }

    public void saveTime() {
        SSpUtil.getInstance(getV().getContext()).putLong(Constants.SP_EXIT_TIME, System.currentTimeMillis());
    }


    //    网路请求示例代码
    public void requestStone() {
        Map<String, Object> map = new HashMap<>();
        map.put("stone", "stone");
        SHttpFactory.getService().postStone(map)
                .compose(SHttpUtils.<StoneModel>getApiTransformer())
                .compose(SHttpUtils.<StoneModel>getScheduler())
                .subscribe(new SNetSubscriber<StoneModel>() {
                    @Override
                    public void onNext(StoneModel model) {
//                        getV().onStoneNext(model.getData());
                    }

                    @Override
                    protected void onFail(SNetError error) {
                        switch (error.getType()) {
//                            case SNetError.NoConnectError:
//                                getV().noNetwork();
//                                break;
//                            default:
//                                getV().onStoneFail(error);
//                                break;
                        }
                    }

                });

    }

    public void httpPost() {
        Map<String, Object> map = new HashMap<>();
        map.put("lat", 22.55314648);
        map.put("lng", 113.90403089);
        map.put("uid", "4817796856230055");
        SHttpFactory.getMerchantdetailService().queryNearMerChantsLocation(map)
                .compose(SHttpUtils.<NearMerChantsModel>getApiTransformer())
                .compose(SHttpUtils.<NearMerChantsModel>getScheduler())
                .subscribe(new SNetSubscriber<NearMerChantsModel>() {

                    @Override
                    public void onNext(NearMerChantsModel model) {
                        getV().showToast("post:"+model.getData().get(0).getName());
                    }

                    @Override
                    protected void onFail(SNetError error) {
                        getV().showToast(error.getMessage());
                    }
                });

    }

    public void httpGet() {
        SHttpFactory.getMerchantdetailService().getNearMerChantsLocation("4817796856230055", "22.55314648", "113.90403089")
                .compose(SHttpUtils.<NearMerChantsModel>getApiTransformer())
                .compose(SHttpUtils.<NearMerChantsModel>getScheduler())
                .subscribe(new SNetSubscriber<NearMerChantsModel>() {

                    @Override
                    public void onNext(NearMerChantsModel model) {
                        getV().showToast("get:"+model.getData().get(0).getName());
                    }

                    @Override
                    protected void onFail(SNetError error) {
                        getV().showToast(error.getMessage());
                    }
                });
    }

}
