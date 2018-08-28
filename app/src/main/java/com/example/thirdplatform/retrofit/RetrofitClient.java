package com.example.thirdplatform.retrofit;

import android.util.Log;

import com.example.thirdplatform.constant.NetConstants;
import com.example.thirdplatform.retrofit.http.Retrofit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhangxingzhong on 2018/8/28.
 */

public class RetrofitClient {
    private static final String TAG = "RetrofitClient";
    private final static UserService userService;

    static {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Log.e(TAG, message);
                    }
                }).setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetConstants.HTTP_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        userService = retrofit.create(UserService.class);
    }

    public static UserService getUserService() {
        return userService;
    }
}
