package com.ads.sapp.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.android.ump.ConsentDebugSettings;
import com.google.android.ump.ConsentForm.OnConsentFormDismissedListener;
import com.google.android.ump.ConsentInformation;
import com.google.android.ump.ConsentInformation.PrivacyOptionsRequirementStatus;
import com.google.android.ump.ConsentRequestParameters;
import com.google.android.ump.UserMessagingPlatform;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * The Google Mobile Ads SDK provides the User Messaging Platform (Google's IAB Certified consent
 * management platform) as one solution to capture consent for users in GDPR impacted countries.
 * This is an example and you can choose another consent management platform to capture consent.
 */
public final class GoogleMobileAdsConsentManager {
  private static GoogleMobileAdsConsentManager instance;
  private final ConsentInformation consentInformation;
  private final AtomicBoolean isMobileAdsInitializeCalled = new AtomicBoolean(false);
  public boolean canReset = false;
  public String deviceHashedId = "";
  public boolean testDebug = false;
  public boolean setTagForUnderAge = true;


  /** Private constructor */
  private GoogleMobileAdsConsentManager(Context context) {
    this.consentInformation = UserMessagingPlatform.getConsentInformation(context);
  }

  /** Public constructor */
  public static GoogleMobileAdsConsentManager getInstance(Context context) {
    if (instance == null) {
      instance = new GoogleMobileAdsConsentManager(context);
    }
    return instance;
  }

  /** Interface definition for a callback to be invoked when consent gathering is complete. */
  public interface OnConsentGatheringCompleteListener {
    void consentGatheringComplete(boolean complete);

  }

  /** Helper variable to determine if the app can request ads. */
  public boolean canRequestAds() {
    return consentInformation.canRequestAds();
  }

  /** Helper variable to determine if the privacy options form is required. */
  public boolean isPrivacyOptionsRequired() {
    return consentInformation.getPrivacyOptionsRequirementStatus()
        == PrivacyOptionsRequirementStatus.REQUIRED;
  }

  /**
   * Helper method to call the UMP SDK methods to request consent information and load/present a
   * consent form if necessary.
   */
  public void gatherConsent(
      Activity activity, OnConsentGatheringCompleteListener onConsentGatheringCompleteListener) {

      // For testing purposes, you can force a DebugGeography of EEA or NOT_EEA.
      ConsentDebugSettings debugSettings = new ConsentDebugSettings.Builder(activity)
                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                // Check your logcat output for the hashed device ID e.g.
                // "Use new ConsentDebugSettings.Builder().addTestDeviceHashedId("ABCDEF012345")" to use
                // the debug functionality.
                .addTestDeviceHashedId(deviceHashedId)
                .build();

      ConsentRequestParameters.Builder paramsBuild = new ConsentRequestParameters.Builder();
      //paramsBuild.setTagForUnderAgeOfConsent(false);

      if(testDebug && !deviceHashedId.equals("")){
          paramsBuild.setConsentDebugSettings(debugSettings);
      }

      paramsBuild.setTagForUnderAgeOfConsent(setTagForUnderAge);

      ConsentRequestParameters params = paramsBuild.build();

    if(canReset){
        consentInformation.reset();
    }
    // Requesting an update to consent information should be called on every app launch.
    consentInformation.requestConsentInfoUpdate(
        activity,
        params,
        () ->
            UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                activity,
                formError -> {
                  // Consent has been gathered.
                  if (formError != null) {
                    Log.e("GoogleMobileAdsConsent", "onConsentInfoUpdateSuccess: " + formError.getMessage());
                  }

                  if (!this.isMobileAdsInitializeCalled.getAndSet(true)) {
                    onConsentGatheringCompleteListener.consentGatheringComplete(getConsentResult(activity));
                  }
                }),
            (requestConsentError ->{
              if (!isMobileAdsInitializeCalled.getAndSet(true)) {
                Log.e("GoogleMobileAdsConsent", "onConsentInfoUpdateFailure: " + requestConsentError.getMessage());
                onConsentGatheringCompleteListener.consentGatheringComplete(getConsentResult(activity));
              }
            }));

