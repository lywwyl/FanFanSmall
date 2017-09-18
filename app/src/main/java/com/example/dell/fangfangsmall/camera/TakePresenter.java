package com.example.dell.fangfangsmall.camera;

import android.os.CountDownTimer;

import com.example.dell.fangfangsmall.camera.IPresenter.ITakePresenter;

/**
 * Created by zhangyuanyuan on 2017/9/17.
 */

public class TakePresenter extends ITakePresenter {


    private ITakeView mTakeView;

    private boolean isFirst;
    private CountDownTimer countDownTimer;

    public static final String PICTURETAKEN = "pictureTaken";

    public TakePresenter(ITakeView baseView) {
        super(baseView);
        mTakeView = baseView;
        isFirst = true;
    }


    @Override
    public void startCountDownTimer() {
        if (isFirst) {
            countDownTimer = new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long l) {
                    String str = String.valueOf(l / 1000);
                    mTakeView.onTick(str);
                }

                @Override
                public void onFinish() {
                    mTakeView.onFinish();
                }
            };
            countDownTimer.start();
            isFirst = false;
        }
    }

    @Override
    public void stopCountDownTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

}
