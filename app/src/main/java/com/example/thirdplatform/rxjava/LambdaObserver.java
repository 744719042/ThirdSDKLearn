package com.example.thirdplatform.rxjava;

public class LambdaObserver<T> implements Observer<T> {
    private Consumer<T> onNext;

    public LambdaObserver(Consumer<T> onNext) {
        this.onNext = onNext;
    }

    @Override
    public void onSubscribe() {

    }

    @Override
    public void onNext(T data) {
        if (onNext != null) {
            onNext.onNext(data);
        }
    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }
}
