package com.example.dell.fangfangsmall.dao;


import com.example.dell.fangfangsmall.FangFangSmallApplication;
import com.example.dell.fangfangsmall.gen.DaoMaster;
import com.example.dell.fangfangsmall.gen.DaoSession;
import com.example.dell.fangfangsmall.helper.MySQLiteOpenHelper;

import org.greenrobot.greendao.database.Database;

/**
 * Created by lyw on 2017/9/26.
 */

public class GreenDaoManager {
    private static final String DB_NAME = "greendao";

    private static GreenDaoManager mInstance;
    private DaoMaster daoMaster;
    private DaoSession daoSession;

    public static GreenDaoManager getInstance() {
        if (mInstance == null) {
            synchronized (GreenDaoManager.class) {
                if (mInstance == null) {
                    mInstance = new GreenDaoManager();
                }
            }
        }
        return mInstance;
    }

    private GreenDaoManager() {
        if (mInstance == null) {
            MySQLiteOpenHelper helper = new MySQLiteOpenHelper(FangFangSmallApplication.getContext(), DB_NAME, null);
            Database db = helper.getWritableDb();
            daoMaster = new DaoMaster(db);
            daoSession = daoMaster.newSession();
        }
    }


    public DaoSession getDaoSession() {
        return daoSession;
    }

    public DaoMaster getDaoMaster() {
        return daoMaster;
    }

}
