package com.example.dell.fangfangsmall.gensimple;


import com.example.dell.fangfangsmall.dao.UserInfo;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import java.util.Map;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig userInfoDaoConfig;

    private final UserInfoDao userInfoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        userInfoDaoConfig = daoConfigMap.get(UserInfoDao.class).clone();
        userInfoDaoConfig.initIdentityScope(type);

        userInfoDao = new UserInfoDao(userInfoDaoConfig, this);

        registerDao(UserInfo.class, userInfoDao);
    }
    
    public void clear() {
        userInfoDaoConfig.clearIdentityScope();
    }

    public UserInfoDao getUserInfoDao() {
        return userInfoDao;
    }

}
