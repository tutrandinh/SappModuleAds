package com.sapp.andmoduleads.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ads.sapp.admob.Admob;
import com.ads.sapp.admob.AppOpenManager;
import com.ads.sapp.ads.CommonAd;
import com.ads.sapp.ads.CommonAdCallback;
import com.ads.sapp.ads.nativeAds.CommonNativeAdView;
import com.ads.sapp.ads.wrapper.ApAdError;
import com.ads.sapp.ads.wrapper.ApInterstitialAd;
import com.ads.sapp.dialog.DialogExitApp1;
import com.ads.sapp.funtion.AdCallback;
import com.ads.sapp.funtion.BannerCommonCallback;
import com.ads.sapp.funtion.DialogExitListener;
import com.ads.sapp.manager.BannerCommon;
import com.ads.sapp.manager.NativeCommon;
import com.ads.sapp.ui.NativeFullActivity;
import com.ads.sapp.util.nativefull.NativeFullConfig;
import com.ads.sapp.util.nativefull.NativeIntentKey;
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

    private RelativeLayout relativeLayoutAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        frAds = findViewById(com.ads.sapp.R.id.fl_adplaceholder);
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
//        commonNativeAdView.setLayoutLoading(com.ads.sapp.R.layout.loading_native_medium);
//        commonNativeAdView.setLayoutCustomNativeAd(layoutNativeCustom);
//        commonNativeAdView.loadNativeAd(this, idNative);

        ArrayList<String> listNative = new ArrayList<>();
        listNative.add("1");
        listNative.add("2");
        listNative.add("2");
        listNative.add("2");
        listNative.add(BuildConfig.ad_native);

        //Load native auto reload
        NativeCommon nativeCommon = new NativeCommon(
                this,
                findViewById(R.id.native_ad_large),
                this,
                listNative,
                R.layout.layout_native_show_large,
                R.layout.layout_native_load_large,
                new AdCallback());
        nativeCommon.setTimeIntervalReload(10000);
        nativeCommon.setReloadAdsOnResume(true);

        //CommonAd.getInstance().loadBanner(this, idBanner);
        //Using API Gup
        //CommonProcess.getInstance().loadBannerDefault(this);

        // Test check
        //CheckAds.checkAd = false; // When show only
        ArrayList<String> list = new ArrayList<>();
        list.add("1");
        list.add("1");
        list.add(BuildConfig.ad_banner);
        //Admob.getInstance().loadBannerFloorAds(this,list);
        //CommonAd.getInstance().loadCollapsibleBanner(this, idBanner, BannerGravity.bottom);

        //Load banner new, time and auto reload
        BannerCommon bannerCommon = new BannerCommon(this, this, list, new BannerCommonCallback());
        bannerCommon.setTimeIntervalReload(6000);
        bannerCommon.setReloadAdsOnResume(true);

        ArrayList<String> listID = new ArrayList<>();
        listID.add("1");
        listID.add("2");
        listID.add("3");
        listID.add(BuildConfig.ad_banner_collap);

        //CommonAd.getInstance().loadCollapsibleBannerFloor(this, listID, BannerGravity.bottom);

        //Load Banner Collapsible not reload
        //Admob.getInstance().loadCollapsibleBannerFloorCheck(this, listID, BannerGravity.bottom);

        // Load Banner Collapsible new, time and auto reload
