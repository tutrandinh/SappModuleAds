package com.ads.sapp.call.api;

import com.google.gson.annotations.SerializedName;

public class AdsModel {
    @SerializedName("unit_order")
    Integer id;
    @SerializedName("unit_name")
    String name;
    @SerializedName("unit_id")
    String ads_id;

    public AdsModel(Integer id, String name, String ads_id){
        this.id = id;
        this.name = name;
        this.ads_id = ads_id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAds_id() {
        return ads_id;
    }

    public void setAds_id(String ads_id) {
        this.ads_id = ads_id;
    }

}