    // This sample attempts to load ads using consent obtained in the previous session.
    if (consentInformation.canRequestAds() && !this.isMobileAdsInitializeCalled.getAndSet(true)) {
      onConsentGatheringCompleteListener.consentGatheringComplete(getConsentResult(activity));
    }
  }

    public void gatherConsentNext(
            Activity activity, OnConsentGatheringCompleteListener onConsentGatheringCompleteListener) {

        // For testing purposes, you can force a DebugGeography of EEA or NOT_EEA.
        ConsentDebugSettings debugSettings = new ConsentDebugSettings.Builder(activity)
                .setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
                // Check your logcat output for the hashed device ID e.g.
                // "Use new ConsentDebugSettings.Builder().addTestDeviceHashedId("ABCDEF012345")" to use
                // the debug functionality.
                .addTestDeviceHashedId(deviceHashedId)
                .build();

        ConsentRequestParameters.Builder paramsBuild = new ConsentRequestParameters.Builder();
        //paramsBuild.setTagForUnderAgeOfConsent(false);

        if(testDebug && !deviceHashedId.equals("")){
            paramsBuild.setConsentDebugSettings(debugSettings);
        }

        paramsBuild.setTagForUnderAgeOfConsent(setTagForUnderAge);

        ConsentRequestParameters params = paramsBuild.build();

        if(canReset){
            consentInformation.reset();
        }
        // Requesting an update to consent information should be called on every app launch.
        consentInformation.requestConsentInfoUpdate(
                activity,
                params,
                () ->
                        UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                                activity,
                                formError -> {
                                    // Consent has been gathered.
                                    if (formError != null) {
                                        Log.e("GoogleMobileAdsConsent", "onConsentInfoUpdateSuccess: " + formError.getMessage());
                                    }else onConsentGatheringCompleteListener.consentGatheringComplete(false);

                                    if (!this.isMobileAdsInitializeCalled.getAndSet(true)) {
                                        onConsentGatheringCompleteListener.consentGatheringComplete(getConsentResult(activity));
                                    }else onConsentGatheringCompleteListener.consentGatheringComplete(false);
                                }),
                (requestConsentError ->{
                    if (!isMobileAdsInitializeCalled.getAndSet(true)) {
                        Log.e("GoogleMobileAdsConsent", "onConsentInfoUpdateFailure: " + requestConsentError.getMessage());
                        onConsentGatheringCompleteListener.consentGatheringComplete(getConsentResult(activity));
                    }else onConsentGatheringCompleteListener.consentGatheringComplete(false);
                }));

        // This sample attempts to load ads using consent obtained in the previous session.
        if (consentInformation.canRequestAds() && !this.isMobileAdsInitializeCalled.getAndSet(true)) {
            onConsentGatheringCompleteListener.consentGatheringComplete(getConsentResult(activity));
        }else onConsentGatheringCompleteListener.consentGatheringComplete(false);
    }

  public void gatherConsent(
          Activity activity,Boolean reset, Boolean debug, String testDeviceHashedId,OnConsentGatheringCompleteListener onConsentGatheringCompleteListener) {

    // For testing purposes, you can force a DebugGeography of EEA or NOT_EEA.
    ConsentDebugSettings debugSettings = new ConsentDebugSettings.Builder(activity)
            //.setDebugGeography(ConsentDebugSettings.DebugGeography.DEBUG_GEOGRAPHY_EEA)
            // Check your logcat output for the hashed device ID e.g.
            // "Use new ConsentDebugSettings.Builder().addTestDeviceHashedId("ABCDEF012345")" to use
            // the debug functionality.
            //.addTestDeviceHashedId("10A66C168A2774EF76E1455DF9097313")
            .build();

    ConsentRequestParameters params = new ConsentRequestParameters.Builder()
            .setConsentDebugSettings(debugSettings)
            .build();

    if(reset){
      consentInformation.reset();
    }

    // Requesting an update to consent information should be called on every app launch.
    consentInformation.requestConsentInfoUpdate(
            activity,
            params,
            () ->
                    UserMessagingPlatform.loadAndShowConsentFormIfRequired(
                            activity,
                            formError -> {
                              // Consent has been gathered.
                              if (formError != null) {
                                Log.e("GoogleMobileAdsConsent", "onConsentInfoUpdateSuccess: " + formError.getMessage());
                              }

                              if (!this.isMobileAdsInitializeCalled.getAndSet(true)) {
                                onConsentGatheringCompleteListener.consentGatheringComplete(getConsentResult(activity));
                              }
                            }),
            (requestConsentError ->{
              if (!isMobileAdsInitializeCalled.getAndSet(true)) {
                Log.e("GoogleMobileAdsConsent", "onConsentInfoUpdateFailure: " + requestConsentError.getMessage());
                onConsentGatheringCompleteListener.consentGatheringComplete(getConsentResult(activity));
              }
            }));

    // This sample attempts to load ads using consent obtained in the previous session.
    if (consentInformation.canRequestAds() && !this.isMobileAdsInitializeCalled.getAndSet(true)) {
      onConsentGatheringCompleteListener.consentGatheringComplete(getConsentResult(activity));
    }
  }

  /***
   * Helper method to check consent
   * @param context
   * @return
   */
  public static boolean getConsentResult(Context context) {
    String consentResult = context.getSharedPreferences(context.getPackageName() + "_preferences", 0).getString("IABTCF_PurposeConsents", "");
    return consentResult.isEmpty() || String.valueOf(consentResult.charAt(0)).equals("1");
  }


  /** Helper method to call the UMP SDK method to present the privacy options form. */
  public void showPrivacyOptionsForm(
      Activity activity,
      OnConsentFormDismissedListener onConsentFormDismissedListener) {
    UserMessagingPlatform.showPrivacyOptionsForm(activity, onConsentFormDismissedListener);
  }

    public boolean isCanReset() {
        return canReset;
    }

    public void setCanReset(boolean canReset) {
        this.canReset = canReset;
    }

    public String getDeviceHashedId() {
        return deviceHashedId;
    }

    public void setDeviceHashedId(String deviceHashedId) {
        this.deviceHashedId = deviceHashedId;
    }

    public boolean isTestDebug() {
        return testDebug;
    }

    public void setTestDebug(boolean testDebug) {
        this.testDebug = testDebug;
    }

    public boolean isSetTagForUnderAge() {
        return setTagForUnderAge;
    }

    public void setSetTagForUnderAge(boolean setTagForUnderAge) {
        this.setTagForUnderAge = setTagForUnderAge;
    }
}
