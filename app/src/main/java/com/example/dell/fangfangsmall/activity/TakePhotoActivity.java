package com.example.dell.fangfangsmall.activity;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.example.dell.fangfangsmall.R;
import com.example.dell.fangfangsmall.camera.BaseActivity;
import com.example.dell.fangfangsmall.camera.IPresenter.ITakePresenter;
import com.example.dell.fangfangsmall.camera.TakePresenter;

/**
 * 进行拍照
 *
 * @author Guanluocang
 *         created at 2017/9/13 11:29
 */
public class TakePhotoActivity extends BaseActivity implements SurfaceHolder.Callback, View.OnClickListener, ITakePresenter.ITakeView {

    private SurfaceView cameraSurfaceView;
    private TextView tvTakePhoto;
    private TextView tvCountTime;

    private TakePresenter mTakePresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_take_photo;
    }

    @Override
    protected void initView() {
        cameraSurfaceView = (SurfaceView) findViewById(R.id.opengl_layout_surfaceview);
        tvTakePhoto = (TextView) findViewById(R.id.tv_take_photo);
        tvCountTime = (TextView) findViewById(R.id.tv_countTime);

        SurfaceHolder Holder = cameraSurfaceView.getHolder(); // 获得SurfaceHolder对象
        Holder.addCallback(this); // 为SurfaceView添加状态监听
        Holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mTakePresenter = new TakePresenter(this, Holder);
    }


    @Override
    protected void initData() {
        tvTakePhoto.setOnClickListener(this);
    }

    @Override
    protected void setListener() {

    }


    @Override
    protected void onPause() {
        super.onPause();
        mTakePresenter.closeCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTakePresenter.stopCountDownTimer();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_take_photo:
                mTakePresenter.cameraTakePicture();
                break;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mTakePresenter.openCamera();
        mTakePresenter.doStartPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mTakePresenter.setMatrix(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mTakePresenter.closeCamera();
    }


    @Override
    public void startPreviewFinish() {
        mTakePresenter.startCountDownTimer();
    }

    @Override
    public void autoFocusSuccess() {
        mTakePresenter.cameraTakePicture();
    }

    @Override
    public void pictureTakenSuccess() {
        Log.i("pictureTaken", "pictureTakenSuccess");
    }

    @Override
    public void pictureTakenFail() {
        Log.i("pictureTaken", "pictureTakenFail");
    }

    @Override
    public void onTick(String l) {
        tvCountTime.setText(l);
        showToast(l);
    }

    @Override
    public void onFinish() {
        mTakePresenter.cameraTakePicture();
    }

    @Override
    public Context getContext() {
        return this;
    }
}
