package com.sapp.andmoduleads.activity;

import static com.ads.sapp.util.GoogleMobileAdsConsentManager.getConsentResult;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ads.sapp.admob.Admob;
import com.ads.sapp.admob.AppOpenManager;
import com.ads.sapp.ads.CommonAd;
import com.ads.sapp.ads.CommonAdCallback;
import com.ads.sapp.ads.CommonAdConfig;
import com.ads.sapp.funtion.AdCallback;
import com.ads.sapp.funtion.BannerCallback;
import com.ads.sapp.util.CheckAds;
import com.ads.sapp.util.GoogleMobileAdsConsentManager;
import com.sapp.andmoduleads.BuildConfig;
import com.sapp.andmoduleads.MyApplication;
import com.sapp.andmoduleads.R;
import com.ads.sapp.call.api.CommonProcess;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private AdCallback adCallback  = null;
    private CommonAdCallback commonAdCallback = null;
    private static final String TAG = "SplashActivity";
    private List<String> list = new ArrayList<>();
    private String idAdSplash;
    boolean isInit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Inter
        commonAdCallback = new CommonAdCallback() {
            @Override
            public void onNextAction() {
                super.onNextAction();
                Log.d(TAG, "onNextAction");
                startMain();

            }
        };

        adCallback = new AdCallback() {
            @Override
            public void onNextAction() {
                super.onNextAction();
                Log.e("Splash ads", "open success");
                startMain();
            }
        };


        ArrayList<String> listIDAdsBannerSplash = new ArrayList<>();
        listIDAdsBannerSplash.add(BuildConfig.ad_banner);

        //Load banner splash
        BannerCallback bannerCallback = new BannerCallback(){
            @Override
            public void onCheckComplete() {
                super.onCheckComplete();

                // Inter
                //                ArrayList<String> list = new ArrayList<>();
                //                list.add(getString(R.string.inter_splash3));
                //                CommonAd.getInstance().loadSplashInterstitialAdsCheck(
                //                        SplashActivity.this,
                //                        list,
                //                        2500,
                //                        5000,
                //                        commonAdCallback
                //                );

                // Open

                ArrayList<String> listID = new ArrayList<>();
                listID.add("ca-app-pub-3940256099942544/9257395921");
                //CheckAds.checkAd = false; When show only
                AppOpenManager.getInstance().loadOpenAppAdSplashFloorCheck(
                        SplashActivity.this,
                        listID,
                        true,
                        adCallback
                );
            }
        };

        // Add list id drive test
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("a95848c5c33cda2b");
        arrayList.add("f51641f27b218873");
        //arrayList.add("a7bae6fe8bf277ae");

        //Call consent
        GoogleMobileAdsConsentManager googleMobileAdsConsentManager;
        googleMobileAdsConsentManager = GoogleMobileAdsConsentManager.getInstance(getApplicationContext());
        googleMobileAdsConsentManager.setSetTagForUnderAge(false);
        googleMobileAdsConsentManager.setTestDebug(true);
        //googleMobileAdsConsentManager.setCanReset(true);
        googleMobileAdsConsentManager.setDeviceHashedId("10A66C168A2774EF76E1455DF9097313");
        googleMobileAdsConsentManager.gatherConsent(this, complete -> {
            if (complete && googleMobileAdsConsentManager.canRequestAds()) {
                if (!isInit) {
                    isInit = true;

                    Application application = getApplication();
                    ((MyApplication) application).initAds();

                    if (CommonAd.getInstance().getMediationProvider() == CommonAdConfig.PROVIDER_ADMOB)
                        idAdSplash = BuildConfig.ad_interstitial_splash;
                    else
                        idAdSplash = getString(R.string.applovin_test_inter);
                }
            }

            if (getConsentResult(this) && googleMobileAdsConsentManager.canRequestAds()) {
                Admob.getInstance().loadBannerSplash(this, listIDAdsBannerSplash, arrayList, bannerCallback,5000);

            } else {
                startMain();
            }
        });

        ArrayList<String> list = new ArrayList<>();
        //list.add(getString(R.string.inter_splash));
        list.add(getString(R.string.inter_splash3));
//
//        CommonAd.getInstance().loadSplashInterstitialAds(
//                this,
//                list,
//                2500,
//                5000,
//                commonAdCallback
//            );

        ArrayList<String> listID = new ArrayList<>();
        listID.add("ca-app-pub-3940256099942544/34198352941");
        listID.add("ca-app-pub-3940256099942544/34198352941");
        listID.add("ca-app-pub-3940256099942544/34198352941");
        listID.add("ca-app-pub-3940256099942544/3419835294");

//        adCallback = new AdCallback(){
//            @Override
//            public void onNextAction() {
//                super.onNextAction();
//                startActivity(new Intent(SplashActivity.this,MainActivity.class));
//                finish();
//            }
//        };
        //AppOpenManager.getInstance().loadOpenAppAdSplash(this,"ca-app-pub-3940256099942544/3419835294",3000,10000,true,adCallback);
        //CommonAd.getInstance().loadOpenAppAdSplashFloor(this,listID,true,adCallback);

        //CommonProcess.getInstance().LoadDataAndShowAdsSplash(this,true,adCallback);
        //CommonProcess.getInstance().loadOpenAppAdSplashFloor(this,true,adCallback);
    };

    private void startMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "Splash onPause: " );
        AppOpenManager.getInstance().onCheckShowSplashWhenFail(this,adCallback,1000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "Splash onPause: " );
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "Splash onStop: " );
    }

    @Override
    protected void onDestroy() {
        AppOpenManager.getInstance().removeFullScreenContentCallback();
        super.onDestroy();
    }

}