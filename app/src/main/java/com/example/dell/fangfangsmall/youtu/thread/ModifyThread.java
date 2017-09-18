package com.example.dell.fangfangsmall.youtu.thread;

import android.os.Handler;

import com.example.dell.fangfangsmall.face.yt.person.YtSetperson;
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

public class ModifyThread extends ReqestThread<YtSetperson> {

    private int type;
    private String modify;
    private String personId;

    public ModifyThread(Handler mHandler, int type, String modify, String personId, SimpleCallback<YtSetperson> simpleCallback) {
        super(mHandler, simpleCallback);
        this.type = type;
        this.modify = modify;
        this.personId = personId;
    }

    @Override
    protected YtSetperson initData() throws KeyManagementException, NoSuchAlgorithmException, JSONException, IOException {
        JSONObject jsonObject = YoutuManager.getInstence().SetInfo(type, modify, personId);
        YtSetperson ytSetperson = GsonUtil.GsonToBean(jsonObject.toString(), YtSetperson.class);
        return ytSetperson;
    }
}
