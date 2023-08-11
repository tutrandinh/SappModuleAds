package com.ads.sapp.call.api;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.ads.sapp.admob.Admob;
import com.ads.sapp.ads.CommonAd;
import com.ads.sapp.ads.wrapper.ApInterstitialAd;
import com.ads.sapp.funtion.AdCallback;
import com.ads.sapp.util.BannerGravity;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonProcess {

    private static ArrayList<AdsModel> listAppOpenSplash = new ArrayList<>();
    private static ArrayList<AdsModel> listInterIntro = new ArrayList<>();
    private static ArrayList<AdsModel> listInterAll = new ArrayList<>();
    private static ArrayList<AdsModel> listNativeLanguage = new ArrayList<>();
    private static ArrayList<AdsModel> listNativeIntro = new ArrayList<>();
    private static ArrayList<AdsModel> listNativeHome = new ArrayList<>();
    private static ArrayList<AdsModel> listNativePermission = new ArrayList<>();
    private static ArrayList<AdsModel> listBannerCollapsible = new ArrayList<>();
    private static ArrayList<AdsModel> listBanner = new ArrayList<>();

    private static DataModel dataModel = new DataModel();

    private String token = "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";
    private String baseUrl = "https://api.github.com/";
    private String path = "repos/tutrandinh/AdsStorage/contents/";
    private String fileName = "";
    private String branch = "";
    private String mimeType = ".json";

    private static volatile CommonProcess INSTANCE;

    public static synchronized CommonProcess getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CommonProcess();
        }
        return INSTANCE;
    }

    public static void LoadDataAndShowAdsSplash(Context context, boolean isShowAdIfReady, AdCallback adCallback){
        dataModel = new DataModel();

        if(getInstance().fileName.equals("") || getInstance().branch.equals("") || getInstance().token.equals("")){
            adCallback.onAdFailedToLoad(null);
            adCallback.onNextAction();
            return;
        }

        if (isNetworkConnected(context)){
            ApiService.apiService.callAdsContentInfo(CommonProcess.getInstance().fileName,CommonProcess.getInstance().branch ).enqueue(new Callback<ContentInfo>() {

                @Override
                public void onResponse(Call<ContentInfo> call, Response<ContentInfo> response) {
                    if(response.body() != null){
                        if(response.isSuccessful()) {
                            ContentInfo contentInfo = response.body();
                            dataModel =  getDataAll(contentInfo);

                            setListDataDefault(dataModel);

                            ArrayList<String> listID = new ArrayList<>();
                            if(listAppOpenSplash != null){
                                for(AdsModel adsModel : listAppOpenSplash) {
                                    listID.add(adsModel.getAds_id());
                                    Log.d("ListID",adsModel.getId()+ "----"+adsModel.getAds_id() + "------" + adsModel.getName());
                                }
                            }

                            if(listID.size() < 1){
                                adCallback.onAdFailedToLoad(null);
                                adCallback.onNextAction();
                                return;
                            }else{
                                CommonAd.getInstance().loadOpenAppAdSplashFloor(context,listID,true,adCallback);
                            }

                        } else {
                            Log.d("ErrorAds","Error no data");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    adCallback.onAdFailedToLoad(null);
                                    adCallback.onNextAction();
                                }
                            },3000);
                        }
                    }else{
                        Log.d("ErrorAds","Error null data");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                adCallback.onAdFailedToLoad(null);
                                adCallback.onNextAction();
                            }
                        },3000);
                    }
                }

                @Override
                public void onFailure(Call<ContentInfo> call, Throwable t) {
                    Log.d("ErrorAds","onFailure");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            adCallback.onAdFailedToLoad(null);
                            adCallback.onNextAction();
                        }
                    },3000);
                }
            });
        }else{
            Log.d("ErrorAds","No Network Connected");
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    adCallback.onAdFailedToLoad(null);
                    adCallback.onNextAction();
                }
            },3000);
        }

    }

    public void loadOpenAppAdSplashFloor(Context context, boolean isShowAdIfReady, AdCallback adCallback) {
        ArrayList<String> listID = new ArrayList<>();
        if(listAppOpenSplash != null){
            if(listAppOpenSplash.size() > 0){
                for(AdsModel adsModel : listAppOpenSplash) {
                    listID.add(adsModel.getAds_id());
                    //Log.d("ContentInfo",adsModel.getAds_id() + "------" + adsModel.getName());
                }
            }
        }

        CommonAd.getInstance().loadOpenAppAdSplashFloor(context,listID,true,adCallback);
    }

    /**
     * Load Ads Native in Intro Screen
     * @param context
     * @param callback
     * @return True if has ads, another case
     */
    public Boolean loadAdsNativeIntro(Context context, final AdCallback callback){
        ArrayList<String> listID = new ArrayList<>();
        if(listNativeIntro != null){
            for(AdsModel adsModel : listNativeIntro) {
                listID.add(adsModel.getAds_id());
                Log.d("ListID",adsModel.getId()+ "----"+adsModel.getAds_id() + "------" + adsModel.getName());
            }
        }
        if(listID.size() > 0){
            Admob.getInstance().loadNativeAd(context, listID,callback);
            return true;
        }else{
            return false;
        }
    }

    /**
     * Load Ads Native in Language Screen
     * @param context
     * @param callback
     * @return True if has ads, another case
     */
    public Boolean loadAdsNativeLanguage(Context context, final AdCallback callback){
        ArrayList<String> listID = new ArrayList<>();
        if(listNativeLanguage != null){
            for(AdsModel adsModel : listNativeLanguage) {
                listID.add(adsModel.getAds_id());
                Log.d("ListID",adsModel.getId()+ "----"+adsModel.getAds_id() + "------" + adsModel.getName());
            }
        }
        if(listID.size() > 0){
            Admob.getInstance().loadNativeAd(context, listID,callback);
            return true;
        }else{
            return false;
        }
    }

    /**
     * Load Ads Native in Permission Screen
     * @param context
     * @param callback
     * @return True if has ads, another case
     */
    public Boolean loadAdsNativePermission(Context context, final AdCallback callback){
        ArrayList<String> listID = new ArrayList<>();
        if(listNativePermission != null){
            for(AdsModel adsModel : listNativePermission) {
                listID.add(adsModel.getAds_id());
                Log.d("ListID",adsModel.getId()+ "----"+adsModel.getAds_id() + "------" + adsModel.getName());
            }
        }
        if(listID.size() > 0){
            Admob.getInstance().loadNativeAd(context, listID,callback);
            return true;
        }else{
            return false;
        }
    }

    /**
     * Load Ads Native in Home Screen
     * @param context
     * @param callback
     * @return True if has ads, another case
     */
    public Boolean loadAdsNativeHome(Context context, final AdCallback callback){
        ArrayList<String> listID = new ArrayList<>();
        if(listNativeHome != null){
            for(AdsModel adsModel : listNativeHome) {
                listID.add(adsModel.getAds_id());
                Log.d("ListID",adsModel.getId()+ "----"+adsModel.getAds_id() + "------" + adsModel.getName());
            }
        }
        if(listID.size() > 0){
            Admob.getInstance().loadNativeAd(context, listID,callback);
            return true;
        }else{
            return false;
        }
    }

    public Boolean loadAdsNativeByName(Context context, String nameId, final AdCallback callback){
        ArrayList<String> listID = new ArrayList<>();
        ArrayList<AdsModel> adsModels = new ArrayList<>();
        adsModels = getDataByNameId(dataModel.getAdsModels(), nameId);
        if(adsModels != null){
            for(AdsModel adsModel : adsModels) {
                listID.add(adsModel.getAds_id());
                Log.d("ListID",adsModel.getId()+ "----"+adsModel.getAds_id() + "------" + adsModel.getName());
            }
        }
        if(listID.size() > 0){
            Admob.getInstance().loadNativeAd(context, listID,callback);
            return true;
        }else{
            callback.onAdFailedToLoad(null);
            return false;
        }
    }

    public Boolean loadAdsNativeByName(Context context, String nameId, FrameLayout frameLayout, final AdCallback callback){
        try{
            ArrayList<String> listID = new ArrayList<>();
            ArrayList<AdsModel> adsModels = new ArrayList<>();
            adsModels = getDataByNameId(dataModel.getAdsModels(), nameId);
            if(adsModels != null){
                for(AdsModel adsModel : adsModels) {
                    listID.add(adsModel.getAds_id());
                    Log.d("ListID",adsModel.getId()+ "----"+adsModel.getAds_id() + "------" + adsModel.getName());
                }
            }
            if(listID.size() > 0){
                Admob.getInstance().loadNativeAd(context, listID,callback);
                return true;
            }else{
                frameLayout.setVisibility(View.INVISIBLE);
                return false;
            }
        }catch (Exception e){
            frameLayout.setVisibility(View.INVISIBLE);
            return false;
        }
    }

    public void loadBannerDefault(final Activity mActivity) {
        try{
            ArrayList<String> listID = new ArrayList<>();
            if(listBanner != null){
                for(AdsModel adsModel : listBanner) {
                    listID.add(adsModel.getAds_id());
                    Log.d("ListID",adsModel.getId()+ "----"+adsModel.getAds_id() + "------" + adsModel.getName());
                }
            }
            if(listID.size() > 0){
                CommonAd.getInstance().loadBanner(mActivity, listID.get(0));
            }else{
                final FrameLayout adContainer = mActivity.findViewById(com.ads.sapp.R.id.banner_container);
                final ShimmerFrameLayout containerShimmer = mActivity.findViewById(com.ads.sapp.R.id.shimmer_container_banner);
                containerShimmer.stopShimmer();
                adContainer.setVisibility(View.GONE);
                containerShimmer.setVisibility(View.GONE);
            }
        }catch (Exception ex){
            final FrameLayout adContainer = mActivity.findViewById(com.ads.sapp.R.id.banner_container);
            final ShimmerFrameLayout containerShimmer = mActivity.findViewById(com.ads.sapp.R.id.shimmer_container_banner);
            containerShimmer.stopShimmer();
            adContainer.setVisibility(View.GONE);
            containerShimmer.setVisibility(View.GONE);
        }
    }

    public void loadBannerByName(final Activity mActivity,String nameId) {
        try{
            ArrayList<String> listID = new ArrayList<>();
            ArrayList<AdsModel> adsModels = new ArrayList<>();
            adsModels = getDataByNameId(dataModel.getAdsModels(), nameId);
            if(adsModels != null){
                for(AdsModel adsModel : adsModels) {
                    listID.add(adsModel.getAds_id());
                    Log.d("ListID",adsModel.getId()+ "----"+adsModel.getAds_id() + "------" + adsModel.getName());
                }
            }
            if(listID.size() > 0){
                CommonAd.getInstance().loadBanner(mActivity, listID.get(0));
            }else{
                final FrameLayout adContainer = mActivity.findViewById(com.ads.sapp.R.id.banner_container);
                final ShimmerFrameLayout containerShimmer = mActivity.findViewById(com.ads.sapp.R.id.shimmer_container_banner);
                containerShimmer.stopShimmer();
                adContainer.setVisibility(View.GONE);
                containerShimmer.setVisibility(View.GONE);
            }
        }catch (Exception ex){
            final FrameLayout adContainer = mActivity.findViewById(com.ads.sapp.R.id.banner_container);
            final ShimmerFrameLayout containerShimmer = mActivity.findViewById(com.ads.sapp.R.id.shimmer_container_banner);
            containerShimmer.stopShimmer();
            adContainer.setVisibility(View.GONE);
            containerShimmer.setVisibility(View.GONE);
        }
    }

    public void loadCollapsibleBannerDefaultFloor(final Activity mActivity, String gravity) {
        ArrayList<String> listID = new ArrayList<>();
        if(listBannerCollapsible != null){
            for(AdsModel adsModel : listBannerCollapsible) {
                listID.add(adsModel.getAds_id());
                Log.d("ListID",adsModel.getId()+ "----"+adsModel.getAds_id() + "------" + adsModel.getName());
            }
        }

        CommonAd.getInstance().loadCollapsibleBannerFloor(mActivity, listID, BannerGravity.bottom);
    }

    public void loadCollapsibleBannerDefaultFragmentFloor(final Activity mActivity, final View rootView, String gravity) {
        ArrayList<String> listID = new ArrayList<>();
        if(listBannerCollapsible != null){
            for(AdsModel adsModel : listBannerCollapsible) {
                listID.add(adsModel.getAds_id());
                Log.d("ListID",adsModel.getId()+ "----"+adsModel.getAds_id() + "------" + adsModel.getName());
            }
        }

        CommonAd.getInstance().loadCollapsibleBannerFragmentFloor(mActivity, listID,rootView,BannerGravity.bottom);
    }

    public void loadCollapsibleBannerByNameFloor(final Activity mActivity, String gravity, String nameId) {
        ArrayList<String> listID = new ArrayList<>();
        ArrayList<AdsModel> adsModels = new ArrayList<>();
        adsModels = getDataByNameId(dataModel.getAdsModels(), nameId);
        if(adsModels != null){
            for(AdsModel adsModel : adsModels) {
                listID.add(adsModel.getAds_id());
                Log.d("ListID",adsModel.getId()+ "----"+adsModel.getAds_id() + "------" + adsModel.getName());
            }
        }

        CommonAd.getInstance().loadCollapsibleBannerFloor(mActivity, listID, BannerGravity.bottom);
    }

    public void loadCollapsibleBannerByNameFloor(final Activity mActivity,final View rootView, String gravity, String nameId) {
        ArrayList<String> listID = new ArrayList<>();
        ArrayList<AdsModel> adsModels = new ArrayList<>();
        adsModels = getDataByNameId(dataModel.getAdsModels(), nameId);
        if(adsModels != null){
            for(AdsModel adsModel : adsModels) {
                listID.add(adsModel.getAds_id());
                Log.d("ListID",adsModel.getId()+ "----"+adsModel.getAds_id() + "------" + adsModel.getName());
            }
        }

        CommonAd.getInstance().loadCollapsibleBannerFragmentFloor(mActivity, listID, rootView, BannerGravity.bottom);
    }

    public ApInterstitialAd getInterstitialAdsInterAll(Context context){
        ArrayList<String> listID = new ArrayList<>();
        if(listInterAll != null){
            for(AdsModel adsModel : listInterAll) {
                listID.add(adsModel.getAds_id());
                Log.d("ListID",adsModel.getId()+ "----"+adsModel.getAds_id() + "------" + adsModel.getName());
            }
        }
        if(listID.size() > 0){
            return CommonAd.getInstance().getInterstitialAds(context,listID);
        }else{
            return new ApInterstitialAd();
        }
    }

    public ApInterstitialAd getInterstitialAdsInterIntro(Context context){
        ArrayList<String> listID = new ArrayList<>();
        if(listInterIntro != null){
            for(AdsModel adsModel : listInterIntro) {
                listID.add(adsModel.getAds_id());
                Log.d("ListID",adsModel.getId()+ "----"+adsModel.getAds_id() + "------" + adsModel.getName());
            }
        }
        if(listID.size() > 0){
            return CommonAd.getInstance().getInterstitialAds(context,listID);
        }else{
            return new ApInterstitialAd();
        }
    }

    public ApInterstitialAd getInterstitialAdsInterByName(Context context,String nameId){
        ArrayList<String> listID = new ArrayList<>();
        ArrayList<AdsModel> adsModels = new ArrayList<>();
        adsModels = getDataByNameId(dataModel.getAdsModels(), nameId);
        if(adsModels != null){
            for(AdsModel adsModel : adsModels) {
                listID.add(adsModel.getAds_id());
                Log.d("ListID",adsModel.getId()+ "----"+adsModel.getAds_id() + "------" + adsModel.getName());
            }
        }
        if(listID.size() > 0){
            return CommonAd.getInstance().getInterstitialAds(context,listID);
        }else{
            return new ApInterstitialAd();
        }
    }

    public static DataModel getDataAll(ContentInfo contentInfo){

        DataModel dataModel = new DataModel();
        IsError isError = new IsError(false,"","","");
        dataModel.setIsError(isError);
        ArrayList<AdsModel>  adsModels = new ArrayList<>();

        try {
            byte[] decodedBytes = Base64.decode(contentInfo.getContent(),Base64.DEFAULT);
            String decodedString = new String(decodedBytes);

            Gson gson = new Gson();
            AdsModel[] userArray = gson.fromJson(decodedString, AdsModel[].class);

            for(AdsModel adsModel : userArray) {
                adsModels.add(adsModel);
                //Log.d("ContentInfo",adsModel.getAds_id() + "------" + adsModel.getName());
            }

            if(adsModels != null){
                if(adsModels.size() < 1) {
                    isError = new IsError(true,"E0001","Error no data","");
                }
            }

            dataModel.setAdsModels(adsModels);
            dataModel.setIsError(isError);
            return dataModel;

        }catch (Exception ex){
            adsModels = new ArrayList<>();
            isError = new IsError(true,"E0002","Exception getDataAll","");

            dataModel.setAdsModels(adsModels);
            dataModel.setIsError(isError);
            return dataModel;
        }
    }

    public static void setListDataDefault(DataModel dataModel){

        listAppOpenSplash = new ArrayList<>();
        listInterIntro = new ArrayList<>();
        listInterAll = new ArrayList<>();
        listNativeLanguage = new ArrayList<>();
        listNativeIntro = new ArrayList<>();
        listNativeHome = new ArrayList<>();
        listNativePermission = new ArrayList<>();
        listBannerCollapsible = new ArrayList<>();
        listBanner = new ArrayList<>();

        for (AdsModel ads : dataModel.getAdsModels()) {
            switch (ads.getName()) {
                case "open_splash":
                    listAppOpenSplash.add(ads);
                    break;
                case "inter_intro":
                    listInterIntro.add(ads);
                    break;
                case "inter_all":
                    listInterAll.add(ads);
                    break;
                case "native_intro":
                    listNativeIntro.add(ads);
                    break;
                case "native_language":
                    listNativeLanguage.add(ads);
                    break;
                case "native_permission":
                    listNativePermission.add(ads);
                    break;
                case "native_home":
                    listNativeHome.add(ads);
                    break;
                case "banner_collapsible":
                    listBannerCollapsible.add(ads);
                    break;
                case "banner":
                    listBanner.add(ads);
                    break;
            }
        }

        sortNew(listAppOpenSplash);
        sortNew(listInterIntro);
        sortNew(listInterAll);
        sortNew(listNativeLanguage);
        sortNew(listNativeIntro);
        sortNew(listNativeHome);
        sortNew(listNativePermission);
        sortNew(listBanner);

    }

    public static ArrayList<AdsModel> getDataByNameId(ArrayList<AdsModel> adsModels, String nameId){
        try{
            ArrayList<AdsModel> listId = new ArrayList<>();

            for (AdsModel ads : adsModels) {
                if(ads.getName().equals(nameId)){
                    listId.add(ads);
                    //Log.d("getDataByNameId",ads.getId()+ "----"+ads.getAds_id() + "------" + ads.getName());
                }
            }

            sortNew(listId);

            return listId;
        }catch (Exception ex){
            return new ArrayList<AdsModel>();
        }
    }

    public static ArrayList<String> getDataByNameId(String nameId){
        try{
            ArrayList<AdsModel> listId = new ArrayList<>();

            for (AdsModel ads : dataModel.getAdsModels()) {
                if(ads.getName().equals(nameId)){
                    listId.add(ads);
                    Log.d("getDataByNameId",ads.getId()+ "----"+ads.getAds_id() + "------" + ads.getName());
                }
            }

            sortNew(listId);

            ArrayList<String> listID = new ArrayList<>();

            if(listId != null){
                for(AdsModel adsModel : listId) {
                    listID.add(adsModel.getAds_id());
                    Log.d("ListID",adsModel.getId()+ "----"+adsModel.getAds_id() + "------" + adsModel.getName());
                }
            }

            return listID;
        }catch (Exception ex){
            return new ArrayList<String>();
        }
    }

//    public static void sort(ArrayList<AdsModel> list) {
//        list.sort((o1, o2)
//                -> o1.getId().compareTo(
//                o2.getId()));
//    }

    public static void sortNew(ArrayList<AdsModel> list){
        Collections.sort(list, new Comparator<AdsModel>() {
            @Override
            public int compare(AdsModel lhs, AdsModel rhs) {
                return lhs.getId().compareTo(rhs.getId());
            }
        });
    }

    private static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName + mimeType;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public static DataModel getDataModel() {
        return dataModel;
    }

    public static void setDataModel(DataModel dataModel) {
        CommonProcess.dataModel = dataModel;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
