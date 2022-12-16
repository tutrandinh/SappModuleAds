package com.sapp.andmoduleads;

import com.ads.sapp.ads.CommonAd;
import com.ads.sapp.ads.CommonAdConfig;
import com.ads.sapp.application.AdsMultiDexApplication;
import com.ads.sapp.admob.Admob;
import com.ads.sapp.admob.AppOpenManager;
import com.sapp.andmoduleads.activity.SplashActivity;


public class MyApplication extends AdsMultiDexApplication {
    private static MyApplication context;

    public static MyApplication getApplication() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        AppOpenManager.getInstance().disableAppResumeWithActivity(SplashActivity.class);
        Admob.getInstance().setNumToShowAds(0);
        initAds();
    }

    private void initAds() {
        commonAdConfig.setMediationProvider(CommonAdConfig.PROVIDER_ADMOB);
        commonAdConfig.setVariant(BuildConfig.build_debug);
        commonAdConfig.setIdAdResume(getString(R.string.admod_app_open_ad_id));
        //listTestDevice.add("EC25F576DA9B6CE74778B268CB87E431");
        commonAdConfig.setListDeviceTest(listTestDevice);

        CommonAd.getInstance().init(this, commonAdConfig, false);
        Admob.getInstance().setOpenActivityAfterShowInterAds(false);
        Admob.getInstance().setDisableAdResumeWhenClickAds(true);

    }
}
