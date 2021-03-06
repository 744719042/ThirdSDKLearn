package com.example.thirdplatform.database.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.example.thirdplatform.database.entity.DownloadEntity;
import com.example.thirdplatform.database.entity.StudentEntity;

import com.example.thirdplatform.database.greendao.DownloadEntityDao;
import com.example.thirdplatform.database.greendao.StudentEntityDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig downloadEntityDaoConfig;
    private final DaoConfig studentEntityDaoConfig;

    private final DownloadEntityDao downloadEntityDao;
    private final StudentEntityDao studentEntityDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        downloadEntityDaoConfig = daoConfigMap.get(DownloadEntityDao.class).clone();
        downloadEntityDaoConfig.initIdentityScope(type);

        studentEntityDaoConfig = daoConfigMap.get(StudentEntityDao.class).clone();
        studentEntityDaoConfig.initIdentityScope(type);

        downloadEntityDao = new DownloadEntityDao(downloadEntityDaoConfig, this);
        studentEntityDao = new StudentEntityDao(studentEntityDaoConfig, this);

        registerDao(DownloadEntity.class, downloadEntityDao);
        registerDao(StudentEntity.class, studentEntityDao);
    }
    
    public void clear() {
        downloadEntityDaoConfig.clearIdentityScope();
        studentEntityDaoConfig.clearIdentityScope();
    }

    public DownloadEntityDao getDownloadEntityDao() {
        return downloadEntityDao;
    }

    public StudentEntityDao getStudentEntityDao() {
        return studentEntityDao;
    }

}
