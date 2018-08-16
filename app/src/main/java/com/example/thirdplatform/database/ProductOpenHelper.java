package com.example.thirdplatform.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.thirdplatform.database.entity.DownloadEntity;

public class ProductOpenHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "books.db";
    private static final int DB_VERSION = 1;

    private Context mContext;

    public ProductOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.mContext = context.getApplicationContext();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DownloadDaoSupport.dropTable(db, DownloadEntity.class, true);
        DownloadDaoSupport.createTable(db, DownloadEntity.class, true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
