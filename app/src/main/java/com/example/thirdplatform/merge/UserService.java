package com.example.thirdplatform.merge;


import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by zhangxingzhong on 2018/8/28.
 */

public interface UserService {
    @GET("/OkHttpServer/login")
    Observable<BaseEntity<UserInfo>> login(@Query("userName") String userName, @Query("password") String password);
}
