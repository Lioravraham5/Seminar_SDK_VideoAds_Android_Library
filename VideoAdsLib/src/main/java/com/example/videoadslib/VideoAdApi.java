package com.example.videoadslib;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface VideoAdApi {
    @GET("/get_ad")
    Call<VideoAd> fetchAd();

    @POST("/add_ad_event/{package_name}")
    Call<Void> addAdEvent(
            @Path(value = "package_name", encoded = true) String packageName,
            @Body AdEvent adEvent);

    @POST("/create_package/{package_name}")
    Call<Void> createPackage(
            @Path(value = "package_name", encoded = true) String packageName);

}
