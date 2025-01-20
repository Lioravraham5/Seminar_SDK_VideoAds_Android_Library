package com.example.videoadslib;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class VideoApiManager {
    private final String BASE_URL = "https://seminar-sdk-flask-api.vercel.app/";
    private final int SUCCESS_CODE = 200;

    public VideoAdApi getVideoAdApi(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        VideoAdApi videoAdApi = retrofit.create(VideoAdApi.class);

        return videoAdApi;
    }

    public void fetchAd(AdFetchedCallback adFetchedCallback){
        Call<VideoAd> call = getVideoAdApi().fetchAd();
        call.enqueue(new Callback<VideoAd>() {
            @Override
            public void onResponse(Call<VideoAd> call, Response<VideoAd> response) {
                if(response.code() == SUCCESS_CODE && response.body() != null){
                    Log.d("VideoApiManager", "Response: " + response.body().toString());
                    VideoAd ad = response.body();
                    adFetchedCallback.onSuccessFetching(ad);
                }
                else {
                    adFetchedCallback.onFailureFetching(response.message());
                }
            }

            @Override
            public void onFailure(Call<VideoAd> call, Throwable throwable) {
                Log.d("VideoApiManager", "Error: " + throwable.getMessage());
                adFetchedCallback.onFailureFetching(throwable.getMessage());
            }
        });
    }

    public void addAdEvent(String packageName, AdEvent adEvent){
        Call<Void> call = getVideoAdApi().addAdEvent(packageName, adEvent);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == SUCCESS_CODE){
                    Log.d("VideoApiManager", "Ad event added successfully");
                }
                else {
                    Log.d("VideoApiManager", "Error adding ad event: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Log.d("VideoApiManager", "Error: " + throwable.getMessage());
            }
        });
    }

    public void createPackage(String packageName){
        Call<Void> call = getVideoAdApi().createPackage(packageName);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code() == SUCCESS_CODE){
                    Log.d("VideoApiManager", "Package created successfully");
                }
                else {
                    Log.d("VideoApiManager", "Error creating package: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                Log.d("VideoApiManager", "Error: " + throwable.getMessage());
            }
        });
    }

    public interface AdFetchedCallback{
        void onSuccessFetching(VideoAd ad);
        void onFailureFetching(String errorMessage);
    }


}
