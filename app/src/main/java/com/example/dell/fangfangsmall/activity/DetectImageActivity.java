package com.example.dell.fangfangsmall.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.dell.fangfangsmall.view.DrawSurfaceView;
import com.example.dell.fangfangsmall.util.FaceRect;
import com.example.dell.fangfangsmall.util.ParseResult;
import com.example.dell.fangfangsmall.R;
import com.iflytek.cloud.FaceDetector;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.util.Accelerometer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class DetectImageActivity extends Activity implements SurfaceHolder.Callback, Camera.PreviewCallback {

    SurfaceView mPreviewSurface;

    DrawSurfaceView mDrawSurfaceView;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    private Camera mCamera;
    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;

    private int PREVIEW_WIDTH = 640;
    private int PREVIEW_HEIGHT = 480;

    private Accelerometer mAcc;
    private FaceDetector mFaceDetector;

    private long mLastClickTime;

    private boolean isDetecting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=" + "59b8fefd");

        requestWindowFeature(Window.FEATURE_NO_TITLE);//没有标题
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//拍照过程屏幕一直处于高亮
        //设置手机屏幕朝向，一共有7种
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        setContentView(R.layout.activity_detect_image);

        mPreviewSurface = (SurfaceView) findViewById(R.id.sfv_preview);
        mDrawSurfaceView = (DrawSurfaceView) findViewById(R.id.sfv_face);

        mPreviewSurface.getHolder().addCallback(this);
        mPreviewSurface.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

//        PREVIEW_HEIGHT = metrics.widthPixels / 4;
//        PREVIEW_WIDTH = metrics.heightPixels / 4;


        setListener();
        mAcc = new Accelerometer(DetectImageActivity.this);
        mFaceDetector = FaceDetector.createDetector(this, null);
    }


    public static void navToDetectImage(Context context) {
        Intent intent = new Intent(context, DetectImageActivity.class);
        context.startActivity(intent);
    }

        private void setListener() {
            mDrawSurfaceView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // 只有一个摄相头，不支持切换
                    if (Camera.getNumberOfCameras() == 1) {
    //                    ToastUtils.showToastInCenter(DetectImageActivity.this, ToastUtils.TYPE_TICK, "只有后置摄像头，不能切换");
                        return;
                    }
                    closeCamera();
                    if (Camera.CameraInfo.CAMERA_FACING_FRONT == mCameraId) {
                        mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                    } else {
                        mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                    }
                    openCamera();
                }
            });

            // 长按SurfaceView 500ms后松开，摄相头聚集
            mDrawSurfaceView.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mLastClickTime = System.currentTimeMillis();
                            break;
                        case MotionEvent.ACTION_UP:
                            if (System.currentTimeMillis() - mLastClickTime > 500) {
                                mCamera.autoFocus(null);
                                return true;
                            }
                            break;

                        default:
                            break;
                    }
                    return false;
                }
            });
        }


    private void openCamera() {
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

        Camera.Parameters params = mCamera.getParameters();
        params.setPictureFormat(PixelFormat.JPEG);

        List<Camera.Size> pictures = params.getSupportedPictureSizes();

        Camera.Size size = pictures.get(pictures.size() - 1);
        params.setPictureSize(size.width, size.height);
        List<String> focusModes = params.getSupportedFocusModes();
        if (focusModes.contains("continuous-video")) {
            params.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
        }
        params.setPreviewFormat(ImageFormat.NV21);

        List<Camera.Size> sizeList = params.getSupportedPreviewSizes();

        // 如果sizeList只有一个我们也没有必要做什么了，因为就他一个别无选择
//        if (sizeList.size() > 1) {
//            Iterator<Camera.Size> itor = sizeList.iterator();
//            while (itor.hasNext()) {
//                Camera.Size cur = itor.next();
//                if (cur.width >= PREVIEW_WIDTH  && cur.height >= PREVIEW_HEIGHT) {
//                    PREVIEW_WIDTH = cur.width;
//                    PREVIEW_HEIGHT = cur.height;
//                    break;
//                }
//            }
//        }

        params.setPreviewSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);
        mCamera.setParameters(params);

        mCamera.setDisplayOrientation(90);
        mCamera.setPreviewCallback(this);

        try {
            mCamera.setPreviewDisplay(mPreviewSurface.getHolder());
            mCamera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void closeCamera() {
        if (null != mCamera) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        if (null != mAcc) {
            mAcc.start();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        closeCamera();
        if (null != mAcc) {
            mAcc.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mFaceDetector) {
            // 销毁对象
            mFaceDetector.destroy();
        }
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        closeCamera();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        openCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    /**
     * Camera — 取数据(onPreviewFrame(Byte[] rawFrameData, Camera camera)) —> 原始帧处理（Rotate/Scale：使用Libyuv/FFmpeg等工具库）
     * —> 编码器编码得到相应的h24数据 —> 发送给流媒体服务器
     *
     * @param data
     * @param camera
     */
    @Override
    public void onPreviewFrame(final byte[] data, Camera camera) {
        detectorFace(data, camera);
    }

    private void detectorFace(byte[] data, Camera camera) {
        int direction = Accelerometer.getDirection();
        boolean frontCamera = (Camera.CameraInfo.CAMERA_FACING_FRONT == mCameraId);
        if (frontCamera) {
            direction = (4 - direction) % 4;
        }

        String result = mFaceDetector.trackNV21(data, PREVIEW_WIDTH, PREVIEW_HEIGHT, 0, direction);

        FaceRect[] faces = ParseResult.parseResult(result);
        Log.e("face", faces.toString());
//        if (null != faces ) {
//            if(faces.length > 0) {
//                if (!isDetecting) {
//                    Bitmap bitmap = transformationBitmap(data, camera, frontCamera);
//
////                    distinguishFace(bitmap, frontCamera);
//                }
//            }else{
////                mDrawSurfaceView.clear();
//            }
//        } else {
////            LogUtils.e("faces:0");
////            mDrawSurfaceView.clear();
//        }
    }

    private Bitmap transformationBitmap(byte[] data, Camera camera, boolean frontCamera) {
        Camera.Size size = camera.getParameters().getPreviewSize();
        YuvImage yuvImage = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, baos);
        byte[] byteArray = baos.toByteArray();
        Bitmap previewBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
        Matrix matrix = new Matrix();
        matrix.postRotate(0.0f, previewBitmap.getWidth() / 2, previewBitmap.getHeight() / 2);
        if (frontCamera) {
            matrix.setRotate(-90);
        } else {
            matrix.setRotate(90);
        }
        return Bitmap.createBitmap(previewBitmap, 0, 0, previewBitmap.getWidth(), previewBitmap.getHeight(), matrix, true);
    }

//    private void distinguishFace(Bitmap bitmap, final boolean frontCamera){
//        PersonManager.detectFace(mHandler, bitmap, 0, new SimpleCallback<YtDetectFace>(DetectImageActivity.this) {
//            @Override
//            public void onBefore() {
//                isDetecting = true;
//            }
//            @Override
//            public void onSuccess(YtDetectFace ytDetectFace) {
//                mDrawSurfaceView.setYtDetectFace(ytDetectFace, frontCamera);
//                mHandler.sendEmptyMessageDelayed(1, 500);
//            }
//            @Override
//            public void onFail(int code, String msg) {
////                ToastUtils.showToastInCenter(DetectImageActivity.this, ToastUtils.TYPE_EXEC, msg);
//                LogUtils.e(code + msg);
//                mHandler.sendEmptyMessageDelayed(1, 500);
//            }
//            @Override
//            public void onError(Exception e) {
//                super.onError(e);
//                mHandler.sendEmptyMessageDelayed(1, 1000);
//            }
//            @Override
//            public void onEnd() {
//                mHandler.sendEmptyMessageDelayed(1, 1000);
//            }
//        });
//
//    }


}
