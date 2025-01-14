package com.example.videoadslib;

import retrofit2.Call;
import retrofit2.http.GET;

public interface VideoAdApi {
    @GET("/get_ad")
    Call<VideoAd> fetchAd();
}
