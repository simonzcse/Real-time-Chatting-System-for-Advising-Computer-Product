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
                    "Authorization:key=AAAAjb__EUM:APA91bFFbKFggcp9b-1yh1PfwMrnsDwIkRwd9bho53yRhvSjs61HXLYqn1M38YK_WQb3s7sSs8gKBxDumwHntwy0xZgn93_GNAwzkdjfdWAcZWztKB_bXwj5dTq8UJpA8nmiuH2BvLoX"
            }
            )
    @POST("fcm/send")
    Call<MyResponse> sendNotification(@Body Sender body);
}
