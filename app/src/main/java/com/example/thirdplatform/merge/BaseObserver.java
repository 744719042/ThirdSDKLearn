package com.example.thirdplatform.merge;


import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by zhangxingzhong on 2018/8/28.
 */

public abstract class BaseObserver<T> implements Observer<T> {
    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
