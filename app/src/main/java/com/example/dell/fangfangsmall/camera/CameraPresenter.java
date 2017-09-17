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
import android.os.Handler;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.WindowManager;

import com.example.dell.fangfangsmall.R;
import com.example.dell.fangfangsmall.face.yt.person.YtVerifyperson;
import com.example.dell.fangfangsmall.util.ConUtil;
import com.example.dell.fangfangsmall.youtu.PersonManager;
import com.example.dell.fangfangsmall.youtu.callback.SimpleCallback;
import com.megvii.facepp.sdk.Facepp;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Created by zhangyuanyuan on 2017/9/14.
 */

public class CameraPresenter extends ICameraPresenter implements Camera.PreviewCallback {

    private ICameraView mCamerView;

//    private Accelerometer mAcc;
//    private FaceDetector mFaceDetector;
    private Facepp facepp;

    private SurfaceHolder mHolder; // 用于控制SurfaceView

    private Camera mCamera;
    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private int previewWidth = 640;
    private int previewHeight = 480;
    private boolean isPreviewing = false;
    private Matrix mScaleMatrix = new Matrix();

    private String pictureTakenPath;

    private boolean isVerifying = false;

    public CameraPresenter(ICameraView baseView, SurfaceHolder surfaceHolder) {
        super(baseView);
        mCamerView = baseView;
        mHolder = surfaceHolder;

//        mAcc = new Accelerometer(mCamerView.getContext());
//        mFaceDetector = FaceDetector.createDetector(mCamerView.getContext(), null);
        facepp = new Facepp();
        String errorCode = facepp.init(mCamerView.getContext(), ConUtil.getFileContent(mCamerView.getContext(), R.raw.megviifacepp_0_4_7_model));//MG
    }

    @Override
    public void accStart() {
//        if (null != mAcc) {
//            mAcc.start();
//        }
    }

    @Override
    public void accStop() {
//        if (null != mAcc) {
//            mAcc.stop();
//        }
    }

    @Override
    public void facedDestroy() {
//        if (null != mFaceDetector) {
//            mFaceDetector.destroy();
//        }
        facepp.release();
    }

    @Override
    public void verificationFace(Handler handler, String authId, Bitmap baseBitmap) {
//        BitmapUtils.saveBitmapToFile(baseBitmap, "123", "baseBitmap.jpg");
        Bitmap copyBitmap = bitmapSaturation(baseBitmap);
//        BitmapUtils.saveBitmapToFile(copyBitmap, "123", "copyBitmap.jpg");
        PersonManager.faceVerify(handler, authId, copyBitmap, new SimpleCallback<YtVerifyperson>((Activity) mCamerView.getContext()) {
            @Override
            public void onBefore() {
                isVerifying = true;
            }

            @Override
            public void onSuccess(YtVerifyperson ytVerifyperson) {
                mCamerView.verificationSuccerr(ytVerifyperson);
                mCamerView.setVerifyingFalse();
            }

            @Override
            public void onFail(int code, String msg) {
                mCamerView.verificationFail(code, msg);
                mCamerView.setVerifyingFalse();
            }

            @Override
            public void onError(Exception e) {
                super.onError(e);
                mCamerView.setVerifyingFalse();
            }

            @Override
            public void onEnd() {
                mCamerView.setVerifyingFalse();
            }
        });
    }

    @Override
    public void closeCamera() {
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopFaceDetection();
            mCamera.stopPreview();
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

//                Camera.Size previewSize = CameraUtils.getInstance().getPreviewSize(parameters.getSupportedPreviewSizes());
//                if (previewSize != null) {
//                    parameters.setPreviewSize(previewSize.width, previewSize.height);
//                }

                parameters.setPreviewSize(previewWidth, previewHeight);
                mCamera.setParameters(parameters);

                // 设置显示的偏转角度，大部分机器是顺时针90度，某些机器需要按情况设置
//                mCamera.setDisplayOrientation(90);
                mCamera.setDisplayOrientation(setCameraDisplayOrientation(mCameraId));
                mCamera.startPreview();
                mCamera.setPreviewCallback(this);

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
    public void setVerifying(boolean verifying) {
        isVerifying = verifying;
    }

    @Override
    public void setMatrix(int width, int height) {
        mScaleMatrix.setScale(width / (float) previewHeight, height / (float) previewWidth);
    }

    @Override
    public void cameraTakePicture(String savePath) {
        pictureTakenPath = savePath;
        mCamera.autoFocus(new Camera.AutoFocusCallback() {//自动对焦
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                // TODO Auto-generated method stub
                if (success) {
                    //设置参数，并拍照
                    Camera.Parameters params = camera.getParameters();
                    params.setPictureFormat(PixelFormat.JPEG);//图片格式
                    camera.setParameters(params);//将参数设置到我的camera
                    camera.takePicture(null, null, myPictureCallback);//将拍摄到的照片给自定义的对象
                }
            }
        });
    }

    public void setCameraId() {
        if (Camera.CameraInfo.CAMERA_FACING_FRONT == mCameraId) {
            mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        } else {
            mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }
    }

    public int setCameraDisplayOrientation(int paramInt) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(paramInt, info);
        int rotation = ((WindowManager) mCamerView.getContext().getSystemService("window")).getDefaultDisplay().getRotation(); // 获得显示器件角度
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
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

    public Bitmap bitmapTransformation(byte[] data, Camera camera, boolean frontCamera) {
        Camera.Size size = camera.getParameters().getPreviewSize();
        YuvImage yuvImage = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, baos);
        byte[] byteArray = baos.toByteArray();
        Bitmap previewBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        Matrix matrix = new Matrix();
        matrix.postRotate(0.0f, previewBitmap.getWidth() / 2, previewBitmap.getHeight() / 2);
//        if (frontCamera) {
//            matrix.setRotate(-90);
//        } else {
//            matrix.setRotate(90);
//        }
        return Bitmap.createBitmap(previewBitmap, 0, 0, previewBitmap.getWidth(), previewBitmap.getHeight(), matrix, true);
    }


    private Bitmap getBitmap(byte[] data) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        File file = new File(pictureTakenPath);
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//将图片压缩的流里面
        bos.flush();// 刷新此缓冲区的输出流
        bos.close();// 关闭此输出流并释放与此流有关的所有系统资源
        return bitmap;
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
//        int direction = Accelerometer.getDirection();
        boolean frontCamera = (Camera.CameraInfo.CAMERA_FACING_FRONT == mCameraId);
//        if (frontCamera) {
//            direction = (4 - direction) % 4;
//        }
//        String result = mFaceDetector.trackNV21(bytes, previewWidth, previewHeight, 0, direction);
//        FaceRect[] faces = ParseResult.parseResult(result);
        final Facepp.Face[] faces = facepp.detect(bytes, previewWidth, previewHeight, Facepp.IMAGEMODE_NV21);

        if (null != faces) {
            Log.e("",faces.length+"");
            if (faces.length == 1) {
                if (!isVerifying) {
                    isVerifying = true;
                    Bitmap verifyBitmap = bitmapTransformation(bytes, camera, frontCamera);
                    mCamerView.tranBitmap(verifyBitmap);
                }
            }
//            drawFacerect(frontCamera, faces);
        }
    }

    private Camera.PictureCallback myPictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] bytes, Camera camera) {
            try {
                Bitmap bitmap = getBitmap(bytes);
                camera.stopPreview();//关闭预览 处理数据
                doStartPreview();
                bitmap.recycle();//回收bitmap空间
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
}
