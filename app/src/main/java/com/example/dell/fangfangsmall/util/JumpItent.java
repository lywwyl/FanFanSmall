/*
 * Created by 岱青海蓝信息系统(北京)有限公司 on 17-6-14 下午4:22
 * Copyright (c) 2017. All rights reserved.
 */

package com.example.dell.fangfangsmall.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.example.dell.fangfangsmall.R;
import com.example.dell.fangfangsmall.activity.VerificationActivity;
import com.example.dell.fangfangsmall.youtu.YoutuManager;
import com.megvii.facepp.sdk.Facepp;
import com.megvii.licensemanager.sdk.LicenseManager;

import java.util.UUID;

/**
*description:封装的Intent页面跳转工具类
*autour:guanluocang
*date:17/6/14 16:22
*/
public class JumpItent {
    public final static String REQUEST_CODE = "REQUEST_CODE";

    public static void jump(Activity activity, Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
    }

    public static void jump(Activity activity, Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(activity, cls);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    public static void jump(Activity activity, Class<?> cls, boolean isFinish) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
        if (isFinish) {
            activity.finish();
        }
    }

    public static void jump(Activity activity, Class<?> cls, int requestCode) {
        Intent intent = new Intent(activity, cls);
        intent.putExtra(REQUEST_CODE, requestCode);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void jump(Activity activity, Class<?> cls, Bundle bundle, int requestCode) {
        Intent intent = new Intent(activity, cls);
        if (bundle != null) {
            bundle.putInt(REQUEST_CODE, requestCode);
            intent.putExtras(bundle);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    public static void jump(Activity activity, Class<?> cls, boolean isFinish, Bundle bundle) {
        Intent intent = new Intent(activity, cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        activity.startActivity(intent);
        if (isFinish) {
            activity.finish();
        }
    }

    public static void jump(Activity activity, Class<?> cls, boolean isFinish, int requestCode) {
        Intent intent = new Intent(activity, cls);
        intent.putExtra(REQUEST_CODE, requestCode);
        activity.startActivityForResult(intent, requestCode);
        if (isFinish) {
            activity.finish();
        }
    }

    public static void jump(Activity activity, Class<?> cls, boolean isFinish, Bundle bundle, int requestCode) {
        Intent intent = new Intent(activity, cls);
        if (bundle != null) {
            bundle.putInt(REQUEST_CODE, requestCode);
            intent.putExtras(bundle);
        }
        activity.startActivityForResult(intent, requestCode);
        if (isFinish) {
            activity.finish();
        }
    }

    public static void jump(Activity activity, String action) {
        Intent intent = new Intent(action);
        activity.startActivity(intent);
    }

    public static void jump(Activity activity, String action, Bundle bundle) {
        Intent intent = new Intent(action);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    public static void jump(Activity activity, String action, int requestCode) {
        Intent intent = new Intent(action);
        intent.putExtra(REQUEST_CODE, requestCode);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void jump(Activity activity, String action, Bundle bundle, int requestCode) {
        Intent intent = new Intent(action);
        bundle.putInt(REQUEST_CODE, requestCode);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void jump(Activity activity, String action, Uri uri) {
        Intent intent = new Intent(action, uri);
        activity.startActivity(intent);
    }

    public static void jump(Activity activity, String action, Uri uri, int requestCode){
        Intent intent = new Intent(action, uri);
        intent.putExtra(REQUEST_CODE, requestCode);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void jumpVerification(final Activity activity, final String authId) {
            final LicenseManager licenseManager = new LicenseManager(activity);
            licenseManager.setExpirationMillis(Facepp.getApiExpirationMillis(activity, ConUtil.getFileContent(activity, R.raw.megviifacepp_0_4_7_model)));
            String uuid = UUID.randomUUID().toString();
            uuid = Base64.encodeToString(uuid.getBytes(), Base64.DEFAULT);
            long apiName = Facepp.getApiName();
            licenseManager.setAuthTimeBufferMillis(0);
            licenseManager.takeLicenseFromNetwork(uuid, YoutuManager.API_KEY, YoutuManager.API_SECRET, apiName,
                    LicenseManager.DURATION_30DAYS, "Landmark", "1", true, new LicenseManager.TakeLicenseCallback() {
                        @Override
                        public void onSuccess() {
                            Log.e("", "onSuccess");
                            Intent intent = new Intent(activity, VerificationActivity.class);
                            intent.putExtra("AuthId", authId);
                            activity.startActivity(intent);
                        }

                        @Override
                        public void onFailed(int i, byte[] bytes) {
                            Log.e("", "onFailed" + i + "  " + new String(bytes));
                        }
                    });

    }

}
