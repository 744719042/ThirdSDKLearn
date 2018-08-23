package com.example.thirdplatform.retrofit;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface UserService {
    @POST("/OkHttpServer/login")
    @FormUrlEncoded
    Call<UserInfoEntity> login(@Field("userName") String account, @Field("password") String pwd);
}
