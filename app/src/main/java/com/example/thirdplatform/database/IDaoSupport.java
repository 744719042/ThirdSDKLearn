package com.example.thirdplatform.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

public interface IDaoSupport<T> {
    void init(SQLiteDatabase database, Class<?> clazz);
    void insert(T t);
    void insertAll(List<T> list);
    List<T> query(String where, String[] args);
    void update(T t);
    void delete(String where, String[] whereArgs);
}
