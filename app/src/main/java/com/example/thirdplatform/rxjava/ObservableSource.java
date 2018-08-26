package com.example.thirdplatform.rxjava;

public interface ObservableSource<T> {
    void subscribe(Observer<T> observer);
}
