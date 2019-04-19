package com.example.example.bean;

import java.io.Serializable;

public class NearMerChantsBean implements Serializable {

    /**
     * id : 43
     * name : 3350801994
     * paw :
     * user_name : 天虹
     * photo:https://beta.playtoken.com/data/upload/client/2018-11-02/5bdc1639ea382.jpeg
     * type : 1
     * phone : +8613690954761
     * assets : 0.00
     * bankcard : null
     * tax : 1
     * fee : null
     * register : 0
     * address : 深圳南山
     * detailed : 广东省深圳市南山区南山大道
     * lat : 22.535507
     * lng : 113.9244496
     * ctime : 1540455998
     * distance : 1 m
     * weekstart : Monday
     * weekend : Sunday
     * daystart : 8:00
     * dayend : 22:00
     * Collection: 0
     * fabulous: 0
     * fabulous_number: 0
     */
    private String id;
    private String name;
    private String paw;
    private String user_name;
    private String photo;
    private String type;
    private String phone;
    private String assets;
    private Object bankcard;
    private int tax;
    private Object fee;
    private String register;
    private String address;
    private String detailed;
    private String lat;
    private String lng;
    private String ctime;
    private String distance;
    private String weekstart;
    private String weekend;
    private String daystart;
    private String dayend;
    private int Collection;
    private int fabulous;
    private int fabulous_number;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPaw() {
        return paw;
    }

    public void setPaw(String paw) {
        this.paw = paw;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAssets() {
        return assets;
    }

    public void setAssets(String assets) {
        this.assets = assets;
    }

    public Object getBankcard() {
        return bankcard;
    }

    public void setBankcard(Object bankcard) {
        this.bankcard = bankcard;
    }

    public int getTax() {
        return tax;
    }

    public void setTax(int tax) {
        this.tax = tax;
    }

    public Object getFee() {
        return fee;
    }

    public void setFee(Object fee) {
        this.fee = fee;
    }

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDetailed() {
        return detailed;
    }

    public void setDetailed(String detailed) {
        this.detailed = detailed;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }


    public String getWeekstart() {
        return weekstart;
    }

    public String getWeekend() {
        return weekend;
    }


    public String getDaystart() {
        return daystart;
    }


    public String getDayend() {
        return dayend;
    }

    public String getPhoto() {
        return photo;
    }

    public int getCollection() {
        return Collection;
    }

    public void setCollection(int collection) {
        Collection = collection;
    }

    public int getFabulous() {
        return fabulous;
    }

    public void setFabulous(int fabulous) {
        this.fabulous = fabulous;
    }

    public int getFabulousNumber() {
        return fabulous_number;
    }

    public void setFabulousNumber(int fabulous_number) {
        this.fabulous_number = fabulous_number;
    }
}
