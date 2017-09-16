package com.example.dell.fangfangsmall.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import com.iflytek.cloud.FaceDetector;
import com.iflytek.cloud.util.Accelerometer;

import java.io.IOException;
import java.util.List;

/**
 * Created by zhangyuanyuan on 2017/9/6.
 */

public class CamerSurfaceView extends SurfaceView implements  SurfaceHolder.Callback {

    private Context mContext;

    private SurfaceHolder mHolder; // 用于控制SurfaceView

    private Camera mCamera;
    private int mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;

    private boolean isPreviewing = false;

    private Camera.PreviewCallback cb;
    private CamerSurListener camerSurListener;

    // Camera nv21格式预览帧的尺寸，默认设置640*480
    private int PREVIEW_WIDTH = 640;
    private int PREVIEW_HEIGHT = 480;
    // 缩放矩阵
    private Matrix mScaleMatrix = new Matrix();

    public CamerSurfaceView(Context context) {
        super(context);
        init(context);
    }

    public CamerSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CamerSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        mHolder = getHolder(); // 获得SurfaceHolder对象
        mHolder.addCallback(this); // 为SurfaceView添加状态监听
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        if (Camera.getNumberOfCameras() == 1) {
            mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        }

//        PREVIEW_HEIGHT = Constants.displayWidth / 4;
//        PREVIEW_WIDTH = Constants.displayHeight / 4;

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (camerSurListener != null) {
                    camerSurListener.onCamerSurClick();
                }
            }
        });
    }


    /**
     * 当SurfaceView创建的时候，调用此函数
     */
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        openCamera();
        doStartPreview();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mScaleMatrix.setScale(width / (float)PREVIEW_HEIGHT, height / (float)PREVIEW_WIDTH);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        closeCamera();
    }



    /**
     * 打开
     */
    public void openCamera() {
        if (mCamera == null) {
            try {
                mCamera = Camera.open(mCameraId);
            } catch (Exception e) {
                e.printStackTrace();
                closeCamera();
                return;
            }
        }
    }

    /**
     * 预览
     */
    public void doStartPreview() {
        if (isPreviewing) {
            mCamera.stopPreview();
            return;
        }
        if (mCamera != null) {

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
            parameters.setPreviewSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);

            mCamera.setParameters(parameters);

            // 设置显示的偏转角度，大部分机器是顺时针90度，某些机器需要按情况设置
            mCamera.setDisplayOrientation(90);
            mCamera.startPreview();
            if (cb != null) {
                mCamera.setPreviewCallback(cb);
            }

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
        }
    }

    /**
     * 关闭
     */
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

    public Matrix getmScaleMatrix() {
        return mScaleMatrix;
    }

    public int getpreviewWidth() {
        return PREVIEW_WIDTH;
    }

    public int getpreviewHeight() {
        return PREVIEW_HEIGHT;
    }

    public void setmCameraId() {
        if (Camera.CameraInfo.CAMERA_FACING_FRONT == mCameraId) {
            mCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
        } else {
            mCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
        }
    }

    public int getmCameraId() {
        return mCameraId;
    }

    public void setSurfacePreview(Camera.PreviewCallback cb) {
        this.cb = cb;
    }

    public void setCamerSurListener(CamerSurListener camerSurListener) {
        this.camerSurListener = camerSurListener;
    }

    public interface CamerSurListener {
        void onCamerSurClick();
    }

}
