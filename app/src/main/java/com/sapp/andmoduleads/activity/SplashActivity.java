package com.sapp.andmoduleads.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.ads.sapp.admob.AppOpenManager;
import com.ads.sapp.ads.CommonAd;
import com.ads.sapp.ads.CommonAdCallback;
import com.ads.sapp.ads.CommonAdConfig;
import com.ads.sapp.ads.CommonInitCallback;
import com.ads.sapp.applovin.AppOpenMax;
import com.sapp.andmoduleads.BuildConfig;
import com.sapp.andmoduleads.MyApplication;
import com.sapp.andmoduleads.R;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private CommonAdCallback adCallback  = null;
    private static final String TAG = "SplashActivity";
    private List<String> list = new ArrayList<>();
    private String idAdSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (CommonAd.getInstance().getMediationProvider() == CommonAdConfig.PROVIDER_ADMOB)
            idAdSplash = BuildConfig.ad_interstitial_splash;
        else
            idAdSplash = getString(R.string.applovin_test_inter);

        adCallback = new CommonAdCallback() {
            @Override
            public void onNextAction() {
                super.onNextAction();
                Log.d(TAG, "onNextAction");
                startMain();

                //When use Max
                //AppOpenMax.getInstance().loadAdResume(MyApplication.getApplication(), getString(R.string.applovin_test_app_open));
            }
        };

        ArrayList<String> list = new ArrayList<>();
        list.add(getString(R.string.inter_splash));
        list.add(getString(R.string.inter_splash1));
        list.add(getString(R.string.inter_splash2));
        list.add(getString(R.string.inter_splash3));

        CommonAd.getInstance().loadSplashInterstitialAds(
                this,
                list,
                25000,
                5000,
                adCallback
            );
    };

    private void startMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "Splash onPause: " );
        CommonAd.getInstance().onCheckShowSplashWhenFail(this, adCallback, 1000);
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