package com.example.dell.fangfangsmall.camera;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.util.Log;
import android.view.SurfaceHolder;

import com.example.dell.fangfangsmall.camera.IPresenter.ICameraPresenter;
import com.example.dell.fangfangsmall.util.BitmapUtils;
import com.example.dell.fangfangsmall.util.CameraUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by zhangyuanyuan on 2017/9/18.
 */

public class CameraPresenter extends ICameraPresenter implements Camera.PreviewCallback, Camera.PictureCallback {


    private ICameraView mCameraView;

    private SurfaceHolder mHolder;
    private Camera mCamera;
    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private int previewWidth = 640;
    private int previewHeight = 480;
    private boolean isPreviewing = false;
    private Matrix mScaleMatrix = new Matrix();
    private int orientionOfCamera;
    private int orientionOfPhoto;
    private boolean isFirst;

    public CameraPresenter(ICameraView baseView, SurfaceHolder surfaceHolder) {
        super(baseView);
        mCameraView = baseView;
        mHolder = surfaceHolder;
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
                List<Camera.Size> pictures = parameters.getSupportedPictureSizes();
                Camera.Size size = pictures.get(pictures.size() - 1);
//                parameters.setPictureSize(size.width, size.height);
                List<String> focusModes = parameters.getSupportedFocusModes();
                if (focusModes.contains("continuous-video")) {
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                }
                parameters.setPreviewFormat(ImageFormat.NV21);
                parameters.setPreviewSize(previewWidth, previewHeight);
                mCamera.setParameters(parameters);

                // 设置显示的偏转角度，大部分机器是顺时针90度，某些机器需要按情况设置
                orientionOfCamera = CameraUtils.setCameraDisplayOrientation((Activity) mCameraView.getContext(), mCameraId);
                mCamera.setDisplayOrientation(orientionOfCamera);
                mCamera.startPreview();
                mCamera.setPreviewCallback(this);
//                mCamera.setFaceDetectionListener(this);

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
                mCameraView.previewFinish();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void changeCamera() {
        if (Camera.getNumberOfCameras() == 1) {
            Log.e("","只有后置摄像头，不能切换");
            return;
        }
        closeCamera();
        setCameraId();
        openCamera();
        doStartPreview();
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
                    mCameraView.autoFocusSuccess();
                }
            }
        });
    }

    @Override
    public void cameraTakePicture() {
        isPreviewing = false;
        mCamera.takePicture(null, null, this);
    }

    public void setCameraId() {
        if (Camera.CameraInfo.CAMERA_FACING_FRONT == mCameraId) {
            mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        } else {
            mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }
    }



    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        Camera.Size size = camera.getParameters().getPreviewSize();
        YuvImage yuvImage = new YuvImage(bytes, ImageFormat.NV21, size.width, size.height, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, baos);
        byte[] byteArray = baos.toByteArray();

        Bitmap previewBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        int width = previewBitmap.getWidth();
        int height = previewBitmap.getHeight();

        Matrix matrix = new Matrix();

        FaceDetector detector = null;
        Bitmap faceBitmap = null;

        detector = new FaceDetector(previewBitmap.getWidth(), previewBitmap.getHeight(), 10);
        orientionOfCamera = 360 - orientionOfCamera;
        if(orientionOfCamera == 360){
            orientionOfCamera = 0;
        }
        switch (orientionOfCamera) {
            case 0:
                detector = new FaceDetector(width, height, 10);
                matrix.postRotate(0.0f, width / 2, height / 2);
                faceBitmap = Bitmap.createBitmap(previewBitmap, 0, 0, width, height, matrix, true);
                break;
            case 90:
                detector = new FaceDetector(height, width, 1);
                matrix.postRotate(-270.0f, height / 2, width / 2);
                faceBitmap = Bitmap.createBitmap(previewBitmap, 0, 0, width, height, matrix, true);
                break;
            case 180:
                detector = new FaceDetector(width, height, 1);
                matrix.postRotate(-180.0f, width / 2, height / 2);
                faceBitmap = Bitmap.createBitmap(previewBitmap, 0, 0, width, height, matrix, true);
                break;
            case 270:
                detector = new FaceDetector(height, width, 1);
                matrix.postRotate(-90.0f, height / 2, width / 2);
                faceBitmap = Bitmap.createBitmap(previewBitmap, 0, 0, width, height, matrix, true);
                break;
        }

        Bitmap copyBitmap = faceBitmap.copy(Bitmap.Config.RGB_565, true);

        FaceDetector.Face[] faces = new FaceDetector.Face[10];
        int faceNumber = detector.findFaces(copyBitmap, faces);
//        Log.e("faceNumber", faceNumber+"");
        if(!isFirst){
            orientionOfPhoto = orientionOfCamera;
            isFirst = true;
        }
        if (faceNumber > 0) {
            if(orientionOfPhoto == orientionOfCamera) {
                mCameraView.tranBitmap(faceBitmap, faceNumber);
            }
        }

        copyBitmap.recycle();
        faceBitmap.recycle();
        previewBitmap.recycle();
    }


    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
        camera.startPreview();
        isPreviewing = true;

        Bitmap previewBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        Matrix matrix = new Matrix();
        matrix.postRotate(0.0f, previewBitmap.getWidth() / 2, previewBitmap.getHeight() / 2);
//        matrix.setRotate(-90);
        Bitmap saveBitmap = Bitmap.createBitmap(previewBitmap, 0, 0, previewBitmap.getWidth(), previewBitmap.getHeight(), matrix, true);

        boolean save = BitmapUtils.saveBitmapToFile(saveBitmap, "PICTURETAKEN", System.currentTimeMillis() + ".jpg");
        if (save) {
            mCameraView.pictureTakenSuccess();
        } else {
            mCameraView.pictureTakenFail();
        }
    }


}
