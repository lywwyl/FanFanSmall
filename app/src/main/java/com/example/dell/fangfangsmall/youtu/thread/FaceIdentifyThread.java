package com.example.dell.fangfangsmall.youtu.thread;

import android.graphics.Bitmap;
import android.os.Handler;

import com.example.dell.fangfangsmall.face.yt.person.face.YtFaceIdentify;
import com.example.dell.fangfangsmall.util.GsonUtil;
import com.example.dell.fangfangsmall.youtu.YoutuManager;
import com.example.dell.fangfangsmall.youtu.callback.SimpleCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zhangyuanyuan on 2017/9/18.
 */

public class FaceIdentifyThread extends ReqestThread<YtFaceIdentify> {

    private Bitmap bitmap;

    public FaceIdentifyThread(Handler mHandler, Bitmap bitmap, SimpleCallback<YtFaceIdentify> simpleCallback) {
        super(mHandler, simpleCallback);
        this.bitmap = bitmap;
    }

    @Override
    protected YtFaceIdentify initData() throws KeyManagementException, NoSuchAlgorithmException, JSONException, IOException {

        JSONObject jsonObject = YoutuManager.getInstence().FaceIdentify(bitmap, YoutuManager.GROUP_ID);
        YtFaceIdentify ytFaceIdentify = GsonUtil.GsonToBean(jsonObject.toString(), YtFaceIdentify.class);

        return ytFaceIdentify;
    }
}
