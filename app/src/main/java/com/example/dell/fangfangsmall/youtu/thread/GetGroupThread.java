package com.example.dell.fangfangsmall.youtu.thread;

import android.os.Handler;

import com.example.dell.fangfangsmall.face.yt.YtGroupids;
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

public class GetGroupThread extends ReqestThread<YtGroupids> {

    public GetGroupThread(Handler mHandler, SimpleCallback<YtGroupids> simpleCallback) {
        super(mHandler, simpleCallback);
    }

    @Override
    protected YtGroupids initData() throws KeyManagementException, NoSuchAlgorithmException, JSONException, IOException {

        JSONObject jsonObject = YoutuManager.getInstence().GetGroupIds();
        YtGroupids ytGroupids = GsonUtil.GsonToBean(jsonObject.toString(), YtGroupids.class);

        return ytGroupids;
    }
}
