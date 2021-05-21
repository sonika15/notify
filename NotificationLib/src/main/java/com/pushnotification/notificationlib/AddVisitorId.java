package com.pushnotification.notificationlib;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.pushnotification.notificationlib.NetworkUtility.ApiResponse;
import com.pushnotification.notificationlib.NetworkUtility.ApiUtil;
import com.pushnotification.notificationlib.NetworkUtility.DeviceSubscribe;
import com.pushnotification.notificationlib.NetworkUtility.NetworkService;
import com.pushnotification.notificationlib.NetworkUtility.Subscription;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AddVisitorId extends Application {
    SharedPreferences prefs;
    String deviceToken = "";
    Context context;
    String visitorId;

    public AddVisitorId(Application application) {
        this.context = application;
    }

    public void setVisitorId(String visitorId) {
        Log.wtf("visitorId", visitorId);
        this.visitorId = visitorId;
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String deviceToken = ApiUtil.getToken(context);
                if (!deviceToken.equals("")) {
                    Log.wtf("deviceToken", deviceToken);
                    sendPost();
                } else {
                    Log.wtf("deviceToken", "empty hai ");
                }
            }
        }, 5000);
    }

    public void sendPost() {
        Log.wtf("deviceToken", deviceToken);
        NetworkService service = ApiUtil.getAPIService();
        Subscription subscription = new Subscription(deviceToken);
        DeviceSubscribe deviceSubscribe = new DeviceSubscribe(visitorId, subscription, "android");
        service.savePost(deviceSubscribe).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    //showResponse(response.body().toString());
                    Log.wtf("post submitted to API.", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.wtf("failure", "Unable to submit post to API.");
            }
        });
    }

}
