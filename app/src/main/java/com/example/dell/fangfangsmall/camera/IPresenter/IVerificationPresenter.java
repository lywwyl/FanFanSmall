package com.example.dell.fangfangsmall.camera.IPresenter;

import android.content.Context;
import android.graphics.Bitmap;

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

    public interface IVerifcationView {

        void saveCount(int count, String path);

        void saveFinish();

        Context getContext();
    }

}
