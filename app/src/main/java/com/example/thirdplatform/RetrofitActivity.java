package com.example.thirdplatform;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.thirdplatform.constant.Logger;
import com.example.thirdplatform.constant.NetConstants;
import com.example.thirdplatform.retrofit.UserInfo;
import com.example.thirdplatform.retrofit.UserInfoEntity;
import com.example.thirdplatform.retrofit.UserService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitActivity extends AppCompatActivity implements View.OnClickListener {
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        login = findViewById(R.id.login);
        login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == login) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(NetConstants.HTTP_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            UserService userService = retrofit.create(UserService.class);
            Call<UserInfoEntity> call = userService.login("Darren", "123456");
            call.enqueue(new Callback<UserInfoEntity>() {
                @Override
                public void onResponse(Call<UserInfoEntity> call, Response<UserInfoEntity> response) {
                    UserInfoEntity userInfo = response.body();
                    Logger.d(userInfo.data.toString());
                }

                @Override
                public void onFailure(Call<UserInfoEntity> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }
}
