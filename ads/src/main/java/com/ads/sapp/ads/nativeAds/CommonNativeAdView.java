package com.ads.sapp.ads.nativeAds;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ads.sapp.R;
import com.ads.sapp.ads.CommonAd;
import com.facebook.shimmer.ShimmerFrameLayout;

public class CommonNativeAdView extends RelativeLayout {

    private int layoutCustomNativeAd;
    private ShimmerFrameLayout layoutLoading;
    private FrameLayout layoutPlaceHolder;

    public CommonNativeAdView(@NonNull Context context) {
        super(context);
        init();
    }

    public CommonNativeAdView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CommonNativeAdView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }


    public CommonNativeAdView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CommonNativeAdView, 0, 0);
        layoutCustomNativeAd = typedArray.getInteger(R.styleable.CommonNativeAdView_layoutCustomNativeAd, 0);
        int idLayoutLoading = typedArray.getInteger(R.styleable.CommonNativeAdView_layoutLoading, 0);
        if (idLayoutLoading != 0)
            layoutLoading = (ShimmerFrameLayout) LayoutInflater.from(getContext()).inflate(idLayoutLoading, null);
        init();
    }

    private void init() {
        layoutPlaceHolder = new FrameLayout(getContext());
        addView(layoutPlaceHolder);
        if (layoutLoading != null)
            addView(layoutLoading);

    }

    public void setLayoutCustomNativeAd(int layoutCustomNativeAd) {
        this.layoutCustomNativeAd = layoutCustomNativeAd;
    }

    public void setLayoutLoading(int idLayoutLoading) {
        this.layoutLoading  = (ShimmerFrameLayout) LayoutInflater.from(getContext()).inflate(idLayoutLoading, null);
        addView(layoutLoading);
    }

    public void setLayoutPlaceHolder(FrameLayout layoutPlaceHolder) {
        this.layoutPlaceHolder = layoutPlaceHolder;
    }

    public void loadNativeAd(Activity activity, String idAd) {
        CommonAd.getInstance().loadNativeAd(activity, idAd, layoutCustomNativeAd, layoutPlaceHolder, layoutLoading);
    }
}