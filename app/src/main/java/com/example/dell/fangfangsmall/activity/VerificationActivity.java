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
import com.example.dell.fangfangsmall.camera.ICameraPresenter;
import com.example.dell.fangfangsmall.face.yt.person.YtVerifyperson;
import com.example.dell.fangfangsmall.view.DrawSurfaceView;


public class VerificationActivity extends BaseActivity implements ICameraPresenter.ICameraView, SurfaceHolder.Callback, View.OnClickListener {

    private SurfaceView cameraSurfaceView;
    private DrawSurfaceView drawSufaceView;
    private TextView tvAddIfofo;

    private CameraPresenter mCameraPresenter;

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    mCameraPresenter.setVerifying(false);
                    break;
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

        SurfaceHolder Holder = cameraSurfaceView.getHolder(); // 获得SurfaceHolder对象
        Holder.addCallback(this); // 为SurfaceView添加状态监听
        Holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mCameraPresenter = new CameraPresenter(this, Holder);
    }


    @Override
    protected void initData() {
        mCameraPresenter.showDialog();
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
    public Context getContext() {
        return this;
    }


    @Override
    public void verificationSuccerr(YtVerifyperson ytVerifyperson) {
        showToast("相似度为 ： " + ytVerifyperson.getConfidence());
//        if (ytVerifyperson.ismatch()) {
//            setResult(RESULT_OK);
//            finish();
//        }
        mHandler.sendEmptyMessageDelayed(1, 1000);
    }

    @Override
    public void verificationFail(int code, String msg) {
        if(code == -1101){
            showToast("正对摄像头，保证画面清晰");
        }else{

            showToast(msg);
        }
        mHandler.sendEmptyMessageDelayed(1, 1000);
    }

    @Override
    public void setVerifyingTrue() {
        mCameraPresenter.setVerifying(true);
    }

    @Override
    public void setVerifyingFalse() {
        mHandler.sendEmptyMessageDelayed(1, 1000);
    }

    @Override
    public void tranBitmap(Bitmap bitmap) {
        mCameraPresenter.verificationFace(mHandler, bitmap);
    }

    @Override
    public void tranBitmapFosave(Bitmap bitmap) {
        mCameraPresenter.saveFace(bitmap);
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
//        mScaleMatrix.setScale(width / (float) previewHeight, height / (float) previewWidth);
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
                mCameraPresenter.showDialog();
                break;
        }
    }
}
