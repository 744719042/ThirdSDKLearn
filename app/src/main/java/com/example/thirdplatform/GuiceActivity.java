package com.example.thirdplatform;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.thirdplatform.guice.MyGuiceModule;
import com.example.thirdplatform.guice.UserService;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.MembersInjector;
import com.google.inject.name.Named;

import okhttp3.OkHttpClient;

public class GuiceActivity extends AppCompatActivity {
    private static final String TAG = "GuiceActivity";
    @Inject
    private UserService userService;
    @Inject
    @Named("settings")
    private SharedPreferences settingPreference;
    @Inject
    @Named("cache")
    private SharedPreferences cachePreference;

    @Inject
    private OkHttpClient okHttpClient;
    @Inject
    private OkHttpClient myHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guice);

        MembersInjector<GuiceActivity> membersInjector = Guice.createInjector(new MyGuiceModule()).getMembersInjector(GuiceActivity.class);
        membersInjector.injectMembers(this);

        userService.addUser();
        userService.deleteUser();

        settingPreference.edit().putString("hello", "world").apply();
        cachePreference.edit().putString("list", "[{ }, { }]").apply();

        Log.e(TAG, "okhttpclient == myhttpclient ? " + (okHttpClient == myHttpClient));
    }
}
