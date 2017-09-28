package com.example.dell.fangfangsmall.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.dell.fangfangsmall.gensimple.DaoMaster;
import com.example.dell.fangfangsmall.gensimple.UserInfoDao;


/**
 * Created by kaifa on 2017/2/23.
 */

public class MySQLiteOpenHelper extends DaoMaster.OpenHelper {
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
//        if (oldVersion == newVersion) {
//            Log.e("onUpgrade", "数据库是最新版本,无需升级" );
//            return;
//        }
//        Log.e("onUpgrade", "数据库从版本" + oldVersion + "升级到版本" + newVersion);
//        switch (oldVersion) {
//            case 1:
//                String sql = "";
//                db.execSQL(sql);
//            case 2:
//            default:
//                break;
//        }
        //将所有生成的dao类传入
        MigrationHelper.migrate(db, UserInfoDao.class);
    }
}
