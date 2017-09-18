package com.example.dell.fangfangsmall.camera;

import android.graphics.Bitmap;

import com.example.dell.fangfangsmall.camera.IPresenter.IVerificationPresenter;
import com.example.dell.fangfangsmall.listener.OnConfimListener;
import com.example.dell.fangfangsmall.util.BitmapUtils;
import com.example.dell.fangfangsmall.view.AddInfoDialog;


public class VerificationPresenter extends IVerificationPresenter implements OnConfimListener {

    private IVerifcationView mVerifcationView;

    private String mAuthId;

    private AddInfoDialog addInfoDialog;

    private int curCount = 0;
    private long curTime;

    public VerificationPresenter(IVerifcationView baseView) {
        super(baseView);
        mVerifcationView = baseView;
        curTime = System.currentTimeMillis();
    }


    @Override
    public void saveFace(final Bitmap bitmap) {
        if (mAuthId == null || mAuthId.equals("")) {
            showDialog();
            return;
        }

        if (curCount < 10) {
            if (System.currentTimeMillis() - curTime > 2000) {
                curCount++;
                boolean save = BitmapUtils.saveBitmapToFile(bitmap, mAuthId, curCount + ".jpg");
                if(save){
                    mVerifcationView.saveCount(curCount, "");
                }
            }
        } else if (curCount == 10) {
            curCount++;
            mVerifcationView.saveFinish();
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
    public void onConfim(String content) {
        mAuthId = content;
        curCount = 0;
    }
}
