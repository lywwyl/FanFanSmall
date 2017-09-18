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

    public abstract void closeCamera();

    public abstract void openCamera();

    public abstract void doStartPreview();

    public abstract void setMatrix(int width, int height);

    public abstract void cameraAutoFocus();

    public abstract void cameraTakePicture();

    public abstract void startCountDownTimer();

    public abstract void stopCountDownTimer();

    public interface ITakeView {

        void startPreviewFinish();

        void autoFocusSuccess();

        void pictureTakenSuccess();

        void pictureTakenFail();

        void onTick(String l);

        void onFinish();

        Context getContext();
    }


}
