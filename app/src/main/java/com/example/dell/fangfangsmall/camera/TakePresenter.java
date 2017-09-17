package com.example.dell.fangfangsmall.camera;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.CountDownTimer;
import android.view.SurfaceHolder;

import com.example.dell.fangfangsmall.util.BitmapUtils;

import java.io.IOException;
import java.util.List;

/**
 * Created by zhangyuanyuan on 2017/9/17.
 */

public class TakePresenter extends ITakePresenter implements Camera.PictureCallback {


    private ITakeView mTakeView;
    private SurfaceHolder mHolder;

    private Camera mCamera;
    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private int previewWidth = 640;
    private int previewHeight = 480;
    private boolean isPreviewing = false;
    private Matrix mScaleMatrix = new Matrix();
    private int orientionOfCamera;

    private boolean isFirst;
    private CountDownTimer countDownTimer;

    public static final String PICTURETAKEN = "pictureTaken";

    public TakePresenter(ITakeView baseView, SurfaceHolder surfaceHolder) {
        super(baseView);
        mTakeView = baseView;
        mHolder = surfaceHolder;

        isFirst = true;
    }



    @Override
    public void closeCamera() {
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.stopFaceDetection();
            isPreviewing = false;
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void openCamera() {
        if (null != mCamera) {
            return;
        }
        if (Camera.getNumberOfCameras() == 1) {
            mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }
        try {
            mCamera = Camera.open(mCameraId);
        } catch (Exception e) {
            e.printStackTrace();
            closeCamera();
            return;
        }
    }

    @Override
    public void doStartPreview() {
        if (isPreviewing) {
            mCamera.stopPreview();
            return;
        }
        if (mCamera != null) {
            try {
                Camera.Parameters parameters = mCamera.getParameters();
                parameters.setPictureFormat(PixelFormat.JPEG);
                List<String> focusModes = parameters.getSupportedFocusModes();
                if (focusModes.contains("continuous-video")) {
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                }
                parameters.setPreviewFormat(ImageFormat.NV21);
                parameters.setPreviewSize(previewWidth, previewHeight);
                mCamera.setParameters(parameters);

                // 设置显示的偏转角度，大部分机器是顺时针90度，某些机器需要按情况设置
                orientionOfCamera = CameraUtils.setCameraDisplayOrientation((Activity) mTakeView.getContext(), mCameraId);
                mCamera.setDisplayOrientation(orientionOfCamera);
                mCamera.startPreview();
                try {
                    mCamera.setPreviewDisplay(mHolder);
                    mCamera.startPreview();//开启预览
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (parameters.getMaxNumDetectedFaces() > 1) {
                    mCamera.startFaceDetection();
                }
                isPreviewing = true;
                mTakeView.startPreviewFinish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setMatrix(int width, int height) {
        mScaleMatrix.setScale(width / (float) previewHeight, height / (float) previewWidth);
    }

    @Override
    public void cameraAutoFocus() {
        mCamera.autoFocus(new Camera.AutoFocusCallback() {//自动对焦
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success) {
                    mTakeView.autoFocusSuccess();
                }
            }
        });
    }

    @Override
    public void cameraTakePicture() {
        isPreviewing = false;
        mCamera.takePicture(null, null, this);
    }

    @Override
    public void startCountDownTimer() {
        if(isFirst){
            countDownTimer = new CountDownTimer(3000, 1000) {
                @Override
                public void onTick(long l) {
                    String str = String.valueOf(l / 1000);
                    mTakeView.onTick(str);
                }

                @Override
                public void onFinish() {
                    mTakeView.onFinish();
                }
            };
            countDownTimer.start();
            isFirst = false;
        }
    }


    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        camera.startPreview();
        isPreviewing = true;

        Bitmap previewBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        Matrix matrix = new Matrix();
        matrix.postRotate(0.0f, previewBitmap.getWidth() / 2, previewBitmap.getHeight() / 2);
        matrix.setRotate(-90);
        Bitmap saveBitmap = Bitmap.createBitmap(previewBitmap, 0, 0, previewBitmap.getWidth(), previewBitmap.getHeight(), matrix, true);

        boolean save = BitmapUtils.saveBitmapToFile(saveBitmap, "PICTURETAKEN", System.currentTimeMillis() + ".jpg");
        if(save){
            mTakeView.pictureTakenSuccess();
        }else{
            mTakeView.pictureTakenFail();
        }
    }
}
