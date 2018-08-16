package com.example.thirdplatform.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.thirdplatform.database.entity.DownloadEntity;

public final class SQLiteDaoFactory {
    private static SQLiteDaoFactory INSTANCE;
    private volatile SQLiteDatabase mDatabase;

    private DownloadDaoSupport mDownloadDao;

    private SQLiteDaoFactory(Context context) {
        mDatabase = new ProductOpenHelper(context).getWritableDatabase();
        mDownloadDao = new DownloadDaoSupport();
        mDownloadDao.init(mDatabase, DownloadEntity.class);
    }

    public static SQLiteDaoFactory getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteDaoFactory.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SQLiteDaoFactory(context);
                }
            }
        }
        return INSTANCE;
    }

    public DownloadDaoSupport getDownloadDao() {
        return mDownloadDao;
    }
}
