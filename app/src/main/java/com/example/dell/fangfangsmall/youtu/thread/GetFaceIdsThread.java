package com.example.dell.fangfangsmall.youtu.thread;

import android.os.Handler;

import com.example.dell.fangfangsmall.face.yt.YtFaceids;
import com.example.dell.fangfangsmall.util.GsonUtil;
import com.example.dell.fangfangsmall.youtu.YoutuManager;
import com.example.dell.fangfangsmall.youtu.callback.SimpleCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zhangyuanyuan on 2017/9/6.
 */

public class GetFaceIdsThread extends ReqestThread<YtFaceids> {

    private String personId;

    public GetFaceIdsThread(Handler mHandler, String personId, SimpleCallback<YtFaceids> simpleCallback) {
        super(mHandler, simpleCallback);
        this.personId = personId;
    }

    @Override
    protected YtFaceids initData() throws KeyManagementException, NoSuchAlgorithmException, JSONException, IOException {

        JSONObject jsonObject = YoutuManager.getInstence().GetFaceIds(personId);

        YtFaceids ytFaceids = GsonUtil.GsonToBean(jsonObject.toString(), YtFaceids.class);
        return ytFaceids;
    }
}