//        BannerCollapsibleCommon bannerCollapsibleCommon = new BannerCollapsibleCommon(this, this, listID, new BannerCommonCallback());
//        bannerCollapsibleCommon.setTimeIntervalReload(4000);
//        bannerCollapsibleCommon.setReloadAdsOnResume(true);
//        bannerCollapsibleCommon.setRm(true);

        //Load from API githup
        //CommonProcess.getInstance().loadCollapsibleBannerDefaultFloor(this, BannerGravity.bottom);
        //CommonProcess.getInstance().loadCollapsibleBannerByNameFloor(this, BannerGravity.bottom, "banner_collapsible");

        loadAdInterstitial();

        findViewById(R.id.btShowAds).setOnClickListener(v -> {
            if (mInterstitialAd.isReady()) {

                ApInterstitialAd inter = CommonAd.getInstance().getInterstitialAds(this, idInter);

            } else {
                Toast.makeText(this, "start loading ads", Toast.LENGTH_SHORT).show();
                loadAdInterstitial();
            }
        });

        findViewById(R.id.btForceShowAds).setOnClickListener(v -> {
            if (mInterstitialAd.isReady()) {
                CommonAd.getInstance().forceShowInterstitial(this, mInterstitialAd, new CommonAdCallback() {
                    @Override
                    public void onAdClosedByTime() {
                        super.onAdClosedByTime();
                    }

                    @Override
                    public void onNextAction() {
                        Log.i(TAG, "onAdClosed: start content and finish main");
                       // startActivity(new Intent(MainActivity.this, SimpleListActivity.class));

                        Intent nextIntent = new Intent(MainActivity.this, SimpleListActivity.class);
                        nextIntent.putExtra("user_id", 123);
                        nextIntent.putExtra("from", "ActivityA");

                        ArrayList<String> adIds = new ArrayList<>();
                        adIds.add("a");
                        adIds.add("ca-app-pub-3940256099942544/2247696110");

                        NativeFullConfig config = new NativeFullConfig(
                                true,
                                adIds,
                                com.ads.sapp.R.layout.layout_native_full_load,
                                com.ads.sapp.R.layout.layout_native_full_show,
                                50000,
                                true,
                                10000
                        );
                        Admob.getInstance().setOpenActivityAfterShowInterAds(false);
                        CommonAd.getInstance().startNativeFull(MainActivity.this, config, nextIntent);
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
                startActivity(new Intent(MainActivity.this, SimpleListActivity.class));
            }

        });

        findViewById(R.id.btNativeFull).setOnClickListener(v -> {
            Intent nextIntent = new Intent(MainActivity.this, SimpleListActivity.class);
            nextIntent.putExtra("user_id", 123);
            nextIntent.putExtra("from", "ActivityA");

            ArrayList<String> adIds = new ArrayList<>();
            adIds.add("a");
            adIds.add("ca-app-pub-3940256099942544/2247696110");

            NativeFullConfig config = new NativeFullConfig(
                    true,
                    adIds,
                    com.ads.sapp.R.layout.layout_native_full_load,
                    com.ads.sapp.R.layout.layout_native_full_show,
                    50000,
                    true,
                    0
            );
            Admob.getInstance().setOpenActivityAfterShowInterAds(false);
            CommonAd.getInstance().startNativeFull(MainActivity.this, config, nextIntent);
        });
    }

    private void configMediationProvider() {
        idBanner = BuildConfig.ad_banner_collap;
        idNative = BuildConfig.ad_native;
        idInter = BuildConfig.ad_interstitial_splash;
        layoutNativeCustom = com.ads.sapp.R.layout.custom_native_admob_free_size;
//        if (CommonAd.getInstance().getMediationProvider() == CommonAdConfig.PROVIDER_ADMOB) {
//            idBanner = BuildConfig.ad_banner_collap;
//            idNative = BuildConfig.ad_native;
//            idInter = BuildConfig.ad_interstitial_splash;
//            layoutNativeCustom = com.ads.sapp.R.layout.custom_native_admob_free_size;
//        } else {
//            idBanner = getString(R.string.applovin_test_banner);
//            idNative = getString(R.string.applovin_test_native);
//            idInter = getString(R.string.applovin_test_inter);
//            layoutNativeCustom = com.ads.sapp.R.layout.custom_native_max_medium;
//        }
    }

    private void loadAdInterstitial() {
        ArrayList<String> list = new ArrayList<>();
        //list.add(getString(R.string.inter_splash));
        //list.add(getString(R.string.inter_splash1));
        //list.add(getString(R.string.inter_splash2));
        list.add(getString(R.string.inter_splash3));
        mInterstitialAd = CommonAd.getInstance().getInterstitialAdsCheck(this, list);
//        mInterstitialAd = CommonProcess.getInstance().getInterstitialAdsInterIntro(this);
        //mInterstitialAd = CommonProcess.getInstance().getInterstitialAdsInterByName(this,"inter_all");
       //mInterstitialAd = CommonAd.getInstance().getInterstitialAdsMax(this,"62d68af9086fc062");

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

//        Admob.getInstance().loadNativeAd(this,list, new AdCallback() {
//            @Override
//            public void onUnifiedNativeAdLoaded(NativeAd unifiedNativeAd) {
//                MainActivity.this.unifiedNativeAd = unifiedNativeAd;
//            }
//        });

//        Boolean a = CommonProcess.getInstance().loadAdsNativeByName(this,"native_home1", new AdCallback() {
//            @Override
//            public void onUnifiedNativeAdLoaded(NativeAd unifiedNativeAd) {
//                MainActivity.this.unifiedNativeAd = unifiedNativeAd;
//            }
//        });

//       ArrayList<String>  arrayList =  CommonProcess.getInstance().getDataByNameId("native_language");
//       for (String ads : arrayList) {
//            Log.d("arrayList",ads);
//       }
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