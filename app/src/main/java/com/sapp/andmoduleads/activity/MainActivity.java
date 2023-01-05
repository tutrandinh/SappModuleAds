package com.sapp.andmoduleads.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ads.sapp.admob.Admob;
import com.ads.sapp.admob.AppOpenManager;
import com.ads.sapp.ads.CommonAd;
import com.ads.sapp.ads.CommonAdCallback;
import com.ads.sapp.ads.CommonAdConfig;
import com.ads.sapp.ads.nativeAds.CommonNativeAdView;
import com.ads.sapp.ads.wrapper.ApAdError;
import com.ads.sapp.ads.wrapper.ApInterstitialAd;
import com.ads.sapp.dialog.DialogExitApp1;
import com.ads.sapp.funtion.AdCallback;
import com.ads.sapp.funtion.DialogExitListener;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.sapp.andmoduleads.BuildConfig;
import com.sapp.andmoduleads.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private FrameLayout frAds;
    private NativeAd unifiedNativeAd;
    private ApInterstitialAd mInterstitialAd;

    private String idBanner = "";
    private String idNative = "";
    private String idInter = "";

    private int layoutNativeCustom;
    private CommonNativeAdView commonNativeAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frAds = findViewById(R.id.fl_adplaceholder);
        commonNativeAdView = findViewById(R.id.commonNativeAds);

        configMediationProvider();
        CommonAd.getInstance().setCountClickToShowAds(3);

        AppOpenManager.getInstance().setEnableScreenContentCallback(true);
        AppOpenManager.getInstance().setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent();
                Log.e("AppOpenManager", "onAdShowedFullScreenContent: ");

            }
        });

        //CommonAd.getInstance().loadNativeAd(this, idNative, layoutNativeCustom);
        commonNativeAdView.setLayoutLoading(R.layout.loading_native_medium);
        commonNativeAdView.setLayoutCustomNativeAd(layoutNativeCustom);
        commonNativeAdView.loadNativeAd(this, idNative);

        CommonAd.getInstance().loadBanner(this, idBanner);

        loadAdInterstitial();

        findViewById(R.id.btShowAds).setOnClickListener(v -> {
            if (mInterstitialAd.isReady()) {

                ApInterstitialAd inter = CommonAd.getInstance().getInterstitialAds(this, idInter);

                CommonAd.getInstance().showInterstitialAdByTimes(this, mInterstitialAd, new CommonAdCallback() {
                    @Override
                    public void onNextAction() {
                        Log.i(TAG, "onNextAction: start content and finish main");
                        startActivity(new Intent(MainActivity.this, ContentActivity.class));
                    }

                    @Override
                    public void onAdFailedToShow(@Nullable ApAdError adError) {
                        super.onAdFailedToShow(adError);
                        Log.i(TAG, "onAdFailedToShow:" + adError.getMessage());
                    }

                    @Override
                    public void onInterstitialShow() {
                        super.onInterstitialShow();
                        Log.d(TAG, "onInterstitialShow");
                    }
                }, true);
            } else {
                Toast.makeText(this, "start loading ads", Toast.LENGTH_SHORT).show();
                loadAdInterstitial();
            }
        });

        findViewById(R.id.btForceShowAds).setOnClickListener(v -> {
            if (mInterstitialAd.isReady()) {
                CommonAd.getInstance().forceShowInterstitial(this, mInterstitialAd, new CommonAdCallback() {
                    @Override
                    public void onNextAction() {
                        Log.i(TAG, "onAdClosed: start content and finish main");
                        startActivity(new Intent(MainActivity.this, SimpleListActivity.class));
                    }

                    @Override
                    public void onAdFailedToShow(@Nullable ApAdError adError) {
                        super.onAdFailedToShow(adError);
                        Log.i(TAG, "onAdFailedToShow:" + adError.getMessage());
                    }

                    @Override
                    public void onInterstitialShow() {
                        super.onInterstitialShow();
                        Log.d(TAG, "onInterstitialShow");
                    }
                }, true);
            } else {
                loadAdInterstitial();
            }

        });
    }

    private void configMediationProvider() {
        if (CommonAd.getInstance().getMediationProvider() == CommonAdConfig.PROVIDER_ADMOB) {
            idBanner = BuildConfig.ad_banner;
            idNative = BuildConfig.ad_native;
            idInter = BuildConfig.ad_interstitial_splash;
            layoutNativeCustom = com.ads.sapp.R.layout.custom_native_admod_medium_rate;
        } else {
            idBanner = getString(R.string.applovin_test_banner);
            idNative = getString(R.string.applovin_test_native);
            idInter = getString(R.string.applovin_test_inter);
            layoutNativeCustom = com.ads.sapp.R.layout.custom_native_max_medium;
        }
    }

    private void loadAdInterstitial() {
        ArrayList<String> list = new ArrayList<>();
        list.add(getString(R.string.inter_splash));
        list.add(getString(R.string.inter_splash1));
        list.add(getString(R.string.inter_splash2));
        list.add(getString(R.string.inter_splash3));
        mInterstitialAd = CommonAd.getInstance().getInterstitialAds(this, list);

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadNativeExit();
    }

    private void loadNativeExit() {

        if (unifiedNativeAd != null)
            return;

        ArrayList<String> list = new ArrayList<>();
        list.add(getString(R.string.native_id));
        list.add(getString(R.string.native_id1));
        list.add(getString(R.string.native_id2));
        list.add(getString(R.string.native_id3));

        Admob.getInstance().loadNativeAd(this,list, new AdCallback() {
            @Override
            public void onUnifiedNativeAdLoaded(NativeAd unifiedNativeAd) {
                MainActivity.this.unifiedNativeAd = unifiedNativeAd;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (unifiedNativeAd == null)
            return;

        DialogExitApp1 dialogExitApp1 = new DialogExitApp1(this, unifiedNativeAd, 1);
        dialogExitApp1.setDialogExitListener(new DialogExitListener() {
            @Override
            public void onExit(boolean exit) {
                MainActivity.super.onBackPressed();
            }
        });
        dialogExitApp1.setCancelable(false);
        dialogExitApp1.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("onActivityResult", "ProductPurchased:" + data.toString());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}