package com.example.dell.fangfangsmall.youtu.thread;

import android.os.Handler;

import com.example.dell.fangfangsmall.face.yt.person.YtDelperson;
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

public class DelpersonThread extends ReqestThread<YtDelperson> {

    private String personId;

    public DelpersonThread(Handler mHandler, String personId, SimpleCallback<YtDelperson> callback) {
        super(mHandler, callback);
        this.personId = personId;
    }


    @Override
    protected YtDelperson initData() throws KeyManagementException, NoSuchAlgorithmException, JSONException, IOException {
        JSONObject jsonObject = YoutuManager.getInstence().delPerson(personId);
        YtDelperson ytDelperson = GsonUtil.GsonToBean(jsonObject.toString(), YtDelperson.class);
        return ytDelperson;
    }
}
