package com.example.dell.fangfangsmall.youtu.thread;

import android.os.Handler;

import com.example.dell.fangfangsmall.face.yt.YtPerson;
import com.example.dell.fangfangsmall.youtu.callback.SimpleCallback;

import org.json.JSONException;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

/**
 * Created by zhangyuanyuan on 2017/9/5.
 */

public abstract class ReqestThread<T extends YtPerson> implements Runnable {

    private Handler mHandler;

    private SimpleCallback<T> simpleCallback;

    public ReqestThread(Handler mHandler, SimpleCallback<T> simpleCallback) {
        this.mHandler = mHandler;
        this.simpleCallback = simpleCallback;
        simpleCallback.onBefore();
    }

    @Override
    public void run() {
        try {
            final T t = initData();

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(t.getErrorcode() == 0){
                        simpleCallback.onSuccess(t);
                    } else {
                        simpleCallback.onFail(t.getErrorcode(), t.getErrormsg());
                    }
                    simpleCallback.onEnd();
                }
            });
        }catch (final Exception e) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    simpleCallback.onError(e);
                    simpleCallback.onEnd();
                }
            });

        }
    }

    protected abstract T initData() throws KeyManagementException, NoSuchAlgorithmException, JSONException, IOException;

}
