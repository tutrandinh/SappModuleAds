package com.sapp.andmoduleads.activity;

import static com.ads.sapp.call.api.event.EventCommon.COST;
import static com.ads.sapp.call.api.event.EventCommon.DEFAULT_VALUE;
import static com.ads.sapp.call.api.event.EventCommon.ROAS;
import static com.ads.sapp.call.api.event.EventCommon.cost;
import static com.ads.sapp.call.api.event.EventCommon.defaultValue;
import static com.ads.sapp.call.api.event.EventCommon.roas;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ads.sapp.admob.AppOpenManager;
import com.ads.sapp.ads.CommonAd;
import com.ads.sapp.ads.CommonAdCallback;
import com.ads.sapp.ads.CommonAdConfig;
import com.ads.sapp.call.api.event.EventCommon;
import com.ads.sapp.funtion.AdCallback;
import com.sapp.andmoduleads.BuildConfig;
import com.sapp.andmoduleads.R;
import com.ads.sapp.call.api.CommonProcess;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
//    private CommonAdCallback adCallback  = null;
    private AdCallback adCallback  = null;
    private CommonAdCallback commonAdCallback = null;
    private static final String TAG = "SplashActivity";
    private List<String> list = new ArrayList<>();
    private String idAdSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //Set default event
        EventCommon.roas = ROAS;
        EventCommon.cost = COST;
        EventCommon.defaultValue = DEFAULT_VALUE;

        if (CommonAd.getInstance().getMediationProvider() == CommonAdConfig.PROVIDER_ADMOB)
            idAdSplash = BuildConfig.ad_interstitial_splash;
        else
            idAdSplash = getString(R.string.applovin_test_inter);

        commonAdCallback = new CommonAdCallback() {
            @Override
            public void onNextAction() {
                super.onNextAction();
                Log.d(TAG, "onNextAction");
                startMain();

                //When use Max
                //AppOpenMax.getInstance().loadAdResume(MyApplication.getApplication(), getString(R.string.applovin_test_app_open));
            }
        };

        //Load data event
        EventCommon.getInstance().LoadDataEvent(this);

        ArrayList<String> list = new ArrayList<>();
        list.add(getString(R.string.inter_splash));
        list.add(getString(R.string.inter_splash1));
        list.add(getString(R.string.inter_splash2));
        list.add(getString(R.string.inter_splash3));
//
        CommonAd.getInstance().loadSplashInterstitialAdsMax(
                this,
                "62d68af9086fc062",
                25000,
                5000,
                commonAdCallback
            );

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

        //Check load event Data
        Log.d("EventModel",roas.toString() +"_S___1");
        Log.d("EventModel",cost.toString() +"_S___2");
        Log.d("EventModel",defaultValue.toString() +"_S___3");

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