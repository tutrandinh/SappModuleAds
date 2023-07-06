package com.sapp.andmoduleads.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ads.sapp.admob.Admob;
import com.ads.sapp.ads.CommonAd;
import com.ads.sapp.ads.CommonAdConfig;
import com.ads.sapp.funtion.AdCallback;
import com.ads.sapp.util.BannerGravity;
import com.sapp.andmoduleads.R;
import com.sapp.andmoduleads.activity.ContentActivity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.gms.ads.interstitial.InterstitialAd;

import java.util.ArrayList;


public class BlankFragment extends Fragment {
    InterstitialAd mInterstitialAd;
    Button button;
    View view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_blank, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        button =    view.findViewById(R.id.btnNextFragment);
        button.setEnabled(false);
//        Admob.getInstance().getInterstitialAds(getContext(), getString(R.string.admod_interstitial_id), new AdCallback() {
//            @Override
//            public void onInterstitialLoad(InterstitialAd interstitialAd) {
//                super.onInterstitialLoad(interstitialAd);
//                mInterstitialAd = interstitialAd;
//                button.setEnabled(true);
//            }
//        });
        View view1 = view.findViewById(R.id.include).getRootView();
        String idBanner;
        if (CommonAd.getInstance().getMediationProvider() == CommonAdConfig.PROVIDER_ADMOB) {
            idBanner = getString(R.string.admod_banner_collap_id);
        } else {
            idBanner = getString(R.string.applovin_test_banner);
        }

//        CommonAd.getInstance().loadBannerFragment(requireActivity(), idBanner, view1, new AdCallback() {
//            @Override
//            public void onAdClicked() {
//                super.onAdClicked();
//                Log.e("TAG", "onAdClicked: BannerFragment");
//            }
//        });

        //CommonAd.getInstance().loadCollapsibleBannerFragment(requireActivity(), idBanner, view1, BannerGravity.bottom);

        ArrayList<String> listID = new ArrayList<>();
        listID.add("1");
        listID.add("2");
        listID.add(getString(R.string.admod_banner_collap_id));

        CommonAd.getInstance().loadCollapsibleBannerFragmentFloor(requireActivity(),listID,view, BannerGravity.bottom);

        button.setOnClickListener(v -> {
            Admob.getInstance().forceShowInterstitial(getActivity(), mInterstitialAd, new AdCallback() {
                @Override
                public void onAdClosed() {
                    ((ContentActivity)getActivity()).showFragment(new InlineBannerFragment(),"BlankFragment2");
                }
            });
        });

//        Admob.getInstance().loadNativeFragment(getActivity(),getString(R.string.admod_native_id),view);
        FrameLayout flPlaceHolder = view.findViewById(R.id.fl_adplaceholder);
        ShimmerFrameLayout shimmerFrameLayout = view.findViewById(R.id.shimmer_container_native);
        CommonAd.getInstance().loadNativeAd(requireActivity(), getString(R.string.admod_native_id), com.ads.sapp.R.layout.custom_native_admob_free_size, flPlaceHolder, shimmerFrameLayout);
    }
}