package com.ads.sapp.ads;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.ads.sapp.R;
import com.ads.sapp.admob.Admob;
import com.ads.sapp.admob.AppOpenManager;
import com.ads.sapp.ads.nativeAds.CommonAdAdapter;
import com.ads.sapp.ads.nativeAds.CommonAdPlacer;
import com.ads.sapp.ads.wrapper.ApAdError;
import com.ads.sapp.ads.wrapper.ApInterstitialAd;
import com.ads.sapp.ads.wrapper.ApNativeAd;
import com.ads.sapp.ads.wrapper.ApRewardAd;
import com.ads.sapp.ads.wrapper.ApRewardItem;
import com.ads.sapp.funtion.AdCallback;
import com.ads.sapp.funtion.RewardCallback;
import com.ads.sapp.ui.NativeFullActivity;
import com.ads.sapp.util.AppUtil;
import com.ads.sapp.util.CheckAds;
import com.ads.sapp.util.nativefull.NativeFullConfig;
import com.ads.sapp.util.nativefull.NativeIntentKey;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;

import java.util.ArrayList;

public class CommonAd {
    public static final String TAG = "CommonAd";
    private static volatile CommonAd INSTANCE;
    private CommonAdConfig adConfig;
    private CommonInitCallback initCallback;
    private Boolean initAdSuccess = false;


