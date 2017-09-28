package com.example.dell.fangfangsmall;

import android.app.Application;
import android.content.Context;

import com.example.dell.fangfangsmall.dao.GreenDaoManager;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by dell on 2017/9/14.
 */

public class FangFangSmallApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        GreenDaoManager.getInstance();
        /**
         * 讯飞初始化
         * 原始app_id = 595c594c
         */
        SpeechUtility.createUtility(getApplicationContext(), SpeechConstant.APPID + "=59b8fefd" + "," + SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC);

    }


    /**
     * 获取的app的Application对象
     *
     * @param context 上下文
     * @return Application对象
     */
    public static FangFangSmallApplication from(Context context) {
        return (FangFangSmallApplication) context.getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }


}
