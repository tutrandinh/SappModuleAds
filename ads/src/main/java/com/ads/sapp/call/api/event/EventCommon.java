package com.ads.sapp.call.api.event;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.ads.sapp.ads.CommonAd;
import com.ads.sapp.call.api.AdsModel;
import com.ads.sapp.call.api.ApiService;
import com.ads.sapp.call.api.ContentInfo;
import com.ads.sapp.call.api.DataModel;
import com.ads.sapp.call.api.IsError;
import com.ads.sapp.event.FirebaseAnalyticsUtil;
import com.ads.sapp.funtion.AdCallback;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventCommon {
    private static EventData eventData = new EventData();

    private String token = "";
    private String baseUrl = "https://api.github.com/";
    private String path = "repos/tutrandinh/AdsStorage/contents/";
    private String fileName = "";
    private String branch = "main";
    private String mimeType = ".json";

    private static boolean callTrue = false;

    //Event
    public static final Integer ROAS = -999;
    public static final Double COST = -999.0;
    public static final Integer DEFAULT_VALUE = 0;

    public static Integer roas = ROAS;
    public static Double cost = COST;
    public static Integer defaultValue = DEFAULT_VALUE;

    private static volatile EventCommon INSTANCE;

    public static synchronized EventCommon getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new EventCommon();
        }
        return INSTANCE;
    }

    public static void LoadDataEvent(Context context){
        eventData = new EventData();

        if(getInstance().fileName.equals("") || getInstance().branch.equals("") || getInstance().token.equals("")){
            return;
        }

        if (isNetworkConnected(context)){
            EventApiService.apiService.callAdsContentInfo(EventCommon.getInstance().fileName, EventCommon.getInstance().branch ).enqueue(new Callback<ContentInfo>() {

                @Override
                public void onResponse(Call<ContentInfo> call, Response<ContentInfo> response) {
                    if(response.body() != null){
                        if(response.isSuccessful()) {
                            ContentInfo contentInfo = response.body();
                            eventData =  getDataAll(contentInfo);
                            if(eventData.getIsError().getError()){
                                //Error
                            }else{
                                ArrayList<EventModel> eventModels = eventData.getEventModel();
                                EventModel eventModel = eventModels.get(0);
                                roas = eventModel.getRoas();
                                cost = eventModel.getCost();
                                defaultValue = eventModel.getDefaultValue();
                                Log.d("EventModel",roas.toString() +"____1");
                                Log.d("EventModel",cost.toString() +"____2");
                                Log.d("EventModel",defaultValue.toString() +"____3");
                            }

                        } else {
                            Log.d("ErrorAds","Error no data");
                        }
                    }else{
                        Log.d("ErrorAds","Error null data");
                    }
                }

                @Override
                public void onFailure(Call<ContentInfo> call, Throwable t) {
                    Log.d("ErrorAds","onFailure");
                }
            });
        }else{
            Log.d("ErrorAds","No Network Connected");
        }
    }

    public static void LoadDataEventWithCallBack(Context context, EventCallback eventCallback){
        try{
            eventData = new EventData();

            if(getInstance().fileName.equals("") || getInstance().branch.equals("") || getInstance().token.equals("")){
                return;
            }

            if (isNetworkConnected(context)){
                EventApiService.apiService.callAdsContentInfo(EventCommon.getInstance().fileName, EventCommon.getInstance().branch ).enqueue(new Callback<ContentInfo>() {

                    @Override
                    public void onResponse(Call<ContentInfo> call, Response<ContentInfo> response) {
                        if(response.body() != null){
                            if(response.isSuccessful()) {
                                ContentInfo contentInfo = response.body();
                                eventData =  getDataAll(contentInfo);
                                if(eventData.getIsError().getError()){
                                    //Error
                                    eventCallback.onFail();
                                }else{
                                    ArrayList<EventModel> eventModels = eventData.getEventModel();
                                    EventModel eventModel = eventModels.get(0);
                                    roas = eventModel.getRoas();
                                    cost = eventModel.getCost();
                                    defaultValue = eventModel.getDefaultValue();
                                    Log.d("EventModel",roas.toString() +"____1");
                                    Log.d("EventModel",cost.toString() +"____2");
                                    Log.d("EventModel",defaultValue.toString() +"____3");
                                    eventCallback.onSuccessful();
                                }

                            } else {
                                Log.d("ErrorAds","Error no data");
                                eventCallback.onFail();
                            }
                        }else{
                            Log.d("ErrorAds","Error null data");
                            eventCallback.onFail();
                        }
                    }

                    @Override
                    public void onFailure(Call<ContentInfo> call, Throwable t) {
                        Log.d("ErrorAds","onFailure");
                        eventCallback.onFail();
                    }
                });
            }else{
                Log.d("ErrorAds","No Network Connected");
                eventCallback.onFail();
            }
        }catch (Exception e){
            eventCallback.onFail();
        }
    }

    public static void eventLogData(Context context, double revenue, int precision, String adUnitId) {
        try{
            double revenueValue = revenue / 1000000.0;

            if(roas == ROAS && callTrue == false){

                LoadDataEventWithCallBack(context, new EventCallback(){
                    @Override
                    public void onSuccessful() {
                        callTrue = true;
                        Bundle params = new Bundle();
                        if(defaultValue == 1){
                            if(cost > revenueValue){
                                params.putDouble("value_micros", revenueValue);
                            }else if(cost == revenueValue){
                                params.putDouble("value_micros", revenueValue);
                            }else {
                                double roasCheck = revenueValue / cost ;
                                if(roasCheck >= 1.0){
                                    params.putDouble("value_micros", cost);
                                } else if(roasCheck < 1.0 && roasCheck >= 0.8) {
                                    params.putDouble("value_micros", cost);
                                } else {
                                    params.putDouble("value_micros", revenueValue);
                                }
                            }
                        } else if (defaultValue == 2) {
                            params.putDouble("value_micros", revenueValue* roas/(100*100));
                        }else if(defaultValue == 3){
                            params.putDouble("value_micros", cost);
                        }else {
                            if(cost > revenueValue){
                                params.putDouble("value_micros", revenueValue);
                            }else if(cost == revenueValue){
                                params.putDouble("value_micros", revenueValue);
                            }else {
                                double roasCheck = revenueValue / cost ;
                                if(roasCheck >= roas/(100*100)){
                                    params.putDouble("value_micros", cost);
                                } else if(roasCheck < (roas/(100*100) +0.5) && roasCheck >= (roas/(100*100) - 0.5)) {
                                    params.putDouble("value_micros", cost);
                                } else {
                                    params.putDouble("value_micros", revenueValue);
                                }
                            }
                        }

                        params.putString("currency_unit", "USD");
                        params.putString("ad_unit_id", adUnitId);
                        eventLog(context, params);

                        Bundle paramsValue = new Bundle();
                        paramsValue.putDouble("value_micros", revenue);
                        paramsValue.putString("currency_unit", "USD");
                        paramsValue.putString("ad_unit_id", adUnitId);
                        eventLogValue(context,paramsValue);

                        super.onSuccessful();
                    }

                    @Override
                    public void onFail() {
                        callTrue = false;
                        Bundle params = new Bundle();
                        params.putDouble("value_micros", revenueValue);
                        params.putString("currency_unit", "USD");
                        params.putString("ad_unit_id", adUnitId);
                        eventLog(context, params);

                        Bundle paramsValue = new Bundle();
                        paramsValue.putDouble("value_micros", revenue);
                        paramsValue.putString("currency_unit", "USD");
                        paramsValue.putString("ad_unit_id", adUnitId);
                        eventLogValue(context,paramsValue);

                        super.onFail();
                    }
                });
            }else {
                Bundle params = new Bundle();
                if(defaultValue == 1){
                    if(cost > revenueValue){
                        params.putDouble("value_micros", revenueValue);
                    }else if(cost == revenueValue){
                        params.putDouble("value_micros", revenueValue);
                    }else {
                        double roasCheck = revenueValue / cost ;
                        if(roasCheck >= 1.0){
                            params.putDouble("value_micros", cost);
                        } else if(roasCheck < 1.0 && roasCheck >= 0.8) {
                            params.putDouble("value_micros", cost);
                        } else {
                            params.putDouble("value_micros", revenueValue);
                        }
                    }
                } else if (defaultValue == 2) {
                    params.putDouble("value_micros", revenueValue* roas/(100*100));
                }else if(defaultValue == 3){
                    params.putDouble("value_micros", cost);
                }else {
                    if(cost > revenueValue){
                        params.putDouble("value_micros", revenueValue);
                    }else if(cost == revenueValue){
                        params.putDouble("value_micros", revenueValue);
                    }else {
                        double roasCheck = revenueValue / cost ;
                        if(roasCheck >= roas/(100*100)){
                            params.putDouble("value_micros", cost);
                        } else if(roasCheck < (roas/(100*100) +0.5) && roasCheck >= (roas/(100*100) - 0.5)) {
                            params.putDouble("value_micros", cost);
                        } else {
                            params.putDouble("value_micros", revenueValue);
                        }
                    }
                }

                params.putString("currency_unit", "USD");
                params.putString("ad_unit_id", adUnitId);
                eventLog(context, params);

                Bundle paramsValue = new Bundle();
                paramsValue.putDouble("value_micros", revenue);
                paramsValue.putString("currency_unit", "USD");
                paramsValue.putString("ad_unit_id", adUnitId);
                eventLogValue(context,paramsValue);
            }

            Log.d("EventModel",roas.toString() +"_d___1");
            Log.d("EventModel",cost.toString() +"_d___2");
            Log.d("EventModel",defaultValue.toString() +"_d___3");

        }catch (Exception e){
        }
    }

    public static void eventLog(Context context, Bundle params) {
        FirebaseAnalytics.getInstance(context).logEvent("ai_ad_impression", params);
    }

    public static void eventLogValue(Context context, Bundle params) {
        FirebaseAnalytics.getInstance(context).logEvent("ai_ad_impression_value", params);
    }

    private static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public static EventData getDataAll(ContentInfo contentInfo) {

        EventData eventData = new EventData();
        IsError isError = new IsError(false,"","","");
        eventData.setIsError(isError);
        ArrayList<EventModel>  eventModels = new ArrayList<>();

        try {
            byte[] decodedBytes = Base64.decode(contentInfo.getContent(),Base64.DEFAULT);
            String decodedString = new String(decodedBytes);

            Gson gson = new Gson();
            EventModel[] userArray = gson.fromJson(decodedString, EventModel[].class);

            for(EventModel eventModel : userArray) {
                eventModels.add(eventModel);
                //Log.d("ContentInfo",adsModel.getAds_id() + "------" + adsModel.getName());
            }

            if(eventModels != null){
                if(eventModels.size() < 1) {
                    isError = new IsError(true,"E0001","Error no data","");
                }
            }

            eventData.setEventModel(eventModels);
            eventData.setIsError(isError);
            return eventData;

        }catch (Exception ex){
            eventModels = new ArrayList<EventModel>();
            isError = new IsError(true,"E0002","Exception getDataAll","");

            eventData.setEventModel(eventModels);
            eventData.setIsError(isError);
            return eventData;
        }
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

    public static EventData getEventData() {
        return eventData;
    }

    public static void setEventData(EventData eventData) {
        EventCommon.eventData = eventData;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
}