    public static synchronized CommonAd getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CommonAd();
        }
        return INSTANCE;
    }

    /**
     * Set count click to show ads interstitial when call showInterstitialAdByTimes()
     *
     * @param countClickToShowAds - default = 3
     */
    public void setCountClickToShowAds(int countClickToShowAds) {
        Admob.getInstance().setNumToShowAds(countClickToShowAds);
    }

    /**
     * Set count click to show ads interstitial when call showInterstitialAdByTimes()
     *
     * @param countClickToShowAds Default value = 3
     * @param currentClicked      Default value = 0
     */
    public void setCountClickToShowAds(int countClickToShowAds, int currentClicked) {
        Admob.getInstance().setNumToShowAds(countClickToShowAds, currentClicked);
    }


    /**
     * @param context
     * @param adConfig CommonAdConfig object used for SDK initialisation
     */
    public void init(Application context, CommonAdConfig adConfig) {
        init(context, adConfig, false);
    }

    /**
     * @param context
     * @param adConfig             CommonAdConfig object used for SDK initialisation
     * @param enableDebugMediation set show Mediation Debugger - use only for Max Mediation
     */
    public void init(Application context, CommonAdConfig adConfig, Boolean enableDebugMediation) {
        if (adConfig == null) {
            throw new RuntimeException("cant not set CommonAdConfig null");
        }
        this.adConfig = adConfig;
        AppUtil.VARIANT_DEV = adConfig.isVariantDev();
        Log.i(TAG, "Config variant dev: " + AppUtil.VARIANT_DEV);

        switch (adConfig.getMediationProvider()) {
            case CommonAdConfig.PROVIDER_MAX:
                break;
            case CommonAdConfig.PROVIDER_ADMOB:
                Admob.getInstance().init(context, adConfig.getListDeviceTest());
                if (adConfig.isEnableAdResume())
                    AppOpenManager.getInstance().init(adConfig.getApplication(), adConfig.getIdAdResume());

                initAdSuccess = true;
                if (initCallback != null)
                    initCallback.initAdSuccess();
                break;
        }
    }

    public int getMediationProvider() {
        if(adConfig == null){
            return CommonAdConfig.PROVIDER_ADMOB;
        }else{
            return adConfig.getMediationProvider();
        }
    }

    public void setInitCallback(CommonInitCallback initCallback) {
        this.initCallback = initCallback;
        if (initAdSuccess)
            initCallback.initAdSuccess();
    }

    public void loadBanner(final Activity mActivity, String id) {
        switch (adConfig.getMediationProvider()) {
            case CommonAdConfig.PROVIDER_ADMOB:
                Admob.getInstance().loadBanner(mActivity, id);
                break;
            case CommonAdConfig.PROVIDER_MAX:
        }
    }

    public void loadCollapsibleBanner(final Activity mActivity, String id, String gravity) {
        switch (adConfig.getMediationProvider()) {
            case CommonAdConfig.PROVIDER_ADMOB:
                Admob.getInstance().loadCollapsibleBanner(mActivity, id, gravity);
                break;
        }
    }

    public void loadCollapsibleBannerFloor(final Activity mActivity, ArrayList<String> listID, String gravity) {
        switch (adConfig.getMediationProvider()) {
            case CommonAdConfig.PROVIDER_ADMOB:
                Admob.getInstance().loadCollapsibleBannerFloor(mActivity, listID, gravity);
                break;
        }
    }

    public void loadCollapsibleBannerFragment(final Activity mActivity, String id, final View rootView, String gravity) {
        switch (adConfig.getMediationProvider()) {
            case CommonAdConfig.PROVIDER_ADMOB:
                Admob.getInstance().loadCollapsibleBannerFragment(mActivity, id, rootView,gravity);
                break;
        }
    }

    public void loadCollapsibleBannerFragmentFloor(final Activity mActivity, ArrayList<String> listID, final View rootView, String gravity) {
        switch (adConfig.getMediationProvider()) {
            case CommonAdConfig.PROVIDER_ADMOB:
                Admob.getInstance().loadCollapsibleBannerFragmentFloor(mActivity,listID,rootView, gravity);
                break;
        }
    }

    public void loadBanner(final Activity mActivity, String id, final AdCallback adCallback) {
        switch (adConfig.getMediationProvider()) {
            case CommonAdConfig.PROVIDER_ADMOB:
                Admob.getInstance().loadBanner(mActivity, id, adCallback);
                break;
            case CommonAdConfig.PROVIDER_MAX:
        }
    }

    public void loadBannerFragment(final Activity mActivity, String id, final View rootView) {
        switch (adConfig.getMediationProvider()) {
            case CommonAdConfig.PROVIDER_ADMOB:
                Admob.getInstance().loadBannerFragment(mActivity, id, rootView);
                break;
            case CommonAdConfig.PROVIDER_MAX:
        }
    }

    public void loadBannerFragment(final Activity mActivity, String id, final View rootView, final AdCallback adCallback) {
        switch (adConfig.getMediationProvider()) {
            case CommonAdConfig.PROVIDER_ADMOB:
                Admob.getInstance().loadBannerFragment(mActivity, id, rootView, adCallback);
                break;
            case CommonAdConfig.PROVIDER_MAX:
        }
    }

    public void loadSplashInterstitialAds(final Context context,String id, long timeOut, long timeDelay, CommonAdCallback adListener) {
        loadSplashInterstitialAds(context, id, timeOut, timeDelay, true, adListener);
    }

    public void loadSplashInterstitialAdsMax(final Context context,String id, long timeOut, long timeDelay, CommonAdCallback adListener) {
        loadSplashInterstitialAdsMax(context, id, timeOut, timeDelay, true, adListener);
    }

    public void loadSplashInterstitialAds(final Context context, ArrayList<String> listID, long timeOut, long timeDelay, CommonAdCallback adListener) {
        loadSplashInterstitialAds(context, listID, timeOut, timeDelay, true, adListener);
    }

    public void loadSplashInterstitialAdsCheck(final Context context, ArrayList<String> listID, long timeOut, long timeDelay, CommonAdCallback adListener) {
        if(!CheckAds.getInstance().isShowAds(context)){
            adListener.onAdFailedToLoad(null);
            adListener.onNextAction();
        }else{
            loadSplashInterstitialAds(context, listID, timeOut, timeDelay, true, adListener);
        }
    }

    public void loadSplashInterstitialAdsNew(final Context context, ArrayList<String> listID, long timeOut, long timeDelay, CommonAdCallback adListener) {
        loadSplashInterstitialAdsNew(context, listID, timeOut, timeDelay, true, adListener);
    }

    public void loadSplashInterstitialAds(final Context context, String id, long timeOut, long timeDelay, boolean showSplashIfReady, CommonAdCallback adListener) {
        switch (adConfig.getMediationProvider()) {
            case CommonAdConfig.PROVIDER_ADMOB:
                Admob.getInstance().loadSplashInterstitialAds(context, id, timeOut, timeDelay, showSplashIfReady, new AdCallback() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        adListener.onAdClosed();
                    }

                    @Override
                    public void onNextAction() {
                        super.onNextAction();
                        adListener.onNextAction();
                    }

                    @Override
                    public void onAdFailedToLoad(@Nullable LoadAdError i) {
                        super.onAdFailedToLoad(i);
                        adListener.onAdFailedToLoad(new ApAdError(i));

                    }

                    @Override
                    public void onAdFailedToShow(@Nullable AdError adError) {
                        super.onAdFailedToShow(adError);
                        adListener.onAdFailedToShow(new ApAdError(adError));

                    }

                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        adListener.onAdLoaded();
                    }

                    @Override
                    public void onAdSplashReady() {
                        super.onAdSplashReady();
                        adListener.onAdSplashReady();
                    }


                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                        if (adListener != null) {
                            adListener.onAdClicked();
                        }
                    }
                });
                break;
            case CommonAdConfig.PROVIDER_MAX:
                break;
        }
    }

    public void loadSplashInterstitialAdsMax(final Context context, String id, long timeOut, long timeDelay, boolean showSplashIfReady, CommonAdCallback adListener) {
    }

    public void loadSplashInterstitialAds(final Context context, ArrayList<String> listID, long timeOut, long timeDelay, boolean showSplashIfReady, CommonAdCallback adListener) {
        switch (adConfig.getMediationProvider()) {
            case CommonAdConfig.PROVIDER_ADMOB:
                switch (adConfig.getMediationFloor()){
                    case CommonAdConfig.FLOOR:
                        Admob.getInstance().loadSplashInterstitialAds(context, listID.get(listID.size() - 1), timeOut, timeDelay, showSplashIfReady, new AdCallback() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                adListener.onAdClosed();
                            }

                            @Override
                            public void onNextAction() {
                                super.onNextAction();
                                adListener.onNextAction();
                            }

                            @Override
                            public void onAdFailedToLoad(@Nullable LoadAdError i) {
                                super.onAdFailedToLoad(i);
                                adListener.onAdFailedToLoad(new ApAdError(i));

                            }

                            @Override
                            public void onAdFailedToShow(@Nullable AdError adError) {
                                super.onAdFailedToShow(adError);
                                adListener.onAdFailedToShow(new ApAdError(adError));

                            }

                            @Override
                            public void onAdLoaded() {
                                super.onAdLoaded();
                                adListener.onAdLoaded();
                            }

                            @Override
                            public void onAdSplashReady() {
                                super.onAdSplashReady();
                                adListener.onAdSplashReady();
                            }


                            @Override
                            public void onAdClicked() {
                                super.onAdClicked();
                                if (adListener != null) {
                                    adListener.onAdClicked();
                                }
                            }
                        });
                        break;
                    case CommonAdConfig.WARTER_FALL:
                        Admob.getInstance().loadSplashInterstitialAds(context, listID, timeOut, timeDelay, showSplashIfReady, new AdCallback() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                adListener.onAdClosed();
                            }

                            @Override
                            public void onNextAction() {
                                super.onNextAction();
                                adListener.onNextAction();
                            }

                            @Override
                            public void onAdFailedToLoad(@Nullable LoadAdError i) {
                                super.onAdFailedToLoad(i);
                                adListener.onAdFailedToLoad(new ApAdError(i));

                            }

                            @Override
                            public void onAdFailedToShow(@Nullable AdError adError) {
                                super.onAdFailedToShow(adError);
                                adListener.onAdFailedToShow(new ApAdError(adError));

                            }

                            @Override
                            public void onAdLoaded() {
                                super.onAdLoaded();
                                adListener.onAdLoaded();
                            }

                            @Override
                            public void onAdSplashReady() {
                                super.onAdSplashReady();
                                adListener.onAdSplashReady();
                            }


                            @Override
                            public void onAdClicked() {
                                super.onAdClicked();
                                if (adListener != null) {
                                    adListener.onAdClicked();
                                }
                            }
                        });
                        break;
                }
        }
    }

    public void loadSplashInterstitialAdsNew(final Context context, ArrayList<String> listID, long timeOut, long timeDelay, boolean showSplashIfReady, CommonAdCallback adListener) {
        switch (adConfig.getMediationProvider()) {
            case CommonAdConfig.PROVIDER_ADMOB:
                switch (adConfig.getMediationFloor()){
                    case CommonAdConfig.FLOOR:
                        Admob.getInstance().loadSplashInterstitialAds(context, listID.get(listID.size() - 1), timeOut, timeDelay, showSplashIfReady, new AdCallback() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                adListener.onAdClosed();
                            }

                            @Override
                            public void onNextAction() {
                                super.onNextAction();
                                adListener.onNextAction();
                            }

                            @Override
                            public void onAdFailedToLoad(@Nullable LoadAdError i) {
                                super.onAdFailedToLoad(i);
                                adListener.onAdFailedToLoad(new ApAdError(i));

                            }

                            @Override
                            public void onAdFailedToShow(@Nullable AdError adError) {
                                super.onAdFailedToShow(adError);
                                adListener.onAdFailedToShow(new ApAdError(adError));

                            }

                            @Override
                            public void onAdLoaded() {
                                super.onAdLoaded();
                                adListener.onAdLoaded();
                            }

                            @Override
                            public void onAdSplashReady() {
                                super.onAdSplashReady();
                                adListener.onAdSplashReady();
                            }


                            @Override
                            public void onAdClicked() {
                                super.onAdClicked();
                                if (adListener != null) {
                                    adListener.onAdClicked();
                                }
                            }
                        });
                        break;
                    case CommonAdConfig.WARTER_FALL:
                        Admob.getInstance().loadSplashInterstitialAdsNew(context, listID, timeOut, timeDelay, showSplashIfReady, new AdCallback() {
                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                adListener.onAdClosed();
                            }

                            @Override
                            public void onNextAction() {
                                super.onNextAction();
                                adListener.onNextAction();
                            }

                            @Override
                            public void onAdFailedToLoad(@Nullable LoadAdError i) {
                                super.onAdFailedToLoad(i);
                                adListener.onAdFailedToLoad(new ApAdError(i));

                            }

                            @Override
                            public void onAdFailedToShow(@Nullable AdError adError) {
                                super.onAdFailedToShow(adError);
                                adListener.onAdFailedToShow(new ApAdError(adError));

                            }

                            @Override
                            public void onAdLoaded() {
                                super.onAdLoaded();
                                adListener.onAdLoaded();
                            }

                            @Override
                            public void onAdSplashReady() {
                                super.onAdSplashReady();
                                adListener.onAdSplashReady();
                            }


                            @Override
                            public void onAdClicked() {
                                super.onAdClicked();
                                if (adListener != null) {
                                    adListener.onAdClicked();
                                }
                            }
                        });
                        break;
                }
        }
    }


    public void onShowSplash(AppCompatActivity activity, CommonAdCallback adListener) {
        switch (adConfig.getMediationProvider()) {
            case CommonAdConfig.PROVIDER_ADMOB:
                Admob.getInstance().onShowSplash(activity, new AdCallback() {
                            @Override
                            public void onAdFailedToShow(@Nullable AdError adError) {
                                super.onAdFailedToShow(adError);
                                adListener.onAdFailedToShow(new ApAdError(adError));
                            }

                            @Override
                            public void onAdClosed() {
                                super.onAdClosed();
                                adListener.onAdClosed();
                            }

                            @Override
                            public void onNextAction() {
                                super.onNextAction();
                                adListener.onNextAction();
                            }


                        }
                );
                break;
            case CommonAdConfig.PROVIDER_MAX:
        }
    }

    /**
     * Called  on Resume - SplashActivity
     * It call reshow ad splash when ad splash show fail in background
     *
     * @param activity
     * @param callback
     * @param timeDelay time delay before call show ad splash (ms)
     */
    public void onCheckShowSplashWhenFail(AppCompatActivity activity, CommonAdCallback callback,
                                          int timeDelay) {
        switch (adConfig.getMediationProvider()) {
            case CommonAdConfig.PROVIDER_ADMOB:
                Admob.getInstance().onCheckShowSplashWhenFail(activity, new AdCallback() {
                    @Override
                    public void onNextAction() {
                        super.onAdClosed();
                        callback.onNextAction();
                    }


                    @Override
                    public void onAdLoaded() {
                        super.onAdLoaded();
                        callback.onAdLoaded();
                    }

                    @Override
                    public void onAdFailedToLoad(@Nullable LoadAdError i) {
                        super.onAdFailedToLoad(i);
                        callback.onAdFailedToLoad(new ApAdError(i));
                    }

                    @Override
                    public void onAdFailedToShow(@Nullable AdError adError) {
                        super.onAdFailedToShow(adError);
                        callback.onAdFailedToShow(new ApAdError(adError));
                    }
                }, timeDelay);
                break;
            case CommonAdConfig.PROVIDER_MAX:
                break;
        }
    }

    public void loadOpenAppAdSplashFloor(Context context, ArrayList<String> listIDResume, boolean isShowAdIfReady, AdCallback adCallback) {
        switch (adConfig.getMediationProvider()) {
            case CommonAdConfig.PROVIDER_ADMOB:
                AppOpenManager.getInstance().loadOpenAppAdSplashFloor(context,listIDResume,isShowAdIfReady,adCallback);
            case CommonAdConfig.PROVIDER_MAX:
                return;
            default:
                return;
        }
    }


    /**
     * Result a ApInterstitialAd in onInterstitialLoad
     *
     * @param context
     * @param id         admob or max mediation
     * @param adListener
     */
    public ApInterstitialAd getInterstitialAds(Context context, String id, CommonAdCallback adListener) {
        ApInterstitialAd apInterstitialAd = new ApInterstitialAd();
        switch (adConfig.getMediationProvider()) {
            case CommonAdConfig.PROVIDER_ADMOB:
                Admob.getInstance().getInterstitialAds(context, id, new AdCallback() {
                    @Override
                    public void onInterstitialLoad(@Nullable InterstitialAd interstitialAd) {
                        super.onInterstitialLoad(interstitialAd);
                        Log.d(TAG, "Admob onInterstitialLoad");
                        apInterstitialAd.setInterstitialAd(interstitialAd);
                        adListener.onInterstitialLoad(apInterstitialAd);
                    }

                    @Override
                    public void onAdFailedToLoad(@Nullable LoadAdError i) {
                        super.onAdFailedToLoad(i);
                        adListener.onAdFailedToLoad(new ApAdError(i));
                    }

                    @Override
                    public void onAdFailedToShow(@Nullable AdError adError) {
                        super.onAdFailedToShow(adError);
                        adListener.onAdFailedToShow(new ApAdError(adError));
                    }

                });
                return apInterstitialAd;

            default:
                return apInterstitialAd;
        }
    }

    /**
     * Result a ApInterstitialAd in onInterstitialLoad
     *
     * @param context
     * @param id      admob or max mediation
     */
    public ApInterstitialAd getInterstitialAds(Context context, String id) {
        ApInterstitialAd apInterstitialAd = new ApInterstitialAd();
        switch (adConfig.getMediationProvider()) {
            case CommonAdConfig.PROVIDER_ADMOB:
                Admob.getInstance().getInterstitialAds(context, id, new AdCallback() {
                    @Override
                    public void onInterstitialLoad(@Nullable InterstitialAd interstitialAd) {
                        super.onInterstitialLoad(interstitialAd);
                        Log.d(TAG, "Admob onInterstitialLoad: ");
                        apInterstitialAd.setInterstitialAd(interstitialAd);
                    }

                    @Override
                    public void onAdFailedToLoad(@Nullable LoadAdError i) {
                        super.onAdFailedToLoad(i);
                    }

                    @Override
                    public void onAdFailedToShow(@Nullable AdError adError) {
                        super.onAdFailedToShow(adError);
                    }

                });
                return apInterstitialAd;

            default:
                return apInterstitialAd;
        }
    }

    /**
     * Result a ApInterstitialAd in onInterstitialLoad
     *
     * @param context
     * @param listID      admob or max mediation
     */
    public ApInterstitialAd getInterstitialAds(Context context, ArrayList<String> listID) {
        ApInterstitialAd apInterstitialAd = new ApInterstitialAd();
        switch (adConfig.getMediationProvider()) {
            case CommonAdConfig.PROVIDER_ADMOB:
                switch (adConfig.getMediationFloor()){
                    case CommonAdConfig.FLOOR:
                        if(listID.size() == 0){
                            apInterstitialAd.setInterstitialAd(null);
                        }
                        if(listID.size() > 0){
                            Admob.getInstance().getInterstitialAds(context, listID.get(listID.size() - 1), new AdCallback() {
                                @Override
                                public void onInterstitialLoad(@Nullable InterstitialAd interstitialAd) {
                                    super.onInterstitialLoad(interstitialAd);
                                    Log.d(TAG, "Admob onInterstitialLoad: ");
                                    apInterstitialAd.setInterstitialAd(interstitialAd);
                                }

                                @Override
                                public void onAdFailedToLoad(@Nullable LoadAdError i) {
                                    super.onAdFailedToLoad(i);
                                }

                                @Override
                                public void onAdFailedToShow(@Nullable AdError adError) {
                                    super.onAdFailedToShow(adError);
                                }

                            });
                        }
                        return apInterstitialAd;
                    case CommonAdConfig.WARTER_FALL:
                        Admob.getInstance().getInterstitialAds(context, listID, new AdCallback() {
                            @Override
                            public void onInterstitialLoad(@Nullable InterstitialAd interstitialAd) {
                                super.onInterstitialLoad(interstitialAd);
                                Log.d(TAG, "Admob onInterstitialLoad: ");
                                apInterstitialAd.setInterstitialAd(interstitialAd);
                            }

                            @Override
                            public void onAdFailedToLoad(@Nullable LoadAdError i) {
                                super.onAdFailedToLoad(i);
                            }

                            @Override
                            public void onAdFailedToShow(@Nullable AdError adError) {
                                super.onAdFailedToShow(adError);
                            }

                        });
                        return apInterstitialAd;
                }
            default:
                return apInterstitialAd;
        }
    }

    public ApInterstitialAd getInterstitialAdsCheck(Context context, ArrayList<String> listID) {
        ApInterstitialAd apInterstitialAd = new ApInterstitialAd();
        Admob.getInstance().getInterstitialAdsCheck(context, listID, new AdCallback() {
            @Override
            public void onInterstitialLoad(@Nullable InterstitialAd interstitialAd) {
                super.onInterstitialLoad(interstitialAd);
                Log.d(TAG, "Admob onInterstitialLoad: ");
                apInterstitialAd.setInterstitialAd(interstitialAd);
            }

            @Override
            public void onAdFailedToLoad(@Nullable LoadAdError i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdFailedToShow(@Nullable AdError adError) {
                super.onAdFailedToShow(adError);
            }

        });
        return apInterstitialAd;
    }

    /**
     * Called force show ApInterstitialAd when ready
     *
     * @param context
     * @param mInterstitialAd
     * @param callback
     */
    public void forceShowInterstitial(Context context, ApInterstitialAd mInterstitialAd,
                                      final CommonAdCallback callback) {
        forceShowInterstitial(context, mInterstitialAd, callback, false);
    }

    /**
     * Called force show ApInterstitialAd when ready
     *
     * @param context
     * @param mInterstitialAd
     * @param callback
     * @param shouldReloadAds auto reload ad when ad close
     */
    public void forceShowInterstitial(@NonNull Context context, ApInterstitialAd mInterstitialAd,
                                      @NonNull final CommonAdCallback callback, boolean shouldReloadAds) {
        if (mInterstitialAd == null || mInterstitialAd.isNotReady()) {
            Log.e(TAG, "forceShowInterstitial: ApInterstitialAd is not ready");
            callback.onNextAction();
            return;
        }
        switch (adConfig.getMediationProvider()) {
            case CommonAdConfig.PROVIDER_ADMOB:
                AdCallback adCallback = new AdCallback() {
                    @Override
                    public void onAdClosedByTime() {
                        super.onAdClosedByTime();
                        callback.onAdClosedByTime();
                    }

                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        Log.d(TAG, "onAdClosed: ");
                        callback.onAdClosed();
                        if (shouldReloadAds) {
                            Admob.getInstance().getInterstitialAds(context, mInterstitialAd.getInterstitialAd().getAdUnitId(), new AdCallback() {
                                @Override
                                public void onInterstitialLoad(@Nullable InterstitialAd interstitialAd) {
                                    super.onInterstitialLoad(interstitialAd);
                                    Log.d(TAG, "Admob shouldReloadAds success");
                                    mInterstitialAd.setInterstitialAd(interstitialAd);
                                    callback.onInterstitialLoad(mInterstitialAd);
                                }

                                @Override
                                public void onAdFailedToLoad(@Nullable LoadAdError i) {
                                    super.onAdFailedToLoad(i);
                                    mInterstitialAd.setInterstitialAd(null);
                                    callback.onAdFailedToLoad(new ApAdError(i));
                                }

                                @Override
                                public void onAdFailedToShow(@Nullable AdError adError) {
                                    super.onAdFailedToShow(adError);
                                    callback.onAdFailedToShow(new ApAdError(adError));
                                }

                            });
                        } else {
                            mInterstitialAd.setInterstitialAd(null);
                        }
                    }

                    @Override
                    public void onNextAction() {
                        super.onNextAction();
                        Log.d(TAG, "onNextAction: ");
                        callback.onNextAction();
                    }

                    @Override
                    public void onAdFailedToShow(@Nullable AdError adError) {
                        super.onAdFailedToShow(adError);
                        Log.d(TAG, "onAdFailedToShow: ");
                        callback.onAdFailedToShow(new ApAdError(adError));
                        if (shouldReloadAds) {
                            Admob.getInstance().getInterstitialAds(context, mInterstitialAd.getInterstitialAd().getAdUnitId(), new AdCallback() {
                                @Override
                                public void onInterstitialLoad(@Nullable InterstitialAd interstitialAd) {
                                    super.onInterstitialLoad(interstitialAd);
                                    Log.d(TAG, "Admob shouldReloadAds success");
                                    mInterstitialAd.setInterstitialAd(interstitialAd);
                                    callback.onInterstitialLoad(mInterstitialAd);
                                }

                                @Override
                                public void onAdFailedToLoad(@Nullable LoadAdError i) {
                                    super.onAdFailedToLoad(i);
                                    callback.onAdFailedToLoad(new ApAdError(i));
                                }

                                @Override
                                public void onAdFailedToShow(@Nullable AdError adError) {
                                    super.onAdFailedToShow(adError);
                                    callback.onAdFailedToShow(new ApAdError(adError));
                                }

                            });
                        } else {
                            mInterstitialAd.setInterstitialAd(null);
                        }
                    }

                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                        if (callback != null) {
                            callback.onAdClicked();
                        }
                    }
                };
                Admob.getInstance().forceShowInterstitial(context, mInterstitialAd.getInterstitialAd(), adCallback);
                break;
            case CommonAdConfig.PROVIDER_MAX:
                break;
        }
    }

    public void forceShowInterstitialByTime(@NonNull Context context, ApInterstitialAd mInterstitialAd,
                                      @NonNull final CommonAdCallback callback, boolean shouldReloadAds) {
        if (mInterstitialAd == null || mInterstitialAd.isNotReady()) {
            Log.e(TAG, "forceShowInterstitial: ApInterstitialAd is not ready");
            callback.onNextAction();
            return;
        }
        switch (adConfig.getMediationProvider()) {
            case CommonAdConfig.PROVIDER_ADMOB:
                AdCallback adCallback = new AdCallback() {
                    @Override
                    public void onAdClosedByTime() {
                        super.onAdClosedByTime();
                        callback.onAdClosedByTime();
                    }

                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        Log.d(TAG, "onAdClosed: ");
                        callback.onAdClosed();
                        if (shouldReloadAds) {
                            Admob.getInstance().getInterstitialAds(context, mInterstitialAd.getInterstitialAd().getAdUnitId(), new AdCallback() {
                                @Override
                                public void onInterstitialLoad(@Nullable InterstitialAd interstitialAd) {
                                    super.onInterstitialLoad(interstitialAd);
                                    Log.d(TAG, "Admob shouldReloadAds success");
                                    mInterstitialAd.setInterstitialAd(interstitialAd);
                                    callback.onInterstitialLoad(mInterstitialAd);
                                }

                                @Override
                                public void onAdFailedToLoad(@Nullable LoadAdError i) {
                                    super.onAdFailedToLoad(i);
                                    mInterstitialAd.setInterstitialAd(null);
                                    callback.onAdFailedToLoad(new ApAdError(i));
                                }

                                @Override
                                public void onAdFailedToShow(@Nullable AdError adError) {
                                    super.onAdFailedToShow(adError);
                                    callback.onAdFailedToShow(new ApAdError(adError));
                                }

                            });
                        } else {
                            mInterstitialAd.setInterstitialAd(null);
                        }
                    }

                    @Override
                    public void onNextAction() {
                        super.onNextAction();
                        Log.d(TAG, "onNextAction: ");
                        callback.onNextAction();
                    }

                    @Override
                    public void onAdFailedToShow(@Nullable AdError adError) {
                        super.onAdFailedToShow(adError);
                        Log.d(TAG, "onAdFailedToShow: ");
                        callback.onAdFailedToShow(new ApAdError(adError));
                        if (shouldReloadAds) {
                            Admob.getInstance().getInterstitialAds(context, mInterstitialAd.getInterstitialAd().getAdUnitId(), new AdCallback() {
                                @Override
                                public void onInterstitialLoad(@Nullable InterstitialAd interstitialAd) {
                                    super.onInterstitialLoad(interstitialAd);
                                    Log.d(TAG, "Admob shouldReloadAds success");
                                    mInterstitialAd.setInterstitialAd(interstitialAd);
                                    callback.onInterstitialLoad(mInterstitialAd);
                                }

                                @Override
                                public void onAdFailedToLoad(@Nullable LoadAdError i) {
                                    super.onAdFailedToLoad(i);
                                    callback.onAdFailedToLoad(new ApAdError(i));
                                }

                                @Override
                                public void onAdFailedToShow(@Nullable AdError adError) {
                                    super.onAdFailedToShow(adError);
                                    callback.onAdFailedToShow(new ApAdError(adError));
                                }

                            });
                        } else {
                            mInterstitialAd.setInterstitialAd(null);
                        }
                    }

                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                        if (callback != null) {
                            callback.onAdClicked();
                        }
                    }
                };
                Admob.getInstance().forceShowInterstitialByTime(context, mInterstitialAd.getInterstitialAd(), adCallback);
                break;
            case CommonAdConfig.PROVIDER_MAX:
                break;
        }
    }

    /**
     * Called force show ApInterstitialAd when reach the number of clicks show ads
     *
     * @param context
     * @param mInterstitialAd
     * @param callback
     * @param shouldReloadAds auto reload ad when ad close
     */
    public void showInterstitialAdByTimes(Context context, ApInterstitialAd mInterstitialAd,
                                          final CommonAdCallback callback, boolean shouldReloadAds) {
        if (mInterstitialAd.isNotReady()) {
            Log.e(TAG, "forceShowInterstitial: ApInterstitialAd is not ready");
            callback.onAdFailedToShow(new ApAdError("ApInterstitialAd is not ready"));
            return;
        }
        switch (adConfig.getMediationProvider()) {
            case CommonAdConfig.PROVIDER_ADMOB:
                AdCallback adCallback = new AdCallback() {
                    @Override
                    public void onAdClosedByTime() {
                        super.onAdClosedByTime();
                        callback.onAdClosedByTime();
                    }

                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        Log.d(TAG, "onAdClosed: ");
                        callback.onAdClosed();
                        if (shouldReloadAds) {
                            Admob.getInstance().getInterstitialAds(context, mInterstitialAd.getInterstitialAd().getAdUnitId(), new AdCallback() {
                                @Override
                                public void onInterstitialLoad(@Nullable InterstitialAd interstitialAd) {
                                    super.onInterstitialLoad(interstitialAd);
                                    Log.d(TAG, "Admob shouldReloadAds success");
                                    mInterstitialAd.setInterstitialAd(interstitialAd);
                                    callback.onInterstitialLoad(mInterstitialAd);
                                }

                                @Override
                                public void onAdFailedToLoad(@Nullable LoadAdError i) {
                                    super.onAdFailedToLoad(i);
                                    mInterstitialAd.setInterstitialAd(null);
                                    callback.onAdFailedToLoad(new ApAdError(i));
                                }

                                @Override
                                public void onAdFailedToShow(@Nullable AdError adError) {
                                    super.onAdFailedToShow(adError);
                                    callback.onAdFailedToShow(new ApAdError(adError));
                                }

                            });
                        } else {
                            mInterstitialAd.setInterstitialAd(null);
                        }
                    }

                    @Override
                    public void onNextAction() {
                        super.onNextAction();
                        Log.d(TAG, "onNextAction: ");
                        callback.onNextAction();
                    }

                    @Override
                    public void onAdFailedToShow(@Nullable AdError adError) {
                        super.onAdFailedToShow(adError);
                        Log.d(TAG, "onAdFailedToShow: ");
                        callback.onAdFailedToShow(new ApAdError(adError));
                        if (shouldReloadAds) {
                            Admob.getInstance().getInterstitialAds(context, mInterstitialAd.getInterstitialAd().getAdUnitId(), new AdCallback() {
                                @Override
                                public void onInterstitialLoad(@Nullable InterstitialAd interstitialAd) {
                                    super.onInterstitialLoad(interstitialAd);
                                    Log.d(TAG, "Admob shouldReloadAds success");
                                    mInterstitialAd.setInterstitialAd(interstitialAd);
                                    callback.onInterstitialLoad(mInterstitialAd);
                                }

                                @Override
                                public void onAdFailedToLoad(@Nullable LoadAdError i) {
                                    super.onAdFailedToLoad(i);
                                    callback.onAdFailedToLoad(new ApAdError(i));
                                }

                                @Override
                                public void onAdFailedToShow(@Nullable AdError adError) {
                                    super.onAdFailedToShow(adError);
                                    callback.onAdFailedToShow(new ApAdError(adError));
                                }

                            });
                        } else {
                            mInterstitialAd.setInterstitialAd(null);
                        }
                    }

                    @Override
                    public void onAdClicked() {
                        super.onAdClicked();
                        if (callback != null) {
                            callback.onAdClicked();
                        }
                    }
                };
                Admob.getInstance().showInterstitialAdByTimes(context, mInterstitialAd.getInterstitialAd(), adCallback);
                break;
            case CommonAdConfig.PROVIDER_MAX:
                break;
        }
    }

    /**
     * Load native ad and auto populate ad to view in activity
     *
     * @param activity
     * @param id
     * @param layoutCustomNative
     */
    public void loadNativeAd(final Activity activity, String id,
                             int layoutCustomNative) {
        FrameLayout adPlaceHolder = activity.findViewById(R.id.fl_adplaceholder);
        ShimmerFrameLayout containerShimmerLoading = activity.findViewById(R.id.shimmer_container_native);
        switch (adConfig.getMediationProvider()) {
            case CommonAdConfig.PROVIDER_ADMOB:
                Admob.getInstance().loadNativeAd(((Context) activity), id, new AdCallback() {
                    @Override
                    public void onUnifiedNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
                        super.onUnifiedNativeAdLoaded(unifiedNativeAd);
                        populateNativeAdView(activity, new ApNativeAd(layoutCustomNative, unifiedNativeAd), adPlaceHolder, containerShimmerLoading);
                    }

                    @Override
                    public void onAdFailedToLoad(@Nullable LoadAdError i) {
                        super.onAdFailedToLoad(i);
                        Log.e(TAG, "onAdFailedToLoad : NativeAd");
                    }
                });
                break;
            case CommonAdConfig.PROVIDER_MAX:
                break;
        }
    }

    /**
     * Load native ad and auto populate ad to adPlaceHolder and hide containerShimmerLoading
     *
     * @param activity
     * @param id
     * @param layoutCustomNative
     * @param adPlaceHolder
     * @param containerShimmerLoading
     */
    public void loadNativeAd(final Activity activity, String id,
                             int layoutCustomNative, FrameLayout adPlaceHolder, ShimmerFrameLayout
                                     containerShimmerLoading) {
        Admob.getInstance().loadNativeAd(((Context) activity), id, new AdCallback() {
            @Override
            public void onUnifiedNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
                super.onUnifiedNativeAdLoaded(unifiedNativeAd);
                populateNativeAdView(activity, new ApNativeAd(layoutCustomNative, unifiedNativeAd), adPlaceHolder, containerShimmerLoading);
            }

            @Override
            public void onAdFailedToLoad(@Nullable LoadAdError i) {
                super.onAdFailedToLoad(i);
                Log.e(TAG, "onAdFailedToLoad : NativeAd");
            }
        });
    }

    /**
     * Result a ApNativeAd in onUnifiedNativeAdLoaded when native ad loaded
     *
     * @param activity
     * @param id
     * @param layoutCustomNative
     * @param callback
     */
    public void loadNativeAdResultCallback(final Activity activity, String id,
                                           int layoutCustomNative, CommonAdCallback callback) {
        Admob.getInstance().loadNativeAd(((Context) activity), id, new AdCallback() {
            @Override
            public void onUnifiedNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
                super.onUnifiedNativeAdLoaded(unifiedNativeAd);
                callback.onNativeAdLoaded(new ApNativeAd(layoutCustomNative, unifiedNativeAd));
            }

            @Override
            public void onAdFailedToLoad(@Nullable LoadAdError i) {
                super.onAdFailedToLoad(i);
                callback.onAdFailedToLoad(new ApAdError(i));
            }

            @Override
            public void onAdFailedToShow(@Nullable AdError adError) {
                super.onAdFailedToShow(adError);
                callback.onAdFailedToShow(new ApAdError(adError));
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                callback.onAdClicked();
            }
        });
    }

    /**
     * Populate Unified Native Ad to View
     *
     * @param activity
     * @param apNativeAd
     * @param adPlaceHolder
     * @param containerShimmerLoading
     */
    public void populateNativeAdView(Activity activity, ApNativeAd apNativeAd, FrameLayout
            adPlaceHolder, ShimmerFrameLayout containerShimmerLoading) {
        if (apNativeAd.getAdmobNativeAd() == null && apNativeAd.getNativeView() == null) {
            containerShimmerLoading.setVisibility(View.GONE);
            Log.e(TAG, "populateNativeAdView failed : native is not loaded ");
            return;
        }

        @SuppressLint("InflateParams") NativeAdView adView = (NativeAdView) LayoutInflater.from(activity).inflate(apNativeAd.getLayoutCustomNative(), null);
        containerShimmerLoading.stopShimmer();
        containerShimmerLoading.setVisibility(View.GONE);
        adPlaceHolder.setVisibility(View.VISIBLE);
        Admob.getInstance().populateUnifiedNativeAdView(apNativeAd.getAdmobNativeAd(), adView);
        adPlaceHolder.removeAllViews();
        adPlaceHolder.addView(adView);
    }


    public ApRewardAd getRewardAd(Activity activity, String id) {
        ApRewardAd apRewardAd = new ApRewardAd();
        Admob.getInstance().initRewardAds(activity, id, new AdCallback() {

            @Override
            public void onRewardAdLoaded(RewardedAd rewardedAd) {
                super.onRewardAdLoaded(rewardedAd);
                Log.i(TAG, "getRewardAd AdLoaded: ");
                apRewardAd.setAdmobReward(rewardedAd);
            }
        });
        return apRewardAd;
    }

    public ApRewardAd getRewardAdInterstitial(Activity activity, String id) {
        ApRewardAd apRewardAd = new ApRewardAd();
        Admob.getInstance().getRewardInterstitial(activity, id, new AdCallback() {

            @Override
            public void onRewardAdLoaded(RewardedInterstitialAd rewardedAd) {
                super.onRewardAdLoaded(rewardedAd);
                Log.i(TAG, "getRewardAdInterstitial AdLoaded: ");
                apRewardAd.setAdmobReward(rewardedAd);
            }
        });
        return apRewardAd;
    }

    public ApRewardAd getRewardAd(Activity activity, String id, CommonAdCallback callback) {
        ApRewardAd apRewardAd = new ApRewardAd();
        Admob.getInstance().initRewardAds(activity, id, new AdCallback() {
            @Override
            public void onRewardAdLoaded(RewardedAd rewardedAd) {
                super.onRewardAdLoaded(rewardedAd);
                apRewardAd.setAdmobReward(rewardedAd);
                callback.onAdLoaded();
            }
        });
        return apRewardAd;
    }

    public ApRewardAd getRewardInterstitialAd(Activity activity, String id, CommonAdCallback callback) {
        ApRewardAd apRewardAd = new ApRewardAd();
        Admob.getInstance().getRewardInterstitial(activity, id, new AdCallback() {
            @Override
            public void onRewardAdLoaded(RewardedInterstitialAd rewardedAd) {
                super.onRewardAdLoaded(rewardedAd);
                apRewardAd.setAdmobReward(rewardedAd);
                callback.onAdLoaded();
            }
        });
        return apRewardAd;
    }

    public void forceShowRewardAd(Activity activity, ApRewardAd apRewardAd, CommonAdCallback
            callback) {
        if (!apRewardAd.isReady()) {
            Log.e(TAG, "forceShowRewardAd fail: reward ad not ready");
            callback.onNextAction();
            return;
        }
        if (apRewardAd.isRewardInterstitial()) {
            Admob.getInstance().showRewardInterstitial(activity, apRewardAd.getAdmobRewardInter(), new RewardCallback() {

                @Override
                public void onUserEarnedReward(RewardItem var1) {
                    callback.onUserEarnedReward(new ApRewardItem(var1));
                }

                @Override
                public void onRewardedAdClosed() {
                    apRewardAd.clean();
                    callback.onNextAction();
                }

                @Override
                public void onRewardedAdFailedToShow(int codeError) {
                    apRewardAd.clean();
                    callback.onAdFailedToShow(new ApAdError(new AdError(codeError, "note msg", "Reward")));
                }

                @Override
                public void onAdClicked() {
                    if (callback != null) {
                        callback.onAdClicked();
                    }
                }
            });
        } else {
            Admob.getInstance().showRewardAds(activity, apRewardAd.getAdmobReward(), new RewardCallback() {

                @Override
                public void onUserEarnedReward(RewardItem var1) {
                    callback.onUserEarnedReward(new ApRewardItem(var1));
                }

                @Override
                public void onRewardedAdClosed() {
                    apRewardAd.clean();
                    callback.onNextAction();
                }

                @Override
                public void onRewardedAdFailedToShow(int codeError) {
                    apRewardAd.clean();
                    callback.onAdFailedToShow(new ApAdError(new AdError(codeError, "note msg", "Reward")));
                }

                @Override
                public void onAdClicked() {
                    if (callback != null) {
                        callback.onAdClicked();
                    }
                }
            });
        }
    }

    /**
     * Result a CommonAdAdapter with ad native repeating interval
     *
     * @param activity
     * @param id
     * @param layoutCustomNative
     * @param layoutAdPlaceHolder
     * @param originalAdapter
     * @param listener
     * @param repeatingInterval
     * @return
     */
    public CommonAdAdapter getNativeRepeatAdapter(Activity activity, String id, int layoutCustomNative, int layoutAdPlaceHolder, RecyclerView.Adapter originalAdapter,
                                                  CommonAdPlacer.Listener listener, int repeatingInterval) {

        return new CommonAdAdapter(Admob.getInstance().getNativeRepeatAdapter(activity, id, layoutCustomNative, layoutAdPlaceHolder,
                originalAdapter, listener, repeatingInterval));
    }

    /**
     * Result a CommonAdAdapter with ad native fixed in position
     *
     * @param activity
     * @param id
     * @param layoutCustomNative
     * @param layoutAdPlaceHolder
     * @param originalAdapter
     * @param listener
     * @param position
     * @return
     */
    public CommonAdAdapter getNativeFixedPositionAdapter(Activity activity, String id, int layoutCustomNative, int layoutAdPlaceHolder, RecyclerView.Adapter originalAdapter,
                                                         CommonAdPlacer.Listener listener, int position) {

        return new CommonAdAdapter(Admob.getInstance().getNativeFixedPositionAdapter(activity, id, layoutCustomNative, layoutAdPlaceHolder,
                originalAdapter, listener, position));
    }

    public void startNativeFull(
            @NonNull Context fromActivity,
            @NonNull NativeFullConfig config,
            @NonNull Intent nextIntent
    ) {
        if (config == null) {
            fromActivity.startActivity(nextIntent);
            return;
        }

        if (!config.isShowAdsOnly) {
            fromActivity.startActivity(nextIntent);
            return;
        }

        Intent intent = new Intent(fromActivity, NativeFullActivity.class);
        intent.putExtra(NativeIntentKey.NATIVE_FULL_CONFIG, config);
        intent.putExtra(NativeIntentKey.EXT_ACTIVITY_NATIVE_FULL, nextIntent);
        fromActivity.startActivity(intent);
    }
}