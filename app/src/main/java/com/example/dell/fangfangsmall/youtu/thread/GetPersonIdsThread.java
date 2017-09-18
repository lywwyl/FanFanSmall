package com.example.dell.fangfangsmall.youtu.thread;

import android.os.Handler;

import com.example.dell.fangfangsmall.face.yt.YtPersonids;
import com.example.dell.fangfangsmall.util.GsonUtil;
import com.example.dell.fangfangsmall.youtu.YoutuManager;
import com.example.dell.fangfangsmall.youtu.callback.SimpleCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zhangyuanyuan on 2017/9/5.
 */

public class GetPersonIdsThread extends ReqestThread<YtPersonids> {

    public GetPersonIdsThread(Handler mHandler, SimpleCallback<YtPersonids> simpleCallback) {
        super(mHandler, simpleCallback);
    }

    @Override
    protected YtPersonids initData() throws KeyManagementException, NoSuchAlgorithmException, JSONException, IOException {
        JSONObject jsonObject = YoutuManager.getInstence().GetPersonIds(YoutuManager.GROUP_ID);
        YtPersonids ytPersonids = GsonUtil.GsonToBean(jsonObject.toString(), YtPersonids.class);
        return ytPersonids;
    }
}
