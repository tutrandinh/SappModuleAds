package com.ads.sapp.call.api.event;
import com.ads.sapp.call.api.ContentInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface EventApiService {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request newRequest  = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + EventCommon.getInstance().getToken())
                    .build();
            return chain.proceed(newRequest);
        }
    }).build();

    EventApiService apiService = new Retrofit.Builder()
            .baseUrl(EventCommon.getInstance().getBaseUrl()+ EventCommon.getInstance().getPath())
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(EventApiService.class);

    @GET("{filename}")
    Call<ContentInfo> callAdsContentInfo(@Path("filename") String fileName, @Query("ref") String branch);
}
