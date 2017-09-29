package com.example.dell.fangfangsmall.camera.IPresenter;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Created by zhangyuanyuan on 2017/9/18.
 */

public abstract class ICameraPresenter {


    private ICameraView mBaseView;

    public ICameraPresenter(ICameraView baseView) {
        mBaseView = baseView;
    }


    public abstract void closeCamera();

    public abstract void openCamera();

    public abstract void doStartPreview();

    public abstract void changeCamera();

    public abstract void setMatrix(int width, int height);

    public abstract void cameraAutoFocus();

    public abstract void cameraTakePicture();

    public abstract int getCameraId();

    public interface ICameraView{

        void previewFinish();

        void pictureTakenSuccess();

        void pictureTakenFail();

        void autoFocusSuccess();

        void tranBitmap(Bitmap bitmap, int num);

        void noFace();

        Context getContext();
    }

}
