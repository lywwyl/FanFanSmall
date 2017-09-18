package com.example.dell.fangfangsmall.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.example.dell.fangfangsmall.R;
import com.example.dell.fangfangsmall.camera.BaseActivity;
import com.example.dell.fangfangsmall.camera.CameraPresenter;
import com.example.dell.fangfangsmall.camera.IPresenter.ICameraPresenter;
import com.example.dell.fangfangsmall.camera.IPresenter.IVerificationPresenter;
import com.example.dell.fangfangsmall.camera.VerificationPresenter;
import com.example.dell.fangfangsmall.view.DrawSurfaceView;


public class VerificationActivity extends BaseActivity implements IVerificationPresenter.IVerifcationView, ICameraPresenter.ICameraView,SurfaceHolder.Callback, View.OnClickListener {

    private SurfaceView cameraSurfaceView;
    private DrawSurfaceView drawSufaceView;
    private TextView tvAddIfofo;

    private VerificationPresenter mVerificationPresenter;
    private CameraPresenter mCameraPresenter;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
            }
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_face_extract;
    }

    @Override
    protected void initView() {
        cameraSurfaceView = (SurfaceView) findViewById(R.id.opengl_layout_surfaceview);
        drawSufaceView = (DrawSurfaceView) findViewById(R.id.draw_sufaceView);
        tvAddIfofo = (TextView) findViewById(R.id.tv_add_info);

        SurfaceHolder holder = cameraSurfaceView.getHolder(); // 获得SurfaceHolder对象
        holder.addCallback(this); // 为SurfaceView添加状态监听
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mVerificationPresenter = new VerificationPresenter(this);
        mCameraPresenter = new CameraPresenter(this, holder);
    }


    @Override
    protected void initData() {
        mVerificationPresenter.showDialog();
    }

    @Override
    protected void setListener() {
        tvAddIfofo.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCameraPresenter.closeCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void previewFinish() {

    }

    @Override
    public void pictureTakenSuccess() {

    }

    @Override
    public void pictureTakenFail() {

    }

    @Override
    public void autoFocusSuccess() {

    }

    @Override
    public void tranBitmap(Bitmap bitmap, int faceNumber) {
        mVerificationPresenter.saveFace(bitmap);
    }

    @Override
    public Context getContext() {
        return this;
    }


    @Override
    public void saveCount(int count, String path) {
        showToast("保存第" + count + "图片");
    }

    @Override
    public void saveFinish() {
        showToast("保存完成");
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_add_info:
                mVerificationPresenter.showDialog();
                break;
        }
    }
}
