package com.example.thirdplatform.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.thirdplatform.database.greendao.DaoMaster;
import com.example.thirdplatform.database.greendao.DaoSession;
import com.example.thirdplatform.database.entity.DownloadEntity;
import com.example.thirdplatform.database.greendao.DownloadEntityDao;

import java.util.List;

public class DownloadHelper {
    private static DownloadHelper sHelper = new DownloadHelper();

    private DaoMaster mMaster;
    private DaoSession mSession;
    private DownloadEntityDao mDao;

    // 实现封装对象的单例
    public static DownloadHelper getInstance() {
        return sHelper;
    }

    private DownloadHelper() {

    }

    // 在Application中调用的初始化方法
    public void init(Context context) {
        // 创建简单的数据库
        SQLiteDatabase db = new DaoMaster.DevOpenHelper(context, "download.db", null).getWritableDatabase();
        // 初始化操作数据库的对象
        mMaster = new DaoMaster(db);
        mSession = mMaster.newSession();
        mDao = mSession.getDownloadEntityDao();
    }

    // 插入entity功能
    public void insert(DownloadEntity entity) {
        mDao.insertOrReplace(entity);
    }

    public void insertAll(List<DownloadEntity> entityList) {
        mDao.saveInTx(entityList.toArray(new DownloadEntity[0]));
    }

    // 查询Entity的功能
    public List<DownloadEntity> getAll(String url) {
        return mDao.queryBuilder()
                .where(DownloadEntityDao.Properties.DownloadUrl.eq(url))
                .orderAsc(DownloadEntityDao.Properties.ThreadId)
                .list();
    }
}
