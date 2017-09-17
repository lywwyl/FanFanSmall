package com.example.dell.fangfangsmall.camera;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import com.example.dell.fangfangsmall.face.yt.person.YtVerifyperson;

/**
 * Created by zhangyuanyuan on 2017/9/14.
 */

public abstract class ICameraPresenter {


    protected ICameraView mBaseView;

    public ICameraPresenter(ICameraView baseView) {
        mBaseView = baseView;
    }

    public abstract void accStart();

    public abstract void accStop();

    public abstract void facedDestroy();

    public abstract void verificationFace(Handler handler, String authId, Bitmap bitmap);

    public abstract void closeCamera();

    public abstract void openCamera();

    public abstract void doStartPreview();

    public abstract void changeCamera();

    public abstract void setVerifying(boolean verifying);

    public abstract void setMatrix(int width, int height);

    public abstract void cameraTakePicture(String pictureTakenPath);

    public interface ICameraView {

        void verificationSuccerr(YtVerifyperson ytVerifyperson);

        void verificationFail(int code, String msg);

        void setVerifyingTrue();

        void setVerifyingFalse();

        void tranBitmap(Bitmap bitmap);

        Context getContext();
    }

}
