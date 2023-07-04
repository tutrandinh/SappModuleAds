package com.ads.sapp.event;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.ads.sapp.ads.CommonAdConfig;
import com.ads.sapp.util.AppUtil;
import com.ads.sapp.util.SharePreferenceUtils;
import com.google.android.gms.ads.AdValue;
import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseAnalyticsUtil {
    private static final String TAG = "FirebaseAnalyticsUtil";

    public static void logEventWithAds(Context context, Bundle params) {
        FirebaseAnalytics.getInstance(context).logEvent("paid_ad_impression", params);
    }

    static void logPaidAdImpressionValue(Context context, Bundle bundle, int mediationProvider) {
        if (mediationProvider == CommonAdConfig.PROVIDER_MAX)
            FirebaseAnalytics.getInstance(context).logEvent("max_paid_ad_impression_value", bundle);
        else
            FirebaseAnalytics.getInstance(context).logEvent("paid_ad_impression_value", bundle);
    }

    static void logPaidAdImpressionValue(Context context, Bundle bundle) {
        FirebaseAnalytics.getInstance(context).logEvent("admob_paid_ad_impression_value", bundle);
    }

    public static void logClickAdsEvent(Context context, Bundle bundle) {
        FirebaseAnalytics.getInstance(context).logEvent("event_user_click_ads", bundle);
    }

    public static void logCurrentTotalRevenueAd(Context context, String eventName, Bundle bundle) {
        FirebaseAnalytics.getInstance(context).logEvent(eventName, bundle);
    }

    public static void logTotalRevenue001Ad(Context context, Bundle bundle) {
        FirebaseAnalytics.getInstance(context).logEvent("paid_ad_impression_value_001", bundle);
    }

}
