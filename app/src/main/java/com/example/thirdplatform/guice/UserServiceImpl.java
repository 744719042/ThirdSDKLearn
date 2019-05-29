package com.example.thirdplatform.guice;

import android.util.Log;

import com.google.inject.Inject;

public class UserServiceImpl implements UserService {
    private static final String TAG = "UserServiceImpl";
    private UserRepository userRepository;

    @Inject
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void addUser() {
        Log.e(TAG, "add user!!");
        userRepository.saveUser();
    }

    @Override
    public void deleteUser() {
        Log.e(TAG, "delete user!!");
        userRepository.deleteUser();
    }
}
