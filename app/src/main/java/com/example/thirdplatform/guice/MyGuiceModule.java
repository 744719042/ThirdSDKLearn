package com.example.thirdplatform.guice;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.thirdplatform.MyApplication;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class MyGuiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(UserService.class).to(UserServiceImpl.class);
        bind(UserRepository.class).to(UserRepositoryImpl.class);
        bind(Context.class).toInstance(MyApplication.getApplication());

        bind(SharedPreferences.class).annotatedWith(Names.named("settings")).toProvider(SettingPreferenceProvider.class);
        bind(SharedPreferences.class).annotatedWith(Names.named("cache")).toProvider(CachePreferenceProvider.class);

        bind(Integer.class).annotatedWith(Names.named("connectTimeout")).toInstance(3000);
    }

    public static class SettingPreferenceProvider implements Provider<SharedPreferences> {
        private Context context;
        @Inject
        public SettingPreferenceProvider(Context context) {
            this.context = context;
        }
        @Override
        public SharedPreferences get() {
            return context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        }
    }

    public static class CachePreferenceProvider implements Provider<SharedPreferences> {
        private Context context;
        @Inject
        public CachePreferenceProvider(Context context) {
            this.context = context;
        }
        @Override
        public SharedPreferences get() {
            return context.getSharedPreferences("cache", Context.MODE_PRIVATE);
        }
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(@Named("connectTimeout") Integer connectTimeout) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder().connectTimeout(connectTimeout, TimeUnit.MINUTES).build();
        return okHttpClient;
    }
}
