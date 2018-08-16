package com.example.thirdplatform;

import android.app.Application;
import android.content.Context;

import com.example.thirdplatform.database.SQLiteDaoFactory;
import com.facebook.stetho.Stetho;

public class MyApplication extends Application {
    private static Context application;

    public static Context getApplication() {
        return application;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
//        DownloadHelper.getInstance().init(this);
        SQLiteDaoFactory.getInstance(this);
        Stetho.initializeWithDefaults(this);
    }
}
