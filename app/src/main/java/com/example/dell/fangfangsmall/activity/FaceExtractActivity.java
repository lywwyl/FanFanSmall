package com.example.dell.fangfangsmall.activity;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dell.fangfangsmall.R;
import com.example.dell.fangfangsmall.listener.OnConfimListener;
import com.example.dell.fangfangsmall.util.FaceRect;
import com.example.dell.fangfangsmall.util.JumpItent;
import com.example.dell.fangfangsmall.util.ParseResult;
import com.example.dell.fangfangsmall.view.AddInfoDialog;
import com.example.dell.fangfangsmall.view.DrawSurfaceView;
import com.iflytek.cloud.FaceDetector;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.util.Accelerometer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 人脸提取
 *
 * @author Guanluocang
 *         created at 2017/9/139:37
 */
public class FaceExtractActivity extends Activity implements View.OnClickListener, SurfaceHolder.Callback, Camera.PreviewCallback {

    private TextView mAddInfo;
    private AddInfoDialog addInfoDialog;

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
        setContentView(R.layout.activity_face_extract);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
        this.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//拍照过程屏幕一直处于高亮
        //设置手机屏幕朝向，一共有7种
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);
//        SpeechUtility.createUtility(this, SpeechConstant.APPID + "=" + "59b8fefd");
        initView();
        initData();
        initListener();
    }

    private void initView() {
        (mAddInfo) = (TextView) findViewById(R.id.tv_add_info);
        mPreviewSurface = (SurfaceView) findViewById(R.id.sfv_preview);
        mDrawSurfaceView = (DrawSurfaceView) findViewById(R.id.sfv_face);

    }

    private void initData() {
        mPreviewSurface.getHolder().addCallback(this);
        mPreviewSurface.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);


        setListener();
        mAcc = new Accelerometer(FaceExtractActivity.this);
        mFaceDetector = FaceDetector.createDetector(this, null);
    }

    private void initListener() {
        mAddInfo.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_info:
                showAddDialog();
                break;
            case R.id.tv_main_first:
                JumpItent.jump(FaceExtractActivity.this, MainActivity.class);
                finish();
                break;
            case R.id.tv_main_two:
                JumpItent.jump(FaceExtractActivity.this, VideoActivity.class);
                finish();
                break;
            case R.id.tv_main_three:
                JumpItent.jump(FaceExtractActivity.this, VoiceActivity.class);
                finish();
                break;
            case R.id.tv_main_four:
                finish();
//                JumpItent.jump(FaceExtractActivity.this, TrainActivity.class);
                break;
        }
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
    }
//        Log.e("face", faces.toString());

    private void showAddDialog() {
        addInfoDialog = new AddInfoDialog(FaceExtractActivity.this, new OnConfimListener() {
            @Override
            public void onConfim() {
                //确认
                Toast.makeText(FaceExtractActivity.this, "添加成功", Toast.LENGTH_LONG).show();
            }
        });
        addInfoDialog.show();
    }
}
