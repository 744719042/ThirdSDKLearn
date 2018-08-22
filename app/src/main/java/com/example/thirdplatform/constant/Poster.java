package com.example.thirdplatform.constant;

import android.os.Handler;
import android.os.Looper;

public class Poster extends Handler {
    private static volatile Poster sInstance;

    private Poster(Looper mainLooper) {
        super(mainLooper);
    }

    public static Poster getInstance() {
        if (sInstance == null) {
            synchronized (Poster.class) {
                if (sInstance == null) {
                    sInstance = new Poster(Looper.getMainLooper());
                }
            }
        }

        return sInstance;
    }
}
