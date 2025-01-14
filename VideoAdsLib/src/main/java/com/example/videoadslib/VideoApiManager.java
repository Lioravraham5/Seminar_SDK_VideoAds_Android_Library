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
                adFetchedCallback.onFailureFetching(throwable.getMessage());
            }
        });
    }

    public interface AdFetchedCallback{
        void onSuccessFetching(VideoAd ad);
        void onFailureFetching(String errorMessage);
    }


}
