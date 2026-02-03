package com.ads.sapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.ads.sapp.R;
import com.ads.sapp.admob.Admob;
import com.ads.sapp.funtion.AdCallback;
import com.ads.sapp.util.CheckAds;
import com.ads.sapp.util.nativefull.NativeFullConfig;
import com.ads.sapp.util.nativefull.NativeIntentKey;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdView;

public class NativeFullActivity extends AppCompatActivity {

    Context context;

    private Intent nextIntent;
    private boolean isHandled = false;

    FrameLayout frAds;
    ImageView btnClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_native_full);

        context = this;
        frAds = findViewById(R.id.frAds);
        btnClose = findViewById(R.id.btnClose);

        try {
            NativeFullConfig config =
                    (NativeFullConfig) getIntent()
                            .getSerializableExtra(NativeIntentKey.NATIVE_FULL_CONFIG);

            nextIntent =
                    getIntent().getParcelableExtra(NativeIntentKey.EXT_ACTIVITY_NATIVE_FULL);

            if (nextIntent == null) {
                finish();
                return;
            }

            if (config == null) {
                goNext();
                return;
            }

            if (!config.isShowAdsOnly) {
                goNext();
                return;
            }

            btnClose.setOnClickListener(v -> goNext());

            showLoading(config);

            Admob.getInstance().loadNativeAd(this, config.adUnitIds, new AdCallback() {

                @Override
                public void onUnifiedNativeAdLoaded(@NonNull NativeAd unifiedNativeAd) {
                    super.onUnifiedNativeAdLoaded(unifiedNativeAd);

                    handleCloseButton(config);

                    NativeAdView adView = (NativeAdView) LayoutInflater
                            .from(context)
                            .inflate(
                                    config.layoutShow == 0
                                            ? R.layout.layout_native_full_show
                                            : config.layoutShow,
                                    frAds,
                                    false
                            );

                    frAds.removeAllViews();
                    frAds.addView(adView);

                    Admob.getInstance().populateUnifiedNativeAdView(unifiedNativeAd, adView);
                    CheckAds.checkAds(adView, CheckAds.OT);
                }

                @Override
                public void onAdFailedToLoad(@Nullable LoadAdError error) {
                    super.onAdFailedToLoad(error);
                    goNext();
                }

                @Override
                public void onAdFailedToShow(@Nullable AdError adError) {
                    super.onAdFailedToShow(adError);
                    goNext();
                }
            });

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (!isHandled) {
                    goNext();
                }
            }, config.timeout);

        }catch (Exception e){
            finish();
        }
    }

    private void goNext() {
        if (isHandled) return;
        isHandled = true;
        startActivity(nextIntent);
        finish();
    }

    private void showLoading(@NonNull NativeFullConfig config) {
        try {
            frAds.removeAllViews();

            int layout = config.layoutLoading != 0
                    ? config.layoutLoading
                    : R.layout.layout_native_full_load;

            View loadingView = LayoutInflater.from(this)
                    .inflate(layout, frAds, false);
            frAds.addView(loadingView);
        }catch (Exception ex){
            finish();
        }
    }

    private void handleCloseButton(@NonNull NativeFullConfig config) {
        try {
            if (!config.showCloseButton) {
                btnClose.setVisibility(View.GONE);
                return;
            }

            if (config.closeButtonDelay <= 0) {
                btnClose.setVisibility(View.VISIBLE);
                return;
            }

            btnClose.setVisibility(View.GONE);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if (!isHandled) {
                    btnClose.setVisibility(View.VISIBLE);
                }
            }, config.closeButtonDelay);
        }catch (Exception ex){
            finish();
        }
    }
}
