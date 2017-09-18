package com.example.dell.fangfangsmall.camera.IPresenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import com.example.dell.fangfangsmall.face.yt.person.face.YtFaceIdentify;

/**
 * Created by zhangyuanyuan on 2017/9/18.
 */

public abstract class IFaceVerifPresenter {

    private IFaceverifView mBaseView;

    public IFaceVerifPresenter(IFaceverifView baseView) {
        mBaseView = baseView;
    }

    public abstract void faceIdentifyFace(Handler handler, Bitmap bitmap);

    public abstract void compareFace(YtFaceIdentify ytFaceIdentify);


    public interface IFaceverifView {

        void verificationSuccess(YtFaceIdentify ytFaceIdentify);

        void identifyFace(String personId);

        void identifyNoFace();

        void verificationFail(int code, String msg);

        Context getContext();
    }


}
