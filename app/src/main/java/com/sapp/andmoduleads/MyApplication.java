package com.sapp.andmoduleads;

import com.ads.sapp.ads.CommonAd;
import com.ads.sapp.ads.CommonAdConfig;
import com.ads.sapp.application.AdsMultiDexApplication;
import com.ads.sapp.admob.Admob;
import com.ads.sapp.admob.AppOpenManager;
import com.ads.sapp.call.api.event.EventCommon;
import com.sapp.andmoduleads.activity.SplashActivity;
import com.ads.sapp.call.api.CommonProcess;


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

        CommonProcess.getInstance().setBranch("develop");
        CommonProcess.getInstance().setToken("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");
        CommonProcess.getInstance().setFileName("ca-app-pub-3940256099942544~3347511713");

        //Config load data event
        EventCommon.getInstance().setBranch("main");
        EventCommon.getInstance().setToken("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");
        EventCommon.getInstance().setFileName("event-setting");

        commonAdConfig.setMediationProvider(CommonAdConfig.PROVIDER_ADMOB);
        commonAdConfig.setVariant(BuildConfig.build_debug);
        commonAdConfig.setIdAdResume(getString(R.string.admod_app_open_ad_id));
        //listTestDevice.add("EC25F576DA9B6CE74778B268CB87E431");
        commonAdConfig.setMediationFloor(CommonAdConfig.WARTER_FALL);
        commonAdConfig.setListDeviceTest(listTestDevice);

        CommonAd.getInstance().init(this, commonAdConfig, false);
        Admob.getInstance().setOpenActivityAfterShowInterAds(true);
        Admob.getInstance().setDisableAdResumeWhenClickAds(true);

    }
}
