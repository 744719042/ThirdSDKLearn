package com.example.thirdplatform.rxjava;

public interface Observer<T> {
    void onSubscribe();
    void onNext(T data);
    void onError(Throwable e);
    void onComplete();
}
