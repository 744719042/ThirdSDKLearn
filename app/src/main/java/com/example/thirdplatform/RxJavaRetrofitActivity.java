package com.example.thirdplatform;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.thirdplatform.constant.Logger;
import com.example.thirdplatform.merge.BaseEntity;
import com.example.thirdplatform.merge.BaseObserver;
import com.example.thirdplatform.merge.RetrofitClient;
import com.example.thirdplatform.merge.UserInfo;

public class RxJavaRetrofitActivity extends AppCompatActivity implements View.OnClickListener {
    private Button retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_retrofit);
        retrofit = findViewById(R.id.retrofit);
        retrofit.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if (view == retrofit) {
            RetrofitClient.getUserService()
                    .login("Darren", "123456")
                    .compose(RetrofitClient.<UserInfo>transformer())
                    .subscribe(new BaseObserver<BaseEntity<UserInfo>>() {
                        @Override
                        public void onNext(BaseEntity<UserInfo> userInfoEntityBaseEntity) {
                            Logger.e(userInfoEntityBaseEntity.msg);
                        }
                    });

//            RetrofitClient.getUserService()
//                    .login("Darren", "123456")
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Observer<UserInfoEntity>() {
//                        @Override
//                        public void onSubscribe(Disposable d) {
//
//                        }
//
//                        @Override
//                        public void onNext(UserInfoEntity userInfoEntity) {
//                            Logger.e(userInfoEntity.msg);
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//
//                        }
//
//                        @Override
//                        public void onComplete() {
//
//                        }
//                    });
        }
    }
}
