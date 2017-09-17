package com.example.dell.fangfangsmall.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.dell.fangfangsmall.R;
import com.example.dell.fangfangsmall.camera.BaseActivity;
import com.example.dell.fangfangsmall.camera.CameraPresenter;
import com.example.dell.fangfangsmall.camera.ICameraPresenter;
import com.example.dell.fangfangsmall.face.yt.person.YtVerifyperson;


public class VerificationActivity extends BaseActivity implements ICameraPresenter.ICameraView, SurfaceHolder.Callback {

    SurfaceView cameraSurfaceView;

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

    private String mAuthId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_face_extract;
    }

    @Override
    protected void initView() {
        cameraSurfaceView = (SurfaceView) findViewById(R.id.opengl_layout_surfaceview);

        SurfaceHolder Holder = cameraSurfaceView.getHolder(); // 获得SurfaceHolder对象
        Holder.addCallback(this); // 为SurfaceView添加状态监听
        Holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        mCameraPresenter = new CameraPresenter(this, Holder);

//        cameraSurfaceView.getHolder().addCallback(this);
//        cameraSurfaceView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        Button button = (Button) findViewById(R.id.camera_shutter);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mCameraPresenter.cameraTakePicture("");
//            }
//        });
    }


    @Override
    protected void initData() {
        mAuthId = getIntent().getStringExtra("AuthId");
    }

    @Override
    protected void setListener() {
//        drawSufaceView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mCameraPresenter.changeCamera();
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();

        mCameraPresenter.accStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mCameraPresenter.closeCamera();
        mCameraPresenter.accStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCameraPresenter.facedDestroy();
    }


    public static void invoke(Context context, String authId) {
        Intent intent = new Intent(context, VerificationActivity.class);
        intent.putExtra("AuthId", authId);
        context.startActivity(intent);
    }

    public static void invoke(Activity context, String authId, int requestCode) {
        Intent intent = new Intent(context, VerificationActivity.class);
        intent.putExtra("AuthId", authId);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    public Context getContext() {
        return this;
    }


    @Override
    public void verificationSuccerr(YtVerifyperson ytVerifyperson) {
        showToast("相似度为 ： " + ytVerifyperson.getConfidence());
        if (ytVerifyperson.ismatch()) {
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void verificationFail(int code, String msg) {
        showToast(msg);
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
        mCameraPresenter.verificationFace(mHandler, mAuthId, bitmap);
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
}
