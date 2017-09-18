package com.example.dell.fangfangsmall.youtu.thread;

import android.os.Handler;

import com.example.dell.fangfangsmall.face.yt.person.YtPersonInfo;
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

public class GetinfoThread extends ReqestThread<YtPersonInfo> {

    private String personId;

    public GetinfoThread(Handler mHandler, String personId, SimpleCallback<YtPersonInfo> simpleCallback) {
        super(mHandler, simpleCallback);
        this.personId = personId;
    }

    @Override
    protected YtPersonInfo initData() throws KeyManagementException, NoSuchAlgorithmException, JSONException, IOException {
        JSONObject jsonObject = YoutuManager.getInstence().GetInfo(personId);
        YtPersonInfo ytPersonInfo = GsonUtil.GsonToBean(jsonObject.toString(), YtPersonInfo.class);
        return ytPersonInfo;
    }
}
