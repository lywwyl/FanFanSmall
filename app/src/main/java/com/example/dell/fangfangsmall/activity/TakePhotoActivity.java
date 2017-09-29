package com.example.dell.fangfangsmall.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.example.dell.fangfangsmall.R;
import com.example.dell.fangfangsmall.camera.BaseActivity;
import com.example.dell.fangfangsmall.camera.CameraPresenter;
import com.example.dell.fangfangsmall.camera.IPresenter.ICameraPresenter;
import com.example.dell.fangfangsmall.camera.IPresenter.ITakePresenter;
import com.example.dell.fangfangsmall.camera.TakePresenter;

/**
 * 进行拍照
 *
 * @author Guanluocang
 *         created at 2017/9/13 11:29
 */
public class TakePhotoActivity extends BaseActivity implements SurfaceHolder.Callback, View.OnClickListener, ITakePresenter.ITakeView, ICameraPresenter.ICameraView {

    private SurfaceView cameraSurfaceView;
    private TextView tvTakePhoto;
    private TextView tvCountTime;

    private TakePresenter mTakePresenter;
    private CameraPresenter mCameraPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_take_photo;
    }

    @Override
    protected void initView() {
        cameraSurfaceView = (SurfaceView) findViewById(R.id.opengl_layout_surfaceview);
        tvTakePhoto = (TextView) findViewById(R.id.tv_take_photo);
        tvCountTime = (TextView) findViewById(R.id.tv_countTime);

        SurfaceHolder holder = cameraSurfaceView.getHolder(); // 获得SurfaceHolder对象
        holder.addCallback(this); // 为SurfaceView添加状态监听
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mTakePresenter = new TakePresenter(this);
        mCameraPresenter = new CameraPresenter(this, holder);
    }


    @Override
    protected void initData() {
        tvTakePhoto.setOnClickListener(this);
        tvTakePhoto.setEnabled(false);
    }

    @Override
    protected void setListener() {

    }


    @Override
    protected void onPause() {
        super.onPause();
        mCameraPresenter.closeCamera();
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
                mCameraPresenter.cameraTakePicture();
                tvTakePhoto.setEnabled(false);
                break;
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mCameraPresenter.openCamera();
        mCameraPresenter.doStartPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCameraPresenter.setMatrix(width, height);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mCameraPresenter.closeCamera();
    }


    @Override
    public void autoFocusSuccess() {
        mCameraPresenter.cameraTakePicture();
    }

    @Override
    public void tranBitmap(Bitmap bitmap, int num) {

    }

    @Override
    public void noFace() {

    }

    @Override
    public void previewFinish() {
        mTakePresenter.startCountDownTimer();
    }

    @Override
    public void pictureTakenSuccess() {
        tvTakePhoto.setEnabled(true);
        showToast("拍照完成");
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
        mCameraPresenter.cameraTakePicture();
        tvTakePhoto.setEnabled(true);
    }

    @Override
    public Context getContext() {
        return this;
    }
}
