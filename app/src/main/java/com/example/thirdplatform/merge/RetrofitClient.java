package com.example.thirdplatform.merge;

import com.example.thirdplatform.constant.Logger;
import com.example.thirdplatform.constant.NetConstants;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhangxingzhong on 2018/8/28.
 */

public class RetrofitClient {
    private static UserService userService;

    static {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log(String message) {
                        Logger.e(message);
                    }
                }).setLevel(HttpLoggingInterceptor.Level.BODY))
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NetConstants.HTTP_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        userService = retrofit.create(UserService.class);
    }

    public static <T> ObservableTransformer<BaseEntity<T>, BaseEntity<T>> transformer() {
        return new ObservableTransformer<BaseEntity<T>, BaseEntity<T>>() {

            @Override
            public ObservableSource<BaseEntity<T>> apply(Observable<BaseEntity<T>> upstream) {
                return upstream.flatMap(new Function<BaseEntity<T>, ObservableSource<BaseEntity<T>>>() {
                    @Override
                    public ObservableSource<BaseEntity<T>> apply(BaseEntity<T> t) throws Exception {
                        if (t == null || !t.code.equals("0000")) {
                            return Observable.error(ErrorHandler.newError(t.code, t.msg));
                        }
                        return Observable.just(t);
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    public static UserService getUserService() {
        return userService;
    }
}
