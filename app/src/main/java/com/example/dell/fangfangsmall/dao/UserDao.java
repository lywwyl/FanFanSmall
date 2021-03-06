package com.example.dell.fangfangsmall.dao;


import com.example.dell.fangfangsmall.gen.UserInfoDao;

import org.greenrobot.greendao.query.Query;

import java.util.List;

/**
 * 操作数据库 增删改查
 * Created by kaifa on 2017/6/28.
 */

public class UserDao {

    private final GreenDaoManager daoManager;
    private static UserDao mUserDao;

    public UserDao() {
        daoManager = GreenDaoManager.getInstance();
    }

    public static UserDao getInstance() {
        if (mUserDao == null) {
            mUserDao = new UserDao();
        }
        return mUserDao;
    }

    /**
     * 插入数据 若未建表则先建表
     *
     * @param userInfo
     * @return
     */
    public boolean insertUserData(UserInfo userInfo) {
        boolean flag = false;
        flag = getUserInfoDao().insert(userInfo) == -1 ? false : true;
        return flag;
    }

    /**
     * 插入或替换数据
     *
     * @param userInfo
     * @return
     */
    public boolean insertOrReplaceData(UserInfo userInfo) {
        boolean flag = false;
        try {
            flag = getUserInfoDao().insertOrReplace(userInfo) == -1 ? false : true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 插入多条数据  子线程完成
     *
     * @param list
     * @return
     */
    public boolean insertOrReplaceMultiData(final List<UserInfo> list) {
        boolean flag = false;
        try {
            getUserInfoDao().getSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (UserInfo userInfo : list) {
                        daoManager.getDaoSession().insertOrReplace(userInfo);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 更新数据
     *
     * @param userInfo
     * @return
     */
    public boolean updateUserData(UserInfo userInfo) {
        boolean flag = false;
        try {
            getUserInfoDao().update(userInfo);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 根据id删除数据
     *
     * @param userInfo
     * @return
     */
    public boolean deleteUserData(UserInfo userInfo) {
        boolean flag = false;
        try {
            getUserInfoDao().delete(userInfo);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除所有数据
     *
     * @return
     */
    public boolean deleteAllData() {
        boolean flag = false;
        try {
            getUserInfoDao().deleteAll();
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 根据主键查询
     *
     * @param key
     * @return
     */
    public UserInfo queryUserDataById(long key) {
        return getUserInfoDao().load(key);
    }

    /**
     * 查询所有数据
     *
     * @return
     */
    public List<UserInfo> queryAllData() {
        return getUserInfoDao().loadAll();
    }

    /**
     * 根据名称查询 以ID降序排列
     *
     * @param type
     * @return
     */
    public List<UserInfo> queryUserByType(String type) {
        Query<UserInfo> build = null;
        try {
            build = getUserInfoDao().queryBuilder()
                    .where(UserInfoDao.Properties.Type.eq(type)).orderAsc
                            (UserInfoDao.Properties.Id)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return build.list();
    }
    /**
     * 根据名称查询 以ID降序排列
     *
     * @param question
     * @return
     */
    public List<UserInfo> queryUserByQuestion(String question) {
        Query<UserInfo> build = null;
        try {
            build = getUserInfoDao().queryBuilder()
                    .where(UserInfoDao.Properties.Question.eq(question))
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return build.list();
    }

    /**
     * 根据参数查询
     *
     * @param where
     * @param param
     * @return
     */
    public List<UserInfo> queryUserByParams(String where, String... param) {
        return getUserInfoDao().queryRaw(where, param);
    }

    public UserInfoDao getUserInfoDao() {
        return daoManager.getDaoSession().getUserInfoDao();
    }
}
