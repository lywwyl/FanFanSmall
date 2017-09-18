package com.example.dell.fangfangsmall.youtu.thread;

import android.graphics.Bitmap;
import android.os.Handler;

import com.example.dell.fangfangsmall.face.yt.person.YtNewperson;
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

public class NewPersonThread extends ReqestThread<YtNewperson> {

    private String personId;
    private Bitmap bitmap;

    public NewPersonThread(Handler mHandler, String personId, Bitmap bitmap, SimpleCallback<YtNewperson> simpleCallback) {
        super(mHandler, simpleCallback);
        this.personId = personId;
        this.bitmap = bitmap;
    }

    @Override
    protected YtNewperson initData() throws KeyManagementException, NoSuchAlgorithmException, JSONException, IOException {

        JSONObject jsonObject = YoutuManager.getInstence().newPerson(bitmap, personId, YoutuManager.getmGroupId());

        YtNewperson ytNewperson = GsonUtil.GsonToBean(jsonObject.toString(), YtNewperson.class);

        return ytNewperson;
    }
}
