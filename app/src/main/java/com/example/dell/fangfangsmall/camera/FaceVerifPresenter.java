package com.example.dell.fangfangsmall.camera;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.FaceDetector;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;

import com.example.dell.fangfangsmall.camera.IPresenter.IFaceVerifPresenter;
import com.example.dell.fangfangsmall.face.yt.person.YtVerifyperson;
import com.example.dell.fangfangsmall.youtu.PersonManager;
import com.example.dell.fangfangsmall.youtu.callback.SimpleCallback;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by zhangyuanyuan on 2017/9/18.
 */

public class FaceVerifPresenter extends IFaceVerifPresenter implements Camera.PreviewCallback {

    private IFaceverifView mFaceverifView;

    private SurfaceHolder mHolder;

    private String mAuthId = "zhangT";

    private Camera mCamera;
    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private int previewWidth = 640;
    private int previewHeight = 480;
    private boolean isPreviewing = false;
    private Matrix mScaleMatrix = new Matrix();
    private int orientionOfCamera;

    private boolean isVerifying = false;

    public FaceVerifPresenter(IFaceverifView baseView, SurfaceHolder surfaceHolder) {
        super(baseView);
        mFaceverifView = baseView;
        mHolder = surfaceHolder;
    }


    @Override
    public void verificationFace(Handler handler, Bitmap baseBitmap) {
        Bitmap copyBitmap = bitmapSaturation(baseBitmap);
        PersonManager.faceVerify(handler, mAuthId, copyBitmap, new SimpleCallback<YtVerifyperson>((Activity) mFaceverifView.getContext()) {
            @Override
            public void onBefore() {
                isVerifying = true;
            }

            @Override
            public void onSuccess(YtVerifyperson ytVerifyperson) {
                mFaceverifView.verificationSuccerr(ytVerifyperson);
                mFaceverifView.setVerifyingFalse();
            }

            @Override
            public void onFail(int code, String msg) {
                mFaceverifView.verificationFail(code, msg);
                mFaceverifView.setVerifyingFalse();
            }

            @Override
            public void onError(Exception e) {
                super.onError(e);
                mFaceverifView.setVerifyingFalse();
            }

            @Override
            public void onEnd() {
                mFaceverifView.setVerifyingFalse();
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
                parameters.setPictureSize(size.width, size.height);
                List<String> focusModes = parameters.getSupportedFocusModes();
                if (focusModes.contains("continuous-video")) {
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
                }
                parameters.setPreviewFormat(ImageFormat.NV21);
                parameters.setPreviewSize(previewWidth, previewHeight);
                mCamera.setParameters(parameters);

                // 设置显示的偏转角度，大部分机器是顺时针90度，某些机器需要按情况设置
//                mCamera.setDisplayOrientation(90);
                orientionOfCamera = CameraUtils.setCameraDisplayOrientation((Activity) mFaceverifView.getContext(), mCameraId);
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
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setVerifying(boolean verifying) {
        isVerifying = verifying;
    }

    @Override
    public void setMatrix(int width, int height) {
        mScaleMatrix.setScale(width / (float) previewHeight, height / (float) previewWidth);
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
        Log.e("faceNumber", faceNumber+"");
        if (faceNumber > 0) {
            if (!isVerifying) {
                isVerifying = true;
                mFaceverifView.tranBitmap(faceBitmap);
            }
        }

        copyBitmap.recycle();
        faceBitmap.recycle();
        previewBitmap.recycle();
    }
}
