package com.example.dell.fangfangsmall.camera.IPresenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import com.example.dell.fangfangsmall.face.yt.person.YtVerifyperson;

/**
 * Created by zhangyuanyuan on 2017/9/18.
 */

public abstract class IFaceVerifPresenter {

    private IFaceverifView mBaseView;

    public IFaceVerifPresenter(IFaceverifView baseView) {
        mBaseView = baseView;
    }

    public abstract void verificationFace(Handler handler, Bitmap bitmap);

    public abstract void closeCamera();

    public abstract void openCamera();

    public abstract void doStartPreview();

    public abstract void setVerifying(boolean verifying);

    public abstract void setMatrix(int width, int height);


    public interface IFaceverifView {

        void verificationSuccerr(YtVerifyperson ytVerifyperson);

        void verificationFail(int code, String msg);

        void setVerifyingTrue();

        void setVerifyingFalse();

        void tranBitmap(Bitmap bitmap);

        Context getContext();
    }


}
