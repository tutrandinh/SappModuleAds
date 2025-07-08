package com.ads.sapp.manager;

import android.app.Activity;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import com.ads.sapp.R;
import com.ads.sapp.admob.Admob;
import com.ads.sapp.funtion.BannerCommonCallback;
import com.ads.sapp.funtion.BannerIntervelCallBack;
import com.ads.sapp.util.BannerGravity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

public class BannerCollapsibleCommon implements LifecycleEventObserver {

    private static final String TAG = "BannerCollapsibleCommon";
    private Activity activity;
    private final LifecycleOwner lifecycleOwner;

    private boolean activeReload = true;
    private boolean reloadAds = false;
    private boolean reloadAdsOnResume = false;

    // Time reload, set = 0: no reload
    private long timeIntervalReload = 0;
    private boolean isStop = false;
    private CountDownTimer countDownTimer;

    public AdView adView;

    //Setting loading
    private boolean isShimmer = true;
    //Setting remove view after ads new show
    private boolean isRm = false;

    private ArrayList listID = new ArrayList();
    private BannerCommonCallback bannerCommonCallback;

    public BannerCollapsibleCommon(Activity activity, LifecycleOwner lifecycleOwner, ArrayList listID, final BannerCommonCallback bannerCommonCallback) {
        this.listID = listID;
        this.reloadAds = true;
        this.lifecycleOwner = lifecycleOwner;
        this.lifecycleOwner.getLifecycle().addObserver(this);
        this.activity = activity;
        this.bannerCommonCallback = bannerCommonCallback;
    }

    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        switch (event) {
            case ON_CREATE:
                Log.d(TAG, "BannerCommon:onStateChanged ON_CREATE");
                loadBannerInterval();
                break;
            case ON_RESUME:
                try{
                    if (countDownTimer != null && isStop) {
                        countDownTimer.start();
                    }
                    if (isStop && (reloadAds || reloadAdsOnResume)) {
                        reloadAds = false;
                        loadBannerInterval();
                    }
                    isStop = false;
                    break;
                }catch (Exception e){
                    break;
                }
            case ON_PAUSE:
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
                try{
                    final FrameLayout adContainer = activity.findViewById(R.id.banner_container);
                    final ShimmerFrameLayout containerShimmer = activity.findViewById(R.id.shimmer_container_banner);
                    if(containerShimmer !=null){
                        containerShimmer.setVisibility(View.GONE);
                    }
                    if(adContainer !=null){
                        adContainer.setVisibility(View.GONE);
                        adContainer.removeAllViews();
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

    public void loadBannerInterval() {
        try{

            //Show loading Shimmer
            if(isShimmer){
                final FrameLayout adContainer = activity.findViewById(R.id.banner_container);
                final ShimmerFrameLayout containerShimmer = activity.findViewById(R.id.shimmer_container_banner);
                if(containerShimmer !=null){
                    containerShimmer.setVisibility(View.GONE);
                }
                if(adContainer !=null){
                    adContainer.setVisibility(View.GONE);
                    adContainer.removeAllViews();
                }
            }

            if(adView != null && isRm){
                adView.removeAllViews();
                adView.destroy();
            }

            Admob.getInstance().loadCollapsibleBannerFloorCheck(activity, listID, BannerGravity.bottom, bannerCommonCallback, new BannerIntervelCallBack(){
                @Override
                public void onStartReload() {
                    super.onStartReload();
                    startReloadBanner();
                }

                @Override
                public void onCheckAdView(AdView adView1) {
                    adView = adView1;
                    super.onCheckAdView(adView1);
                }
            });
        }catch (Exception e){
            bannerCommonCallback.onAdFailedToLoad();
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
                    loadBannerInterval();
                }
            };
        }
    }

    private void startReloadBanner() {
        if (countDownTimer != null && this.lifecycleOwner.getLifecycle().getCurrentState() == Lifecycle.State.RESUMED) {
            countDownTimer.cancel();
            countDownTimer.start();
        }
    }

    public void setReloadAdsOnResume(boolean reloadAdsOnResume) {
        this.reloadAdsOnResume = reloadAdsOnResume;
    }

    public void cancelAutoReloadBanner() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    public void activeAutoReloadBanner() {
        if (countDownTimer != null) {
            countDownTimer.start();
        }
    }

    public void unActiveReloadBanner() {
        activeReload = false;
    }

    public void setShimmer(boolean shimmer) {
        isShimmer = shimmer;
    }

    public ArrayList getListID() {
        return listID;
    }

    public void setListID(ArrayList listID) {
        this.listID = listID;
    }

    public void setRm(boolean rm) {
        isRm = rm;
    }
}
