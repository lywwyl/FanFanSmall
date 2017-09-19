package com.example.dell.fangfangsmall.camera;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.util.ArrayMap;

import com.example.dell.fangfangsmall.camera.IPresenter.IFaceVerifPresenter;
import com.example.dell.fangfangsmall.face.yt.person.face.IdentifyItem;
import com.example.dell.fangfangsmall.face.yt.person.face.YtFaceIdentify;
import com.example.dell.fangfangsmall.youtu.PersonManager;
import com.example.dell.fangfangsmall.youtu.callback.SimpleCallback;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class FaceVerifPresenter extends IFaceVerifPresenter {

    private IFaceverifView mFaceverifView;

    private long curTime;

    public FaceVerifPresenter(IFaceverifView baseView) {
        super(baseView);
        mFaceverifView = baseView;
        curTime = System.currentTimeMillis();
    }


    @Override
    public void faceIdentifyFace(Handler handler, Bitmap baseBitmap) {
        if (System.currentTimeMillis() - curTime < 2000) {
            return;
        }
        Bitmap copyBitmap = bitmapSaturation(baseBitmap);
        PersonManager.faceIdentify(handler, copyBitmap, new SimpleCallback<YtFaceIdentify>((Activity) mFaceverifView.getContext()) {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onSuccess(YtFaceIdentify ytFaceIdentify) {
                mFaceverifView.verificationSuccess(ytFaceIdentify);
            }

            @Override
            public void onFail(int code, String msg) {
                mFaceverifView.verificationFail(code, msg);
            }

            @Override
            public void onEnd() {
                super.onEnd();
                curTime = System.currentTimeMillis();
            }
        });
//        PersonManager.faceVerify(handler, mAuthId, copyBitmap, new SimpleCallback<YtVerifyperson>((Activity) mFaceverifView.getContext()) {
//            @Override
//            public void onBefore() {
//                isVerifying = true;
//            }
//
//            @Override
//            public void onSuccess(YtVerifyperson ytVerifyperson) {
//                mFaceverifView.verificationSuccerr(ytVerifyperson);
//                mFaceverifView.setVerifyingFalse();
//            }
//
//            @Override
//            public void onFail(int code, String msg) {
//                mFaceverifView.verificationFail(code, msg);
//                mFaceverifView.setVerifyingFalse();
//            }
//
//            @Override
//            public void onError(Exception e) {
//                super.onError(e);
//                mFaceverifView.setVerifyingFalse();
//            }
//
//            @Override
//            public void onEnd() {
//                mFaceverifView.setVerifyingFalse();
//            }
//        });
    }

    private Bitmap bitmapSaturation(Bitmap baseBitmap) {
        Bitmap copyBitmap = Bitmap.createBitmap(baseBitmap.getWidth(), baseBitmap.getHeight(), baseBitmap.getConfig());
        ColorMatrix mImageViewMatrix = new ColorMatrix();
        ColorMatrix mBaoheMatrix = new ColorMatrix();
        float sat = (float) 0.0;
        mBaoheMatrix.setSaturation(sat);
        mImageViewMatrix.postConcat(mBaoheMatrix);
        ColorMatrixColorFilter colorFilter = new ColorMatrixColorFilter(mImageViewMatrix);//再把该mImageViewMatrix作为参数传入来实例化ColorMatrixColorFilter
        Paint paint = new Paint();
        paint.setColorFilter(colorFilter);//并把该过滤器设置给画笔
        Canvas canvas = new Canvas(copyBitmap);//将画纸固定在画布上
        canvas.drawBitmap(baseBitmap, new Matrix(), paint);//传如baseBitmap表示按照原图样式开始绘制，将得到是复制后的图片
        canvas.drawBitmap(baseBitmap, new Matrix(), paint);//传如baseBitmap表示按照原图样式开始绘制，将得到是复制后的图片
        return copyBitmap;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void compareFace(YtFaceIdentify ytFaceIdentify) {
        ArrayMap<IdentifyItem, Integer> countMap = new ArrayMap<IdentifyItem, Integer>();

        ArrayList<IdentifyItem> identifyItems = ytFaceIdentify.getCandidates();
        if (identifyItems != null && identifyItems.size() > 0) {
            for (int i = 0; i < identifyItems.size(); i++) {
                IdentifyItem identifyItem = identifyItems.get(i);

                if (countMap.containsKey(identifyItem)) {
                    countMap.put(identifyItem, countMap.get(identifyItem) + 1);
                } else {
                    countMap.put(identifyItem, 1);
                }
            }

            ArrayMap<Integer, List<IdentifyItem>> resultMap = new ArrayMap<Integer, List<IdentifyItem>>();
            List<Integer> tempList = new ArrayList<Integer>();

            Iterator iterator = countMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<IdentifyItem, Integer> entry = (Map.Entry<IdentifyItem, Integer>) iterator.next();

                IdentifyItem key = entry.getKey();
                int value = entry.getValue();

                if (resultMap.containsKey(value)) {
                    List list = (List) resultMap.get(value);
                    list.add(key);
                } else {
                    List<IdentifyItem> list = new ArrayList<IdentifyItem>();
                    list.add(key);
                    resultMap.put(value, list);
                    tempList.add(value);
                }
            }

            Collections.sort(tempList);

            int size = tempList.size();
            List<IdentifyItem> list = resultMap.get(tempList.get(size - 1));
            IdentifyItem identifyItem = list.get(0);

            mFaceverifView.identifyFace(identifyItem.getPerson_id());
        } else {
            mFaceverifView.identifyNoFace();
        }
    }


}
