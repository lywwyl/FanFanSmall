package com.example.dell.fangfangsmall.camera.IPresenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

import com.example.dell.fangfangsmall.face.yt.person.face.YtDetectFace;

import java.util.List;

/**
 * Created by zhangyuanyuan on 2017/9/14.
 */

public abstract class IVerificationPresenter {


    protected IVerifcationView mBaseView;

    public IVerificationPresenter(IVerifcationView baseView) {
        mBaseView = baseView;
    }

    public abstract void saveFace(Bitmap bitmap);

    public abstract void showDialog();

    public abstract void foundPerson(Handler handler, Bitmap bitmap);

    public abstract void uploadFaceBitmap(Handler handler, List<String> paths);

    public abstract void distinguishFace(Handler handler, Bitmap bitmap);

    public abstract void setDetecting(boolean isDetecting);

    public interface IVerifcationView {

        void saveCount(int count, String path);

        void saveFinish();

        void newPerson(Bitmap bitmap);

        void addFace(List<String> paths);

        void newpersonSuccess(String personId);

        void saveFirstFail();

        void newpersonFail(int code, String msg);

        void uploadBitmapFinish(int c);

        void uploadBitmapFail(int code, String msg);

        void distinguishFaceSuccess(YtDetectFace ytDetectFace);

        void distinguishFail(int code, String msg);

        void distinguishError();

        void distinguishEnd();

        Context getContext();
    }

}
