package com.codetutor.geolocationdemos.Registration;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.codetutor.geolocationdemos.Utils.RestAPIs;
import com.codetutor.geolocationdemos.Utils.ToDoAppRestAPI;
import com.codetutor.geolocationdemos.model.DriverRegistrationModel;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistrationRequest {
    public OkHttpClient okHttpClient;


    public void createRequest(DriverRegistrationModel regObj,final Context context){
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10,TimeUnit.SECONDS)
                .build();
        final Activity activity = (Activity) context;
        Request request = new Request.Builder().url(RestAPIs.getBaseUrl()+ ToDoAppRestAPI.registerAuthor)
                .addHeader("Content-Type","application/json")
                .post(RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(regObj)))
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call,final IOException e) {

                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call,final Response response) throws IOException {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(activity,response.body().toString(),Toast.LENGTH_LONG).show();
                    }
                });

            }
        });

    }
}
