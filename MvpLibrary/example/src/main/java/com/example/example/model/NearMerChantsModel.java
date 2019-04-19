package com.example.example.model;

import com.example.example.bean.NearMerChantsBean;

import java.util.List;

/**
 * Stone
 * 2019/4/19
 **/
public class NearMerChantsModel extends BaseModel {
    //data对应返回数据中json字段
    private List<NearMerChantsBean> data;

    public List<NearMerChantsBean> getData() {
        return data;
    }

    public void setData(List<NearMerChantsBean> data) {
        this.data = data;
    }

    @Override
    public boolean isNull() {
        return data==null||data.isEmpty();
    }
}
