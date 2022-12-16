package com.ads.sapp.application;

import androidx.multidex.MultiDexApplication;

import com.ads.sapp.ads.CommonAdConfig;
import com.ads.sapp.util.AppUtil;
import com.ads.sapp.util.SharePreferenceUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class AdsMultiDexApplication extends MultiDexApplication {

    protected CommonAdConfig commonAdConfig;
    protected List<String> listTestDevice ;
    @Override
    public void onCreate() {
        super.onCreate();
        listTestDevice = new ArrayList<String>();
        commonAdConfig = new CommonAdConfig();
        commonAdConfig.setApplication(this);
        if (SharePreferenceUtils.getInstallTime(this) == 0) {
            SharePreferenceUtils.setInstallTime(this);
        }
        AppUtil.currentTotalRevenue001Ad = SharePreferenceUtils.getCurrentTotalRevenue001Ad(this);
    }


}
