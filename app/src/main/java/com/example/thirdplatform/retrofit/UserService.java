package com.example.thirdplatform.retrofit;

import com.example.thirdplatform.retrofit.http.Call;
import com.example.thirdplatform.retrofit.http.GET;
import com.example.thirdplatform.retrofit.http.Query;

public interface UserService {
    @GET("/OkHttpServer/login")
    Call<UserInfoEntity> login(@Query("userName") String account, @Query("password") String pwd);
}
