package com.example.fypchatapps.Fragments;

import com.example.fypchatapps.Notifications.MyResponse;
import com.example.fypchatapps.Notifications.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key="
            }
            )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
