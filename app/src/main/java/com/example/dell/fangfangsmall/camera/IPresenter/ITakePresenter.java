package com.example.dell.fangfangsmall.camera.IPresenter;

import android.content.Context;

/**
 * Created by zhangyuanyuan on 2017/9/17.
 */

public abstract class ITakePresenter {

    protected ITakeView mBaseView;

    public ITakePresenter(ITakeView baseView) {
        mBaseView = baseView;
    }

    public abstract void startCountDownTimer();

    public abstract void stopCountDownTimer();

    public interface ITakeView {

        void onTick(String l);

        void onFinish();

        Context getContext();
    }


}
