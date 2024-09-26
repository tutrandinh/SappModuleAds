package com.ads.sapp.call.api;

import java.util.ArrayList;

public class DataModel {
    private ArrayList<AdsModel> adsModels;
    private IsError isError;

    public DataModel() {}

    public DataModel(ArrayList<AdsModel> adsModels, IsError isError) {
        this.adsModels = adsModels;
        this.isError = isError;
    }

    public ArrayList<AdsModel> getAdsModels() {
        return adsModels;
    }

    public void setAdsModels(ArrayList<AdsModel> adsModels) {
        this.adsModels = adsModels;
    }

    public IsError getIsError() {
        return isError;
    }

    public void setIsError(IsError isError) {
        this.isError = isError;
    }
}
