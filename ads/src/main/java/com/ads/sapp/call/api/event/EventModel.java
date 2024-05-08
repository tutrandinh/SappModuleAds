package com.ads.sapp.call.api.event;

import com.google.gson.annotations.SerializedName;

public class EventModel {
    @SerializedName("roas")
    Integer roas;
    @SerializedName("cost")
    Double cost;
    @SerializedName("default_value")
    Integer default_value;

    public EventModel(Integer roas, Double cost, Integer default_value){
        this.roas = roas;
        this.cost = cost;
        this.default_value = default_value;
    }

    public Integer getRoas() {
        return roas;
    }

    public void setRoas(Integer roas) {
        this.roas = roas;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Integer getDefaultValue() {
        return default_value;
    }

    public void setDefaultValue(Integer defaultValue) {
        this.default_value = defaultValue;
    }
}
