package com.example.thirdplatform.rxjava;

public interface Function<T, R> {
    R apply(T item) throws Exception;
}
