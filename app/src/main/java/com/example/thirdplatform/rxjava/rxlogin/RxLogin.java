package com.example.thirdplatform.rxjava.rxlogin;

import android.app.Activity;

public class RxLogin {
    private Activity activity;

    private RxLogin(Activity activity) {
        this.activity = activity;
    }

    public static RxLogin create(Activity activity) {
        return new RxLogin(activity);
    }


}
