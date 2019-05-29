package com.example.thirdplatform.guice;

import android.util.Log;

public class UserRepositoryImpl implements UserRepository {
    private static final String TAG = "UserRepository";

    public void saveUser() {
        Log.e(TAG, "save user....");
    }

    public void deleteUser() {
        Log.e(TAG, "delete user....");
    }
}
