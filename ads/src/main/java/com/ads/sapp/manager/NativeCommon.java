package com.ads.sapp.manager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.ads.sapp.admob.Admob;
import com.ads.sapp.funtion.AdCallback;
import com.ads.sapp.funtion.NativeCommonCallback;
import com.ads.sapp.util.CheckAds;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

import java.util.ArrayList;

public class NativeCommon implements LifecycleEventObserver {

    private static final String TAG = "NativeCommon";
    private Activity activity;
    private final LifecycleOwner lifecycleOwner;
    private ViewGroup viewGroup;

    private boolean activeReload = true;
    private boolean reloadAds = false;
    private boolean reloadAdsOnResume = false;

    // Time reload, set = 0: no reload
    private long timeIntervalReload = 0;
    private boolean isStop = false;
    private CountDownTimer countDownTimer;

    //Setting number reload if fail
    private int totalLoad = 1; //Count times reload
    private int totalLoadMax = 10; // Max default 3 times

    //Setting loading
    private boolean isShimmer = true;
    //Setting remove view after ads new show
    private boolean isRm = false;

    private boolean onLoad = false;

    private AdCallback adCallback;
    //Config Ads
    private ArrayList listID;
    private int layoutId;
    private int layoutIdSimmer;

    public NativeCommon(Activity activity, ViewGroup viewGroup, LifecycleOwner lifecycleOwner,ArrayList listID, int layoutId, int layoutIdSimmer, final AdCallback adCallback) {
        this.reloadAds = true;
        this.lifecycleOwner = lifecycleOwner;
        this.lifecycleOwner.getLifecycle().addObserver(this);
        this.activity = activity;
        this.viewGroup = viewGroup;
        this.adCallback = adCallback;
        this.listID = listID;
        this.layoutId = layoutId;
        this.layoutIdSimmer = layoutIdSimmer;
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        switch (event) {
            case ON_CREATE:
                loadNativeInterval();
                Log.d(TAG,"loadNativeInterval: ON_CREATE");
                break;
            case ON_RESUME:
                Log.d(TAG,"loadNativeInterval: ON_RESUME");

                try{
                    if (countDownTimer != null && isStop) {
                        startReloadNative();
                    }
                    if (isStop && (reloadAds || reloadAdsOnResume)) {
                        reloadAds = false;
                        loadNativeInterval();
                    }
                    isStop = false;
                    break;
                }catch (Exception e){
                    break;
                }
            case ON_PAUSE:
                Log.d(TAG,"loadNativeInterval: ON_PAUSE");

                try{
                    isStop = true;
                    if (countDownTimer != null) {
                        countDownTimer.cancel();
                    }
                    break;
                }catch (Exception e){
                   break;
                }
            case ON_DESTROY:
                Log.d(TAG,"loadNativeInterval: ON_DESTROY");
                try{
                    if(viewGroup != null){
                        viewGroup.removeAllViews();
                    }

                    if(lifecycleOwner !=null){
                        this.lifecycleOwner.getLifecycle().removeObserver(this);
                    }
                    break;
                }catch (Exception e){
                    break;
                }
        }
    }

    public void loadNativeInterval() {
        try{
            Log.d(TAG,"loadNativeInterval: loadNativeInterval");

            if(!CheckAds.getInstance().isShowAds(activity)){
                viewGroup.removeAllViews();
                return;
            }

            if(isRm){
                viewGroup.removeAllViews();
            }

            //Show loading Shimmer
            if(isShimmer){
                Log.d(TAG,"loadNativeInterval: isShimmer");

                @SuppressLint("InflateParams") NativeAdView adViewLoad = (NativeAdView) LayoutInflater.from(activity).inflate(layoutIdSimmer, null);
                viewGroup.removeAllViews();
                viewGroup.addView(adViewLoad);
                viewGroup.setVisibility(View.VISIBLE);
            }

            ArrayList listIdOrigin = new ArrayList<>();

            for(Object id: listID){
                listIdOrigin.add(id);
            }

            Log.d(TAG,"loadNativeInterval: listID: "+ listID.toString());

            Admob.getInstance().loadNativeAd(activity, listIdOrigin, adCallback, new NativeCommonCallback() {
                @Override
                public void onUnifiedNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
                    @SuppressLint("InflateParams") NativeAdView adView = (NativeAdView) LayoutInflater.from(activity).inflate(layoutId, null);
                    viewGroup.removeAllViews();
                    viewGroup.addView(adView);
                    Admob.getInstance().populateUnifiedNativeAdView(unifiedNativeAd, adView);
                    CheckAds.getInstance().checkAds(adView, CheckAds.OT);
                    adCallback.onUnifiedNativeAdLoaded(unifiedNativeAd);
                }

                @Override
                public void onAdFailedToLoad() {
                    super.onAdFailedToLoad();
                    Log.d(TAG,"loadNativeInterval: onAdFailedToLoad");

                    viewGroup.setVisibility(View.GONE);
                    if(totalLoad <= totalLoadMax){
                        totalLoad = totalLoad + 1;
                        startReloadNative();
                    }
                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                    Log.d(TAG,"loadNativeInterval: onAdImpression");

                    onLoad = false;
                    startReloadNative();

                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    onLoad = true;
                }
            });

        }catch (Exception e){
            viewGroup.removeAllViews();
        }
    }

    public void setTimeIntervalReload(long timeIntervalReload) {
        if (timeIntervalReload > 0 && this.activeReload == true) {
            this.timeIntervalReload = timeIntervalReload;
            countDownTimer = new CountDownTimer(this.timeIntervalReload, 1000) {
                @Override
                public void onTick(long l) {
                }

                @Override
                public void onFinish() {
                    loadNativeInterval();
                }
            };
        }
    }

    private void startReloadNative() {
        if (countDownTimer != null && this.lifecycleOwner.getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
            countDownTimer.cancel();
            countDownTimer.start();
        }
    }

    public void setReloadAdsOnResume(boolean reloadAdsOnResume) {
        this.reloadAdsOnResume = reloadAdsOnResume;
    }

    public void cancelAutoReloadNative() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public void activeAutoReloadNative() {
        if (countDownTimer != null) {
            countDownTimer.start();
        }
    }

    public void unActiveReloadNative() {
        activeReload = false;
    }

    public void setShimmer(boolean shimmer) {
        isShimmer = shimmer;
    }

    public void setRm(boolean rm) {
        isRm = rm;
    }

    /// ...
    /// Set totalLoadMax = 0 when not reload on fail load Ads
    ///

    public void MaxTotalFailToLoad(int totalLoadMax) {
        this.totalLoadMax = totalLoadMax;
    }

}
