package com.example.pakistanlawyerdiary.Fragments;

import com.example.pakistanlawyerdiary.Notifications.MyResponse;
import com.example.pakistanlawyerdiary.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
            "Content-Type:application/json",
            "Authorization:Key=AAAAdCB2NJw:APA91bE0eEvSDFf9doUmQ_vXrM50BmYvE_JD1kUirRCvjriYUvq98oC929J6n0f1xicZzt87yKIOVoxkE9dh4SnY1wkHSPP4Nfz0WWaudm2pw9Qdmxiy-oUIzZPUWGPfRw-mI5egmV3T"
    }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}

