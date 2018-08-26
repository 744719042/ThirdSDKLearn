package com.example.thirdplatform.rxjava;

public abstract class Schedulers {
    static Schedulers IO = new IOSchedulers();
    static Schedulers MAIN = new MainSchedulers();

    public static Schedulers io() {
        return IO;
    }

    public abstract void scheduleDirect(Runnable task);

    public static Schedulers mainThread() {
        return MAIN;
    }
}
