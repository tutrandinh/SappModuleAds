package com.ads.sapp.util;

import android.util.Log;
import android.widget.TextView;

import com.ads.sapp.R;
import com.google.android.gms.ads.AdValue;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.nativead.NativeAdView;

public class CustomShowAds {

        private static String TAG_CHECK ="AD_TEST_";

       public static boolean CheckNativeInfo(NativeAdView adView){
           TextView headlineView = adView.findViewById(R.id.ad_headline);
           Log.d( TAG_CHECK + "NativeAdAdmob", adView.getHeadlineView().toString());
           Log.d( TAG_CHECK + "NativeAdAdmob",headlineView.getText().toString());
           return false;
       }

    public static boolean CheckResponsesInfo(InterstitialAd interstitialAd, AdValue adValue){

        Log.d(TAG_CHECK + "getResponseInfo", interstitialAd.getResponseInfo().toString());
        Log.d(TAG_CHECK + "getResponseInfo", interstitialAd.getResponseInfo().getAdapterResponses().get(0).getAdSourceInstanceName());
        Log.d(TAG_CHECK + "getResponseInfo",String.valueOf(adValue.getValueMicros()));

        return false;
    }
}
