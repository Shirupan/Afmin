package com.example.example.model;

import com.example.example.bean.StoneBean;

import java.util.List;

/**
 * Stone
 * 2019/4/4
 **/
public class StoneModel extends BaseModel {

    //data对应返回数据中json字段
    private List<StoneBean> data;

    public List<StoneBean> getData() {
        return data;
    }

    public void setData(List<StoneBean> data) {
        this.data = data;
    }

    @Override
    public boolean isNull() {
        return data==null||data.isEmpty();
    }

}
