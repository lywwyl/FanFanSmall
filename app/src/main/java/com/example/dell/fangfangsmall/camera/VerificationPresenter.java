package com.example.dell.fangfangsmall.camera;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Handler;

import com.example.dell.fangfangsmall.camera.IPresenter.IVerificationPresenter;
import com.example.dell.fangfangsmall.face.yt.person.YtAddperson;
import com.example.dell.fangfangsmall.face.yt.person.YtNewperson;
import com.example.dell.fangfangsmall.listener.OnConfimListener;
import com.example.dell.fangfangsmall.util.BitmapUtils;
import com.example.dell.fangfangsmall.view.AddInfoDialog;
import com.example.dell.fangfangsmall.youtu.PersonManager;
import com.example.dell.fangfangsmall.youtu.callback.SimpleCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class VerificationPresenter extends IVerificationPresenter implements OnConfimListener {

    private IVerifcationView mVerifcationView;

    private String mAuthId;

    private AddInfoDialog addInfoDialog;

    private int curCount = 0;
    private List<String> mPaths;
    private long curTime;
    private boolean isnewPerson;
    private boolean isFirst;

    public VerificationPresenter(IVerifcationView baseView) {
        super(baseView);
        mVerifcationView = baseView;
        curTime = System.currentTimeMillis();
        mPaths = new ArrayList<>();
        isFirst = true;
        showDialog();
    }


    @Override
    public void saveFace(final Bitmap bitmap) {
        if (mAuthId == null || mAuthId.equals("")) {

            return;
        }
        if (System.currentTimeMillis() - curTime > 5000) {

            if (curCount == 0) {
                if(!isnewPerson){
                    boolean save = BitmapUtils.saveBitmapToFile(bitmap, mAuthId, curCount + ".jpg");
                    if(save) {
                        mVerifcationView.newPerson(bitmap);
                    }else{
                        mVerifcationView.saveFirstFail();
                    }
                }
            }
            if (!isFirst) {
                if (curCount < 5) {
                    boolean save = BitmapUtils.saveBitmapToFile(bitmap, mAuthId, curCount + ".jpg");
                    if(save) {
                        mPaths.add(BitmapUtils.projectPath + mAuthId + File.separator + curCount + ".jpg");
                        mVerifcationView.saveCount(curCount, "");
                        curCount++;
                    }else{
                        mVerifcationView.saveFirstFail();
                    }
                } else if (curCount == 5) {
                    if (mPaths != null && mPaths.size() > 0) {
                        mVerifcationView.addFace(mPaths);
                    }
                    curCount++;
                    mVerifcationView.saveFinish();
                }
            }
            curTime = System.currentTimeMillis();

        }
    }

    @Override
    public void showDialog() {
        if (addInfoDialog == null) {
            addInfoDialog = new AddInfoDialog(mVerifcationView.getContext(), this);
        }
        addInfoDialog.show();
    }

    @Override
    public void foundPerson(Handler handler, final Bitmap bitmap) {
        Bitmap copyBitmap = bitmapSaturation(bitmap);
        PersonManager.newperson(handler, mAuthId, copyBitmap, new SimpleCallback<YtNewperson>((Activity) mVerifcationView.getContext()) {
            @Override
            public void onBefore() {
                super.onBefore();
                isnewPerson = true;
            }

            @Override
            public void onSuccess(YtNewperson ytNewperson) {
                String faceId = ytNewperson.getFace_id();
                    isFirst = false;
                    curCount++;
                    mVerifcationView.newpersonSuccess(faceId);
            }

            @Override
            public void onFail(int code, String msg) {
                mVerifcationView.newpersonFail(code, msg);
                isnewPerson = true;
            }

            @Override
            public void onEnd() {
                super.onEnd();
                isnewPerson = true;
            }

            @Override
            public void onError(Exception e) {
                super.onError(e);
                isnewPerson = true;
            }
        });
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

    @Override
    public void uploadFaceBitmap(Handler handler, List<String> paths) {
        PersonManager.addFacefoPath(handler, mAuthId, paths, new SimpleCallback<YtAddperson>((Activity) mVerifcationView.getContext()) {
            @Override
            public void onSuccess(YtAddperson ytAddperson) {//-1312 对个体添加了几乎相同的人脸
                List<Integer> integerList = ytAddperson.getRet_codes();
                int c = 0;
                for(int i = 0; i < integerList.size(); i++){
                    if (integerList.get(i) == 0) {

                        c++;
                    }
                }
                mVerifcationView.uploadBitmapFinish(c);
            }

            @Override
            public void onFail(int code, String msg) {
                mVerifcationView.uploadBitmapFail(code, msg);
                isnewPerson = false;
            }
        });
    }


    @Override
    public void onConfim(String content) {
        mAuthId = content;
        curCount = 0;
        mPaths.clear();
        isFirst = true;
    }
}
