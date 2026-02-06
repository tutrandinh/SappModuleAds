package com.ads.sapp.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
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

    // Quản lý Handler để tránh lỗi timeout chạy ngầm
    private final Handler timeoutHandler = new Handler(Looper.getMainLooper());
    private Runnable timeoutRunnable;
    private final Handler closeButtonHandler = new Handler(Looper.getMainLooper());
    private Runnable closeButtonRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("NativeFullActivity"," onCreate");

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
                Log.d("NativeFullActivity"," nextIntent is null");
                finish();
                return;
            }

            if (config == null) {
                Log.d("NativeFullActivity"," config is null");
                goNext();
                return;
            }

            if (!config.isShowAdsOnly) {
                Log.d("NativeFullActivity"," isShowAdsOnly is false");
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
                    Log.d("NativeFullActivity"," show native full ads");
                }

                @Override
                public void onAdFailedToLoad(@Nullable LoadAdError error) {
                    super.onAdFailedToLoad(error);
                    Log.d("NativeFullActivity"," load native full ads failed: " + error);
                    goNext();
                }

                @Override
                public void onAdFailedToShow(@Nullable AdError adError) {
                    super.onAdFailedToShow(adError);
                    Log.d("NativeFullActivity"," show native full ads failed: " + adError);
                    goNext();
                }
            });

            // Khởi tạo logic Timeout
            timeoutRunnable = () -> {
                if (!isHandled) {
                    Log.d("NativeFullActivity"," timeout native full ads");
                    goNext();
                }
            };
            timeoutHandler.postDelayed(timeoutRunnable, config.timeout);

        } catch (Exception e){
            Log.d("NativeFullActivity"," exception: " + e.getMessage());
            finish();
        }
    }

    /**
     * Hàm chuyển sang Activity tiếp theo và dọn dẹp tài nguyên
     */
    private void goNext() {
        if (isHandled) {
            Log.d("NativeFullActivity"," already handled");
            return;
        }
        Log.d("NativeFullActivity"," goNext to nextIntent");
        isHandled = true;

        // 1. Hủy bỏ các bộ đếm thời gian đang chạy
        if (timeoutHandler != null && timeoutRunnable != null) {
            timeoutHandler.removeCallbacks(timeoutRunnable);
        }
        if (closeButtonHandler != null && closeButtonRunnable != null) {
            closeButtonHandler.removeCallbacks(closeButtonRunnable);
        }

        if (nextIntent != null) {
            // 2. Sửa lỗi Back: Chuyển tiếp Result từ màn hình D về lại màn hình A
            nextIntent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);

            // 3. Đảm bảo stack hoạt động ổn định
            nextIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

            startActivity(nextIntent);
        }

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
            Log.d("NativeFullActivity", " show loading view");
        } catch (Exception ex){
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

            closeButtonRunnable = () -> {
                if (!isHandled) {
                    btnClose.setVisibility(View.VISIBLE);
                }
            };
            closeButtonHandler.postDelayed(closeButtonRunnable, config.closeButtonDelay);

        } catch (Exception ex){
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        // Luôn dọn dẹp Handler khi Activity bị hủy để tránh Memory Leak
        if (timeoutHandler != null && timeoutRunnable != null) {
            timeoutHandler.removeCallbacks(timeoutRunnable);
        }
        if (closeButtonHandler != null && closeButtonRunnable != null) {
            closeButtonHandler.removeCallbacks(closeButtonRunnable);
        }
        super.onDestroy();
    }
}