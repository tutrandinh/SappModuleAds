package com.ads.sapp.util.nativefull;

import java.io.Serializable;
import java.util.ArrayList;

public class NativeFullConfig implements Serializable {

    public ArrayList<String> adUnitIds;
    public int layoutLoading;
    public int layoutShow;
    public long timeout = 20000; // ms
    public boolean showCloseButton = true;
    public boolean showCloseButtonLeft = true;
    public boolean showCloseButtonRight = true;
    public long closeButtonDelay = 5000; // ms (0 = hiện ngay)
    public boolean isShowAdsOnly = true;

    public NativeFullConfig(boolean isShowAdsOnly,
                            ArrayList<String> adUnitIds,
                            int layoutLoading,
                            int layoutShow,
                            long timeout,
                            boolean showCloseButton) {
        this.isShowAdsOnly = isShowAdsOnly;
        this.adUnitIds = adUnitIds;
        this.layoutLoading = layoutLoading;
        this.layoutShow = layoutShow;
        this.timeout = timeout;
        this.showCloseButton = showCloseButton;
    }

    public NativeFullConfig(boolean isShowAdsOnly,
                            ArrayList<String> adUnitIds,
                            int layoutLoading,
                            int layoutShow) {
        this.isShowAdsOnly = isShowAdsOnly;
        this.adUnitIds = adUnitIds;
        this.layoutLoading = layoutLoading;
        this.layoutShow = layoutShow;
    }

    public NativeFullConfig(boolean isShowAdsOnly,
                            ArrayList<String> adUnitIds,
                            int layoutLoading,
                            int layoutShow,
                            long timeout,
                            boolean showCloseButton,
                            long closeButtonDelay) {
        this.isShowAdsOnly = isShowAdsOnly;
        this.adUnitIds = adUnitIds;
        this.layoutLoading = layoutLoading;
        this.layoutShow = layoutShow;
        this.timeout = timeout;
        this.showCloseButton = showCloseButton;
        this.closeButtonDelay = closeButtonDelay;
    }

    public NativeFullConfig(boolean isShowAdsOnly,
                            ArrayList<String> adUnitIds,
                            int layoutLoading,
                            int layoutShow,
                            long timeout,
                            boolean showCloseButton,
                            boolean showCloseButtonLeft,
                            boolean showCloseButtonRight,
                            long closeButtonDelay) {
        this.isShowAdsOnly = isShowAdsOnly;
        this.adUnitIds = adUnitIds;
        this.layoutLoading = layoutLoading;
        this.layoutShow = layoutShow;
        this.timeout = timeout;
        this.showCloseButton = showCloseButton;
        this.showCloseButtonLeft = showCloseButtonLeft;
        this.showCloseButtonRight = showCloseButtonRight;
        this.closeButtonDelay = closeButtonDelay;
    }

}
